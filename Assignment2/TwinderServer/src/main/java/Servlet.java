import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.rabbitmq.client.MessageProperties;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

@WebServlet(name = "Servlet", value = "/Servlet")
public class Servlet extends HttpServlet {

    private final int requestValue1 = 1;
    private final int requestValue2 = 5000;
    private final int requestValue3 = 1000000;
    private final int characterValue = 256;

//    private final int numOfChannel = 10;
    private static final String QUEUE_NAME = "swipe_queue";
//    private final String EXCHANGE_NAME = "swipe_exchange";
    private Gson gson = new Gson();
    private ChannelPool channelPool;
    private static AtomicInteger count = new AtomicInteger(0);
    private static AtomicInteger count1 = new AtomicInteger(0);


//    synchronized public static void Add(){
//
//        System.out.println("xxx"+count1.incrementAndGet());
//    }
    @Override
    public void init() throws ServletException {
        super.init();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            Connection connection = factory.newConnection();
            channelPool = new ChannelPool(100, connection);
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String url = request.getPathInfo();
        System.out.println(url);
        if (!isValidUrlPath(url)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            System.out.println("Invalid Url!");
            return;
        }

        String[] urlParsing = url.split("/");

        if (!(isValidUrlParsing(urlParsing))){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            System.out.println("Invalid Url Parsing!");
            return;
        }

        StringBuilder swipeDetails = new StringBuilder();
        String line;

        while ((line = request.getReader().readLine()) != null) {
            swipeDetails.append(line);
        }

        JsonObject swipeDetailsJson = gson.fromJson(swipeDetails.toString(), JsonObject.class);
        String leftOrRight = urlParsing[2];
        swipeDetailsJson.addProperty("leftOrRight", leftOrRight);
        String payload = gson.toJson(swipeDetailsJson);
//        System.out.println(payload);
        try{
            Swipe req = gson.fromJson(payload, Swipe.class);
            if (!isValid(req)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            else{
                response.setStatus(HttpServletResponse.SC_OK);
//                Add();
                response.getWriter().write("Valid Url!");
                try{
                    sendPayloadToQueue(payload);
                }catch (Exception e){
                    e.printStackTrace();
                }
                response.getWriter().write("swipe doPost works!");
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
//            System.out.println("123");
//            sendPayloadToQueue(payload);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendPayloadToQueue(String payload){
        Channel channel = null;
        try{
            channel = channelPool.getChannel();
            channel.queueDeclare(Servlet.QUEUE_NAME, true, false, false, null);
            channel.basicPublish("", Servlet.QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    payload.getBytes(StandardCharsets.UTF_8));
            count.getAndIncrement();
            System.out.println(count);
            System.out.println("Message has been sent!");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if(channel != null){
                    channelPool.returnChannel(channel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValid(Swipe req){
        if (Integer.valueOf(req.getSwiper()) < requestValue1 || Integer.valueOf(req.getSwiper()) > requestValue2) {
            return false;
        }

        if (Integer.valueOf(req.getSwipee()) < requestValue1 || Integer.valueOf(req.getSwipee()) > requestValue3) {
            return false;
        }

        if (req.getComment().length() > characterValue) {
            return false;
        }

        return true;
    }

    private boolean isValidUrlPath(String url){
        return url != null && !url.isEmpty();
    }

    private boolean isValidUrlParsing(String[] urlParsing){
//        System.out.println(Arrays.toString(urlParsing));
//        System.out.println(urlParsing.length);
        if (urlParsing.length == 3){
            return (("left".equals(urlParsing[2]) ) || ("right".equals(urlParsing[2])));
        }
        return false;
    }
}