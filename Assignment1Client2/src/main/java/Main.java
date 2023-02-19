import io.swagger.client.ApiClient;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static CountDownLatch completed;
    public static void main(String[] args) throws InterruptedException{
        final Integer numOfThreads = 200;
        final String basePath = "http://35.91.128.109:8080/TwinderServer_war/servlet";
        final String filePath = "/Users/renxinxie/Desktop/School/CS 6650 Distributed Systems/Assignment/requestrecords1.csv";
        final RequestCounter counter = new RequestCounter();
        AtomicInteger succ = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
        completed = new CountDownLatch(numOfThreads);
        BlockingDeque<Record> requestRecords = new LinkedBlockingDeque<>();
        BlockingDeque<Long> responseTimes = new LinkedBlockingDeque<>();
        BlockingDeque<Double> throughputList = new LinkedBlockingDeque<>();

        Instant start = Instant.now();


//        int totalSuccess = 0;
//        int totalFail = 0;

        for (int i = 0; i < numOfThreads; i++){

//            List<Integer> fail = new ArrayList<Integer>();
            SwipeRequests requests = new SwipeRequests(succ,fail, requestRecords);
            Thread thread = new Thread(requests);
            thread.start();
            counter.inc();
//            totalSuccess += requests.getSuccessLen();
//            totalFail += requests.getFailLen();

        }

        completed.await();
        CsvGenerator.exportToCsv(requestRecords, filePath);

        Instant end = Instant.now();
        Duration totalRunTime = Duration.between(start, end);
        double totalThroughput =  succ.doubleValue() / (double)totalRunTime.toMillis()*1000;

        Timestamp lastTimestamp = requestRecords.getLast().getBeforeRequest();
        Timestamp currentTimestamp = requestRecords.getFirst().getBeforeRequest();
        long throughput = 0;

        while(!currentTimestamp.after(lastTimestamp)) {
            while(requestRecords.peek() != null && requestRecords.peek().getBeforeRequest().before(currentTimestamp)) {
                Record record = requestRecords.poll();
                responseTimes.add(record.getLatency());
                throughput++;
            }
            throughputList.add((double) throughput);
            throughput = 0;
            currentTimestamp = new Timestamp(currentTimestamp.getTime() + 1000);
        }

        long sum = 0;
        for (long responseTime : responseTimes) {
            sum += responseTime;
        }
        double meanResponseTime = (double) sum / responseTimes.size();

        ArrayList<Long> responseTimeList = new ArrayList<>(responseTimes);
        responseTimeList.sort(Long::compareTo);
        double medianResponseTime;
        if (responseTimeList.size() % 2 == 0) {
            medianResponseTime = ((double)responseTimeList.get(responseTimeList.size() / 2 - 1) + (double)responseTimeList.get(responseTimeList.size() / 2)) / 2;
        } else {
            medianResponseTime = (double)responseTimeList.get(responseTimeList.size() / 2);
        }

        double p99ResponseTime = responseTimeList.get((int) (responseTimeList.size() * 0.99));


        long minResponseTime = responseTimeList.get(0);
        long maxResponseTime = responseTimeList.get(responseTimeList.size() - 1);


        System.out.println("Value should be equal to " + numOfThreads + " It is: " + counter.getVal() + ".");
        System.out.println("number of successful requests sent: "+ succ.doubleValue());
        System.out.println("number of unsuccessful requests: " + fail.doubleValue());
        System.out.println("the total run time (wall time) for all threads to complete: "+ totalRunTime.toMillis() + " milliseconds");
        System.out.println("the total throughput in requests per second: "+ totalThroughput + " requests per second");
        System.out.println("The Mean Response Time is " + meanResponseTime + " milliseconds.");
        System.out.println("The Median Response Time is " + medianResponseTime + " milliseconds.");
        System.out.println("The p99 Response Time is " + p99ResponseTime + " milliseconds.");
        System.out.println("The Min Response Time is " + minResponseTime + " milliseconds.");
        System.out.println("The Max Response Time is " + maxResponseTime + " milliseconds.");
    }
}