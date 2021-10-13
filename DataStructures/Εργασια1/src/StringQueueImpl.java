import java.io.PrintStream;
import java.util.NoSuchElementException;

public class StringQueueImpl implements StringQueue {

    public Node front; //oldest item
    public Node rear; //newest item
    public int sizeOfQueue = 0;

    //Returns true if the queue is empty
    public boolean isEmpty(){
        return front == null;
    }

    //Inserts items at the back of the queue
    //Editing the tail node
    @Override
    public void put(String item) {
        Node new_node = new Node(item);
        if(!isEmpty()){
           rear.setNext(new_node);
           rear = new_node;
        }else{
            front = rear = new_node;
        }
        sizeOfQueue++;
    }

    @Override
    public String get() throws NoSuchElementException {
        if(isEmpty()) throw new NoSuchElementException();

        String data = front.getData();

        if(front==rear){
            front = rear = null;
        }else{
            front = front.getNext();
        }
        sizeOfQueue--;
        return data;
    }

    @Override
    public String peek() throws NoSuchElementException {
        if(isEmpty()) throw new NoSuchElementException();

        return front.getData();

    }

    @Override
    public void printQueue(PrintStream stream) {
        Node current = front;

        while(current!=null){
            stream.println(current.data);
            current = current.next;
        }
    }

    @Override
    public int size() {
        return sizeOfQueue;
    }
}
