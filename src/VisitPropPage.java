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

public class VisitPropPage implements Initializable{
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
    @FXML
    public TextField textRating;
    @FXML
    public Button logVisit_button;


    @Override
    public void initialize (URL location, ResourceBundle resources) {
        loadPropertyDetails();
        getOwnerInfo();
        getCropAndAnimal();
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
        visit.setText("Visits : " + temp.getVisits());
        rate.setText("Avg. Rating: " + temp.getRating());
        type.setText("Type: " + temp.getType());
        isPublic.setText("Public: " + temp.getIpublic());
        isCommercial.setText("Commercial: " + temp.getCommercial());
        id.setText("ID: " + temp.getId());
    }

    public void getOwnerInfo() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();

            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Owner, Email FROM USER, PROPERTY WHERE USER.Username = PROPERTY.Owner AND Name = '" + OtherOwnerProperties.getSelectedUser().getPropName() + "'");
            if (rs.next()) {
                owner.setText("Owner: " + rs.getString("Owner"));
                email.setText("Owner Email: " + rs.getString("Email"));
            }
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());

        }
    }

    public void getCropAndAnimal() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();

            String crops = "";
            ResultSet rs = server.createStatement().executeQuery("SELECT P_id, Item FROM HAS, FARM_ITEM WHERE P_id = '" + OtherOwnerProperties.getSelectedUser().getId() + "' AND FARM_ITEM.Type != 'ANIMAL'");
            while (rs.next() && !crops.contains(rs.getString("Item"))) {
                crops += rs.getString("Item") + ", ";
            }
            crop.setText("Crop: " + crops);

        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }

        if ("FARM".equalsIgnoreCase(OtherOwnerProperties.getSelectedUser().getType())) {
            try {
                System.out.println("WORKING");
                Connection server = Connect.SQLConnecter.connect();

                String animals = "";
                ResultSet rs = server.createStatement().executeQuery("SELECT P_id, Item FROM HAS, FARM_ITEM WHERE P_id = '" + OtherOwnerProperties.getSelectedUser().getId() + "' AND FARM_ITEM.Type = 'ANIMAL'");
                while (rs.next() && !animals.contains(rs.getString("Item"))) {
                    animals += rs.getString("Item") + ", ";
                }
                animal.setText("Animals: " + animals);
            } catch (Exception e) {
                System.out.println("something went wrong + " + e.getMessage());
            }
        }
    }

    private void back() {

        Stage stage;
        Parent root = null;
        stage = (Stage) back.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("welcome_visitor.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logVisit(ActionEvent actionEvent) {
        try {
            Connection server = Connect.SQLConnecter.connect();
            userPropDetails tempb = OtherOwnerProperties.getSelectedUser();
            User tempa = User.getInstance();
            String uname = tempa.getUsername();
            int id = tempb.getId();
            String insert= "INSERT INTO VISITS (Username, P_id, Date, Rating) VALUES " + uname + " " + id + " " + id;

            server.createStatement().execute(insert);
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());

        }

    }
}
