package com.ccoins.bff.dto.coins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoinsReport {

    private Long coinsId;
    private String date;
    private Long coins;
    private String activity;
    private String client;

}
