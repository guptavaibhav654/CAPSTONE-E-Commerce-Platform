package service;

import model.CartItem;
import model.Product;

import java.util.List;

public interface CartService {

    void addToCart(Product product,int qty);
    void updateQty(int productId,int qty);
    void removeItem(int productId);
    void viewCart();

    double getTotal();
    boolean isEmpty();
    List<CartItem> getItems();
    void clear();
}
