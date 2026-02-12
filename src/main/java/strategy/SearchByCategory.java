package strategy;

import model.Product;
import java.util.List;
import java.util.stream.Collectors;

public class SearchByCategory implements SearchStrategy {

    @Override
    public List<Product> search(List<Product> products,String keyword){
        return products.stream()
                .filter(p->p.getCategory().equalsIgnoreCase(keyword))
                .collect(Collectors.toList());
    }
}
