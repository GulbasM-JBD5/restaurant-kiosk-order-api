package com.example._4group.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class ProductRequest {
      @DecimalMin(value = "0.0", inclusive = false,
              message = "Qiymət 0-dan böyük olmalıdır")
        private BigDecimal price;
    @NotBlank(message="Ad boşola bilməz")
        private String name;
    @NotBlank(message="Aid oldugu kataqoriyani qeyd edin")
        private String category;
    private Long restoranId;

        public String getName() {
            return name;
        }
        public  BigDecimal getPrice() {
            return price;
        }

        public String getCategory() {
            return category;
        }

    public Long getRestoranId() {
        return restoranId;
    }

    public void setName(String name) {
            this.name = name;
        }
        public void setCategory(String category) {
            this.category = category;
        }

        public void setPrice( BigDecimal price) {
            this.price = price;
        }

    public void setRestoran_id(Long restoranId) {
        this.restoranId = restoranId;
    }
}


