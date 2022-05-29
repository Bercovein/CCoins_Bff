package com.ccoins.Bff.feign;

import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarDTO;
import com.ccoins.Bff.dto.bars.TableDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/bars/{id}/active")
    ResponseEntity<BarDTO> activeBar(@PathVariable("id") Long id);

    //TABLES

    @PostMapping
    ResponseEntity<TableDTO> saveOrUpdateTable(@RequestBody TableDTO tableDTO);

    @GetMapping("/tables/bar/{barId}")
    ResponseEntity<ListDTO> findAllTablesByBar(@PathVariable("barId") Long barId);

    @GetMapping("/tables/{id}")
    ResponseEntity<TableDTO> findTableById(@PathVariable("id") Long id);

    @GetMapping("/tables/{id}/active")
    ResponseEntity<TableDTO> activeTable(@PathVariable("id") Long id);
}
