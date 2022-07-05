package com.ccoins.bff.dto.prizes;

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
public class PartyDTO {

    private Long id;

    private String name;

    @JsonFormat(pattern = DDMMYYYY_HHMM)
    private LocalDateTime startDate;

    private boolean active;

    private Long table;
}
