/// Gai Ashkenazy
// 204459127
// Ex-3

/**
 * A synchronized bounded-size queue for multithreaded producer-consumer applications.
 *
 * @param <T> Type of data items
 */
public class SynchronizedQueue<T> {

    private T[] buffer;
    private int producers;

    private int capacity; //q capacity
    private int size;     //q size (num of elements in the q)

    private int deqIndex; //index of element to dequeue
    private int enqIndex; //index of element to enqueue


    /**
     * Constructor. Allocates a buffer (an array) with the given capacity and
     * resets pointers and counters.
     * @param capacity Buffer capacity
     */
    public SynchronizedQueue(int capacity) {
        this.buffer = (T[])(new Object[capacity]);
        this.producers = 0;
        // TODO: Add more logic here as necessary
        this.capacity = this.buffer.length;
        this.size = 0;
        this.deqIndex = 0;
        this.enqIndex = 0;
    }

    /**
     * Dequeue's the first item from the queue and returns it.
     * If the queue is empty but producers are still registered to this queue,
     * this method blocks until some item is available.
     * If the queue is empty and no more items are planned to be added to this
     * queue (because no producers are registered), this method returns null.
     *
     * @return The first item, or null if there are no more items
     * @see #registerProducer()
     * @see #unregisterProducer()
     */
    public T dequeue() {
        // initialize T to null
        T element = null;

        synchronized (this) {
            while (this.size == 0) {
                if (this.producers == 0) {
                    return null;
                }
                try{
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
            }

            element = this.buffer[this.deqIndex];
            this.deqIndex = (this.deqIndex + 1) % this.capacity;
            this.size--;

            //notify all other threads that dequeue is done
            this.notifyAll();
        }

        return element;
    }

    /**
     * Enqueues an item to the end of this queue. If the queue is full, this
     * method blocks until some space becomes available.
     *
     * @param item Item to enqueue
     */
    public void enqueue(T item) {

        synchronized (this) {
            while (this.size == this.capacity) {
                try{
                    // wait until this thread will be notified
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
            }

            this.buffer[this.enqIndex] = item;
            this.enqIndex = (this.enqIndex + 1) % this.capacity;
            this.size++;

            // notify all threads that enqueue is done
            this.notifyAll();
        }
    }

    /**
     * Returns the capacity of this queue
     * @return queue capacity
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Returns the current size of the queue (number of elements in it)
     * @return queue size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Registers a producer to this queue. This method actually increases the
     * internal producers counter of this queue by 1. This counter is used to
     * determine whether the queue is still active and to avoid blocking of
     * consumer threads that try to dequeue elements from an empty queue, when
     * no producer is expected to add any more items.
     * Every producer of this queue must call this method before starting to
     * enqueue items, and must also call <see>{@link #unregisterProducer()}</see> when
     * finishes to enqueue all items.
     *
     * @see #dequeue()
     * @see #unregisterProducer()
     */
    public synchronized void registerProducer() {
            this.producers++;
    }

    /**
     * Unregisters a producer from this queue. See <see>{@link #registerProducer()}</see>.
     *
     * @see #dequeue()
     * @see #registerProducer()
     */
    public synchronized void unregisterProducer() {
        this.producers--;
        // if there are no producers notify all
        this.notifyAll();
    }
}