package com.example._4group.service;

import com.example._4group.dto.RestoranRequest;
import com.example._4group.entity.Restoran;
import com.example._4group.repository.RestoranRepository;
import org.springframework.stereotype.Service;

@Service
public class RestoranService {

    private final RestoranRepository restoranRepository;

    public RestoranService(RestoranRepository restoranRepository) {
        this.restoranRepository = restoranRepository;
    }

    public Restoran createRestaurant(RestoranRequest request) {
        Restoran restoran=new Restoran();
        restoran.setName(request.getName());
        return restoranRepository.save(restoran);
    }
}
