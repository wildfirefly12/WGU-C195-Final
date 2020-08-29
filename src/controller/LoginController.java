package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.DBUser.getUsers;
import static util.DBUser.users;

public class LoginController implements Initializable {
    @FXML
    private AnchorPane mainRootPane;

    @FXML
    private TextField UsernameField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private Button LoginButton;

    @FXML
    private Button ExitButton;

    private final static Logger logger = Logger.getLogger("UserLog");

    public static void createLog(){
        try {
            FileHandler logHandler = new FileHandler("userLog.txt", true);
            logger.addHandler(logHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*Exit the application*/
        ExitButton.setOnAction(e -> System.exit(0));

        /*Login*/
       LoginButton.setOnAction(e -> {
           getUsers();
           String userName = UsernameField.getText();
           String password = PasswordField.getText();
           boolean verified = verifyLogin(userName, password);
           Locale currentLocale = Locale.getDefault();
           LocalDateTime now = LocalDateTime.now();

           //if passwords match, open main window
           if(verified){
               System.out.println("Login successful");
               User.setLoggedUser(userName);
               logger.log(Level.INFO,"User " + userName + " login successful: " + now);
               try {
                   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/Main.fxml"));
                   Parent parent = fxmlLoader.load();
                   Stage newWindow = new Stage();
                   newWindow.setTitle("Scheduling App");
                   newWindow.setScene(new Scene(parent));
                   newWindow.show();
                   Stage currentWindow = (Stage) LoginButton.getScene().getWindow();
                   currentWindow.close();
               } catch (IOException error) {
                   error.printStackTrace();
               }
           } else { //if passwords don't match print error message
               Alert noMatch = new  Alert(Alert.AlertType.ERROR);
               if(currentLocale.getLanguage().equals("en")){
                   noMatch.setContentText("The username and password doesn't match.");
               }
               if(currentLocale.getLanguage().equals("es")){
                  noMatch.setContentText("El nombre de usuario y la contraseña no coinciden.");
               }
               noMatch.show();
               logger.log(Level.INFO,"User " + userName + " login unsuccessful: " + now);
           }

        });
    }

    public Boolean verifyLogin(String userName, String password){
        Boolean returnValue = false;

        for(User user : users){
            String selectedUserName = user.getUserName();
            String selectedPassword = user.getPassword();

            if(selectedUserName.equals(userName)) {
                if (selectedPassword.equals(password)) {
                    returnValue = true;
                    break;
                }
            }
        }

        return returnValue;
    }

    public void getMainWindow(ActionEvent actionEvent) throws IOException {

        Parent home_page = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Stage app = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        app.setScene(new Scene(home_page,300, 275));
        app.show();
    }

}
