package DS;

public class SinglyLinkedList
{
    static public class node
    {
        public String data;
        public node next;
        node(String data)
        {
            this.data=data;
            this.next=null;
        }
    }
    public node first=null;
    public void insertAtLast(String y)
    {
        node n=new node(y);
        node temp=first;
        if(first==null)
        {
            first=n;
        }
        else
        {
            while(temp.next!=null)
            {
                temp=temp.next;
            }
            temp.next=n;
        }
    }
}
