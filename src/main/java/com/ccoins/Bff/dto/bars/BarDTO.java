package com.ccoins.Bff.dto.bars;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BarDTO {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    private String menuLink;

    private Long owner;
}
