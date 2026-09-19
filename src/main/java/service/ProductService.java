package service;

import model.Product;

import java.util.Collection;
import java.util.Optional;

public interface ProductService {

    void addProduct(String name,String category,double price);
    void updateProduct(int id,String name,String category,double price);
    void deleteProduct(int id);

    Collection<Product> getAllProducts();
    Optional<Product> getProductById(int id);
}
