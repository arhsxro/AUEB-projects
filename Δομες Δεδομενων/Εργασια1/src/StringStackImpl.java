import java.io.PrintStream;
import java.util.NoSuchElementException;

public class StringStackImpl implements StringStack{

    public Node top;
    public int sizeOfStack = 0;

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public void push(String item) {
        Node new_node = new Node(item);
        if(!isEmpty()){
            new_node.next = top;
            top = new_node;
        }else{
            top = new_node;
        }
        sizeOfStack++;
    }

    @Override
    public String pop() throws NoSuchElementException {
        if(isEmpty()) throw new NoSuchElementException();

        String removed_item = top.data;
        top = top.next;
        sizeOfStack--;
        return removed_item;
    }

    @Override
    public String peek() throws NoSuchElementException {
        if(isEmpty()) throw new NoSuchElementException();

        return top.data;
    }

    @Override
    public void printStack(PrintStream stream) {
        Node current = top;
        while (current!=null){
            stream.println(current.data);
            current = current.next;
        }
    }

    @Override
    public int size() {
        return sizeOfStack;
    }
}
