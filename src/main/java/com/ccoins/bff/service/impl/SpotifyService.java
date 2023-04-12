package com.ccoins.bff.service.impl;

import com.ccoins.bff.configuration.CredentialsSPTFConfig;
import com.ccoins.bff.dto.EmptyDTO;
import com.ccoins.bff.dto.bars.GameDTO;
import com.ccoins.bff.dto.coins.SongDTO;
import com.ccoins.bff.dto.coins.VotingDTO;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.feign.SpotifyFeign;
import com.ccoins.bff.service.IServerSentEventService;
import com.ccoins.bff.service.ISpotifyService;
import com.ccoins.bff.service.IVoteService;
import com.ccoins.bff.spotify.sto.*;
import com.ccoins.bff.utils.*;
import com.ccoins.bff.utils.enums.EventNamesSPTFEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.ccoins.bff.utils.SpotifyUtils.REPEAT_STATE;

@Service
@Slf4j
public class SpotifyService implements ISpotifyService {

    private final SpotifyFeign feign;
    private final CredentialsSPTFConfig credentials;
    private final IServerSentEventService sseService;
    private final IVoteService voteService;
    private final BarsFeign barsFeign;

    private Map<Long,BarTokenDTO> barTokens = new ConcurrentHashMap<>();

    @Value("${spotify.max-to-vote}")
    private int maxToVote;

    @Value("${spotify.playback.track-url}")
    private String trackLink;

    @Value("${spotify.vote-before-ms}")
    private int votesBeforeEndSongMs;

    @Autowired
    public SpotifyService(SpotifyFeign feign, CredentialsSPTFConfig credentials, IServerSentEventService sseService, IVoteService voteService, BarsFeign barsFeign) {
        this.feign = feign;
        this.credentials = credentials;
        this.sseService = sseService;
        this.voteService = voteService;
        this.barsFeign = barsFeign;
    }

    @Scheduled(fixedDelayString = "${spotify.playback.cron}")
    public void sendPlayback(){
        this.barTokens.forEach((c,v) -> this.sendPlaybackToClients(v));
    }

    @Override
    public void generateToken(HttpHeaders headers, BarTokenDTO request){

        //debe generar el token en base al id del bar y el codigo
        //guardarlo en memoria y devolverlo para guardar en la base
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type",credentials.getAuthorizationCode());
        requestBody.put("code",request.getCode());
        requestBody.put("redirect_uri",credentials.getRedirectURI());

//        try {
            TokenSPTF token = this.spotifyTokenFeign.getOrRefreshToken(headers, requestBody);
//        }catch (Exception e){
//            throw
//        }
        request.setRefreshToken(token.getRefreshToken());
        request.setToken(token.getAccessToken());
        request.setExpirationDate(DateUtils.nowLocalDateTime().plusSeconds(token.getExpiresIn()));
        request.setExpiresIn(token.getExpiresIn());

        this.usersFeign.saveOrUpdateRefreshTokenSpotify(request.getOwnerId(),
                RefreshTokenDTO.builder().refreshToken(request.getRefreshToken()).build());
    }

    @Override
    public void refreshToken(HttpHeaders headers, BarTokenDTO request){
        TokenSPTF response;

        //si el refresh falla, debe avisar por socket de que llame de nuevo a la api
        //tambien debe quitarlo de la lista en memoria
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("grant_type",credentials.getRefreshToken());
            requestBody.put("refresh_token",request.getRefreshToken());

            response = this.spotifyTokenFeign.getOrRefreshToken(
                    headers,requestBody);

            request.setToken(response.getAccessToken());
            request.setExpiresIn(response.getExpiresIn());
            request.setExpirationDate(DateUtils.nowLocalDateTime().plusSeconds(request.getExpiresIn()));

//            request.setRefreshToken(response.getRefreshToken());
        }catch (Exception e){
            this.barTokens.remove(request.getId());
            this.usersFeign.saveOrUpdateRefreshTokenSpotify(request.getOwnerId(), RefreshTokenDTO.builder().build());
            this.sseService.dispatchEventToSingleBar(EventNamesSPTFEnum.REQUEST_SPOTIFY_AUTHORIZATION.name(),null,request.getId());
        }

    }

    @Override
    public BarTokenDTO getOrRefreshToken(BarTokenDTO request){

        HttpHeaders headers = new HttpHeaders();
        String auth = this.credentials.getClientId().concat(":").concat(this.credentials.getClientSecret());
        HeaderUtils.setParameterAuthorization(headers, StringsUtils.stringToBase64(auth));
        HeaderUtils.setParameterContentType(headers, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        //si el code existe y el refresh no, debe pedir un token
        if(request.getCode() != null
                && request.getRefreshToken() == null){
            this.generateToken(headers, request);
            this.barTokens.put(request.getId(), request);
        }

        //si el token expiró, lo renueva (si la hora actual supera a la hora de expiración del token)
        if (DateUtils.isAfterLocalDateTime(DateUtils.nowLocalDateTime(), request.getExpirationDate())){
            this.refreshToken(headers, request);
            this.barTokens.put(request.getId(), request);
        }

        return request;
    }

    @Override
    public ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers) {

        HeaderUtils.setParameters(headers);
        PlaylistSPTF response =  this.feign.getQueue(headers);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RecentlyPlayedSPTF> getRecentlyPlayed(HttpHeaders headers, Integer limit) {

        HeaderUtils.setParameters(headers);
        RecentlyPlayedSPTF response =  this.feign.getRecentlyPlayed(headers,limit);
        return ResponseEntity.ok(response);
    }

    @Override
    public void addTrackToQueue(HttpHeaders headers, List<String> tracks, int position, String playlistId) {

        HeaderUtils.setParameters(headers);
        this.feign.addItemsToPlaylist(headers,playlistId, ItemToPlaylistSPTF.builder().position(position).uris(tracks).build());
    }

    @Override
    public CredentialsSPTFDTO getCredentials(){
        return MapperUtils.map(this.credentials, CredentialsSPTFDTO.class);
    }


    @Override
    public void startPlayback(BarTokenDTO request){

        if(request != null && request.getToken() != null && request.getId() != null && request.getRefreshToken() != null){
            this.barTokens.put(request.getId(),request);
        }
    }

    @Override
    public PlaybackSPTF getPlayback(BarTokenDTO request){

        //traer el estado de la canción desde spotify con el token
        if(request.getToken() != null && request.getRefreshToken() != null){
            HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(request.getToken());
            request.setPlayback(this.feign.getPlaybackState(headers));
        }

        //si falla, tirar el refresh token

        return request.getPlayback();
    }

    @Override
    @Async
    public void sendPlaybackToClients(BarTokenDTO request){

        Long barId = request.getId();

        PlaybackSPTF playbackSPTF = this.getPlayback(request);
        String token = request.getToken();

        if(playbackSPTF == null){
            return;
        }

        //setea la url de la canción actual
        playbackSPTF.setSongLink(this.generateSpotifyLinkFromPlaybackItem(playbackSPTF.getItem()));

        //envia a todos los usuarios el estado de la canción actual (asi se esté reproduciendo o no)
        this.sseService.dispatchEventToAllClientsFromBar(EventNamesSPTFEnum.ACTUAL_SONG_SPTF.name(), playbackSPTF, barId);

        //si no cumple con las condiciones, no se genera la votación
        if(this.votingIsCanceled(playbackSPTF)){
            this.sseService.dispatchEventToAllClientsFromBar(EventNamesSPTFEnum.ACTUAL_VOTES_SPTF.name(), new ArrayList<>(), barId);
            return;
        }

        //quita el modo aleatorio de la lista si es que lo tiene
        //obliga a dejar el modo de repetición de la playlist
        this.changeStateOfPlayback(token, playbackSPTF);

        if(!playbackSPTF.isPlaying()){
            return;
        }

        try {

            if(!this.isVotingActive(barId)){
                this.sseService.dispatchEventToAllClientsFromBar(EventNamesSPTFEnum.ACTUAL_VOTES_SPTF.name(), new ArrayList<>(), barId);
                return;
            }

            //toma la votación actual del bar
            VotingDTO actualVoting = this.voteService.getActualVotingByBar(barId);

            //si faltan mas segundos antes de que termine la canción, no se genera nueva votación
            if(actualVoting != null
                    && !DateUtils.isLowerFromNowThan(actualVoting.getMatch().getStartDate(), 60)
                    && request.getPlayback().getItem().getDurationMs() - request.getPlayback().getProgressMs() <= this.votesBeforeEndSongMs){
                this.resolveAndGenerateVotation(request, actualVoting);
                actualVoting = null;
            }

            //si la votación no existe, crea una nueva (caso de ser el primer tema en reproducción)
            if (actualVoting == null) {
                actualVoting = this.newVoting(token, playbackSPTF, barId);
            }

            //devuelve la votación actual
            if (actualVoting != null)
                this.sseService.dispatchEventToAllClientsFromBar(EventNamesSPTFEnum.ACTUAL_VOTES_SPTF.name(), actualVoting.getSongs(), barId);

        }catch(Exception e){
            log.error(e.getMessage());
        }

    }

    private boolean isVotingActive(Long barId){

        boolean response = false;
        ResponseEntity <GameDTO> gameResponse = this.barsFeign.findVotingGameByBarId(barId);

        if(gameResponse.hasBody()) {
            GameDTO game = gameResponse.getBody();
            assert game != null;
            if (game.isActive()
                    && ((game.getOpenTime() == null && game.getCloseTime() == null)
                    || (DateUtils.isNowBetweenLocalTimes(game.getOpenTime(), game.getCloseTime())))) {
                response = true;
            }
        }
        return response;
    }

    private boolean votingIsCanceled(PlaybackSPTF playbackSPTF){
        return playbackSPTF.getItem() == null
                || playbackSPTF.getContext() == null
                || !SpotifyUtils.isPlaylist(playbackSPTF.getContext().getType())
                || StringsUtils.isNullOrEmpty(playbackSPTF.getContext().getUri());
    }

    private String generateSpotifyLinkFromPlaybackItem(PlaybackItemSPTF item){

        if(item.getUri() != null) {
            return trackLink.concat(SpotifyUtils.getUriId(item.getUri()));
        }
        return null;
    }

    @Override
    public void resolveAndGenerateVotation(BarTokenDTO request, VotingDTO actualVoting){

        Long barId = request.getId();
        PlaybackSPTF playbackSPTF = request.getPlayback();
        String token = request.getToken();

        //genera un nuevo ganador
        SongDTO winnerSong = this.newWinner(barId, actualVoting);

        //añade la canción ganadora en la posición siguiente a reproducir
        this.addVotedSongToNextPlayback(token, playbackSPTF, winnerSong);
    }

    private void changeStateOfPlayback(String token,PlaybackSPTF playbackSPTF){

        //quita el modo aleatorio de la lista si es que lo tiene
        this.changeShuffleState(token, playbackSPTF.isShuffleState());

        //obliga a dejar el modo de repetición de la playlist
        this.changeRepeatState(token, playbackSPTF.getRepeatState());
    }

    private void changeShuffleState(String token, boolean bool){

        try {
            if(bool){
                HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);
                this.feign.changeShuffleState(headers, false, EmptyDTO.builder().build());
            }
        }catch (Exception ignored){}
    }

    private void changeRepeatState(String token, String state){

        try {
            if(!REPEAT_STATE.equals(state)){
                HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);
                this.feign.changeRepeatState(headers, REPEAT_STATE, EmptyDTO.builder().build());
            }
        }catch (Exception ignored){}
    }

    @Override
    public SongDTO newWinner(Long barId, VotingDTO actualVoting) {

        //resolver la votación
        VotingDTO voting = this.voteService.resolveVoting(actualVoting);
        SongDTO winnerSong = null;

        if (voting != null) {
            winnerSong = voting.getWinnerSong();
            this.sseService.dispatchEventToAllClientsFromBar(EventNamesSPTFEnum.NEW_WINNER_SPTF.name(), voting.getWinnerSong(), barId);

            this.voteService.giveSongCoinsByGame(barId, voting);
        }

        return winnerSong;
    }

    @Override
    public VotingDTO newVoting(String token, PlaybackSPTF playbackSPTF, Long barId) {

        List<SongSPTF> list = this.getNextVotes(token); //ESTAS CANCIONES VIAJAN A LA NUEVA VOTACIÓN

        if(list.isEmpty()){
            return null;
        }

        return this.voteService.createNewVoting(barId, list);
    }

    @Override
    public List<SongSPTF> getNextVotes(String token){

        HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);
        PlaylistSPTF playlistSPTF = this.feign.getQueue(headers); //trae la playlist actual
        List<SongSPTF> songs = playlistSPTF.getQueue();

        if(songs.isEmpty() || songs.size() == 1)
            return new ArrayList<>();

        Random rand = new Random();
        List<SongSPTF> votingSongs = new ArrayList<>();

        for (int i=1; i <= Math.min(songs.size(), maxToVote); i++){
            votingSongs.add(songs.remove(rand.nextInt(songs.size())));
        }

        return votingSongs;
    }

    @Override
    @Async
    public void addVotedSongToNextPlayback(String token, PlaybackSPTF playbackSPTF, SongDTO winnerSong)  {

        if(winnerSong == null || StringsUtils.isNullOrEmpty(winnerSong.getUri())){
            return;
        }

        HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);

        UriSPTF playlistUri = playbackSPTF.getContext();
        String playlistId = SpotifyUtils.getUriId(playlistUri.getUri());

        UriSPTF trackUri = UriSPTF.builder().uri(winnerSong.getUri()).build();

        try {
            //trae la lista actual para validación
            PlaylistSPTF playlistSPTF = this.feign.getQueue(headers);
            List<SongSPTF> queue = playlistSPTF.getQueue();

            //checkea si es el proximo en reproducirse, si lo es o está vacía, no hace nada
            if (queue.isEmpty() || trackUri.getUri().equals(queue.get(0).getUri())) {
                return;
            }

            PlaylistTracksSPTF playlistTracks = this.feign.getPlaylist(headers,playlistId);
            List<ItemSPTF> tracks = playlistTracks.getTracks().getItems();
            Optional<ItemSPTF> actualSongOpt = tracks.stream().filter(t -> t.getTrack().getUri().equals(playbackSPTF.getItem().getUri())).findFirst();

            if(actualSongOpt.isPresent()) {

                int position = tracks.indexOf(actualSongOpt.get());
                position++;

                //quitar la canción de la lista
                this.feign.removeSongFromPlaylist(headers,
                        playlistId,
                        TrackUriListSPTF.builder().tracks(List.of(trackUri)).build()
                );

                //agregarla a continuación
                this.addTrackToQueue(headers, List.of(trackUri.getUri()), position, playlistId);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
