package repository;

import model.Order;

import java.util.*;

public class OrderRepository {

    private final Map<Integer, List<Order>> orders = new HashMap<>();

    public void save(int userId, Order order){
        orders.computeIfAbsent(userId,k->new ArrayList<>()).add(order);
    }

    public List<Order> findByUser(int userId){
        return orders.getOrDefault(userId,new ArrayList<>());
    }
}
