
import java.util.Comparator;

/**
 * Priority Queue implementation based in Heap
 */
public class MaxPQ implements MaxPQInterface {
    /**
     * Heap based implementation of PriorityQueue
     * To implement it you need to implement the following helper functions as well
     * swim, sing, swap, grow
     */

    private Disk[] heap; // the heap to store data in
    private int size; // current size of the queue
    private Comparator<Disk> comparator; // the comparator to use between the objects

    private static final int DEFAULT_CAPACITY = 4; // default capacity
    private static final int AUTOGROW_SIZE = 4; // default auto grow

    /**
     * Queue constructor
     *
     * @param comparator
     */
    public MaxPQ(Comparator<Disk> comparator) {
        this.heap = new Disk[DEFAULT_CAPACITY + 1];
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * Inserts the specified element into this priority queue.
     *
     * @param item
     */
    @Override
    public void add(Disk item) {
       //If queue is full
        if (this.size == this.heap.length -1) {
            this.grow();
        }

        this.size++;

        this.heap[this.size] = item;

        this.swim(size);

    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of the queue
     */
    @Override
    public Disk peek() {

        if (this.size == 0) {
            return null;
        }

        return this.heap[1];

    }

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of the queue
     */
    @Override
    public Disk getMax() {
        if (this.size == 0) {
            return null;
        }

        Disk element = this.heap[1];

        this.heap[1] = this.heap[this.size]; 
        this.size--;

        this.sink(1);

        return element;
    }

    /**
     * Helper function to swim items to the top
     *
     * @param i the index of the item to swim
     */
    private void swim(int i) {
        //If there is only one element
        if (i == 1) {
            return;
        }
        int parent = i / 2; 

        while (i != 1 && this.comparator.compare(this.heap[i], this.heap[parent]) > 0) {
            this.swap(i, parent);
            i = parent;
            parent = i / 2;
        }
    }

    /**
     * Helper function to swim items to the bottom
     *
     * @param i the index of the item to sink
     */
    private void sink(int i) {
        int leftChildPos = 2 * i;
        int rightChildPos = 2 * i + 1; 

        
        if (leftChildPos > this.size) {
            return;
        }

        while (leftChildPos <= this.size) {
            int max = leftChildPos; 

            if (rightChildPos <= this.size) {

                if (this.comparator.compare(this.heap[leftChildPos], this.heap[rightChildPos]) < 0) {
                    max = rightChildPos;
                }
            }

            if (this.comparator.compare(this.heap[1], this.heap[max]) >= 0) {
                break;
            } else {
                this.swap(i, max);
                i = max;
                leftChildPos = 2 * i;
                rightChildPos = 2 * i + 1;
            }
        }
    }

    /**
     * Helper function to swap two elements in the heap
     *
     * @param i the first element's index
     * @param j the second element's index
     */
    private void swap(int i, int j) {
        Disk tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    /**
     * Helper function to grow the size of the heap
     */
    private void grow() {
        Disk[] newHeap = (Disk[]) new Disk[heap.length + AUTOGROW_SIZE];

        // copy array (notice: in the priority queue, elements are located in the array slots with positions in [1, size])
        for (int i = 0; i <= size; i++) {
            newHeap[i] = heap[i];
        }

        heap = newHeap;
    }
}