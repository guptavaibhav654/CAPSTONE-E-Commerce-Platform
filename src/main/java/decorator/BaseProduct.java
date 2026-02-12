package decorator;

public class BaseProduct implements ProductComponent {

    private double price;

    public BaseProduct(double price){
        this.price=price;
    }

    @Override
    public double getPrice(){
        return price;
    }
}
