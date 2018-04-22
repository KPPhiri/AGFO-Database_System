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

import java.util.ResourceBundle;



public class VisitDetails implements Initializable{

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

    private int pid;

    private String emailstring;

    private String ownerstring;

    private String cropstring;

    private String animalstring = "";

    private ObservableList<userPropDetails> data;

    User user = User.getInstance();
    String userst = user.getUsername();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDataFromDatabase();
        back.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        back();
                                    }

        });

        logVisit_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                unlogVisit();
            }

        });
    }

    @FXML
    private void loadDataFromDatabase() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            data = FXCollections.observableArrayList();

            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Address, City, Zip, Acres, P_type, IsPublic, IsCommercial , ID, ApprovedBy FROM PROPERTY WHERE ID = '" + VisitorHistory.pid + "'");

            ResultSet rb = server.createStatement().executeQuery("SELECT Owner FROM PROPERTY WHERE ID = '" + VisitorHistory.pid + "'");
            while(rb.next()) {
                ownerstring = rb.getString(1);
            }

            ResultSet rc = server.createStatement().executeQuery("SELECT Email FROM USER WHERE Username = '" + ownerstring + "'");
            while(rc.next()) {
                emailstring = rc.getString(1);
            }
            //System.out.println("c" + ownerstring + "c");

            String crops = "";
            ResultSet rd = server.createStatement().executeQuery("SELECT P_id, Item FROM HAS, FARM_ITEM WHERE P_id = '" + VisitorHistory.pid + "' AND FARM_ITEM.Type != 'ANIMAL'");
            while (rd.next() && !crops.contains(rd.getString("Item"))) {
                crops += rd.getString("Item") + ", ";
            }
            //crop.setText("Crop: " + crops);
            cropstring = crops;

            String isFarm = "";
            ResultSet rx = server.createStatement().executeQuery("SELECT P_type FROM PROPERTY WHERE ID = '" + VisitorHistory.pid + "'");
            while (rx.next()) {
                isFarm += rx.getString(1);
            }
            System.out.println(isFarm);
            if (isFarm.equals("FARM")) {
                String animals = "";
                ResultSet rf = server.createStatement().executeQuery("SELECT P_id, Item FROM HAS, FARM_ITEM WHERE P_id = '" + VisitorHistory.pid + "' AND FARM_ITEM.Type != 'ANIMAL'");
                while (rf.next() && !animals.contains(rf.getString("Item"))) {
                    animals += rf.getString("Item") + ", ";
                }
                animalstring = animals;
                //animal.setText("Animals: " + animals);
            }





            while (rs.next()) {
                int id = rs.getInt(9);
                ResultSet ra = server.createStatement().executeQuery("SELECT COUNT(P_id) FROM VISITS WHERE P_id = " + VisitorHistory.pid);
                pid = 0;
                if(ra.next()) {
                    pid = ra.getInt(1);
                }

                ResultSet re = server.createStatement().executeQuery("SELECT avg(Rating) FROM VISITS WHERE P_id = " + VisitorHistory.pid);
                double avgRating = 0.0;
                if(re.next()) {
                    avgRating = Math.round((re.getDouble(1)) * 10.0) / 10.0;
                }

                boolean isValid = rs.getBoolean(10);
                data.add(new userPropDetails(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6),rs.getBoolean(7), rs.getBoolean(8),rs.getInt(9),isValid, pid,  avgRating));
            }


        } catch(Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
            e.printStackTrace();

        }
        //Set cell value factory to tableview.
        //NB.PropertyValue Factory must be the same with the one set in model class.
        name.setText("Name: " + (data.get(0)).getPropName().toString());
        owner.setText("Owner: " + ownerstring);
        email.setText("Owner Email:" + emailstring);
        crop.setText("Crop:" + cropstring);
        //animal.setText("Animals" + );

        address.setText("Address: " + (data.get(0)).getAddress().toString());
        city.setText("City: " + (data.get(0)).getCity());
        zip.setText("Zip: " + (data.get(0)).getZip());
        size.setText("Size: " + (data.get(0)).getSize().toString());
        type.setText("Type: " + (data.get(0)).getType());

        visit.setText("Visits " + Integer.toString(pid));
        if (data.get(0).getIpublic()) {
            isPublic.setText("Public: " + "true");
        } else {
            isPublic.setText("Public: " + "false");
        }
        if (data.get(0).getCommercial()) {
            isCommercial.setText("Public: " + "true");
        } else {
            isCommercial.setText("Public: " + "false");
        }
        id.setText("ID: " + Integer.toString((data.get(0)).getId()));
        crop.setText("Crop: " + cropstring);
        animal.setText("Animals: " + animalstring);
        rate.setText("Avg. Rating: " + Double.toString(data.get(0).getRating()));


        //table.setItems(null);
        System.out.println("Should be adding");
        System.out.println(data.size());
        //table.setItems(data);

    }





















//    @Override
//
//    public void initialize (URL location, ResourceBundle resources) {
//
//        loadPropertyDetails();
//
//        getOwnerInfo();
//
//        getCropAndAnimal();
//
//        back.setOnAction(e -> back());
//
//    }
//
//
//
//    private void loadPropertyDetails() {
//
//
//
//
//
//        userPropDetails temp = OtherOwnerProperties.getSelectedUser();
//
//        title.setText(temp.getPropName() + " Details");
//
//        name.setText("Name: " + temp.getPropName());
//
//        address.setText("Address: " + temp.getAddress());
//
//        city.setText("City: " + temp.getCity());
//
//        zip.setText("Zip: " + temp.getZip());
//
//        size.setText("Size: " + temp.getSize());
//
//        visit.setText("Visits : " + temp.getVisits());
//
//        rate.setText("Avg. Rating: " + temp.getRating());
//
//        type.setText("Type: " + temp.getType());
//
//        isPublic.setText("Public: " + temp.getIpublic());
//
//        isCommercial.setText("Commercial: " + temp.getCommercial());
//
//        id.setText("ID: " + temp.getId());
//
//    }
//
//
//
//    public void getOwnerInfo() {
//
//        try {
//
//            System.out.println("WORKING");
//
//            Connection server = Connect.SQLConnecter.connect();
//
//
//
//            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Owner, Email FROM USER, PROPERTY WHERE USER.Username = PROPERTY.Owner AND Name = '" + OtherOwnerProperties.getSelectedUser().getPropName() + "'");
//
//            if (rs.next()) {
//
//                owner.setText("Owner: " + rs.getString("Owner"));
//
//                email.setText("Owner Email: " + rs.getString("Email"));
//
//            }
//
//        } catch (Exception e) {
//
//            System.out.println("something went wrong + " + e.getMessage());
//
//
//
//        }
//
//    }
//
//
//
//    public void getCropAndAnimal() {
//
//        try {
//
//            System.out.println("WORKING");
//
//            Connection server = Connect.SQLConnecter.connect();
//
//
//
//            String crops = "";
//
//            ResultSet rs = server.createStatement().executeQuery("SELECT P_id, Item FROM HAS, FARM_ITEM WHERE P_id = '" + OtherOwnerProperties.getSelectedUser().getId() + "' AND FARM_ITEM.Type != 'ANIMAL'");
//
//            while (rs.next() && !crops.contains(rs.getString("Item"))) {
//
//                crops += rs.getString("Item") + ", ";
//
//            }
//
//            crop.setText("Crop: " + crops);
//
//
//
//        } catch (Exception e) {
//
//            System.out.println("something went wrong + " + e.getMessage());
//
//        }
//
//
//
//        if ("FARM".equalsIgnoreCase(OtherOwnerProperties.getSelectedUser().getType())) {
//
//            try {
//
//                System.out.println("WORKING");
//
//                Connection server = Connect.SQLConnecter.connect();
//
//
//
//                String animals = "";
//
//                ResultSet rs = server.createStatement().executeQuery("SELECT P_id, Item FROM HAS, FARM_ITEM WHERE P_id = '" + OtherOwnerProperties.getSelectedUser().getId() + "' AND FARM_ITEM.Type = 'ANIMAL'");
//
//                while (rs.next() && !animals.contains(rs.getString("Item"))) {
//
//                    animals += rs.getString("Item") + ", ";
//
//                }
//
//                animal.setText("Animals: " + animals);
//
//            } catch (Exception e) {
//
//                System.out.println("something went wrong + " + e.getMessage());
//
//            }
//
//        }
//
//    }



    private void back() {



        Stage stage;

        Parent root = null;

        stage = (Stage) back.getScene().getWindow();

        try {

            root = FXMLLoader.load(getClass().getResource("visitor_history.fxml"));

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

            server.createStatement().executeUpdate("DELETE FROM VISITS WHERE Username = '" + userst + "' AND P_id = '" + VisitorHistory.pid + "'");
            loadDataFromDatabase();
            System.out.println(userst + "" + VisitorHistory.pid);


            //server.createStatement().execute(insert);
            Stage stage;
            Parent root = null;
            stage = (Stage) logVisit_button.getScene().getWindow();
            try {
                root = FXMLLoader.load(getClass().getResource("visitor_history.fxml"));
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





// SELECT PROPERTY.name, PROPERTY.owner, USER.email, count()