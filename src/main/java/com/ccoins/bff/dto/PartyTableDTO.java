package com.ccoins.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PartyTableDTO {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private boolean active;
    private Long table;
    private Long tableNumber;

}
