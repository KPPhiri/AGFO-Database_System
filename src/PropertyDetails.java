import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class PropertyDetails implements Initializable{
    @FXML
    public Label title;
    @FXML
    public Label name;
    @FXML
    public Label owner;
    @FXML
    public Label email;
    @FXML
    public Label visit;
    @FXML
    public Label address;
    @FXML
    public Label city;
    @FXML
    public Label zip;
    @FXML
    public Label size;
    @FXML
    public Label rate;
    @FXML
    public Label type;
    @FXML
    public Label isPublic;
    @FXML
    public Label isCommercial;
    @FXML
    public Label id;
    @FXML
    public Label crop;
    @FXML
    public Label animal;
    @FXML
    public Button back;


    @Override
    public void initialize (URL location, ResourceBundle resources) {
        loadPropertyDetails();
        back.setOnAction(e -> back());
    }

    private void loadPropertyDetails() {
        userPropDetails temp = OtherOwnerProperties.getSelectedUser();
        title.setText(temp.getPropName() + " Details");
        name.setText("Name: " + temp.getPropName());
        address.setText("Address: " + temp.getAddress());
        city.setText("City: " + temp.getCity());
        zip.setText("Zip: " + temp.getZip());
        size.setText("Size: " + temp.getSize());
        type.setText("Type: " + temp.getType());
        isPublic.setText("Public: " + temp.getIpublic());
        isCommercial.setText("Commercial: " + temp.getCommercial());
        id.setText("ID: " + temp.getId());
    }

    private void back() {

        Stage stage;
        Parent root = null;
        stage = (Stage) back.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("other_owner_properties.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
