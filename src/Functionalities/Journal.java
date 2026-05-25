package Functionalities;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Journal extends LoginSignup
{
    Connection con=super.con;
    /*static{
        try
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
        }
    }*/
    Scanner sc=new Scanner(System.in);
    public void displayMenu() throws Exception //Displays the menu for Journal (overridden method)
    {
        System.out.println();
        int x1;
        do
        {
            System.out.println();
            System.out.println("\u001B[36mJOURNAL:\u001B[0m");
            System.out.println("\u001B[33mENTER 1 TO ADD ENTRY\u001B[0m");
            System.out.println("\u001B[33mENTER 2 TO EDIT ENTRY\u001B[0m");
            System.out.println("\u001B[33mENTER 3 TO DELETE ENTRY\u001B[0m");
            System.out.println("\u001B[33mENTER 4 TO SEARCH ENTRY\u001B[0m");
            System.out.println("\u001B[33mENTER 5 TO EXIT\u001B[0m");
            System.out.println();
            while (true)
            {
                try
                {
                    System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    x1 = sc.nextInt();
                    sc.nextLine(); break;
                }
                catch (Exception e)
                {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                    sc.nextLine();
                }
            }
            switch(x1)
            {
                case 1:
                    System.out.println();
                    addEntry();
                    break;
                case 2:
                    System.out.println();
                    updateEntry();
                    break;
                case 3:
                    System.out.println();
                    deleteEntry();
                    break;
                case 4:
                    System.out.println();
                    searchEntry();
                    break;
                case 5:
                    System.out.println();
                    System.out.println("\u001B[34mExiting from Journal!\u001B[0m");
                    break;
                default:
                    System.out.println("\u001B[35mEnter a valid choice\u001B[0m");
            }
        }while(x1!=5);
    }

    void addEntry() throws Exception //Adds a new journal entry
    {
        String date="{call getDate(?)}";
        CallableStatement cst=con.prepareCall(date);
        cst.executeQuery();
        System.out.println("\u001B[36mEnter journal title:\u001B[0m");
        String title=sc.nextLine();
        System.out.println("\u001B[36mEnter your journal entry:\u001B[0m");
        String entry=sc.nextLine();
        String insert="Insert into journal (user_id,title,journal_data,date_time) values(?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(insert);
        pst.setInt(1,userId);
        pst.setString(2,title);
        pst.setString(3,entry);
        pst.setTimestamp(4,cst.getTimestamp(1));
        int n=pst.executeUpdate();
        if(n>0)
        {
            System.out.println("\u001B[32m"+n+" journal entry added successfully\u001B[0m");
        }
        System.out.println();
        int n1;
        do {
            System.out.println();
            System.out.println("\u001B[36mFILE:\u001B[0m");
            System.out.println("\u001B[33mENTER 1 TO IMPORT FILE\u001B[0m");
            System.out.println("\u001B[33mENTER 2 TO EXPORT FILE\u001B[0m");
            System.out.println("\u001B[33mENTER 3 TO EXIT\u001B[0m");
            System.out.println();
            while (true)
            {
                try
                {
                    System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    n1 = sc.nextInt();
                    sc.nextLine(); break;
                }
                catch (Exception e)
                {
                    System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                    sc.nextLine();
                }
            }

            switch (n1)
            {
                case 1:
                    importFile(title);
                    break;
                case 2:
                    exportFile(title);
                    break;
                case 3:
                    System.out.println("Exiting");
                    break;
                default:
                    System.out.println("Enter a valid choice");
            }
        }while(n1!=3);
    }

    void deleteEntry() throws Exception //Deletes an already existing journal entry by the title name taken from the user
    {
        con.setAutoCommit(false);
        System.out.print("\u001B[36mEnter the title of your entry:\u001B[0m");
        String journalTitle=sc.nextLine();
        String delete="Delete from journal where title=? and user_id=?";
        PreparedStatement pst3=con.prepareStatement(delete);
        pst3.setString(1,journalTitle);
        pst3.setInt(2,userId);
        System.out.print("\u001B[31mDo your really want to delete this entry?(Yes/No)\u001B[0m");
        String response=sc.next(); sc.nextLine();
        if(response.equalsIgnoreCase("yes"))
        {
            System.out.println("\u001B[35mDo you want to export the file?(Yes/No):\u001B[0m");
            String ans1=sc.next(); sc.nextLine();
            if(ans1.equalsIgnoreCase("yes"))
            {
                exportFile(journalTitle);
            }
            int n3=pst3.executeUpdate();
            con.commit();
            if(n3>0)
            {
                System.out.println("\u001B[32mJournal entry with title "+journalTitle+" deleted\u001B[0m");
            }
            else
            {
                System.out.println("\u001B[31mNo such entry found!\u001B[0m");
            }
        }
        else
        {
            con.rollback();
        }
    }

    void updateEntry() throws Exception //Updates the existing Journal entry on the basis of journal title provided by the user
    {
        int x;
        do
        {
            System.out.println();
            System.out.println("\u001B[36mEDIT ENTRY:\u001B[0m");
            System.out.println("\u001B[33mENTER 1 FOR ADDING DATA INTO THE JOURNAL ENTRY\u001B[0m");
            System.out.println("\u001B[33mENTER 2 FOR DELETING DATA FROM THE JOURNAL ENTRY\u001B[0m");
            System.out.println("\u001B[33mENTER 3 FOR REPLACING ONE WORD WITH ANOTHER IN THE JOURNAL ENTRY\u001B[0m");
            System.out.println("\u001B[33mENTER 4 TO EXIT\u001B[0m");
            System.out.println();
            while (true)
            {
                try {
                    System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                    x = sc.nextInt();
                    sc.nextLine(); break;
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
                    String q="{call getJournalData(?,?,?)}";
                    CallableStatement cst=con.prepareCall(q);
                    System.out.println("\u001B[36mEnter Journal Title:\u001B[0m");
                    String title=sc.nextLine();
                    cst.setString(2,title);
                    cst.setInt(3,userId);
                    cst.executeQuery();
                    String entry=cst.getString(1);
                    if(entry==null)
                    {
                        System.out.println("\u001B[31mNo such title found\u001B[0m");
                        break;
                    }
                    System.out.println("\u001B[36mEnter the data you want to add:\u001B[0m");
                    String updatedEntry=sc.nextLine();
                    con.setAutoCommit(false);
                    System.out.print("\u001B[31mDo you really want to update the entry?(Yes/No):\u001B[0m");
                    String ans=sc.next();
                    if(ans.equalsIgnoreCase("yes"))
                    {
                        String q1="Update journal set journal_data=? where title=? and user_id=?";
                        PreparedStatement pst=con.prepareStatement(q1);
                        pst.setString(1, (entry+" "+updatedEntry));
                        pst.setString(2, title); pst.setInt(3,userId);
                        int n=pst.executeUpdate();
                        if(n>0)
                        {
                            System.out.println("\u001B[32m"+title+" updated\u001B[0m");
                        }
                        con.commit();
                        System.out.println();
                    }
                    else
                    {
                        con.rollback();
                        System.out.println();
                    }
                    int n1;
                    do {
                        System.out.println();
                        System.out.println("\u001B[36mFILE:\u001B[0m");
                        System.out.println("\u001B[33mENTER 1 TO IMPORT FILE\u001B[0m");
                        System.out.println("\u001B[33mENTER 2 TO EXPORT FILE\u001B[0m");
                        System.out.println("\u001B[33mENTER 3 TO EXIT\u001B[0m");
                        System.out.println();
                        while (true)
                        {
                            try
                            {
                                System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                                n1 = sc.nextInt();
                                sc.nextLine(); break;
                            }
                            catch (Exception e)
                            {
                                System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                                sc.nextLine();
                            }
                        }
                        switch (n1)
                        {
                            case 1:
                                importFile(title);
                                break;
                            case 2:
                                exportFile(title);
                                break;
                            case 3:
                                System.out.println("\u001B[34mExiting\u001B[0m");
                                break;
                            default:
                                System.out.println("\u001B[35mEnter a valid choice\u001B[0m");
                        }
                    }while(n1!=3);
                    break;
                case 2:
                    String fetchQ = "{call getJournalData(?,?,?)}";
                    CallableStatement call = con.prepareCall(fetchQ);
                    System.out.println("\u001B[36mEnter Journal Title:\u001B[0m");
                    String delTitle = sc.nextLine();
                    call.setString(2, delTitle); call.setInt(3,userId);
                    call.executeQuery();
                    String data = call.getString(1);
                    if(data==null)
                    {
                        System.out.println("\u001B[31mNo such title found\u001B[0m");
                        break;
                    }
                    String[] lines = data.split("\\.");
                    System.out.println("\u001B[33mCurrent Entry:\u001B[0m");
                    for (int i = 0; i < lines.length; i++)
                    {
                        System.out.println((i + 1) + ". " + lines[i]+".");
                    }
                    System.out.print("\u001B[36mEnter line number to delete: \u001B[36m");
                    int lineNo;
                    while(true)
                    {
                        try {
                            lineNo = sc.nextInt();
                            sc.nextLine(); break;
                        }
                        catch (Exception e)
                        {
                            System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                            sc.nextLine();
                        }
                    }
                    if (lineNo < 1 || lineNo > lines.length)
                    {
                        System.out.println("\u001B[31mInvalid line number!\u001B[0m");
                        break;
                    }
                    String updatedData = "";
                    for (int i = 0; i < lines.length; i++)
                    {
                        if (i != (lineNo - 1))
                        {
                            updatedData += lines[i] + ".";
                        }
                    }
                    System.out.print("\u001B[31mConfirm deletion? (Yes/No): \u001B[0m");
                    if (!sc.next().equalsIgnoreCase("yes"))
                    {
                        System.out.println("\u001B[31mCancelled.\u001B[0m");
                        break;
                    }
                    con.setAutoCommit(false);
                    String updateQ = "UPDATE journal SET journal_data=? WHERE title=? and user_id=?";
                    PreparedStatement pstUpdate = con.prepareStatement(updateQ);
                    pstUpdate.setString(1, updatedData.trim());
                    pstUpdate.setString(2, delTitle);
                    pstUpdate.setInt(3,userId);
                    pstUpdate.executeUpdate();
                    con.commit();
                    System.out.println("\u001B[32mEntry updated after deletion.\u001B[0m");
                    break;
                case 3:
                    System.out.println("\u001B[36mEnter title:\u001B[0m");
                    String title3=sc.nextLine();
                    String q3="Select journal_data from journal where title=? and user_id=?";
                    PreparedStatement pst3=con.prepareStatement(q3);
                    pst3.setString(1,title3); pst3.setInt(2,userId);
                    ResultSet rs=pst3.executeQuery();
                    String [] entry3;
                    String updateEntry3="";
                    if(rs.next()==false)
                    {
                        System.out.println("\u001B[31mNo such title found\u001B[0m");
                        break;
                    }
                    System.out.print("\u001B[35mEnter the word you want to change:\u001B[0m");
                    String word=sc.next();
                    System.out.print("\u001B[36mEnter the word you want to replace with:\u001B[0m"); sc.nextLine();
                    String update=sc.nextLine();
                    entry3=rs.getString(1).split(" ");
                    boolean flag=false;
                    for(int i=0;i<entry3.length;i++)
                    {
                        if(entry3[i].equalsIgnoreCase(word))
                        {
                            entry3[i]=update;
                            flag=true;
                        }
                        updateEntry3+=entry3[i]+" ";
                    }
                    if(flag==false)
                    {
                        System.out.println("\u001B[31mNo such word found!\u001B[0m");
                        break;
                    }
                    String q31="Update journal set journal_data=? where title=? and user_id=?";
                    PreparedStatement pst31=con.prepareStatement(q31);
                    pst31.setString(1,updateEntry3); pst31.setString(2,title3);
                    pst31.setInt(3,userId);
                    int n3=pst31.executeUpdate();
                    if(n3>0)
                    {
                        System.out.println("\u001B[32mJournal entry updated!\u001B[0m");
                    }
                    int n31;
                    do {
                        System.out.println();
                        System.out.println("\u001B[36mFILE:\u001B[0m");
                        System.out.println("\u001B[33mENTER 1 TO IMPORT FILE\u001B[0m");
                        System.out.println("\u001B[33mENTER 2 TO EXPORT FILE\u001B[0m");
                        System.out.println("\u001B[33mENTER 3 TO EXIT\u001B[0m");
                        System.out.println();
                        while (true)
                        {
                            try
                            {
                                System.out.print("\u001B[35mENTER YOUR CHOICE:\u001B[0m");
                                n31 = sc.nextInt();
                                sc.nextLine(); break;
                            }
                            catch (Exception e)
                            {
                                System.out.println("\u001B[31mPlease enter integer value!\u001B[0m");
                                sc.nextLine();
                            }
                        }
                        switch (n31)
                        {
                            case 1:
                                importFile(title3);
                                break;
                            case 2:
                                exportFile(title3);
                                break;
                            case 3:
                                System.out.println("\u001B[34mExiting!\u001B[0m");
                                break;
                            default:
                                System.out.println("\u001B[35mEnter a valid choice\u001B[35m");
                        }
                    }while(n31 !=3);
                    break;
                case 4:
                    System.out.println("\u001B[34mExiting Journal Editing!!!\u001B[0m");
                    break;
                default:
                    System.out.println("\u001B[35mEnter a valid choice\u001B[0m");
            }
        }while(x!=4);
    }

    void searchEntry() throws Exception //Searches existing journal entry for the user provided title
    {
        System.out.println("\u001B[36mEnter the Title of your Journal Entry:\u001B[0m");
        String title=sc.nextLine();
        String q="Select journal_data,date_time from journal where title=? and user_id=?";
        PreparedStatement pst=con.prepareStatement(q);
        pst.setString(1,title); pst.setInt(2,userId);
        ResultSet rs=pst.executeQuery();
        if(rs.next()==true)
        {
            System.out.println("\u001B[33mTitle:\u001B[0m"+title);
            System.out.println("\u001B[33mJournal data:\u001B[0m");
            System.out.println(rs.getString(1));
            System.out.println("\u001B[33mDate and Time:\u001B[0m"+rs.getTimestamp(2));
        }
        else
        {
            System.out.println("\u001B[31mNo such journal title found!\u001B[0m");
        }
    }

    void exportFile(String title) throws Exception //Exports the journal entry
    {
        String q="Select * from journal where title=? and user_id=?";
        PreparedStatement pst=con.prepareStatement(q);
        pst.setString(1,title); pst.setInt(2,userId);
        ResultSet rs=pst.executeQuery();
        if(rs.next())
        {
            BufferedWriter bw=new BufferedWriter(new FileWriter(title+".txt"));
            bw.write(title);
            bw.newLine();
            bw.write(String.valueOf(rs.getTimestamp(5)));
            bw.newLine();
            bw.write(rs.getString(3));
            bw.close();
        }
        System.out.println("\u001B[32mFile exported successfully to the path: D:\\javacodes\\ReflectoFinal_1\u001B[0m");
    }

    void importFile(String title) throws Exception //Imports an existing file to the journal entry
    {
        System.out.print("\u001B[36mEnter file name with path:\u001B[0m");
        String file=sc.next();
        File f = new File(file);
        if (!f.exists())
        {
            System.out.println("\u001B[31mFile not found! Please check the path and try again.\u001B[0m");
            return;
        }
        FileInputStream fis=new FileInputStream(file);
        String q = "UPDATE journal SET journal_files = ? WHERE title = ? and user_id=?";
        PreparedStatement pst = con.prepareStatement(q);
        pst.setBlob(1, fis);
        pst.setString(2, title); pst.setInt(3,userId);
        int n = pst.executeUpdate();
        if (n > 0)
        {
            System.out.println("\u001B[32mFile imported successfully!\u001B[0m");
        }
    }
}

