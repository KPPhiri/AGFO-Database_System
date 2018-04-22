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
    public TextField cropName;
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
    @FXML
    public Button addApproved;
    @FXML
    public Button submit;
    @FXML
    public ComboBox newAnimal;
    @FXML
    public Button addApprovedAnimal;
    @FXML
    public Button removeFarmItem;

    public userPropDetails current = OwnerWelcome.getSelectedOwnerProp();
    private ArrayList<String> farmItems = new ArrayList<>();
    private ArrayList<String> approvedItems = new ArrayList<>();
    private ArrayList<String> approvedAnimals = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createSpinners();
        loadDataFields();



        getCropAndAnimal();
        if (farmItems.size() > 0) {
            items.setValue(farmItems.get(0));
        }


        createFarmSpinner();
        requestCrop.setValue("Animal");


        loadApprovedCrops();
        if (approvedAnimals.size() > 0) {
            newAnimal.setValue(approvedAnimals.get(0));
        }
        if (approvedItems.size() > 0 ) {
            newCrop.setValue(approvedItems.get(0));
        }


        cropName.setText("");
        submit.setOnAction(e -> requestItem());
        addApproved.setOnAction(e -> addApprovedItem());
        addApprovedAnimal.setOnAction(e -> addApprovedAnimal());
        back.setOnAction(e -> backToWelcomePage());
        delete.setOnAction(e -> deleteProperty());
        save.setOnAction(e -> saveProperty());
        removeFarmItem.setOnAction(e -> deleteFarmItem());
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

    private void createFarmSpinner() {
        requestCrop.getItems().addAll("Animal", "Flower", "Vegetable", "Fruit", "Nut");
    }


    private void getCropAndAnimal() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();

            ResultSet rs = server.createStatement().executeQuery("SELECT P_id, Item FROM HAS, FARM_ITEM WHERE P_id = '" + OwnerWelcome.getSelectedOwnerProp().getId() + "'");
            while (rs.next() && !farmItems.contains(rs.getString("Item"))) {
                farmItems.add(rs.getString("Item"));
            }
            items.setItems(FXCollections.observableArrayList(farmItems));
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }

    private void loadApprovedCrops() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            ResultSet rs = server.createStatement().executeQuery("SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND Type != 'ANIMAL'");
            while (rs.next() && !approvedItems.contains(rs.getString("Name"))) {
                approvedItems.add(rs.getString("Name"));
            }
            newCrop.setItems(FXCollections.observableArrayList(approvedItems));
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }

        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            ResultSet rs = server.createStatement().executeQuery("SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND Type = 'ANIMAL'");
            while (rs.next() && !approvedAnimals.contains(rs.getString("Name"))) {
                approvedAnimals.add(rs.getString("Name"));
            }
            newAnimal.setItems(FXCollections.observableArrayList(approvedAnimals));
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }

    private void addApprovedItem() {
        if (farmItems.contains((newCrop.getValue()))) {
            return;
        }
        farmItems.add((String)newCrop.getValue());
        items.getItems().add((String) newCrop.getValue());
    }

    private void addApprovedAnimal() {
        if (farmItems.contains((newAnimal.getValue()))) {
            return;
        }
        farmItems.add((String)newAnimal.getValue());
        items.getItems().add((String) newAnimal.getValue());
    }

    private void deleteFarmItem() {
        farmItems.remove(items.getValue());
        items.getItems().remove(items.getValue());
    }

    private void requestItem() {
        if (cropName.getText().equals("") || cropName.getText() == null) {
            return;
        }
        String cropType = (String) requestCrop.getValue();
        cropName.getText();

        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            server.createStatement().executeUpdate("INSERT INTO FARM_ITEM (Name, isApproved, Type) VALUES('" + cropName.getText() + "', '0', '" + cropType.toUpperCase() + "')");
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }

    private void deleteProperty() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            server.createStatement().executeUpdate("DELETE FROM PROPERTY WHERE ID= '"+ current.getId() +"'");
            backToWelcomePage();
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }

    private void saveProperty() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            server.createStatement().executeUpdate("DELETE FROM HAS WHERE P_id= '"+ current.getId() +"'");

            for (String s: farmItems) {
                server.createStatement().executeUpdate("INSERT INTO HAS (P_id, Item) VALUES(" + current.getId() + ", '" + s + "')");
            }

            server.createStatement().executeUpdate("UPDATE PROPERTY SET Name = '"+ name.getText() +"', Address = '" + address.getText() + "', City = '" + city.getText() + "', Zip = '" + zip.getText() + "',  Acres = '" + size.getText() + "', IsPublic = " + isPublic.getValue() + ", IsCommercial = " + isCommercial.getValue() + ", ApprovedBy = null WHERE ID = "+ current.getId() + "");
            backToWelcomePage();
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
