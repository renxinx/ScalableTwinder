import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.impl.ChannelN;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LikesDislikesNumber {
    private static final String QUEUE_NAME = "swipe_queue";
    private static final ConcurrentHashMap<String, UserLikes> userLikesMap = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();
    private static final int NUM_OF_CONSUMER_THREAD = 100;



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
////        ConnectionFactory factory = new ConnectionFactory();
////        factory.setHost("localhost");
////        factory.setPort(5672);
////        factory.setUsername("guest");
////        factory.setPassword("guest");
////        Connection connection = factory.newConnection();
//
////        Runnable consumerThread = () -> {
////            try{
////                Channel channel = connection.createChannel();
////                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
////                channel.basicQos(20);
////                System.out.println("[*] Waiting for messages. To exit press CTRL+C");
////
////                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
////                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
////                    SwipeInfo swipeInfo = gson.fromJson(message, SwipeInfo.class);
////                    processSwipeInfo(swipeInfo);
////                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
////                    System.out.println(" [x] Received '" + message + "'");
////                };
////                channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
////            }catch (IOException e){
////                e.printStackTrace();
////            }
////        };
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//
//        ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_CONSUMER_THREAD);
//
//        for(int i = 0; i < NUM_OF_CONSUMER_THREAD; i++){
//            executor.submit(() -> {
//                try {
//                    channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
//                        String message = new String(delivery.getBody(), "UTF-8");
//                        SwipeInfo swipeInfo = gson.fromJson(message, SwipeInfo.class);
//                        processSwipeInfo(swipeInfo);
//                    }, consumerTag -> {});
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//
//        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);



//        for (int i = 0; i < NUM_OF_CONSUMER_THREAD; i++){
//            new Thread(consumerThread).start();
//        }
    }

    public static void processSwipeInfo(SwipeInfo swipeInfo){
        String swiper = swipeInfo.getSwiper();
        String leftOrRight = swipeInfo.getLeftOrRight();
        UserLikes userLikes = userLikesMap.get(swiper);
        if (userLikes == null){
            userLikes = new UserLikes();
            userLikesMap.put(swiper, userLikes);
        }
        if ((leftOrRight).equals("right")){
            userLikes.incrementLikes();
        }else {
            userLikes.incrementDislikes();
        }
//        System.out.println(userLikes.getNumLikes());
//        System.out.println(userLikes.getNumDislikes());

        userLikesMap.put(swiper, userLikes);
    }
}
