import com.google.gson.Gson;
import com.rabbitmq.client.ConnectionFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.TimeoutException;

@WebServlet(name = "StatsServlet", value = "/StatsServlet")
public class StatsServlet extends HttpServlet {
    private Gson gson = new Gson();

    String dburl = "jdbc:postgresql://database-twinder.c5dgl4vukdbf.us-west-2.rds.amazonaws.com:5432/database-twinder";
    String username = "master";
    String password = "123456789qaz";
    private ChannelPool channelPool;

    @Override
    public void init() throws ServletException {
        super.init();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            com.rabbitmq.client.Connection connection = factory.newConnection();
            channelPool = new ChannelPool(100, connection);
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String url = request.getPathInfo();
        if (!isValidUrlPath(url)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            System.out.println("Invalid Url!");
            return;
        }

        String[] urlParsing = url.split("/");

//        if (!(isValidUrlParsing(urlParsing))){
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            System.out.println("Invalid Url Parsing!");
//            return;
//        }

        try {
            Connection conn = DriverManager.getConnection(dburl, username, password);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM stats_table");

            while (rs.next()) {
                String swiperid = rs.getString("swiperid");
                int userlikes = rs.getInt("userlikes");
                int userdislikes = rs.getInt("userdislikes");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new ServletException("Error accessing database", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private boolean isValidUrlPath(String url){
        return url != null && !url.isEmpty();
    }
}
