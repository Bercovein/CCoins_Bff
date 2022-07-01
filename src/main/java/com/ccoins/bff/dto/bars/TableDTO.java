package com.ccoins.bff.dto.bars;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableDTO {

    private Long id;

    private Long number;

    private boolean active;

    private Long bar;
}
