package com.ccoins.bff.dto.bars;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BarHourDTO {

    private Long id;
    private Long barId;
    private DayDTO day;
    private LocalTime openTime;
    private LocalTime closeTime;
}
