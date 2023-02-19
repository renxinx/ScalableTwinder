import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class SwipeRequests implements Runnable{
    String basePath = "http://35.91.128.109:8080/TwinderServer_war/servlet";
    final Integer lowerBound = 1;
    final Integer upperSwiper = 5001;
    final Integer upperSwipee = 1000001;
    final Integer lengthOfComments = 256;
    final Integer lowerAscii = 33;
    final Integer upperAscii = 127;

    final Integer numOfRequests = 2500;



//    private static AtomicInteger successfulThreads = new AtomicInteger(0);
//    private static AtomicInteger failedThreads = new AtomicInteger(0);

//    private static Integer successfulThreads;
//
//    private static Integer failedThreads;

    private AtomicInteger succ;

    private AtomicInteger fail;




    BlockingDeque<Record> requestRecord;

    public SwipeRequests(AtomicInteger succ, AtomicInteger fail, BlockingDeque<Record> requestRecord) {
        this.succ = succ;
        this.fail = fail;
        this.requestRecord = requestRecord;
    }


//    public static Integer getSuccessfulThreads() {
//        return successfulThreads;
//    }
//
//    public static Integer getFailedThreads() {
//        return failedThreads;
//    }
//
//    public static void setSuccessfulThreads(Integer successfulThreads) {
//        SwipeRequests.successfulThreads = successfulThreads;
//    }
//
//    public static void setFailedThreads(Integer failedThreads) {
//        SwipeRequests.failedThreads = failedThreads;
//    }

    //    public static AtomicInteger getSuccessfulThreads() {
//        return successfulThreads;
//    }
//
//    public static AtomicInteger getFailedThreads() {
//        return failedThreads;
//    }
    private boolean sendRequest(SwipeApi apiInstance, SwipeDetails body, Timestamp beforeRequest){
        final Integer tryTimes = 5;
        for (int i = 0; i < tryTimes; i++){
            try{
                ApiResponse<Void> swipeResponse = apiInstance.swipeWithHttpInfo(body, getRandomSwipe());
                int statusCode = swipeResponse.getStatusCode();

                if (statusCode == 200 || statusCode == 201){
                    Timestamp afterResponse = Timestamp.from(Instant.now());
                    long latency = afterResponse.getTime() - beforeRequest.getTime();
                    requestRecord.add(new Record(beforeRequest, "POST", statusCode, latency));
                    return true;
                }
            } catch (ApiException e){
                System.err.println("Exception when calling SwipeApi#swipe");
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getRandomSwipe(){
        final double div = 0.5;
        if (Math.random() < div){
            return "left";
        }
        else{
            return "right";
        }
    }

    private String getRandomSwiper(){
        return String.valueOf(ThreadLocalRandom.current().nextInt(lowerBound,upperSwiper));
    }

    private String getRandomSwipee(){
        return String.valueOf(ThreadLocalRandom.current().nextInt(lowerBound,upperSwipee));
    }

    private String getRandomComment(){
        char[] chars = new char[lengthOfComments];
        for (int i =0; i < lengthOfComments; i++){
            chars[i] = (char) ThreadLocalRandom.current().nextInt(lowerAscii,upperAscii);
        }
        return new String(chars);
    }

    @Override
    public void run() {
        ApiClient apiClientInstance = new ApiClient();
        apiClientInstance.setBasePath(basePath);
        SwipeApi apiInstance = new SwipeApi(apiClientInstance);
        for(int i = 0; i < numOfRequests; i++){
            Timestamp beforeRequest = Timestamp.from(Instant.now());
            SwipeDetails body = new SwipeDetails();
            body.setSwiper(getRandomSwiper());
            body.setSwipee(getRandomSwipee());
            body.setComment(getRandomComment());

            if (sendRequest(apiInstance, body, beforeRequest)){
                succ.getAndIncrement();

            }
            else{
                fail.getAndIncrement();
            }
        }
        Main.completed.countDown();
    }

//    public int getSuccessLen(){
//        return succ.size();
//    }
//
//    public int getFailLen(){
//        return fail.size();
//    }
}
