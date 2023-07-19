package sk.stuba.fei.uim.oop.assignment3.product.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;
import sk.stuba.fei.uim.oop.assignment3.product.logic.IProductService;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.Amount;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductRequest;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductResponse;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProdUpReq;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService service;

    public ProductController(IProductService service) {
        this.service = service;
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductResponse> getAllProducts() {
        List<Product> productList = service.getAll();
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product product: productList) {
            productResponseList.add(new ProductResponse(product));
        }
        return productResponseList;
    }
@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest) {
    Product product = new Product();
    product.setName(productRequest.getName());
    product.setDescription(productRequest.getDescription());
    product.setAmount(productRequest.getAmount());
    product.setUnit(productRequest.getUnit());
    product.setPrice(productRequest.getPrice());
    Product createdProduct = service.create(productRequest);
    ProductResponse productResponse = new ProductResponse(createdProduct);
    return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
}
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) throws NotFoundException {
        Product product = service.getById(id);
        ProductResponse productResponse = new ProductResponse(product);
        return ResponseEntity.ok(productResponse);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProdUpReq request) {
        Product updatedProduct = null;
        try {
            updatedProduct = service.update(id, request);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        ProductResponse updatedProductResponse = new ProductResponse(updatedProduct);
        return ResponseEntity.ok(updatedProductResponse);
    }
    @PostMapping(value = "/{id}/amount", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Amount addAmount(@PathVariable("id") Long productId, @RequestBody Amount body) throws NotFoundException {
        return new Amount(this.service.addAmount(productId, body.getAmount()));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        try {
            this.service.delete(productId);
            return ResponseEntity.ok("Product deleted successfully!");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product!");
        }
    }
    @GetMapping(value = "/{id}/amount", produces = MediaType.APPLICATION_JSON_VALUE)
    public Amount getAmount(@PathVariable("id") Long productId) throws NotFoundException {
        return new Amount(this.service.getAmount(productId));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        String errorMessage = "Not found: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

}

