import java.util.NoSuchElementException;

public interface ListInterface {
    /**
     * Inserts an data at the front of the list
     *
     * @param data the inserted data
     */


    void insertAtFront(int data);

    /**
     * Inserts an data at the end of the list
     *
     * @param data the inserted item
     */
    void insertAtBack(int data);

    /**
     * Returns and removes the data from the list head
     *
     * @return the data contained in the list head
     * @throws NoSuchElementException if the list is empty
     */
    int removeFromFront() throws NoSuchElementException;

    /**
     * Returns and removes the data from the list tail
     *
     * @return the data contained in the list tail
     * @throws NoSuchElementException if the list is empty
     */
    int removeFromBack() throws NoSuchElementException;

    /**
     * Determine whether list is empty
     *
     * @return true if list is empty
     */
    boolean isEmpty();

    int peek();
}
