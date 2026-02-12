package menu;

import service.ProductService;
import decorator.*;

import java.util.Scanner;

public class AdminMenu {

    public static void show(ProductService productService, Scanner sc){

        while(true){

            System.out.println("""
                ===== ADMIN MENU =====
                1. View Products
                2. Add Product
                3. Update Product
                4. Delete Product
                5. Check Price with GST (Decorator Demo)
                6. Logout
                """);

            int ch = get(sc);

            switch(ch){

                case 1 -> productService.getAllProducts()
                        .forEach(System.out::println);

                case 2 -> {
                    System.out.print("Name: ");
                    String n=sc.nextLine();
                    System.out.print("Category: ");
                    String c=sc.nextLine();
                    System.out.print("Price: ");
                    double p=sc.nextDouble(); sc.nextLine();

                    productService.addProduct(n,c,p);
                }

                case 3 -> {
                    System.out.print("Id: ");
                    int id=get(sc);
                    System.out.print("Name: ");
                    String n=sc.nextLine();
                    System.out.print("Category: ");
                    String c=sc.nextLine();
                    System.out.print("Price: ");
                    double p=sc.nextDouble(); sc.nextLine();

                    productService.updateProduct(id,n,c,p);
                }

                case 4 -> {
                    System.out.print("Id: ");
                    int id=get(sc);
                    productService.deleteProduct(id);
                }

                case 5 -> {
                    // Decorator demo
                    System.out.print("Enter base price: ");
                    double price = sc.nextDouble(); sc.nextLine();

                    ProductComponent product = new BaseProduct(price);
                    product = new FestivalDiscount(product);

                    System.out.println("Final price with GST: ₹"+product.getPrice());
                }

                case 6 -> { return; }

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
