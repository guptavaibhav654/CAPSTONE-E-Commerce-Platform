package strategy;

import model.Product;
import java.util.List;
import java.util.stream.Collectors;

public class SearchByPriceRange implements SearchStrategy {

    private double min;
    private double max;

    public SearchByPriceRange(double min,double max){
        this.min=min;
        this.max=max;
    }

    @Override
    public List<Product> search(List<Product> products,String keyword){

        return products.stream()
                .filter(p -> p.getPrice()>=min && p.getPrice()<=max)
                .collect(Collectors.toList());
    }
}
