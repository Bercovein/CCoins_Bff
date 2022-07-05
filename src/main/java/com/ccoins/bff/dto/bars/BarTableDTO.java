package com.ccoins.bff.dto.bars;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ccoins.bff.utils.DateUtils.DDMMYYYY_HHMM;

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

    @JsonFormat(pattern = DDMMYYYY_HHMM)
    private LocalDateTime startDate;

}
