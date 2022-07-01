package com.ccoins.bff.service;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.bars.GameDTO;
import org.springframework.http.ResponseEntity;

public interface IGamesService {

    ResponseEntity<GameDTO> saveOrUpdate(GameDTO barDTO);

    ResponseEntity<GameDTO> findById(IdDTO id);

    ResponseEntity<ListDTO> findAllByBar(Long id);

    ResponseEntity<GameDTO> active(IdDTO id);

    ResponseEntity<ListDTO> findAllGamesTypes();
}
