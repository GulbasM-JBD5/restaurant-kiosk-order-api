package com.example._4group.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;
    @NotBlank(message="Ad boşola bilməz")
    private String name;
    @NotBlank(message="Aid oldugu kataqoriyani qeyd edin")
    private String category;
    @ManyToOne(fetch = FetchType.LAZY)
    private Restoran restoran;
    private boolean available=true;
    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public Restoran getRestoran() {
        return restoran;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setRestoran(Restoran restoran) {
        this.restoran = restoran;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

