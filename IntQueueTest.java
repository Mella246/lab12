package edu.cmu.cs.cs214.rec02;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.*;

public class IntQueueTest {
    private static final Logger logger = LogManager.getLogger(IntQueueTest.class);

    private IntQueue mQueue;

    @Before
    public void setUp() {
        logger.info("Setting up test");
        mQueue = new ArrayIntQueue(); // You can switch to LinkedIntQueue here
    }

    @Test
    public void testIsEmpty() {
        logger.debug("Running testIsEmpty");
        assertTrue(mQueue.isEmpty());
        mQueue.enqueue(1);
        assertFalse(mQueue.isEmpty());
    }

    @Test
    public void testPeekEmptyQueue() {
        logger.debug("Running testPeekEmptyQueue");
        assertNull(mQueue.peek());
    }

    @Test
    public void testPeekNonEmptyQueue() {
        logger.debug("Running testPeekNonEmptyQueue");
        mQueue.enqueue(5);
        assertEquals(Integer.valueOf(5), mQueue.peek());
        assertEquals(1, mQueue.size()); // size should not change
    }

    @Test
    public void testEnqueueDequeue() {
        logger.debug("Running testEnqueueDequeue");
        int testValue = 42;
        mQueue.enqueue(testValue);
        assertEquals(Integer.valueOf(testValue), mQueue.dequeue());
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testDequeueEmptyQueue() {
        logger.debug("Running testDequeueEmptyQueue");
        assertNull(mQueue.dequeue());
    }

    @Test
    public void testDequeue() {
        logger.debug("Running testDequeue");
        mQueue.enqueue(10);
        mQueue.enqueue(20);
        assertEquals(Integer.valueOf(10), mQueue.dequeue());
        assertEquals(Integer.valueOf(20), mQueue.dequeue());
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testClear() {
        logger.debug("Running testClear");
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.clear();
        assertTrue(mQueue.isEmpty());
        assertEquals(0, mQueue.size());
    }

    @Test
    public void testResize() {
        logger.info("Testing queue resize behavior");
        int initial = 10;
        for (int i = 0; i < initial * 2; i++) {
            mQueue.enqueue(i);
        }
        assertEquals(initial * 2, mQueue.size());

        for (int i = 0; i < initial * 2; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testCircularBehavior() {
        logger.debug("Running testCircularBehavior");
        for (int i = 0; i < 5; i++) {
            mQueue.enqueue(i);
        }

        mQueue.dequeue(); // remove 0
        mQueue.dequeue(); // remove 1

        mQueue.enqueue(5);
        mQueue.enqueue(6);

        assertEquals(5, mQueue.size());

        assertEquals(Integer.valueOf(2), mQueue.dequeue());
        assertEquals(Integer.valueOf(3), mQueue.dequeue());
        assertEquals(Integer.valueOf(4), mQueue.dequeue());
        assertEquals(Integer.valueOf(5), mQueue.dequeue());
        assertEquals(Integer.valueOf(6), mQueue.dequeue());

        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testMultipleOperations() {
        logger.debug("Running testMultipleOperations");
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        assertEquals(Integer.valueOf(1), mQueue.dequeue());

        mQueue.enqueue(3);
        mQueue.enqueue(4);
        assertEquals(Integer.valueOf(2), mQueue.dequeue());

        mQueue.enqueue(5);
        assertEquals(Integer.valueOf(3), mQueue.peek());
        assertEquals(3, mQueue.size());
    }

    @Test
    public void testContentFromFile() throws IOException {
        logger.debug("Running testContentFromFile");
        InputStream in = new FileInputStream("src/test/resources/data.txt");
        try (Scanner scanner = new Scanner(in)) {
            scanner.useDelimiter("\\s*fish\\s*");
            List<Integer> correctResult = new ArrayList<>();
            while (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                correctResult.add(input);
                mQueue.enqueue(input);
            }

            for (Integer expected : correctResult) {
                assertEquals(expected, mQueue.dequeue());
            }

            assertTrue(mQueue.isEmpty());
        }
    }
}
