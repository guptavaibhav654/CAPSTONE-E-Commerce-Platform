package menu;

import model.User;
import model.Product;
import service.*;
import strategy.*;

import java.util.*;

public class UserMenu {

    public static void show(User user,
                            ProductService productService,
                            CartService cartService,
                            OrderService orderService,
                            Scanner scanner){

        while(true){

            System.out.println("""
                ===== USER MENU =====
                1. View Products
                2. Add to Cart
                3. View Cart
                4. Remove Item
                5. Place Order
                6. Order History
                7. Search Product
                8. Logout
                """);

            int choice = getChoice(scanner);

            switch(choice){

                case 1 -> productService.getAllProducts()
                        .forEach(System.out::println);

                case 2 -> {
                    System.out.print("Enter product id: ");
                    int productId = getChoice(scanner);

                    productService.getProductById(productId).ifPresentOrElse(product->{
                        System.out.print("Enter quantity: ");
                        int qty = getChoice(scanner);
                        cartService.addToCart(product,qty);
                    },()-> System.out.println("Product not found"));
                }

                case 3 -> cartService.viewCart();

                case 4 -> {
                    System.out.print("Enter product id: ");
                    int productId = getChoice(scanner);
                    cartService.removeItem(productId);
                }

                case 5 -> orderService.placeOrder(user);

                case 6 -> orderService.viewOrders(user);

                // 🔥 SEARCH FEATURE (STRATEGY PATTERN)
                case 7 -> {
                    System.out.println("""
                        Search By:
                        1. Name
                        2. Category
                        3. Price Range
                        """);

                    int option = getChoice(scanner);

                    List<Product> allProducts =
                            new ArrayList<>(productService.getAllProducts());

                    switch(option){

                        case 1 -> {
                            System.out.print("Enter product name: ");
                            String name = scanner.nextLine();

                            SearchStrategy strategy = new SearchByName();
                            strategy.search(allProducts,name)
                                    .forEach(System.out::println);
                        }

                        case 2 -> {
                            System.out.print("Enter category: ");
                            String category = scanner.nextLine();

                            SearchStrategy strategy = new SearchByCategory();
                            strategy.search(allProducts,category)
                                    .forEach(System.out::println);
                        }

                        case 3 -> {
                            System.out.print("Enter minimum price: ");
                            double min = scanner.nextDouble();
                            System.out.print("Enter maximum price: ");
                            double max = scanner.nextDouble();
                            scanner.nextLine();

                            SearchStrategy strategy =
                                    new SearchByPriceRange(min,max);

                            strategy.search(allProducts,"")
                                    .forEach(System.out::println);
                        }

                        default -> System.out.println("Invalid search option");
                    }
                }

                case 8 -> {
                    System.out.println("Logged out");
                    return;
                }

                default -> System.out.println("Invalid option");
            }
        }
    }

    private static int getChoice(Scanner scanner){
        while(!scanner.hasNextInt()){
            System.out.print("Enter valid number: ");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
}
