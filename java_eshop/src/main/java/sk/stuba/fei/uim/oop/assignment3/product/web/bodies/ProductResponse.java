package sk.stuba.fei.uim.oop.assignment3.product.web.bodies;

import lombok.Getter;
import org.springframework.beans.BeanUtils;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;

@Getter
public class ProductResponse {
    private final long id;
    private final long amount;
    private final String unit;
    private final double price;
    private final String name;
    private final String description;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.amount = product.getAmount();
        this.unit = product.getUnit();
        this.price = product.getPrice();
        this.name = product.getName();
        this.description = product.getDescription();
    }


}
