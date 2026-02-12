package builder;

import model.CartItem;
import model.Order;
import model.User;

import java.util.List;

public class OrderBuilder {

    private User user;
    private List<CartItem> items;
    private double total;

    public OrderBuilder setUser(User user){
        this.user=user;
        return this;
    }

    public OrderBuilder setItems(List<CartItem> items){
        this.items=items;
        return this;
    }

    public OrderBuilder setTotal(double total){
        this.total=total;
        return this;
    }

    public Order build(){
        return new Order(user,items,total);
    }
}
