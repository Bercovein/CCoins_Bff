package com.ccoins.bff.dto.coins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoinsDTO {

    private Long id;
    private LocalDateTime dateTime;
    private Long quantity;
    private boolean active;
    private Long client;
    private Long party;
    private String state;
}
