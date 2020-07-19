package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.*;

public class DBUser {

    //database connection
    private static final Connection conn = DBConnection.openConnection();

    static Statement stmt = null;
    static String allUsersQuery = "SELECT * FROM user";
    public static ObservableList<User> users = FXCollections.observableArrayList();

    public static void getUsers() {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(allUsersQuery);

            while (rs.next()) {
                String userName = rs.getString("userName");
                String password = rs.getString("password");

                User user = new User();
                user.setUserName(userName);
                user.setPassword(password);

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int getUserId(String user){
        int userId = 0;
        try {
            String query = "SELECT * FROM user WHERE userName = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                userId = rs.getInt("userId");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return userId;
    }
}
