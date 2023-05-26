package com.ccoins.bff.spotify.sto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeDTO {

    private Long id;
    private Long matchId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String code;
    private Long points;
    private Long prize;
    private String prizeName;
    private boolean perPerson;
    private boolean oneUse;
    private boolean active;
    private String state;
    private boolean closeable;
    private boolean editable;
    private Long game;

}
