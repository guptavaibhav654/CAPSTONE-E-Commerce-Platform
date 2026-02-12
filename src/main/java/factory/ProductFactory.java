package factory;

import model.Product;

public class ProductFactory {

    private static int idCounter = 100;

    public static Product createProduct(String name,String category,double price){
        return new Product(idCounter++,name,category,price);
    }
}
