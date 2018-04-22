import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class VisitPropPage2 implements Initializable{
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

    public static int propide = 0;


    @Override
    public void initialize (URL location, ResourceBundle resources) {
        loadPropertyDetails();
        getOwnerInfo();
        getCropAndAnimal();
        back.setOnAction(e -> back());

        //logVisit_button.setOnAction(new EventHandler<ActionEvent>() {
        logVisit_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    unlogVisit();
                }

            });
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
        propide = (temp.getId());
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
            ResultSet rs = server.createStatement().executeQuery("SELECT Item FROM HAS, FARM_ITEM WHERE Item=Name AND P_id = '" + OtherOwnerProperties.getSelectedUser().getId() + "'AND Type != 'ANIMAL' ");
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
                ResultSet rs = server.createStatement().executeQuery("SELECT Item FROM HAS, FARM_ITEM WHERE Item=Name AND P_id = '" + OtherOwnerProperties.getSelectedUser().getId() + "'AND Type = 'ANIMAL' ");
                while (rs.next() && !animals.contains(rs.getString("Item"))) {
                    animals += rs.getString("Item") + ", ";
                }
                animal.setText("Animals: " + animals);
            } catch (Exception e) {
                System.out.println("something went wrong + " + e.getMessage());
            }
        } else {animal.setText("Animals: N/A");}
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

    public void unlogVisit() {

        try {

            Connection server = Connect.SQLConnecter.connect();

            userPropDetails tempb = OtherOwnerProperties.getSelectedUser();

            User tempa = User.getInstance();

            String uname = tempa.getUsername();

            //int id = tempb.getId();

            server.createStatement().executeUpdate("DELETE FROM VISITS WHERE Username = '" + VisitorWelcome.user.getUsername() + "' AND P_id = '" + OtherOwnerProperties.getSelectedUser().getId()+ "'");
            //loadDataFromDatabase();
            //System.out.println(userst + "" + VisitorHistory.pid);


            //server.createStatement().execute(insert);
            Stage stage;
            Parent root = null;
            stage = (Stage) logVisit_button.getScene().getWindow();
            try {
                root = FXMLLoader.load(getClass().getResource("welcome_visitor.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();





        } catch (Exception e) {

            System.out.println("something went wrong + " + e.getMessage());
            e.printStackTrace();



        }



    }
}
