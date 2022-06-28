package com.ccoins.Bff.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RowToPdfDTO {

    private Long numberLeft;
    private InputStream imageLeft;

    private Long numberMedium;
    private InputStream imageMedium;

    private Long numberRight;
    private InputStream imageRight;
}
