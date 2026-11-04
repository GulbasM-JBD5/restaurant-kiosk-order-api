package com.example._4group.dto;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class ProductPriceRequest {

    @DecimalMin(value = "0.0", inclusive = false,
            message = "Qiymət 0-dan böyük olmalıdır")
    private BigDecimal price;

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}