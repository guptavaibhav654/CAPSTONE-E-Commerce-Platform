package model;

public class CartItem {

    private Product product;
    private int qty;

    public CartItem(Product product,int qty){
        this.product=product;
        this.qty=qty;
    }

    public Product getProduct(){ return product; }
    public int getQty(){ return qty; }
    public void setQty(int qty){ this.qty=qty; }

    public double getTotal(){
        return product.getPrice()*qty;
    }

    @Override
    public String toString(){
        return product.getName()+" x "+qty+" = ₹"+getTotal();
    }
}