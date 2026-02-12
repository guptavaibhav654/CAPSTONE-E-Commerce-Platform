package service.impl;

import model.Order;
import model.User;
import repository.OrderRepository;
import service.CartService;
import service.OrderService;
import builder.OrderBuilder;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository repo;
    private final CartService cart;

    public OrderServiceImpl(OrderRepository repo, CartService cart){
        this.repo = repo;
        this.cart = cart;
    }

    @Override
    public void placeOrder(User user){

        if(cart.isEmpty()){
            System.out.println("Cart empty");
            return;
        }

        Order order = new OrderBuilder()
                .setUser(user)
                .setItems(cart.getItems())
                .setTotal(cart.getTotal())
                .build();
        repo.save(user.getId(),order);
        cart.clear();

        System.out.println("Order placed");
    }

    @Override
    public void viewOrders(User user){
        List<Order> orders = repo.findByUser(user.getId());

        if(orders.isEmpty()){
            System.out.println("No orders");
            return;
        }

        orders.forEach(System.out::println);
    }
}
