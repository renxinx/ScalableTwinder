import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class PotentialMatches {
    private static final String QUEUE_NAME = "swipe_queue";
    private static final Gson gson = new Gson();
    private static final int NUM_OF_CONSUMER_THREAD = 100;
    private static final ConcurrentHashMap<String, PotentialMatchesIds> userPotentialMatchesMap = new ConcurrentHashMap<String, PotentialMatchesIds>();


    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Runnable consumerThread = () -> {
            try{
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                channel.basicQos(100);
                System.out.println("[*] Waiting for messages. To exit press CTRL+C");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
//                    System.out.println(message);
                    SwipeInfo swipeInfo = gson.fromJson(message, SwipeInfo.class);
//                    System.out.println(swipeInfo.toString());
                    processSwipeInfo(swipeInfo);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    System.out.println(" [x] Received '" + message + "'");
                };
                channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        for (int i = 0; i < NUM_OF_CONSUMER_THREAD; i++){
            new Thread(consumerThread).start();
        }
    }

    private static void processSwipeInfo(SwipeInfo swipeInfo) {
        String swiper = swipeInfo.getSwiper();
        String swipee = swipeInfo.getSwipee();
        String leftOrRight = swipeInfo.getLeftOrRight();
        PotentialMatchesIds potentialMatchesIds = userPotentialMatchesMap.get(swiper);
        if (potentialMatchesIds == null){
            potentialMatchesIds = new PotentialMatchesIds();
            userPotentialMatchesMap.put(swiper, potentialMatchesIds);
        }
        if(leftOrRight.equals("right")){
            potentialMatchesIds.addRightId(swipee);
        }
        userPotentialMatchesMap.put(swiper, potentialMatchesIds);
    }
}
