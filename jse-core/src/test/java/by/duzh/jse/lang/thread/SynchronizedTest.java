package by.duzh.jse.lang.thread;

import org.junit.Assert;
import org.junit.Test;

class SynchronizedMethod {
    public int value;

    public synchronized void incValue() {
        try {
            Thread.sleep(500);
            value++;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

class SynchronizedBlock {
    public int value;

    public void incValue() {
        synchronized (this) {
            try {
                Thread.sleep(500);
                value++;
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}

public class SynchronizedTest {

    @Test
    public void testSynchronizedMethod() throws InterruptedException {
        SynchronizedMethod object1 = new SynchronizedMethod();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                object1.incValue();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                object1.incValue();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        Assert.assertEquals(15, object1.value);
    }

    @Test
    public void testSynchronizedBlock() throws InterruptedException {
        SynchronizedBlock object1 = new SynchronizedBlock();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                object1.incValue();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                object1.incValue();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        Assert.assertEquals(15, object1.value);
    }
}