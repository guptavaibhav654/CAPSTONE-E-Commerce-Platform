package service.impl;

import model.Product;
import repository.ProductRepository;
import service.ProductService;

import java.util.Collection;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo){
        this.repo = repo;
    }

    @Override
    public void addProduct(String name,String category,double price){
        repo.save(name,category,price);
        System.out.println("Product added");
    }

    @Override
    public void updateProduct(int id,String name,String category,double price){
        repo.update(id,name,category,price);
        System.out.println("Product updated");
    }

    @Override
    public void deleteProduct(int id){
        repo.delete(id);
        System.out.println("Product deleted");
    }

    @Override
    public Collection<Product> getAllProducts(){
        return repo.findAll();
    }

    @Override
    public Optional<Product> getProductById(int id){
        return repo.findById(id);
    }
}
