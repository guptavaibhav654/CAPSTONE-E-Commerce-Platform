package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private static int counter=1;

    private int orderId;
    private User user;
    private List<CartItem> items;
    private double total;
    private LocalDateTime date;

    public Order(User user,List<CartItem> items,double total){
        this.orderId=counter++;
        this.user=user;
        this.items=new ArrayList<>(items);
        this.total=total;
        this.date=LocalDateTime.now();
    }

    public int getOrderId(){ return orderId; }

    @Override
    public String toString(){
        return "Order#"+orderId+" | "+user.getName()+" | ₹"+total+" | "+date;
    }
}
