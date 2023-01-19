package com.ccoins.bff.dto.coins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VotingDTO {
    private Long id;
    private SongDTO winnerSong;
    private List<SongDTO> songs;
    private MatchDTO match;
}
