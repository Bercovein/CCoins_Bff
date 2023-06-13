package com.ccoins.bff.dto.coins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoinsReportStatesDTO {

    private Long coinsId;
    private String date;
    private Long tableNumber;
    private String prizeName;
    private String state;
    private Boolean updatable;

}
