package ms.order.service.domain.mock;

import ms.order.service.domain.model.Product;

import java.math.BigDecimal;
import java.util.UUID;


public class ProductMockFactory {

    private Product product;

    public ProductMockFactory() {
        getProduct();
    }

    public ProductMockFactory getProduct() {
        this.product = Product.builder()
                .id(UUID.fromString("4449f3a2-ea5c-411d-87dc-ab6d52ff5bee"))
                .amount(new BigDecimal("149.90"))
                .productName("Mochila Dev")
                .description("Mochila com divisórias para laptop e gadgets")
                .status("ACTIVE")
                .build();
        return this;
    }

    public Product build() {
        return this.product;
    }


    public ProductMockFactory ofId(UUID id) {
        this.product.setId(id);
        return this;
    }

    public ProductMockFactory ofAmount(BigDecimal amount) {
        this.product.setAmount(amount);
        return this;
    }

    public ProductMockFactory ofProductName(String productName) {
        this.product.setProductName(productName);
        return this;
    }

    public ProductMockFactory ofDescription(String description) {
        this.product.setDescription(description);
        return this;
    }

    public ProductMockFactory ofStatus(String status) {
        this.product.setStatus(status);
        return this;
    }
}