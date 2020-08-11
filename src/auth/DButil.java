package auth;

import java.sql.*;

public class DButil {
    private DButil(){};

    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/java_bank?characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
                "root","123456123456");
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
