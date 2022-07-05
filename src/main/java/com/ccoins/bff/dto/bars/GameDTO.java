package com.ccoins.bff.dto.bars;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

import static com.ccoins.bff.utils.DateUtils.HH_MM;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDTO {

    private Long id;
    private String name;
    private String rules;
    private Long points;

    @JsonFormat(pattern = HH_MM)
    private LocalTime openTime;

    @JsonFormat(pattern = HH_MM)
    private LocalTime closeTime;
    private boolean active;
    private Long bar;
    private GameTypeDTO gameType;
}
