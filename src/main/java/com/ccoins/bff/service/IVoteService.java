package com.ccoins.bff.service;

import com.ccoins.bff.dto.coins.SongDTO;
import com.ccoins.bff.dto.coins.VotingDTO;
import com.ccoins.bff.spotify.sto.SongSPTF;

import java.util.List;

public interface IVoteService {

    VotingDTO getActualVotingByBar(Long barId);

    SongDTO resolveVoting(Long barId);

    VotingDTO createNewVoting(Long barId, List<SongSPTF> list);
}
