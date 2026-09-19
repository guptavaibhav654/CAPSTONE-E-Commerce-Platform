package service;

import model.User;

public interface OrderService {

    void placeOrder(User user);
    void viewOrders(User user);
}
