//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Properties;
//
//public class DatabaseConnection {
//
//    // AWS RDS PostgreSQL database configurations
//    private static final String dbUrl = "jdbc:postgresql://database-1.c5dgl4vukdbf.us-west-2.rds.amazonaws.com:5432/database_twinder";
//    private static String username = "master";
//    private static String password = "123456789qaz";
//    static Properties props = new Properties();
//
//    private static void main() {
//        // Set connection properties
//        props.setProperty("user", username);
//        props.setProperty("password", password);
//        props.setProperty("ssl", "true"); // set SSL to true for secure connections
//        props.setProperty("sslmode", "require"); // set the SSL mode to require
//    }
//
//    private static void getConnection(){
//        try (Connection conn = DriverManager.getConnection(dbUrl, props)) {
//            System.out.println("Connected to database successfully!");
//            // Perform database operations here
//        } catch (SQLException e) {
//            System.out.println("Connection to database failed: " + e.getMessage());
//        }
//    }
//}
