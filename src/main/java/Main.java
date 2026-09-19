import menu.*;
import model.User;
import repository.*;
import service.*;
import service.impl.*;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // repositories
        UserRepository userRepo = new UserRepository();
        ProductRepository productRepo = new ProductRepository();
        OrderRepository orderRepo = new OrderRepository();

        // services (DIP)
        CartService cartService = new CartServiceImpl();
        UserService userService = new UserServiceImpl(userRepo);
        ProductService productService = new ProductServiceImpl(productRepo);
        OrderService orderService = new OrderServiceImpl(orderRepo,cartService);

        while(true){

            WelcomeMenu.show();
            int ch=get(sc);

            switch(ch){

                case 1 -> {
                    System.out.print("Email: ");
                    String email=sc.nextLine();
                    System.out.print("Password: ");
                    String pass=sc.nextLine();

                    // admin login
                    if(email.equals("admin@gmail.com") && pass.equals("admin123")){
                        System.out.println("Admin login success");
                        AdminMenu.show(productService,sc);
                        break;
                    }

                    Optional<User> user = userService.login(email,pass);

                    if(user.isPresent()){
                        cartService.clear();
                        UserMenu.show(user.get(),productService,cartService,orderService,sc);
                    }else{
                        System.out.println("Invalid login");
                    }
                }

                case 2 -> {
                    System.out.print("Name: ");
                    String n=sc.nextLine();
                    System.out.print("Email: ");
                    String e=sc.nextLine();
                    System.out.print("Password: ");
                    String p=sc.nextLine();

                    if(userService.register(n,e,p))
                        System.out.println("Registered");
                }

                case 3 -> {
                    System.out.println("Bye");
                    return;
                }
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
