package com.ccoins.bff.service;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.coins.SongDTO;
import com.ccoins.bff.dto.coins.VotingDTO;
import com.ccoins.bff.spotify.sto.SongSPTF;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface IVoteService {

    VotingDTO getActualVotingByBar(Long barId);

    SongDTO resolveVoting(Long barId);

    VotingDTO createNewVoting(Long barId, List<SongSPTF> list);

    void voteSong(HttpHeaders headers, IdDTO request);
}
