package service.impl;

import model.CartItem;
import model.Product;
import service.CartService;

import java.util.*;

public class CartServiceImpl implements CartService {

    private final Map<Integer, CartItem> cart = new HashMap<>();

    @Override
    public void addToCart(Product product,int qty){

        if(qty<=0){
            System.out.println("Invalid quantity");
            return;
        }

        cart.compute(product.getId(),(k,v)->{
            if(v==null) return new CartItem(product,qty);
            v.setQty(v.getQty()+qty);
            return v;
        });

        System.out.println("Added to cart");
    }

    @Override
    public void updateQty(int productId,int qty){
        CartItem item = cart.get(productId);

        if(item==null){
            System.out.println("Not in cart");
            return;
        }

        if(qty<=0){
            cart.remove(productId);
            System.out.println("Item removed");
            return;
        }

        item.setQty(qty);
        System.out.println("Updated");
    }

    @Override
    public void removeItem(int productId){
        cart.remove(productId);
        System.out.println("Removed");
    }

    @Override
    public void viewCart(){
        if(cart.isEmpty()){
            System.out.println("Cart empty");
            return;
        }

        cart.values().forEach(System.out::println);
        System.out.println("Total: ₹"+getTotal());
    }

    @Override
    public double getTotal(){
        return cart.values()
                .stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    @Override
    public boolean isEmpty(){
        return cart.isEmpty();
    }

    @Override
    public List<CartItem> getItems(){
        return new ArrayList<>(cart.values());
    }

    @Override
    public void clear(){
        cart.clear();
    }
}
