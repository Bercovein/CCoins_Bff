package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.bars.BarDTO;
import com.ccoins.bff.dto.bars.GameDTO;
import com.ccoins.bff.dto.coins.*;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.exceptions.UnauthorizedException;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.feign.CoinsFeign;
import com.ccoins.bff.service.IVoteService;
import com.ccoins.bff.spotify.sto.SongSPTF;
import com.ccoins.bff.utils.DateUtils;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ccoins.bff.exceptions.constant.ExceptionConstant.*;

@Service
public class VoteService implements IVoteService {

    private final CoinsFeign coinsFeign;

    private final BarsFeign barsFeign;

    private final ClientService clientService;

    private final PartiesService partiesService;

    @Autowired
    public VoteService(CoinsFeign coinsFeign, BarsFeign barsFeign, ClientService clientService, PartiesService partiesService) {
        this.coinsFeign = coinsFeign;
        this.barsFeign = barsFeign;
        this.clientService = clientService;
        this.partiesService = partiesService;
    }

    @Override
    public VotingDTO getActualVotingByBar(Long barId){
        return this.coinsFeign.getActualVotingByBar(barId).getBody();
    }

    @Override
    public VotingDTO resolveVoting(Long barId){

        //resolver resultado de la votación, si ninguno fue votado, tomar cualquiera y cerrar la votación
        VotingDTO voting = this.getActualVotingByBar(barId);

        if(voting == null){
            return null;
        }

        List<SongDTO> voteList = voting.getSongs();
        Optional<SongDTO> maxValueOpt = voteList.stream().max(Comparator.comparing(SongDTO::getVotes));
        List<SongDTO> winnerSongs;

        if(maxValueOpt.isPresent() && maxValueOpt.get().getVotes() != 0){
            winnerSongs = voteList.stream().filter(s -> Objects.equals(s.getVotes(), maxValueOpt.get().getVotes())).collect(Collectors.toList());
        }else{ //si el voto maximo fué 0, entonces nadie votó
            winnerSongs = voteList;
        }
        Random rand = new Random(); //toma un tema random en caso de empate
        SongDTO winner = winnerSongs.get(rand.nextInt(winnerSongs.size()));
        voting.setWinnerSong(winner);
        voting.getMatch().setEndDate(DateUtils.nowLocalDateTime());

        this.coinsFeign.updateVoting(voting); //actualiza el match y la votación

        return voting;
    }

    @Override
    public List<String> giveSongCoinsByGame(Long barId, VotingDTO voting){

        List<String> ipClients = new ArrayList<>();

        //buscar el game
        GameDTO game = this.barsFeign.findVotingGameByBarId(barId).getBody();

        //buscar a los clientes que votaron la canción
        List<Long> clientsIdList = this.coinsFeign.getClientsIdWhoVotedSong(voting.getWinnerSong().getId()).getBody();

        //crear coins en base al match para cada cliente
        assert game != null;
        CoinsToWinnersDTO request = CoinsToWinnersDTO.builder()
                .quantity(game.getPoints())
                .matchId(voting.getMatch().getId())
                .clients(clientsIdList)
                .build();

        ResponseEntity<List<Long>> clientsResponse = this.coinsFeign.giveCoinsToClients(request);

        if(clientsResponse.hasBody()){
            this.partiesService.findByIdIn(clientsResponse.getBody()).forEach(clientDTO -> ipClients.add(clientDTO.getIp()));
        }

        return ipClients;
    }

    @Override
    public VotingDTO createNewVoting(Long barId, List<SongSPTF> list) {
        List<SongDTO> songDTOList = list.stream().map(SongDTO::convert).collect(Collectors.toList());

        GameDTO game = this.barsFeign.findVotingGameByBarId(barId).getBody();

        assert game != null;
        MatchDTO matchDTO = MatchDTO.builder()
                .startDate(DateUtils.nowLocalDateTime())
                .active(true)
                .game(game.getId())
                .build();

        VotingDTO votingDTO = VotingDTO.builder()
                .songs(songDTOList).match(matchDTO).build();

        return this.coinsFeign.saveVoting(votingDTO);
    }

    @Override
    public void voteSong(HttpHeaders headers, IdDTO request) {

        //buscar si la votación está activa
        VotingDTO voting = this.coinsFeign.getVotingBySong(request.getId());

        if(voting.getWinnerSong() != null){
            throw new UnauthorizedException(VOTES_IS_OVER_ERROR_CODE, VOTES_IS_OVER_ERROR);
        }

        IdDTO barIdByParty;
        BarDTO barBySong;

        //buscar si el cliente pertenece a ese bar (cliente vs bar asociado a la canción)
        Long partyId = HeaderUtils.getPartyId(headers);
        ResponseEntity<IdDTO> barResponseEntity = this.barsFeign.getBarIdByParty(partyId);

        if (barResponseEntity.hasBody()){
            barIdByParty = barResponseEntity.getBody();

            //trae bar por el juego al que está ligada la canción
            ResponseEntity<BarDTO> barRs = this.barsFeign.getBarByGame(voting.getMatch().getGame());

            if(barRs.hasBody()){
                barBySong = barRs.getBody();

                if(!Objects.equals(barBySong.getId(), barIdByParty.getId())){
                    throw new UnauthorizedException(WRONG_BAR_ERROR_CODE, WRONG_BAR_ERROR);
                }

                if(!DateUtils.isNowBetweenLocalTimes(barBySong.getOpenTime(),barBySong.getCloseTime())){
                    throw new UnauthorizedException(WRONG_BAR_TIME_ERROR_CODE, WRONG_BAR_TIME_ERROR);
                }

                //aumentar en 1 el voto de forma transaccional
                this.voteSong(request.getId(), HeaderUtils.getClient(headers));
            }
        }
    }

    @Override
    public ResponseEntity<GenericRsDTO<ResponseDTO>> checkVote(HttpHeaders headers) {

        Long partyId = HeaderUtils.getPartyId(headers);
        String userIp = HeaderUtils.getClient(headers);
        boolean hasVoted = false;

        //buscar bar al que pertenece
        ResponseEntity<IdDTO> barResponseEntity = this.barsFeign.getBarIdByParty(partyId);

        if(!barResponseEntity.hasBody() || barResponseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericRsDTO<>(
                    BAR_OR_PARTY_NO_EXIST_ERROR_CODE, BAR_OR_PARTY_NO_EXIST_ERROR, null
            ));
        }

        IdDTO barId = barResponseEntity.getBody();

        //buscar la votación actual por bar
        if(barResponseEntity.hasBody()){
            hasVoted = this.coinsFeign.hasVotedAlready(userIp, barId.getId());
        }

        if(hasVoted){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericRsDTO<>(
                    ALREADY_VOTE_ERROR_CODE, ALREADY_VOTE_ERROR, null
            ));
        }

        return ResponseEntity.ok(null);
    }

    public void voteSong(Long songId, String clientIp){
        ClientDTO clientDTO = this.clientService.findActiveByIp(clientIp);
        this.coinsFeign.voteSong(VoteDTO.builder().song(songId).client(clientDTO.getId()).build());
    }
}
