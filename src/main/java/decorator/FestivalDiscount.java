package decorator;

public class FestivalDiscount extends DiscountDecorator {

    public FestivalDiscount(ProductComponent product){
        super(product);
    }

    @Override
    public double getPrice(){
        return product.getPrice()*0.9; // 10% discount
    }
}
