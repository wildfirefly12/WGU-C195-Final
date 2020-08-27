package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private Button ByMonthButton;

    @FXML
    private Button ByConsultantButton;

    @FXML
    private Button ByCustomerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ByMonthButton.setOnAction( e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/AppointmentsByMonth.fxml"));
                Parent parent = fxmlLoader.load();
                Stage newWindow = new Stage();
                newWindow.initModality(Modality.APPLICATION_MODAL);
                newWindow.setScene(new Scene(parent));
                newWindow.show();
            } catch (IOException error) {
                error.printStackTrace();
            }
        });

        ByConsultantButton.setOnAction( e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/AppointmentsByConsultant.fxml"));
                Parent parent = fxmlLoader.load();
                Stage newWindow = new Stage();
                newWindow.initModality(Modality.APPLICATION_MODAL);
                newWindow.setScene(new Scene(parent));
                newWindow.show();
            } catch (IOException error) {
                error.printStackTrace();
            }
        });
    }
}
