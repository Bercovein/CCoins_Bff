package com.ccoins.bff.dto.bars;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDTO {

    private Long id;
    private String name;
    private String rules;
    private Long points;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private Long bar;
    private GameTypeDTO gameType;
}
