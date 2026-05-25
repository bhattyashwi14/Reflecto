package Functionalities;


import DS.Deque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class To_Do_List extends LoginSignup
{
    Connection con=super.con;
    //static
    //{
        /*try
        {
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reflectofinal", "root", "");
            if (con!=null)
            {
                //System.out.println("Connection done");
                String uid="{call getUserId(?,?,?)}";
                CallableStatement cst1=con.prepareCall(uid);
                cst1.setString(2,Reflecto.ob.userName);
                cst1.setString(3,Reflecto.ob.passCode);
                cst1.executeQuery();
                userId=cst1.getInt(1);
            }
        }
        catch (Exception e)
        {
            System.out.println("Connection Error");
        }*/
    //}
    Scanner sc=new Scanner(System.in);
    int count=0;
    Deque obb=new Deque(100);
    public void displayMenu() throws Exception //Displays the menu for To-Do List (overridden method)
    {
        System.out.println();
        int x2;
        do {
            System.out.println();
            System.out.println("\u001B[36mTO-DO LIST:\u001B[0m");
            System.out.println("\u001B[33mENTER 1 TO CREATE TO-DO LIST\u001B[0m");
            System.out.println("\u001B[33mENTER 2 TO EDIT TO-DO LIST\u001B[0m");
            System.out.println("\u001B[33mENTER 3 TO DELETE TO-DO LIST\u001B[0m");
            System.out.println("\u001B[33mENTER 4 TO SEARCH/VIEW TO-DO LIST\u001B[0m");
            System.out.println("\u001B[33mENTER 5 TO MARK TASKS AS DONE\u001B[0m");
            System.out.println("\u001B[33mENTER 6 TO EXIT\u001B[0m");
            System.out.println();
            while (true)
            {
                try
                {
                    System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    x2 = sc.nextInt();
                    sc.nextLine(); break;
                }
                catch (Exception e)
                {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                    sc.nextLine();
                }
            }
            switch (x2)
            {
                case 1:
                    System.out.println();
                    createToDoList();
                    break;
                case 2:
                    System.out.println();
                    edit_list();
                    break;
                case 3:
                    System.out.println();
                    deleteList();
                    break;
                case 4:
                    System.out.println();
                    searchList();
                    break;
                case 5:
                    System.out.println();
                    markAsDone();
                    break;
                case 6:
                    System.out.println();
                    System.out.println("\u001B[34mExiting from to-do list!\u001B[0m");
                    break;
                default:
                    System.out.println("\u001B[35mEnter a valid choice!\u001B[0m");
            }
        }while(x2!=6);
    }

    void createToDoList() throws Exception //Creates a new To-Do List
    {
        System.out.print("\u001B[36mEnter title for to-do list:\u001B[0m");
        String name=sc.nextLine();
        System.out.println("\u001B[35mWrite end when you're done adding tasks!\u001B[0m");
        String tasks="";
        while(true)
        {
            count++;
            System.out.println("Enter task " + count + ". :");
            tasks = sc.nextLine();
            if (tasks.equalsIgnoreCase("end"))
            {break;}
            obb.insertAtRear(tasks);
        }
        String q = "INSERT INTO To_do_list (title,user_id) VALUES ('" + name + "',"+userId+")";
        Statement st=con.createStatement();
        st.executeUpdate(q);
        String q1="Update to_do_list set list=? where title='"+name+"' and user_id="+userId;
        PreparedStatement pst=con.prepareStatement(q1);
        String list="";
        for(int i=1;i<count;i++)
        {
            list+=i+". "+obb.deleteAtFront()+"\n";
        }
        pst.setString(1,list);
        int n=pst.executeUpdate();
        if(n>0)
        {
            System.out.println("\u001B[32m"+n+" to-do list added successfully!\u001B[0m");
        }
        super.quotes.displayRandomQuote();//Displays a random quote to boost user motivation
    }

    void edit_list() throws Exception //Edits an existing To-Do List on the basis of title provided by the user
    {
        int x;
        do {
            System.out.println("\u001B[36mEDIT TO-DO LIST:\u001B[0m");
            System.out.println("\u001B[33mENTER 1 TO ADD MORE TASKS\u001B[0m");
            System.out.println("\u001B[33mENTER 2 TO DELETE TASKS\u001B[0m");
            System.out.println("\u001B[33mENTER 3 TO CHANGE OR RENAME TASKS\u001B[0m");
            System.out.println("\u001B[33mENTER 4 TO EXIT\u001B[0m");
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
            String tasks="";
            switch (x)
            {
                case 1:
                    System.out.print("\u001B[36mEnter Name of title:\u001B[0m");
                    String title=sc.nextLine();
                    String q="Select list from to_do_list where title='"+title+"' and user_id="+userId;
                    Statement st=con.createStatement();
                    ResultSet rs=st.executeQuery(q);
                    int count=1;
                    String list="";
                    if(rs.next())
                    {
                        list=rs.getString(1);
                    }
                    else
                    {
                        System.out.println("\u001B[31mNo such title found!\u001B[0m");
                        break;
                    }
                    System.out.print("\u001B[35mEnter the number of tasks you want to add:\u001B[0m");
                    int n;
                    while(true)
                    {
                        try
                        {
                            n=sc.nextInt();
                            sc.nextLine(); break;
                        }
                        catch (Exception e)
                        {
                            System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                            sc.nextLine();
                        }
                    }
                    for(int i=1;i<=n;i++)
                    {
                        System.out.println("Enter task " + count + ". :");
                        tasks=sc.nextLine();
                        obb.insertAtRear(tasks);
                        count++;
                    }
                    int c=list.split("\n").length + 1; ;
                    for(int i=1;i<=n;i++)
                    {
                        list+=c+". "+obb.deleteAtFront()+"\n";
                        c++;
                    }
                    con.setAutoCommit(false);
                    System.out.print("\u001B[31mDo you really want to update the entry?(Yes/No):\u001B[0m");
                    String ans=sc.next();
                    sc.nextLine();
                    if(ans.equalsIgnoreCase("yes"))
                    {
                        String q1="Update to_do_list set list=? where title='" + title + "' and user_id="+userId;
                        PreparedStatement pst=con.prepareStatement(q1);
                        pst.setString(1, list);
                        int n1=pst.executeUpdate();
                        if(n1>0)
                        {
                            System.out.println("\u001B[32m"+title+" updated\u001B[0m");
                        }
                        con.commit();
                    }
                    else
                    {
                        con.rollback();
                    }
                    break;
                case 2:
                    System.out.print("\u001B[36mEnter the title of the list: \u001B[0m");
                    String delTitle = sc.nextLine();
                    String qDel = "SELECT list FROM to_do_list WHERE title=? and user_id=?";
                    PreparedStatement pstDel = con.prepareStatement(qDel);
                    pstDel.setString(1, delTitle); pstDel.setInt(2,userId);
                    ResultSet rsDel = pstDel.executeQuery();
                    if (!rsDel.next())
                    {
                        System.out.println("\u001B[31mNo such title found!\u001B[0m");
                        break;
                    }
                    System.out.print("\u001B[36mEnter the task you want to delete: \u001B[0m");
                    String delTask = sc.nextLine();
                    String[] tasksArr = rsDel.getString(1).split("\n");
                    List<String> updatedTasks = new ArrayList<>();
                    boolean found = false;
                    for (String task : tasksArr)
                    {
                        String content = task.split("\\. ", 2)[1];
                        if (content.equalsIgnoreCase(delTask))
                        {
                            found = true;
                            continue;
                        }
                        updatedTasks.add(task);
                    }
                    if (found==false)
                    {
                        System.out.println("\u001B[31mTask not found!\u001B[0m");
                        break;
                    }
                    String finalList = "";
                    for (int i = 0; i < updatedTasks.size(); i++)
                    {
                        String content = updatedTasks.get(i).split("\\. ", 2)[1];
                        finalList += (i + 1) + ". " + content + "\n";
                    }
                    con.setAutoCommit(false);
                    System.out.print("\u001B[31mConfirm deletion? (Yes/No): \u001B[0m");
                    String anss=sc.next();
                    sc.nextLine();
                    if (anss.equalsIgnoreCase("yes"))
                    {
                        PreparedStatement pstUpdate = con.prepareStatement("UPDATE to_do_list SET list=? WHERE" +
                                " title=? and user_id=?");
                        pstUpdate.setString(1, finalList);
                        pstUpdate.setString(2, delTitle);
                        pstUpdate.setInt(3,userId);
                        pstUpdate.executeUpdate();
                        con.commit();
                        System.out.println("\u001B[32mTask deleted and list updated!\u001B[0m");
                    }
                    else
                    {
                        con.rollback();
                        System.out.println("\u001B[31mCancelled.\u001B[0m");
                    }
                    break;
                case 3:
                    System.out.print("\u001B[36mEnter the title of list:\u001B[0m");
                    String name=sc.nextLine();
                    String q2="Select list from to_do_list where title='"+name+"' and user_id="+userId;
                    Statement st2=con.createStatement();
                    ResultSet rs2=st2.executeQuery(q2);
                    String list2="";
                    if(rs2.next()==false)
                    {
                        System.out.println("\u001B[31mNo such title found\u001B[0m");
                        break;
                    }
                    list2=rs2.getString(1);
                    System.out.print("\u001B[35mEnter the name of task you want to change/rename:\u001B[0m");
                    String task=sc.nextLine();
                    System.out.print("\u001B[36mEnter the new task:\u001B[0m");
                    String newTask=sc.nextLine();
                    String[] lines = list2.split("\n");
                    boolean found3= false;
                    String updatedList="";
                    for (int i = 0; i < lines.length; i++)
                    {
                        String content = lines[i].split("\\. ", 2)[1];
                        if (content.equalsIgnoreCase(task))
                        {
                            lines[i] = (i+1) + ". " + newTask;
                            found3 = true;
                        }
                        updatedList+=lines[i]+"\n";
                    }
                    if (found3)
                    {
                        String updateQ = "UPDATE to_do_list SET list=? WHERE title=? and user_id=?";
                        PreparedStatement pst2 = con.prepareStatement(updateQ);
                        pst2.setString(1, updatedList);
                        pst2.setString(2, name);
                        pst2.setInt(3,userId);
                        int rowsUpdated = pst2.executeUpdate();
                        if (rowsUpdated > 0)
                        {
                            System.out.println("\u001B[32mTask renamed successfully!\u001B[0m");
                        }
                    }
                    else
                    {
                        System.out.println("\u001B[31mTask not found in the list!\u001B[0m");
                    }
                    break;
                case 4:
                    System.out.println("\u001B[34mExiting from to-do list editing\u001B[0m");
                    break;
                default:
                    System.out.println("\u001B[35mEnter a valid choice!\u001B[0m");
            }
        }while(x!=4);
    }

    void searchList() throws Exception //Searches and displays existing To-Do List for the user provided title
    {
        System.out.print("\u001B[36mEnter the title of list:\u001B[0m");
        String title=sc.nextLine();
        String q="Select title,list from To_do_list where title=? and user_id=?";
        PreparedStatement pst=con.prepareStatement(q);
        pst.setString(1,title);
        pst.setInt(2,userId);
        ResultSet rs=pst.executeQuery();
        if(rs.next())
        {
            System.out.println("\u001B[33mTitle:\u001B[0m"+rs.getString(1));
            System.out.println("\u001B[33mTo-Do list:\u001B[0m"+"\n"+rs.getString(2));
        }
        else
        {
            System.out.println("\u001B[31mNo such title found\u001B[0m");
        }
    }

    void deleteList() throws Exception //Deletes an existing To-Do List by the title provided by the user
    {
        con.setAutoCommit(false);
        System.out.print("\u001B[36mEnter the title of the to-do list:\u001B[0m");
        String title=sc.nextLine();
        System.out.print("\u001B[31mAre you sure, you want to delete the list?(Yes/No):\u001B[0m");
        String ans=sc.next();
        sc.nextLine();
        if(ans.equalsIgnoreCase("yes"))
        {
            String q="Delete from to_do_list where title='"+title+"' and user_id="+userId;
            Statement st=con.createStatement();
            int n=st.executeUpdate(q);
            if (n>0)
            {
                System.out.println("\u001B[32m"+title+" deleted!\u001B[0m");
            }
            else
            {
                System.out.println("\u001B[31mNo such title found!\u001B[0m");
            }
            con.commit();
        }
        else
        {
            System.out.println("\u001B[31mDeletion aborted!\u001B[0m");
            con.rollback();
        }
    }

    void markAsDone() throws Exception //Keeps a record of completed tasks from an existing To-Do List
    {
        System.out.print("\u001B[36mEnter the title of the to-do list: \u001B[0m");
        String title = sc.nextLine();
        String q = "SELECT list, completed_tasks FROM to_do_list WHERE title=? and user_id=?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setString(1, title); pst.setInt(2,userId);
        ResultSet rs = pst.executeQuery();
        if (!rs.next())
        {
            System.out.println("\u001B[31mNo such title found!\u001B[0m");
            return;
        }
        System.out.print("\u001B[35mEnter the completed task: \u001B[0m");
        String completed = sc.nextLine();
        String[] tasks = rs.getString("list").split("\n");
        String completedList = rs.getString("completed_tasks");
        String list = "", done = null;
        for (String t : tasks)
        {
            String content = t.split("\\. ", 2)[1];
            if (done == null && content.equalsIgnoreCase(completed))
            {
                done = t;
            }
            else
            {
                list += t + "\n";
            }
        }
        if (done == null)
        {
            System.out.println("\u001B[31mTask not found!\u001B[0m");
            return;
        }
        if (completedList == null)
        {
            completedList = "";
        }
        completedList += done + "\n";
        con.setAutoCommit(false);
        System.out.print("\u001B[31mConfirm mark as done? (Yes/No): \u001B[0m");
        String ans=sc.next();
        sc.nextLine();
        if (!ans.equalsIgnoreCase("yes"))
        {
            System.out.println("\u001B[31mCancelled.\u001B[0m");
            return;
        }
        String update = "UPDATE to_do_list SET list=?, completed_tasks=? WHERE title=? and user_id=?";
        PreparedStatement pst2 = con.prepareStatement(update);
        pst2.setString(1, list);
        pst2.setString(2, completedList);
        pst2.setString(3, title); pst2.setInt(4,userId);
        pst2.executeUpdate();
        con.commit();
        System.out.println("\u001B[32mTask marked as completed!\u001B[0m");
        super.quotes.displayRandomQuote();
        //Generates a random quote about anticipation for completing a task, boosting the user’s confidence.
    }
}

