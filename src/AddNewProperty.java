import Connect.SQLConnecter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddNewProperty implements Initializable {

    private ObservableList<String> propType = FXCollections.observableArrayList(
            "FARM",
            "GARDEN",
            "ORCHARD");

    private ObservableList<String> animals = FXCollections.observableArrayList(createAnimalList());

    private ObservableList<String> farmlist = FXCollections.observableArrayList(createCropList("FARM"));

    private ObservableList<String> orchardlist = FXCollections.observableArrayList(createCropList("ORCHARD"));

    private ObservableList<String> gardenlist = FXCollections.observableArrayList(createCropList("GARDEN"));

    @FXML
    private TextField newPropName;
    @FXML
    private TextField newPropAddress;
    @FXML
    private TextField newPropCity;
    @FXML
    private TextField newPropZip;
    @FXML
    private TextField newPropAcres;

    @FXML
    private ComboBox newPropType;

    @FXML
    private ComboBox newPropCrop;

    @FXML
    private ComboBox newPropPublic;

    @FXML
    private ComboBox newPropAnimal;

    @FXML
    private Label newPropAnimalLabel;

    @FXML
    private ComboBox newPropCommercial;

    @FXML
    private Button newPropAddButton;

    @FXML
    private Button newPropCancel;

    @FXML
    private Label newProp_ErrorMessage;

    public AddNewProperty() throws SQLException {
    }

    public void createMenu() {
        newPropType.setValue("GARDEN");
        newPropAnimalLabel.setVisible(false);
        newPropAnimal.setVisible(false);
        newPropType.getItems().addAll(propType);
        newPropCrop.getItems().addAll(gardenlist);
        newPropPublic.getItems().addAll(true, false);
        newPropAnimal.getItems().addAll(animals);
        newPropCommercial.getItems().addAll(true, false);
    }

    private ArrayList<String> createCropList(String farmtype) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        Connection server = SQLConnecter.connect();
        String selectStatementCrop;
        if (farmtype.equals("GARDEN")) {
            selectStatementCrop = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND (Type = 'FLOWER' OR Type = 'VEGETABLE')";
            System.out.println(selectStatementCrop);
        } else if (farmtype.equals("ORCHARD")) {
            selectStatementCrop = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND (Type = 'FRUIT' OR Type = 'NUT')";
            System.out.println(selectStatementCrop);
        } else {
            selectStatementCrop = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND Type != 'ANIMAL'";
            System.out.println(selectStatementCrop);
        }

        ResultSet val = server.createStatement().executeQuery(selectStatementCrop);

        while (val.next() && !list.contains(val.getString("Name"))) {
            list.add(val.getString("Name"));
        }
        server.close();
        return list;
    }

    private ArrayList<String> createAnimalList() throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        Connection server = SQLConnecter.connect();
        String selectStatementAnimal;
        selectStatementAnimal = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND Type = 'ANIMAL'";
        System.out.println(selectStatementAnimal);

        ResultSet val = server.createStatement().executeQuery(selectStatementAnimal);

        while (val.next() && !list.contains(val.getString("Name"))) {
            list.add(val.getString("Name"));
        }
        server.close();
        return list;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { createMenu(); }

    @FXML
    private void selectPropType(ActionEvent actionEvent) {
        if (newPropType.getValue().toString().equals("FARM")) {
            newPropAnimalLabel.setVisible(true);
            newPropAnimal.setVisible(true);
            newPropCrop.getItems().clear();
            newPropCrop.getItems().addAll(farmlist);
        } else if (newPropType.getValue().toString().equals("ORCHARD")){
            newPropAnimalLabel.setVisible(false);
            newPropAnimal.setVisible(false);
            newPropCrop.getItems().clear();
            newPropCrop.getItems().addAll(orchardlist);
        } else {
            newPropAnimalLabel.setVisible(false);
            newPropAnimal.setVisible(false);
            newPropCrop.getItems().clear();
            newPropCrop.getItems().addAll(gardenlist);
        }
    }

    private void sceneChanger(Button button, String fxmlName) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) button.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(fxmlName));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void pressNewPropCancel(ActionEvent actionEvent) throws IOException {
        sceneChanger(newPropCancel, "welcome_owner.fxml");
    }

    public void pressRegisterOwner(ActionEvent actionEvent) throws IOException {
        newProp_ErrorMessage.setText("");
        Boolean registered = false;
        Boolean passed = false;

        if (newPropName.getText().length() == 0) {
            newProp_ErrorMessage.setText(newProp_ErrorMessage.getText()
                    + "\nYou must supply a Property Name.");
            passed = true;
        }
        if (newPropCity.getText().length() == 0) {
            newProp_ErrorMessage.setText(newProp_ErrorMessage.getText()
                    + "\nYou must supply a City.");
            passed = true;
        }
        if (newPropAddress.getText().length() == 0) {
            newProp_ErrorMessage.setText(newProp_ErrorMessage.getText()
                    + "\nYou must supply an Address.");
            passed = true;
        }

        try {
            Integer.parseInt(newPropZip.getText());
        }catch (NumberFormatException e) {
            newProp_ErrorMessage.setText(newProp_ErrorMessage.getText()
                    + "\nZipcode only contains numbers.");
            passed = true;
        }
        if (newPropZip.getText().length() == 0) {
            newProp_ErrorMessage.setText(newProp_ErrorMessage.getText()
                    + "\nYou must supply a ZipCode.");
            passed = true;
        } else if (newPropZip.getText().length() != 5) {
            newProp_ErrorMessage.setText(newProp_ErrorMessage.getText()
                    + "\nZipCode must be 5 digits.");
            passed = true;
        }
        try {
            Integer.parseInt(newPropAcres.getText());
        }catch (NumberFormatException e) {
            newProp_ErrorMessage.setText(newProp_ErrorMessage.getText()
                    + "\nAcres only contains numbers.");
            passed = true;
        }
        if (newPropAcres.getText().length() == 0) {
            newProp_ErrorMessage.setText(newProp_ErrorMessage.getText()
                    + "\nYou must supply Acres.");
            passed = true;
        }

        if (!passed) {
            try {
                Connection server = SQLConnecter.connect();
                if (!server.isClosed()) {
                    newProp_ErrorMessage.setText("Server is Closed or not Connected to it.");
                }

                String selectStatement = "SELECT Name FROM PROPERTY WHERE Name = '"
                        + newPropName.getText() + "'";
                System.out.println(selectStatement);

                ResultSet val = server.createStatement().executeQuery(selectStatement);

                if (val.next()) {
                    boolean notUnique = false;
                    if (val.getString(1).equals(newPropName.getText())) {
                        newProp_ErrorMessage.setText("Property name must be unique.");
                        notUnique = true;
                    }
                    if (notUnique) {
                        return;
                    }
                }
                int propID = 0;
                val = server.createStatement().executeQuery("SELECT MAX(ID) FROM PROPERTY");
                if (val.next()) {
                    propID = val.getInt(1) + 1;
                }
                String insert;
                if (newPropType.getValue() != null
                        || newPropCrop.getValue() != null
                        || newPropPublic.getValue() != null
                        || newPropAnimal.getValue() != null
                        || newPropCommercial.getValue() != null) {
                    insert = "INSERT INTO PROPERTY (ID, Name, Acres, IsCommercial, IsPublic, Address, City, "
                            + "Zip, P_type, Owner, ApprovedBy) "
                            + "VALUES(" + propID + ", '"
                            + newPropName.getText() + "', "
                            + newPropAcres.getText() + ", "
                            + newPropCommercial.getValue().toString().toUpperCase() + ", "
                            + newPropPublic.getValue().toString().toUpperCase() + ", '"
                            + newPropAddress.getText() + "', '"
                            + newPropCity.getText() + "', "
                            + newPropZip.getText() + ", '"
                            + newPropType.getValue().toString() + "', '"
                            + User.getInstance().getUsername() + "', "
                            + "null)";
                    System.out.println(insert);
                    String insert2, insert3, insert4;
                    if (newPropType.getValue().toString().equals("FARM")) {
                        insert2 = "INSERT INTO HAS (P_id, Item) VALUES("
                                + propID + ", '" + newPropAnimal.getValue().toString()
                                + "')";
                        insert3 = "INSERT INTO HAS (P_id, Item) VALUES("
                                + propID + ", '" + newPropCrop.getValue().toString()
                                + "')";
                        System.out.println(insert2);
                        System.out.println(insert3);
                        server.createStatement().execute(insert);
                        System.out.println("Insert User worked");
                        server.createStatement().execute(insert2);
                        System.out.println("Insert Animal worked");
                        server.createStatement().execute(insert3);
                        System.out.println("Insert Crop worked");
                    } else {
                        insert4 = "INSERT INTO HAS (P_id, Item) VALUES("
                                + propID + ", '" + newPropCrop.getValue().toString()
                                + "')";
                        System.out.println(insert4);
                        server.createStatement().execute(insert);
                        System.out.println("Insert User worked");
                        server.createStatement().execute(insert4);
                        System.out.println("Insert Crop worked");
                    }
                    server.close();
                    registered = true;
                } else {
                    newProp_ErrorMessage.setText(newProp_ErrorMessage.getText()
                            + "\nPlease complete all the fields.");
                }

            } catch (Exception e) {
                System.out.println("A SQL Statement could not be executed.");
                return;
            }
        }
        if (registered) {
            System.out.println("Visitor Registration Successful");
            sceneChanger(newPropAddButton, "welcome_owner.fxml");
        }
    }
}
