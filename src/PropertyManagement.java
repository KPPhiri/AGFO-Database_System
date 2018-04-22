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
import java.util.ArrayList;
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
    public ComboBox items;
    @FXML
    public ComboBox newCrop;
    @FXML
    public ComboBox requestCrop;
    @FXML
    public Label id;
    @FXML
    public Label title;
    @FXML
    public TextField cropName;
    @FXML
    public Button addApproved;
    @FXML
    public Button submit;
    @FXML
    public Button deleteItem;

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
        if (farmItems.size() > 0) {
            items.setValue(farmItems.get(0));
        }

        loadApprovedCrops();
        if (approvedItems.size() > 0 ) {
            newCrop.setValue(approvedItems.get(0));
        }

        cropName.setText("");
        back.setOnAction(e -> backToWelcomePage());

        submit.setOnAction(e -> requestItem());
        delete.setOnAction(e -> deleteProperty());
        addApproved.setOnAction(e -> addApprovedItem());
        deleteItem.setOnAction(e -> deleteFarmItem());
        save.setOnAction(e -> saveProperty());
    }

    public void loadDataFields() {
        title.setText("Manage " + current.getPropName());
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
            server.close();
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
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
            server.close();
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
            server.close();
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
            server.close();
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
            server.createStatement().executeUpdate("DELETE FROM VISITS WHERE P_id= '" + current.getId() + "'");
            backToWelcomePage();
            server.close();
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
