package edu.cmu.cs.cs214.rec02;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * A resizable-array implementation of the {@link IntQueue} interface.
 */
public class ArrayIntQueue implements IntQueue {
    private static final Logger logger = LogManager.getLogger(ArrayIntQueue.class);

    private static final int INITIAL_SIZE = 10;

    private int[] elementData;
    private int head;
    private int size;

    public ArrayIntQueue() {
        elementData = new int[INITIAL_SIZE];
        head = 0;
        size = 0;
        logger.debug("Initialized new ArrayIntQueue with capacity: {}", INITIAL_SIZE);
    }

    @Override
    public void clear() {
        logger.debug("Clearing queue (current size: {})", size);
        head = 0;
        size = 0;
    }

    @Override
    public Integer dequeue() {
        if (isEmpty()) {
            logger.warn("Attempt to dequeue from empty queue");
            return null;
        }
        Integer value = elementData[head];
        head = (head + 1) % elementData.length;
        size--;
        logger.debug("Dequeued value: {} (new size: {})", value, size);
        return value;
    }

    @Override
    public boolean enqueue(Integer value) {
        logger.debug("Enqueueing value: {} (current size: {})", value, size);
        ensureCapacity();
        int tail = (head + size) % elementData.length;
        elementData[tail] = value;
        size++;
        return true;
    }

    @Override
    public boolean isEmpty() {
        boolean empty = size == 0;
        logger.trace("isEmpty() called - returned: {}", empty);
        return empty;
    }

    @Override
    public Integer peek() {
        if (isEmpty()) {
            logger.warn("Attempt to peek empty queue");
            return null;
        }
        Integer value = elementData[head];
        logger.debug("Peeked value: {}", value);
        return value;
    }

    @Override
    public int size() {
        logger.trace("size() called - returned: {}", size);
        return size;
    }

    private void ensureCapacity() {
        if (size == elementData.length) {
            int oldCapacity = elementData.length;
            int newCapacity = 2 * oldCapacity + 1;
            logger.info("Resizing queue from {} to {}", oldCapacity, newCapacity);

            int[] newData = new int[newCapacity];
            for (int i = 0; i < size; i++) {
                newData[i] = elementData[(head + i) % oldCapacity];
            }
            elementData = newData;
            head = 0;
        }
    }
}
