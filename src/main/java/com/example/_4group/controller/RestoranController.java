package com.example._4group.controller;

import com.example._4group.dto.RestoranRequest;
import com.example._4group.entity.Restoran;
import com.example._4group.repository.RestoranRepository;

import com.example._4group.service.RestoranService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestoranController {

    private final RestoranService restoranService;

    public RestoranController(RestoranService restoranService) {
        this.restoranService = restoranService;
    }

    @PostMapping
    public ResponseEntity<Restoran> createRestaurant(
            @Valid @RequestBody RestoranRequest request) {

        Restoran savedRestoran =
                restoranService.createRestaurant(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedRestoran);
    }
}
