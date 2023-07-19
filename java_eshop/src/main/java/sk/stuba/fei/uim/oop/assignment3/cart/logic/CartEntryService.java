package sk.stuba.fei.uim.oop.assignment3.cart.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.cart.data.CartEntry;
import sk.stuba.fei.uim.oop.assignment3.cart.data.CartEntryRepository;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;

@Service
public class CartEntryService implements ICartEntryService {

    private final CartEntryRepository repository;

    @Autowired
    public CartEntryService(CartEntryRepository repository) {
        this.repository = repository;
    }
    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }
    @Override
    public CartEntry create() {
        return repository.save(new CartEntry());
    }
    @Override
    public CartEntry save(CartEntry cartEntry) {
        return repository.save(cartEntry);
    }
    @Override
    public CartEntry getById(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

}
