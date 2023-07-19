package sk.stuba.fei.uim.oop.assignment3.cart.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.stuba.fei.uim.oop.assignment3.cart.data.ShoppingCart;
import sk.stuba.fei.uim.oop.assignment3.cart.data.ShoppingCartRepository;
import sk.stuba.fei.uim.oop.assignment3.cart.web.bodies.CartEntry;
import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.product.logic.IProductService;

import java.util.List;

@Service
public class ShoppingCartService implements IShoppingCartService {

    @Autowired
    private ShoppingCartRepository repository;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICartEntryService cartEntryService;

    @Override
    @Transactional
    public ShoppingCart create() {
        ShoppingCart cart = new ShoppingCart();
        cart.setPaid(false);
        return repository.save(cart);
    }
    @Override
    public ShoppingCart getById(long id) throws NotFoundException {
        ShoppingCart cart = this.repository.findShoppingCartById(id);
        if (cart == null) {
            throw new NotFoundException();
        }
        return cart;
    }

    @Override
    @Transactional
    public void delete(long id) throws NotFoundException {
        ShoppingCart cartNew = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        repository.delete(cartNew);
    }

    @Transactional
    public ShoppingCart getUnpaid(long id) throws NotFoundException, IllegalOperationException {
        ShoppingCart cart = repository.findById(id).orElseThrow(NotFoundException::new);
        if (cart.isPaid()) {
            throw new IllegalOperationException();
        }
        return cart;
    }

    @Override
    public ShoppingCart addToCart(long cartId, CartEntry body) throws NotFoundException, IllegalOperationException {
        ShoppingCart shoppingcart = this.getUnpaid(cartId);
        this.productService.removeAmount(body.getProductId(), body.getAmount());
        var exists = this.findCartEntryWithProduct(shoppingcart.getShoppingList(), body.getProductId());
        if (exists == null) {
            sk.stuba.fei.uim.oop.assignment3.cart.data.CartEntry cartEntry = cartEntryService.create();
            cartEntry.setAmount(body.getAmount());
            cartEntry.setProduct(productService.getById(body.getProductId()));
            shoppingcart.getShoppingList().add(cartEntryService.save(cartEntry));
        } else {
            exists.setAmount(exists.getAmount() + body.getAmount());
            cartEntryService.save(exists);
        }
        return this.repository.save(shoppingcart);
    }

    @Override
    @Transactional
    public double payForCart(long id) throws NotFoundException, IllegalOperationException {
        ShoppingCart cart = repository.findById(id).orElseThrow(NotFoundException::new);
        if (cart.isPaid()) {
            throw new IllegalOperationException();
        }
        double sum = cart.getShoppingList().stream()
                .mapToDouble(entry -> entry.getProduct().getPrice() * entry.getAmount())
                .sum();
        cart.setPaid(true);
        repository.save(cart);
        return sum;
    }
    private sk.stuba.fei.uim.oop.assignment3.cart.data.CartEntry findCartEntryWithProduct(List<sk.stuba.fei.uim.oop.assignment3.cart.data.CartEntry> entries, long productId) {
        return entries.stream()
                .filter(entry -> entry.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

}