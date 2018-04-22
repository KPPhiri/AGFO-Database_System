import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminWelcome implements Initializable {
    @FXML
    private Button visitorsbut;
    @FXML
    private Button ownersbut;
    @FXML
    private Button confirmedbut;
    @FXML
    private Button unconfirmedbut;
    @FXML
    private Button approvedbut;
    @FXML
    private Button pendingbut;
    @FXML
    private Button logoutbut;
    @FXML
    private Label welcomelabel;

    User currentUser = User.getInstance();

    public void initialize(URL location, ResourceBundle resources) {
        welcomelabel.setText("Welcome " + currentUser.getUsername());
        //establish();
    }
    public void goToConfirmed(){
        Stage stage;
        Parent root = null;
        stage = (Stage) confirmedbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("confirmed_properties.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToUnconfirmed(){
        Stage stage;
        Parent root = null;
        stage = (Stage) unconfirmedbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("unconfirmed_properties.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void gotoOwnerlist(){
        Stage stage;
        Parent root = null;
        stage = (Stage) unconfirmedbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("owner_overview.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void gotoVisitorList(){
        Stage stage;
        Parent root = null;
        stage = (Stage) unconfirmedbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("visitor_overview.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void gotoApprovedCrops(){
        Stage stage;
        Parent root = null;
        stage = (Stage) unconfirmedbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("approved_crops.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void gotoPendingCrops(){
        Stage stage;
        Parent root = null;
        stage = (Stage) unconfirmedbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("pending_crops.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void logOut(){
        Stage stage;
        Parent root = null;
        stage = (Stage) unconfirmedbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("page_login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
