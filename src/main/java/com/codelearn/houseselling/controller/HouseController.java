package com.codelearn.houseselling.controller;

import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.service.HouseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @PostMapping
    public House createHouse(@Valid @RequestBody House house) {
        return houseService.createHouse(house);
    }

    @GetMapping
    public List<House> getAllHouses() {
        return houseService.getAllHouses();
    }

    @GetMapping("/{id}")
    public House getHouseById(@PathVariable Long id) {
        return houseService.getHouseById(id);
    }

    @PutMapping("/{id}")
    public House updateHouse(
            @PathVariable Long id,
            @Valid @RequestBody House house) {

        return houseService.updateHouse(id, house);
    }

    @DeleteMapping("/{id}")
    public String deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return "House deleted successfully";
    }
}