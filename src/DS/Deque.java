package DS;
public class Deque
{
    int cap,front,rear;
    String [] Q;
    public Deque(int size)
    {
        cap=size;
        front=-1;
        rear=-1;
        Q=new String [cap];
    }
    public void insertAtRear(String y)
    {
        if((front==rear+1) || (front==0 && rear==cap-1))
        {
            System.out.println("Overflow");
        }
        else
        {
            rear=(rear+1)%cap;
            Q[rear]=y;
            if(front==-1)
            {
                front=0;
            }
        }
    }

    public String deleteAtFront()
    {
        if(front<0)
        {
            System.out.println("The queue is empty");
            return null;
        }
        else
        {
            String y=Q[front];
            if(front==rear)
            {
                front=-1;
                rear=-1;
            }
            else
            {
                front=(front+1)%cap;
            }
            return y;
        }
    }
}
