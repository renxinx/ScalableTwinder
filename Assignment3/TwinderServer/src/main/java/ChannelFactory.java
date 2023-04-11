import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ChannelFactory extends BasePooledObjectFactory<Channel> {
    private final Connection connection;

    public ChannelFactory(Connection connection){
        this.connection = connection;
    }

    public Channel create() throws Exception{
        return connection.createChannel();
    }

    public PooledObject<Channel> wrap(Channel channel){
        return new DefaultPooledObject<>(channel);
    }
}
