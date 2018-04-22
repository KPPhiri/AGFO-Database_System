import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PropertyFarmManagement implements Initializable {
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
    public ComboBox items;
    @FXML
    public ComboBox newCrop;
    @FXML
    public ComboBox requestCrop;
    @FXML
    public Label id;

    public userPropDetails current = OwnerWelcome.getSelectedOwnerProp();
    private ArrayList<String> farmItems = new ArrayList<>();
    private ArrayList<String> approvedItems = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createSpinners();
        loadDataFields();
        if ("GARDEN".equals(current.getType())) {
            createGardenSpinner();
            requestCrop.setValue("Flower");
        } else if ("ORCHARD".equals(current.getType())) {
            createOrchardSpinner();
            requestCrop.setValue("Fruit");
        }
        getCropAndAnimal();
        items.setValue(farmItems.get(0));

        loadApprovedCrops();
        newCrop.setValue(approvedItems.get(0));

        back.setOnAction(e -> backToWelcomePage());

    }

    public void loadDataFields() {
        name.setText(current.getPropName());
        address.setText(current.getAddress());
        city.setText(current.getCity());
        zip.setText(current.getZip());
        size.setText(current.getSize());
        type.setText("Type: " + current.getType());
        isPublic.setValue(current.getIpublic());
        isCommercial.setValue(current.getCommercial());
        id.setText("ID: " + current.getId());
    }

    private void createSpinners() {
        isPublic.getItems().addAll(true, false);
        isCommercial.getItems().addAll(true, false);
    }

    private void createGardenSpinner() {
        requestCrop.getItems().addAll("Flower", "Vegetable");
    }

    private void createOrchardSpinner() {
        requestCrop.getItems().addAll("Fruit", "Nut");
    }

    private void getCropAndAnimal() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();

            ResultSet rs = server.createStatement().executeQuery("SELECT P_id, Item FROM HAS, FARM_ITEM WHERE P_id = '" + OwnerWelcome.getSelectedOwnerProp().getId() + "' AND FARM_ITEM.Type != 'ANIMAL'");
            while (rs.next() && !farmItems.contains(rs.getString("Item"))) {
                farmItems.add(rs.getString("Item"));
            }
            items.setItems(FXCollections.observableArrayList(farmItems));
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }

//        if ("FARM".equalsIgnoreCase(OtherOwnerProperties.getSelectedUser().getType())) {
//            try {
//                System.out.println("WORKING");
//                Connection server = Connect.SQLConnecter.connect();
//
//                String animals = "";
//                ResultSet rs = server.createStatement().executeQuery("SELECT P_id, Item FROM HAS, FARM_ITEM WHERE P_id = '" + OtherOwnerProperties.getSelectedUser().getId() + "' AND FARM_ITEM.Type = 'ANIMAL'");
//                while (rs.next() && !animals.contains(rs.getString("Item"))) {
//                    animals += rs.getString("Item") + ", ";
//                }
//                animal.setText("Animals: " + animals);
//            } catch (Exception e) {
//                System.out.println("something went wrong + " + e.getMessage());
//            }
//        }
    }

    private void loadApprovedCrops() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            String query = "";
            if ("GARDEN".equals(OwnerWelcome.getSelectedOwnerProp().getType())) {
                query = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND (Type = 'FLOWER' OR Type = 'VEGETABLE')";
            }
            if ("ORCHARD".equals(OwnerWelcome.getSelectedOwnerProp().getType())) {
                query = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND (Type = 'FRUIT' OR Type = 'NUT')";
            }
            ResultSet rs = server.createStatement().executeQuery(query);
            while (rs.next() && !approvedItems.contains(rs.getString("Name"))) {
                approvedItems.add(rs.getString("Name"));
            }
            newCrop.setItems(FXCollections.observableArrayList(approvedItems));
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
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
