import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PropertyManagement implements Initializable {
    @FXML
    public Button save;
    @FXML
    public Button back;
    @FXML
    public Button delete;
    @FXML
    public TextField name;
    @FXML
    public TextField address;
    @FXML
    public TextField city;
    @FXML
    public TextField zip;
    @FXML
    public TextField size;
    @FXML
    public Label type;
    @FXML
    public ComboBox isPublic;
    @FXML
    public ComboBox isCommercial;
    @FXML
    public Label id;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setOnAction(e -> backToWelcomePage());

    }

    public void loadDataFields() {
        name.setText("");
        address.setText("");
        city.setText("");
        zip.setText("");
        size.setText("");
        type.setText("Type: ");
        isPublic.setValue("");
        isCommercial.setValue("");
        id.setText("");
    }


    private void backToWelcomePage() {
        Stage stage;
        Parent root = null;
        stage = (Stage) back.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("welcome_owner.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
