package com.ccoins.bff.dto.coins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoinStateDTO {

    private Long id;
    private String name;
}
