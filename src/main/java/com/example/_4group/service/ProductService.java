package com.example._4group.service;

import com.example._4group.dto.ProductPriceRequest;
import com.example._4group.dto.ProductRequest;
import com.example._4group.entity.Product;
import com.example._4group.entity.Restoran;
import com.example._4group.repository.ProductRepository;
import com.example._4group.repository.RestoranRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {
    ProductRepository productRepository;
    RestoranRepository restoranRepository;
    public  ProductService(ProductRepository productRepository, RestoranRepository
            restoranRepository   ){
        this.productRepository=productRepository;
        this.restoranRepository=restoranRepository;
    }
    public void addProduct( ProductRequest request){
        Product product=new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        Restoran restoran=restoranRepository.findById(request.getRestoranId()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Restoran tapılmadı!"
        ));
        product.setRestoran(restoran);
        productRepository.save(product);
    }
    public void updatePrice(ProductPriceRequest request,Long id){
        Product product=productRepository.findById(id).orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND,
                "Mehsul tapilmadi"));
        product.setPrice(request.getPrice());
    }
    public void removeFromSale(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Məhsul tapılmadı!"
                ));

        product.setAvailable(false);
        productRepository.save(product);
    }

    public void returnToSale(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Məhsul tapılmadı!"
                ));

        product.setAvailable(true);
        productRepository.save(product);
    }



}
