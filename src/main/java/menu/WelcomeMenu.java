package menu;

public class WelcomeMenu {

    public static void show(){
        System.out.println("""
                =========================
                WELCOME TO E-COMMERCE APP
                =========================
                1. Login
                2. Register
                3. Exit
                """);
        System.out.print("Choose option: ");
    }
}
