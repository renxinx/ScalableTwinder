import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class GetThread implements Runnable{
//    private final CountDownLatch latch;
    private final int numRequests = 5;
    static final Integer lowerBound = 1;
    static final Integer upperSwiper = 5001;
    private AtomicInteger getsucc;
    private AtomicInteger getfail;

    public GetThread(AtomicInteger getsucc, AtomicInteger getfail) {
        this.getsucc = getsucc;
        this.getfail = getfail;
    }

    @Override
    public void run() {
        String MATCHES_PATH =
                "http://localhost:8080/TwinderServer_war_exploded/matchesservlet";
        String STATS_PATH =
                "http://localhost:8080/TwinderServer_war_exploded/statsservlet";

        for (int i = 0; i < numRequests; i++){
            String BASE_PATH = STATS_PATH;
            int randInt = ThreadLocalRandom.current().nextInt(2);
            if (randInt == 1) {
                BASE_PATH = MATCHES_PATH;
            }
            String userId = String.valueOf(ThreadLocalRandom.current().nextInt(lowerBound,upperSwiper));
            long startTime = System.currentTimeMillis();
            long endTime;
            int statusCode = -1;

            try{
                URL url = new URL(BASE_PATH + "/"+userId + "/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                statusCode = connection.getResponseCode();
                endTime = System.currentTimeMillis();
                getsucc.getAndIncrement();
            } catch (Exception e){
                endTime = System.currentTimeMillis();
                getfail.getAndIncrement();
            }
            try{
                Thread.sleep(200);
            }catch (InterruptedException ignore){
                return;
            }
            Main.completed.countDown();
        }
    }
}
