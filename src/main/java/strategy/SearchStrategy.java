package strategy;

import model.Product;
import java.util.List;

public interface SearchStrategy {
    List<Product> search(List<Product> products,String keyword);
}