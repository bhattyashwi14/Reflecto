import Functionalities.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Reflecto
{
    static Connection con;

    {
        try
        {
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/reflectofinal", "root", "");
            if (con!=null)
            {
                //System.out.println("Connection done");
            }
        }
        catch (Exception e)
        {
            System.out.println("\u001B[31mConnection Error\u001B[0m");
        }
    }
    static Scanner sc=new Scanner(System.in);
    static LoginSignup ob=new LoginSignup();
    public static void main(String[] args) throws Exception
    {
        welcomeMessage();
        ob.LoginSignup();
        int x;
        do {
            System.out.println();
            System.out.println("\u001B[36mMAIN MENU:\u001B[0m");
            System.out.println("\u001B[33mENTER 1 FOR JOURNAL\u001B[0m");
            System.out.println("\u001B[33mENTER 2 FOR TO-DO LIST\u001B[0m");
            System.out.println("\u001B[33mENTER 3 FOR CHALLENGES\u001B[0m");
            System.out.println("\u001B[33mENTER 4 FOR POSTS\u001B[0m");
            System.out.println("\u001B[33mENTER 5 FOR EXIT\u001B[0m");
            System.out.println();
            while(true)
            {
                try {
                    System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    x = sc.nextInt();
                    sc.nextLine();
                    break;
                }
                catch (Exception e)
                {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                    sc.nextLine();
                }
            }
            switch (x)
            {
                case 1:
                {
                    Journal j = new Journal();
                    j.displayMenu();
                }
                break;
                case 2:
                    To_Do_List td=new To_Do_List();
                    td.displayMenu();
                    break;
                case 3:
                    Challenges c = new Challenges();
                    c.challengesMenu(ob);
                    break;
                case 4:
                    Posts p=new Posts();
                    p.displayMenu();
                    break;
                case 5:
                    System.out.println();
                    System.out.println("\u001B[34mThank you for using reflecto :)\u001B[34m");
                    break;
                default:
                    System.out.println("\u001B[35mEnter valid choice\u001B[0m");
            }
        }while(x!=5);
    }

    static public void welcomeMessage(){
        loadingAnimationWithText(1);
        separatorLine();
        System.out.println("\t██     ██ ███████ ██       ██████   ████████  ███    ███ ███████       ████████ ████████       ████████ ███████ ███████ ██      ███████ ██████ ████████ ████████");
        System.out.println("\t██     ██ ██      ██       ██       ██    ██  ████  ████ ██               ██    ██    ██       ██   ██  ██      ██      ██      ██      ██        ██    ██    ██");
        System.out.println("\t██  █  ██ █████   ██       ██       ██    ██  ██ ████ ██ █████            ██    ██    ██       █████    █████   ████    ██      ████    ██        ██    ██    ██");
        System.out.println("\t██ ███ ██ ██      ██       ██       ██    ██  ██  ██  ██ ██               ██    ██    ██       ██  ██   ██      ██      ██      ██      ██        ██    ██    ██");
        System.out.println("\t ███ ███  ███████ ███████  ██████   ████████  ██      ██ ███████          ██    ████████       ██    ██ ███████ ██      ███████ ███████ ██████    ██    ████████");
        separatorLine();
        try{
            Thread.sleep(500);
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static public void separatorLine(){
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
    }

    static public void loadingAnimationWithText(int limitingValue){
        for (int i = 0; i < limitingValue; i++) {
            try {
                System.out.print("\rLoading   ");
                Thread.sleep(500);
                System.out.print("\rLoading.  ");
                Thread.sleep(500);
                System.out.print("\rLoading.. ");
                Thread.sleep(500);
                System.out.print("\rLoading...");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("\rDone!      ");
    }

}