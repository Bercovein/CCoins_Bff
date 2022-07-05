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
public class BarTableDTO {

    private Long id;

    private Long number;

    private boolean active;

    private Long bar;

    private String code;

    private LocalDateTime startDate;

}
