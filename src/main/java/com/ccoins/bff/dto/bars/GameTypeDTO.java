package com.ccoins.bff.dto.bars;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameTypeDTO {

    private Long id;
    private String name;
}
