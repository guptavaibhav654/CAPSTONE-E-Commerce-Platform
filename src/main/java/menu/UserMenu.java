package menu;

import model.User;
import service.*;

import java.util.Scanner;

public class UserMenu {

    public static void show(User user, ProductService productService, CartService cartService, OrderService orderService, Scanner sc){
        while(true){

            System.out.println("""
                ===== USER MENU =====
                1. View Products
                2. Add to Cart
                3. View Cart
                4. Remove Item
                5. Place Order
                6. Order History
                7. Logout
                """);

            int ch=get(sc);

            switch(ch){

                case 1 -> productService.getAllProducts()
                        .forEach(System.out::println);

                case 2 -> {
                    System.out.print("Product id: ");
                    int id=get(sc);

                    productService.getProductById(id).ifPresentOrElse(p->{
                        System.out.print("Qty: ");
                        int q=get(sc);
                        cartService.addToCart(p,q);
                    },()-> System.out.println("Not found"));
                }

                case 3 -> cartService.viewCart();

                case 4 -> {
                    System.out.print("Product id: ");
                    int id=get(sc);
                    cartService.removeItem(id);
                }

                case 5 -> orderService.placeOrder(user);

                case 6 -> orderService.viewOrders(user);

                case 7 -> { return; }

                default -> System.out.println("Invalid");
            }
        }
    }

    private static int get(Scanner sc){
        while(!sc.hasNextInt()){
            System.out.print("Enter number: ");
            sc.next();
        }
        int c=sc.nextInt();
        sc.nextLine();
        return c;
    }
}
