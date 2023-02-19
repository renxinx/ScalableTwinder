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
        final Integer numOfThreads = 200;
        final String basePath = "http://35.91.128.109:8080/TwinderServer_war/servlet";
        completed = new CountDownLatch(numOfThreads);
        Instant start = Instant.now();

        AtomicInteger succ = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
//        int totalSuccess = 0;
//        int totalFail = 0;

        for (int i = 0; i < numOfThreads; i++){

//            List<Integer> fail = new ArrayList<Integer>();
            SwipeRequests requests = new SwipeRequests(succ,fail);
            Thread thread = new Thread(requests);
            thread.start();
//            totalSuccess += requests.getSuccessLen();
//            totalFail += requests.getFailLen();

        }

//        1.main里面放一个线程安全的list，blockingQueue，传进去swipeRequests，就会有一个attribute是这个list
//        2. swipeRequest里

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