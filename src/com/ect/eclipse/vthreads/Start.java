package com.ect.eclipse.vthreads;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Start {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private void start() throws InterruptedException {
        final ExecutorService otherService = Executors.newFixedThreadPool(20);
        final Random r = new Random();
        final CountDownLatch latch = new CountDownLatch(10);
        this.executorService.scheduleWithFixedDelay(() -> {
            System.out.println(Thread.currentThread().getName() + ": Performing work...");
            try {
                Thread.sleep(r.nextLong(9999));
            } catch (final InterruptedException e) {
            }
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + ": Done with work");
        }, 0, 10, TimeUnit.SECONDS);
        final Runnable run = () -> {
            final Runnable perform = () -> {
                System.out.println(Thread.currentThread().getName() + ": Performing other work...");
                try {
                    Thread.sleep(r.nextLong(9999));
                } catch (final InterruptedException e) {
                }
                Thread.ofVirtual().start(() -> performVirtual(r.nextInt(20)));
                if (r.nextBoolean()) {
                    Thread.ofVirtual().start(() -> performVirtual(r.nextInt(20)));
                }
                latch.countDown();
                System.out.println(Thread.currentThread().getName() + ": Done with other work");
            };
            otherService.execute(perform);
        };

        this.executorService.scheduleWithFixedDelay(run, 0, 10, TimeUnit.SECONDS);
        latch.await();
    }

    private void performVirtual(final int cnt) {
        System.out.println("Performing virtual work: " + cnt);
        final int i = fibonacci(cnt);
        System.out.println("Done with virtual work: " + i);
    }

    private int fibonacci(final int n) {
        if (n == 0)
            return 0;
        if (n == 1)
            return 1;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void main(final String[] args) throws InterruptedException {
        new Start().start();
    }
}
