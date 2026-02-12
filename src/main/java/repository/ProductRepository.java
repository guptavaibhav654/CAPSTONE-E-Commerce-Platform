package repository;

import model.Product;
import factory.ProductFactory;
import java.util.*;

public class ProductRepository {

    private final Map<Integer, Product> products = new HashMap<>();
    private int idCounter = 5;

    public ProductRepository(){
        products.put(1,new Product(1,"HP Laptop","Electronics",55000));
        products.put(2,new Product(2,"iPhone 15","Mobile",150000));
        products.put(3,new Product(3,"Keyboard","Accessories",800));
        products.put(4,new Product(4,"Monitor","Electronics",12000));
    }

    public void save(String name,String category,double price){
        Product p = ProductFactory.createProduct(name,category,price);
        products.put(p.getId(),p);
    }

    public Collection<Product> findAll(){
        return products.values();
    }

    public Optional<Product> findById(int id){
        return Optional.ofNullable(products.get(id));
    }

    public void delete(int id){
        products.remove(id);
    }

    public void update(int id,String name,String category,double price){
        Product p = products.get(id);
        if(p!=null){
            p.setName(name);
            p.setCategory(category);
            p.setPrice(price);
        }
    }
}
