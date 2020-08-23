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

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

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

           //if passwords match, open main window
           if(verified){
               System.out.println("Login successful");
               User.setLoggedUser(userName);
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
