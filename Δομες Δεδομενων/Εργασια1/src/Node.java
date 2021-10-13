public class Node {
    protected String data;
    protected Node next = null;

    Node(String data){
        this.data = data;
    }

    public String getData(){
        return data;
    }

    public Node getNext(){
        return next;
    }

    public void setNext(Node next){
        this.next = next;
    }
}
