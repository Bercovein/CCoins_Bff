package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.GameDTO;
import org.springframework.http.ResponseEntity;

public interface IGamesService {

    ResponseEntity<GameDTO> saveOrUpdate(GameDTO barDTO);

    ResponseEntity<GameDTO> findById(IdDTO id);

    ResponseEntity<ListDTO> findAllByBar(Long id);

    ResponseEntity<GameDTO> active(IdDTO id);

    ResponseEntity<ListDTO> findAllGamesTypes();
}
