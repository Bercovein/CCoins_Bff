package com.ccoins.Bff.dto.prizes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WinnerDTO {


    private Long id;

    private PrizeDTO prize;

    private Long points;

    private LocalDateTime startDate;

    private boolean active;
}
