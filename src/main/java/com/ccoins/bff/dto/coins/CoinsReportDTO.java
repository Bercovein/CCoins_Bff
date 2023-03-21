package com.ccoins.bff.dto.coins;

import com.ccoins.bff.decoder.RestPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoinsReportDTO {

    private Long totalCoins;
    private RestPage<CoinsReport> report;
}
