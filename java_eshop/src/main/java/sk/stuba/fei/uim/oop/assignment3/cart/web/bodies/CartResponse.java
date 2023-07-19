package sk.stuba.fei.uim.oop.assignment3.cart.web.bodies;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.assignment3.cart.data.ShoppingCart;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CartResponse {
    private boolean payed;
    private long id;
    private List<CartEntry> shoppingList;

    public CartResponse(ShoppingCart shoppingCart) {
        this.payed = shoppingCart.isPaid();
        this.id = shoppingCart.getId();
        this.shoppingList = shoppingCart.getShoppingList().stream().map(CartEntry::new).collect(Collectors.toList());
    }
}
