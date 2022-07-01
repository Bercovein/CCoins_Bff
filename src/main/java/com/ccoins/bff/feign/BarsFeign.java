package com.ccoins.bff.feign;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongListDTO;
import com.ccoins.bff.dto.bars.*;
import com.ccoins.bff.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "${feign.bars-ms.name}", url = "${feign.bars-ms.url}")
@RequestMapping("${feign.bars-ms.req-map}")
public interface BarsFeign {

    //BARS
    @PostMapping("/bars")
    ResponseEntity<BarDTO> saveOrUpdateBar(@RequestBody BarDTO barDTO);

    @GetMapping("/bars/owner/{ownerId}")
    ResponseEntity<ListDTO> findAllBarsByOwner(@PathVariable("ownerId") Long ownerId);

    @GetMapping("/bars/{id}")
    ResponseEntity<BarDTO> findBarById(@PathVariable("id") Long id);

    @PutMapping("/bars/{id}/active")
    ResponseEntity<BarDTO> activeBar(@PathVariable("id") Long id);


    //TABLES
    @PostMapping("/tables")
    ResponseEntity<TableDTO> saveOrUpdateTable(@RequestBody TableDTO tableDTO);

    @GetMapping("/tables/bar/{barId}")
    ResponseEntity<ListDTO> findAllTablesByBar(@PathVariable("barId") Long barId);

    @GetMapping("/tables/bar/{barId}/{status}")
    ResponseEntity<ListDTO> findAllTablesByBarAndOptStatus(
            @PathVariable("barId") Long barId,
            @PathVariable("status") Optional<String> status);

    @GetMapping("/tables/{id}")
    ResponseEntity<TableDTO> findTableById(@PathVariable("id") Long id);

    @PutMapping("/tables/{id}/active")
    ResponseEntity<TableDTO> activeTable(@PathVariable("id") Long id);

    @PostMapping("/tables/quantity")
    ResponseEntity<GenericRsDTO> createByQuantity(@RequestBody TableQuantityDTO request);

    @DeleteMapping("/tables/quantity")
    ResponseEntity<GenericRsDTO> deleteByQuantity(@RequestBody TableQuantityDTO request);

    @PutMapping("/tables")
    ResponseEntity<GenericRsDTO> activeByList(@RequestBody LongListDTO request);

    @PutMapping("/tables/codes")
    ResponseEntity<ResponseDTO> generateCodesByList(@RequestBody LongListDTO request);

    @PostMapping("/tables/list")
    List<BarTableDTO> findByIdIn(@RequestBody LongListDTO request);

    //GAMES
    @PostMapping("/games")
    ResponseEntity<GameDTO> saveOrUpdateGame(@RequestBody GameDTO barDTO);

    @GetMapping("/games/bar/{id}")
    ResponseEntity<ListDTO> findAllGamesByBar(@PathVariable("id") Long id);

    @GetMapping("/games/{id}")
    ResponseEntity<GameDTO> findGameById(@PathVariable("id") Long id);

    @PutMapping("/games/{id}/active")
    ResponseEntity<GameDTO> activeGame(@PathVariable("id") Long id);

    @GetMapping("/games/types")
    ResponseEntity<ListDTO> findAllGamesTypes();

}