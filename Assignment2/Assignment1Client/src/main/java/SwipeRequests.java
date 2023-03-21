import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class SwipeRequests implements Runnable{
    static final String basePath = "http://localhost:8080/TwinderServer_war_exploded/servlet";
    static final Integer lowerBound = 1;
    static final Integer upperSwiper = 5001;
    static final Integer upperSwipee = 1000001;
    static final Integer lengthOfComments = 256;
    static final Integer lowerAscii = 33;
    static final Integer upperAscii = 127;

    static final Integer numOfRequests = 2500;

    private AtomicInteger succ;

    private AtomicInteger fail;
    public SwipeRequests(AtomicInteger succ, AtomicInteger fail) {
        this.succ = succ;
        this.fail = fail;
    }

    @Override
    public void run() {
        SwipeApi apiInstance = new SwipeApi();
        ApiClient apiClientInstance = apiInstance.getApiClient();
        apiClientInstance.setBasePath(basePath);

        for(int i = 0; i < numOfRequests; i++){
            SwipeDetails swipeDetails = new SwipeDetails();
            swipeDetails.setSwiper(getRandomSwiper());
            swipeDetails.setSwipee(getRandomSwipee());
            swipeDetails.setComment(getRandomComment());

            if (sendRequest(apiInstance, swipeDetails)){
                succ.getAndIncrement();
            }
            else{
                fail.getAndIncrement();
            }
        }
        Main.completed.countDown();
    }

    private static boolean sendRequest(SwipeApi apiInstance, SwipeDetails swipeDetails){
        final Integer tryTimes = 5;
        for (int i = 0; i < tryTimes; i++){
            try{

                String leftOrRight = getRandomLeftOrRight();
                apiInstance.swipe(swipeDetails, leftOrRight);
                ApiResponse<Void> swipeResponse = apiInstance.swipeWithHttpInfo(swipeDetails,leftOrRight);
                int statusCode = swipeResponse.getStatusCode();
                if (statusCode == 200 || statusCode == 201){
                    return true;
                }
            } catch (ApiException e){
                System.err.println("Exception when calling SwipeApi#swipe");
                e.printStackTrace();
            }
        }
        return false;
    }

    private static String getRandomLeftOrRight(){
        final double div = 0.5;
        if (Math.random() < div){
            return "left";
        }
        else{
            return "right";
        }
    }

    private static String getRandomSwiper(){
        return String.valueOf(ThreadLocalRandom.current().nextInt(lowerBound,upperSwiper));
    }

    private static String getRandomSwipee(){
        return String.valueOf(ThreadLocalRandom.current().nextInt(lowerBound,upperSwipee));
    }

    private static String getRandomComment(){
        char[] chars = new char[lengthOfComments];
        for (int i =0; i < lengthOfComments; i++){
            chars[i] = (char) ThreadLocalRandom.current().nextInt(lowerAscii,upperAscii);
        }
        return new String(chars);
    }


}
