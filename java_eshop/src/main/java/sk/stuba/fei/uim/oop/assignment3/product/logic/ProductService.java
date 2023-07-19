package sk.stuba.fei.uim.oop.assignment3.product.logic;
import org.springframework.transaction.annotation.Transactional;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.product.data.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductRequest;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProdUpReq;
import java.util.List;


@Service
public class ProductService implements IProductService {
    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Product create(ProductRequest request) {
        return this.repository.save(new Product(request));
    }

    @Override
    public Product getById(long id) throws NotFoundException {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Product update(long id, ProdUpReq request) throws NotFoundException {
        Product product = this.repository.findById(id)
                .orElseThrow(NotFoundException::new);

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        return this.repository.save(product);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        this.repository.delete(this.getById(id));
    }

    @Override
    public long getAmount(long id) throws NotFoundException {
        return this.getById(id).getAmount();
    }

    @Override
    public long setAmount(long id, long newAmount) {
        return 0;
    }

    @Transactional
    public long addAmount(long id, long increment) throws NotFoundException {
        Product product = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        long updatedAmount = product.getAmount() + increment;
        product.setAmount(updatedAmount);
        repository.save(product);
        return updatedAmount;
    }

    @Override
    @Transactional
    public void removeAmount(long id, long decrement) throws NotFoundException, IllegalOperationException {
        Product product = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        long updatedAmount = product.getAmount() - decrement;
        if (updatedAmount < 0) {
            throw new IllegalOperationException();
        }
        product.setAmount(updatedAmount);
        repository.save(product);
    }


}
