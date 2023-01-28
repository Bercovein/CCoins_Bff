package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.bars.BarDTO;
import com.ccoins.bff.dto.bars.GameDTO;
import com.ccoins.bff.dto.coins.MatchDTO;
import com.ccoins.bff.dto.coins.SongDTO;
import com.ccoins.bff.dto.coins.VoteDTO;
import com.ccoins.bff.dto.coins.VotingDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.feign.CoinsFeign;
import com.ccoins.bff.service.IVoteService;
import com.ccoins.bff.spotify.sto.SongSPTF;
import com.ccoins.bff.utils.DateUtils;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VoteService implements IVoteService {

    private final CoinsFeign coinsFeign;

    private final BarsFeign barsFeign;

    private final ClientService clientService;

    @Autowired
    public VoteService(CoinsFeign coinsFeign, BarsFeign barsFeign, ClientService clientService) {
        this.coinsFeign = coinsFeign;
        this.barsFeign = barsFeign;
        this.clientService = clientService;
    }

    @Override
    public VotingDTO getActualVotingByBar(Long barId){
        return this.coinsFeign.getActualVotingByBar(barId).getBody();
    }

    @Override
    public SongDTO resolveVoting(Long barId){
        //resolver resultado de la votación, si ninguno fue votado, tomar cualquiera y cerrar la votación
        VotingDTO voting = this.getActualVotingByBar(barId);
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

        this.coinsFeign.saveOrUpdateVoting(voting); //actualiza el match y la votación

        return winner;
    }

    @Override
    public VotingDTO createNewVoting(Long barId, List<SongSPTF> list) {
        List<SongDTO> songDTOList = list.stream().map(SongDTO::convert).collect(Collectors.toList());

        GameDTO game = this.barsFeign.findVotingGameByBarId(barId).getBody();

        MatchDTO matchDTO = MatchDTO.builder()
                .startDate(DateUtils.nowLocalDateTime())
                .active(true)
                .game(game.getId())
                .build();

        VotingDTO votingDTO = VotingDTO.builder()
                .songs(songDTOList).match(matchDTO).build();

        return this.coinsFeign.saveOrUpdateVoting(votingDTO);
    }

    @Override
    public void voteSong(HttpHeaders headers, IdDTO request) {

        //buscar si la votación está activa
        VotingDTO voting = this.coinsFeign.getVotingBySong(request.getId());

        if(voting.getWinnerSong() != null){
            System.out.println("LA VOTACIÓN YA TERMINÓ");
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

                if(barBySong.getId() != barIdByParty.getId()){
                    System.out.println("EL USUARIO NO ESTÁ EN EL BAR CORRECTO");
                }

                if(!DateUtils.isNowBetweenLocalTimes(barBySong.getOpenTime(),barBySong.getCloseTime())){
                    System.out.println("EL USUARIO VOTÓ FUERA DEL HORARIO DEL BAR");
                }

                //aumentar en 1 el voto de forma transaccional
                this.voteSong(request.getId(), HeaderUtils.getClient(headers));
            }

        }
    }

    public void voteSong(Long songId, String clientIp){
        ClientDTO clientDTO = this.clientService.findActiveByIp(clientIp);
        this.coinsFeign.voteSong(VoteDTO.builder().song(songId).client(clientDTO.getId()).build());
    }
}
