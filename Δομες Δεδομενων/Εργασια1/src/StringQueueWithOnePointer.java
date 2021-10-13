import java.io.PrintStream;
import java.util.NoSuchElementException;

public class StringQueueWithOnePointer implements StringQueue{
	public Node rear;
	public int size;
	
	@Override
	public boolean isEmpty() {
		return rear == null;
	}
	
	@Override
	public void put(String item) {
		Node nodeToInsert = new Node(item);
		if(isEmpty()) {
			nodeToInsert.next = nodeToInsert;
		}else {
			nodeToInsert.next = rear.next;
			rear.next = nodeToInsert;
		}
		rear = nodeToInsert;
		size++;
	}
	
	@Override
	public String get() throws NoSuchElementException {
        if(isEmpty()) throw new NoSuchElementException();
        
        String data = rear.next.data;
        if(rear.next == rear) { // If there is only one node in the queue
        	rear = null;
        }else {
        	rear.next = rear.next.next;
        }
        size--;
		return data;

	}
	@Override
	public String peek() throws NoSuchElementException {
		if(isEmpty()) throw new NoSuchElementException();
		return rear.next.data;
	}
	
	@Override
	public void printQueue(PrintStream stream) {
		Node current = rear.next;
		while(current!=null){
            stream.println(current.data);
            current = current.next;
        }
		
	}
	@Override
	public int size() {
		return this.size;
	}
	
	
	
	
}
