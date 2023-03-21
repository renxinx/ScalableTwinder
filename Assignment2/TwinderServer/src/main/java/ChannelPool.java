import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class ChannelPool {
    private final BlockingDeque<Channel> channelPool;
    private final Connection connection;


    public ChannelPool(int initCapacity, Connection connection) throws IOException{
        this.connection = connection;
        channelPool = new LinkedBlockingDeque<>(initCapacity);
        for (int i = 0; i < initCapacity; i++){
            Channel channel = connection.createChannel();
            channelPool.offer(channel);
        }
    }

    public Channel getChannel() throws InterruptedException, IOException{
        Channel channel = channelPool.poll(5, TimeUnit.SECONDS);
        if (channel == null){
            channel = connection.createChannel();
            channelPool.offer(channel);
        }
        return channel;
    }

    public void returnChannel(Channel channel){
        if(channel != null){
            channelPool.offer(channel);
        }
    }
}
