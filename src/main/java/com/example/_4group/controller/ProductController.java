
package com.example._4group.controller;

import com.example._4group.dto.ProductPriceRequest;
import com.example._4group.dto.ProductRequest;
import com.example._4group.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> addProduct(
            @Valid @RequestBody ProductRequest request) {

        productService.addProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Məhsul uğurla yaradıldı");
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<String> updatePrice(
            @PathVariable Long id,
            @Valid @RequestBody ProductPriceRequest request) {

        productService.updatePrice(request, id);

        return ResponseEntity.ok("Məhsulun qiyməti yeniləndi");
    }

    @PutMapping("/{id}/remove-from-sale")
    public ResponseEntity<String> removeFromSale(
            @PathVariable Long id) {

        productService.removeFromSale(id);

        return ResponseEntity.ok("Məhsul satışdan çıxarıldı");
    }

    @PutMapping("/{id}/return-to-sale")
    public ResponseEntity<String> returnToSale(
            @PathVariable Long id) {

        productService.returnToSale(id);

        return ResponseEntity.ok(
                "Məhsul yenidən satışa qaytarıldı");
    }
}

