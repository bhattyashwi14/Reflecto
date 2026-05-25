package Quotes;

import DS.SinglyLinkedList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Quotes
{
    Connection con;
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
    SinglyLinkedList quoteList = new SinglyLinkedList();
    public void displayRandomQuote() throws Exception //Displays and removes a random quote, refilling the list when empty
    {
        if (quoteList.first == null)
        {
            fetchNewQuotes();
        }

        int size = 0;
        SinglyLinkedList.node temp = quoteList.first;
        while (temp != null)
        {
            size++;
            temp = temp.next;
        }

        int randomIndex = (int) (Math.random() * size);

        SinglyLinkedList.node prev = null;
        temp = quoteList.first;
        for (int i = 0; i < randomIndex; i++)
        {
            prev = temp;
            temp = temp.next;
        }

        System.out.println("\n\u001B[34mQuote of the moment:\u001B[0m");
        System.out.println(temp.data);
        if (prev == null)
        {
            quoteList.first = temp.next;
        }
        else
        {
            prev.next = temp.next;
        }
        temp.next = null;
        if (quoteList.first == null)
        {
            fetchNewQuotes();
        }
    }

    void fetchNewQuotes() throws Exception //Fetches 5 random quotes from the database and stores them in the user's quote list
    {
        String q = "SELECT quote FROM quotes ORDER BY RAND() LIMIT 5";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(q);
        while (rs.next())
        {
            quoteList.insertAtLast(rs.getString("quote"));
        }
        rs.close();
        st.close();
    }
}

