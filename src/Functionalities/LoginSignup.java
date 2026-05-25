package Functionalities;

import Quotes.Quotes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Scanner;
import Exception.*;
interface DisplayCall
{
    void displayMenu() throws Exception;
}
public class LoginSignup implements DisplayCall
{
    public static String userName;
    public static String passCode;
    public static int userId;
    protected Connection con;
    Quotes quotes=new Quotes();
    public void displayMenu() throws Exception //Overridden method
    {}
    public LoginSignup() //Constructor
    {
        try
        {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reflectofinal", "root", "");
            if (con != null)
            {
                //System.out.println("Connection done");
            }
        }
        catch (Exception e)
        {
            System.out.println("\u001B[31mConnection Error\u001B[31m");
        }
    }
    public void LoginSignup() throws Exception //Handles user registration and login authentication
    {
        Scanner sc=new Scanner(System.in);
        int loginInput;
        while(true)
        {
            System.out.println("\u001B[36mLogin/Signup:\u001B[0m");
            System.out.println("\u001B[33mEnter 1 for login\u001B[0m");
            System.out.println("\u001B[33mEnter 2 for signup\u001B[0m");
            System.out.println("\u001B[33mEnter 3 to exit\u001B[0m");
            while (true)
            {
                try
                {
                    System.out.print("\u001B[35mEnter your choice:\u001B[0m");
                    loginInput=sc.nextInt(); sc.nextLine();
                    break;
                }
                catch (Exception e)
                {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                    sc.nextLine();
                }
            }
            System.out.println();
            if (loginInput==2)
            {
                while (true)
                {
                    while (true)
                    {
                        System.out.print("Create UserName:");
                        userName = sc.next().toLowerCase();
                        try
                        {
                            LoginSignup.signUpUserName(userName);
                            break;
                        }
                        catch (LengthException | EmptyDataException | DigitException e)
                        {
                            System.out.println(e);
                        }
                    }
                    while (true)
                    {
                        System.out.print("Create PassCode:");
                        passCode = sc.next();
                        try
                        {
                            LoginSignup.signUpPassCode(passCode);
                            break;
                        }
                        catch (LengthException | EmptyDataException e)
                        {
                            System.out.println(e);
                        }
                    }
                    String getUserID = "{call getNewUserId()}";
                    CallableStatement cst = con.prepareCall(getUserID);
                    ResultSet rs = cst.executeQuery();
                    rs.next();
                    userId = rs.getInt(1);
                    System.out.println("\u001B[32mSigned in Successfully!!!\u001B[0m");
                    String insertUser = "{call insertUser(?,?,?)}";
                    CallableStatement cst1 = con.prepareCall(insertUser);
                    cst1.setInt(1, userId);
                    cst1.setString(2, userName);
                    cst1.setString(3, passCode);
                    cst1.executeUpdate();
                    break;
                }
            }
            else if (loginInput==1)
            {
                while(true)
                {
                    System.out.print("Enter username:");
                    userName=sc.next().toLowerCase();
                    String loginUser="{call loginUser(?)}";
                    CallableStatement cst2=con.prepareCall(loginUser);
                    cst2.setString(1,userName);
                    ResultSet rs2=cst2.executeQuery();
                    if(rs2.next())
                    {
                        while(true)
                        {
                            System.out.print("Enter passcode:");
                            passCode=sc.next();
                            String checkPasscode="{call checkPasscode(?,?)}";
                            CallableStatement cst3=con.prepareCall(checkPasscode);
                            cst3.setString(1,userName);
                            cst3.executeQuery();
                            if(cst3.getString(2).equals(passCode))
                            {
                                System.out.println("\u001B[32mLogged in successfully!\u001B[0m");
                                quotes.displayRandomQuote();
                                String uid="{call getUserId(?,?,?)}";
                                CallableStatement cst1=con.prepareCall(uid);
                                cst1.setString(2,userName);
                                cst1.setString(3,passCode);
                                cst1.executeQuery();
                                userId=cst1.getInt(1);
                                break;
                            }
                            else
                            {
                                System.out.println("\u001B[31mThe passcode is incorrect for the entered username\u001B[0m");
                            }
                        }
                        break;
                    }
                    else
                    {
                        System.out.println("\u001B[31mNo such username found\u001B[0m");
                    }
                }
                break;
            }
            else if(loginInput==3)
            {
                System.out.println("\u001B[34mThank you for using reflecto :)\u001B[0m");
                System.exit(1);
            }
        }
    }
    static void signUpUserName(String userName) //Handles username creation and prints the required error messages
    {
        if(userName.isEmpty())
        {
            throw new EmptyDataException("\u001B[31mUser name can't be empty\u001B[0m");
        }
        if(userName.length()<4 || userName.length()>20)
        {
            throw new LengthException("\u001B[31mThe length of user name should be greater than 4 and less than 20\u001B[0m");
        }
        if(Character.isDigit(userName.charAt(0)))
        {
            throw new DigitException("\u001B[0mThe First character of user name should not be a digit\u001B[0m");
        }
    }

    static void signUpPassCode(String passCode) //Handles passcode creation and prints the required error messages
    {
        if(passCode.isEmpty())
        {
            throw new EmptyDataException("\u001B[31mPasscode can't be empty\u001B[0m");
        }
        if(passCode.length()<8)
        {
            throw new LengthException("\u001B[31mPasscode should be of minimum 8 character\u001B[0m");
        }
        if(passCode.length()>15)
        {
            throw new LengthException("\u001B[31mPasscode shouldn't be longer than 15 characters\u001B[0m");
        }
    }

}

