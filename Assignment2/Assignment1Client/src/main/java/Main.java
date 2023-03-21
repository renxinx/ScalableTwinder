import io.swagger.client.ApiClient;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static CountDownLatch completed;
    public static void main(String[] args) throws InterruptedException{
        AtomicInteger succ = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
        final Integer numOfThreads = 200;
        completed = new CountDownLatch(numOfThreads);
        Instant start = Instant.now();

        for (int i = 0; i < numOfThreads; i++){

            SwipeRequests requests = new SwipeRequests(succ,fail);
            Thread thread = new Thread(requests);
            thread.start();
        }

        completed.await();
        Instant end = Instant.now();
        Duration totalRunTime = Duration.between(start, end);
        double totalThroughput =  succ.doubleValue() / (double)totalRunTime.toMillis()*1000;

        System.out.println("number of successful requests sent: "+ succ.doubleValue());
        System.out.println("number of unsuccessful requests: " + fail.doubleValue());
        System.out.println("the total run time (wall time) for all threads to complete: "+ totalRunTime.toMillis() + " milliseconds");
        System.out.println("the total throughput in requests per second: "+ totalThroughput + " requests per second");
    }
}