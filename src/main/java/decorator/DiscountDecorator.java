package decorator;

public abstract class DiscountDecorator implements ProductComponent {

    protected ProductComponent product;

    public DiscountDecorator(ProductComponent product){
        this.product=product;
    }
}
