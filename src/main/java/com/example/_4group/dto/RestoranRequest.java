package com.example._4group.dto;

import jakarta.validation.constraints.NotBlank;

public class RestoranRequest {

    @NotBlank(message = "Restoran adi bos ola bilmez")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}