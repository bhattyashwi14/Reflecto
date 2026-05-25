package Functionalities;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Scanner;

public class Posts extends LoginSignup
{
    Connection con=super.con;
    Scanner sc = new Scanner(System.in);
    /*static Connection Connection() throws Exception
    {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reflectofinal", "root", "");
        Scanner sc = new Scanner(System.in);
//        if (con != null) {
//            System.out.println("Connection done");
//        }
        return con;
    }*/

    public void displayMenu() throws Exception //Displays the menu for Posts (overridden method)
    {
        boolean b=true;
        while(b)
        {
            System.out.println("\u001B[36m\nPOSTS: \u001B[0m");
            System.out.println("\u001B[33mENTER 1: TO VIEW POSTS\nENTER 2: TO VIEW YOUR POSTS\nENTER 3: TO MAKE A NEW POST\nENTER 4: TO GO BACK\u001B[0m");
            int ch;
            while(true)
            {
                try {
                    System.out.println("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    ch=sc.nextInt(); sc.nextLine();
                    break;
                }
                catch (Exception e)
                {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0");
                    sc.nextLine();
                }
            }
            switch (ch)
            {
                case 1:
                    viewPosts();
                    break;
                case 2:
                    viewUserPosts(userId);
                    break;
                case 3:
                    addPost(userId);
                    break;
                case 4:
                    b=false;
                    break;
                default:
                    continue;
            }
        }
    }

    void viewPosts() throws Exception //Displays the existing posts
    {
        System.out.println();

        boolean a=true;
        while(a) {
            System.out.println("\u001B[36mVIEW POSTS:\u001B[0m");
            System.out.println("\u001B[33mENTER 1: TO READ LATEST POST\nENTER 2: TO READ RANDOM POST\nENTER 3: TO GO BACK\u001B[0m");
            int ch2;

            while(true)
            {
                try {
                    System.out.println("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    ch2=sc.nextInt(); sc.nextLine();
                    break;
                }
                catch (Exception e)
                {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0");
                    sc.nextLine();
                }
            }

            switch (ch2)
            {
                case 1:
                    readLatestpost();
                    break;
                case 2:
                    readRandomPost();
                    break;
                case 3:
                    a = false;
                default:
                    continue;
            }
        }
        System.out.println();
    }

    void readLatestpost() throws Exception //Displays the latest posts uploaded
    {
        String q1="{call readLatestPost()}";

        CallableStatement cst1= con.prepareCall(q1);

        ResultSet rs= cst1.executeQuery();

        int post_id=0;
        String post_usernm=null;

        while(rs.next())
        {
            post_id=rs.getInt(1);
            post_usernm=rs.getString(3);
            System.out.println("\u001B[33m\nPOST: \u001B[0m"+rs.getString(2)+"\n\u001B[33mFIELD: \u001B[0m"+rs.getString(5)+"\n\u001B[33mLIKES: \u001B[0m"+rs.getInt(4)+"\n\u001B[33mPOSTED BY: \u001B[0m"+rs.getString(3));
        }

        //System.out.println(post_id);
        System.out.println("\u001B[35mDO YOU WANT TO LIKE THIS POST ? (YES/NO): \u001B[0m");
        String s1= sc.nextLine();

        if(s1.equalsIgnoreCase("yes"))
        {
            String q3="{call likePost(?)}";

            CallableStatement cst3= con.prepareCall(q3);
            cst3.setInt(1,post_id);

            int n1= cst3.executeUpdate();

            if(n1>0)
            {
                System.out.println("\u001B[32mYOU LIKED THE POST BY \u001B[0m"+post_usernm);
            }
        }
        System.out.println();
    }

    void readRandomPost() throws Exception //Displays any random posts uploaded
    {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        //Connection con2= Connection();

        con.setAutoCommit(false);
        boolean f=true;

        while(f)
        {
            int maxPostId = 0;

            String q2 = "{call getNewPostId()}";
            CallableStatement cst2 = con.prepareCall(q2);
            ResultSet rs = cst2.executeQuery();

            while (rs.next()) {
                maxPostId = rs.getInt(1);
            }

            int randomPostId= (int)(Math.random()*(maxPostId-101)) +100;
            //System.out.println(randomPostId);

            String q1="{call readRandomPost(?)}";

            CallableStatement cst1= con.prepareCall(q1);
            cst1.setInt(1,randomPostId);

            ResultSet rs1= cst1.executeQuery();

            String post_usernm=null;

            while (rs1.next())
            {
                System.out.println("\u001B[33m\nPOST: \u001B[0m"+rs1.getString(1)+"\n\u001B[33mFIELD: \u001B[0m"+rs1.getString(4)+"\n\u001B[33mLIKES: \u001B[0m"+rs1.getInt(3)+"\n\u001B[33mPOSTED BY :\u001B[0m"+rs1.getString(2));
                post_usernm = rs1.getString(2);
            }
            if(post_usernm!=null)
            {
                con.commit();
                f=false;
            }

            System.out.println("\u001B[35mDO YOU WANT TO LIKE THIS POST ? (YES/NO): \u001B[0m");
            String s1= sc.nextLine();

            if(s1.equalsIgnoreCase("yes"))
            {
                String q3="{call likePost(?)}";

                CallableStatement cst3= con.prepareCall(q3);
                cst3.setInt(1,randomPostId);

                int n1= cst3.executeUpdate();

                if(n1>0)
                {
                    System.out.println("\u001B[32mYOU LIKED THE POST BY \u001B[0m"+post_usernm);
                }
            }
        }
        System.out.println();
    }

    void viewUserPosts(int u_id) throws Exception //Displays the user's own uploaded posts
    {

        String q1="call getUserPosts(?)";
        CallableStatement cst= con.prepareCall(q1);
        cst.setInt(1,u_id);

        ResultSet rs= cst.executeQuery();

        int count=0;

        boolean a=true;
        while(a && rs.next())
        {
            count++;
            int post_id=rs.getInt(1);

            System.out.println();
            System.out.println("\u001B[33mTITLE: \u001B[0m"+rs.getString(2)+"\n\u001B[33mFIELD: \u001B[0m"+rs.getString(4)+"\n\u001B[33mPOST: \u001B[0m"+rs.getString(3)+"\n\u001B[33mLIKES: \u001B[0m"+rs.getInt(5));
            System.out.println();

            System.out.println("\u001B[36mVIEW YOUR POSTS:\u001B[36m");
            System.out.println("\u001B[33mENTER 1: TO VIEW NEXT POST\nENTER 2: TO DELETE THIS POST\nENTER 3: TO GO BACK\u001B[0m");
            int ch;
            while(true)
            {
                try {
                    System.out.println("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    ch=sc.nextInt(); sc.nextLine();
                    break;
                }
                catch (Exception e)
                {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0");
                    sc.nextLine();
                }
            }
            switch (ch)
            {
                case 1:
                {
                    if(rs.wasNull()==false)
                    {
                        System.out.println("\u001B[35mYOU'VE VIEWED ALL YOUR POSTS\u001B[0m");
                    }
                    //rs.next();
                }
                break;
                case 2:
                {
                    deleteUserPost(post_id);
                    //rs.next();
                }
                break;
                case 3:
                {
                    a = false;
                }
                break;
                default:
                {
                    System.out.println("\u001B[31mINVALID CHOICE\u001B[0m");
                }
            }
        }
        if(count==0)
        {
            System.out.println("\u001B[35mYOU HAVE NO POSTS\u001B[0m");
        }
        System.out.println();
    }

    void deleteUserPost(int p_id) throws Exception //Deletes an existing post on the basis of post ID
    {
        String q1="call deleteUserPost(?)";
        CallableStatement cst= con.prepareCall(q1);
        cst.setInt(1,p_id);

        int n= cst.executeUpdate();

        if(n>0)
        {
            System.out.println("\u001B[32mPOST DELETED SUCCESSFULLY\u001B[0m");
        }
        System.out.println();
    }

    void addPost(int user_id) throws Exception //Uploads a new post
    {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String q2 = "{call getNewPostId()}";
        CallableStatement cst2 = con.prepareCall(q2);
        ResultSet rs = cst2.executeQuery();
        int new_post_id = 0;
        while (rs.next()) {
            new_post_id = rs.getInt(1);
        }

        System.out.print("\u001B[36mENTER TITLE FOR THE POST: \u001B[0m");
        String title = sc.nextLine();
        System.out.print("\u001B[36mENTER WHAT FIELD THIS POST BELONGS: \u001B[0m");
        String field = sc.nextLine();
        System.out.println("\u001B[35mWRITE YOUR POST: \u001B[0m");
        String post_dt = sc.nextLine();

        String q3 = "{call addPost(?,?,?,?,?,?,?)}";
        CallableStatement cst3 = con.prepareCall(q3);
        cst3.setInt(1, new_post_id);
        cst3.setInt(2, user_id);
        cst3.setString(3, title);
        cst3.setString(4, post_dt);
        cst3.setString(5, field);
        cst3.setInt(6,0);
        cst3.setTimestamp(7, timestamp);

        int n = cst3.executeUpdate();

        if (n > 0) {
            System.out.println("\u001B[32mPOST UPLOADED SUCCESSSFULLY\u001B[0m");
        }
        System.out.println();
    }
}
