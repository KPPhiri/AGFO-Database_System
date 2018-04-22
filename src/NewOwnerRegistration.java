import Connect.SQLConnecter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class NewOwnerRegistration implements Initializable {
    @FXML
    private TextField ownerRegistrationEmail;
    @FXML
    private TextField ownerRegistrationUser;
    @FXML
    private PasswordField ownerRegistrationPassword;
    @FXML
    private PasswordField ownerRegistrationConfirmPassword;
    @FXML
    private TextField ownerRegistrationPropName;
    @FXML
    private TextField ownerRegistrationAddress;
    @FXML
    private TextField ownerRegistrationCity;
    @FXML
    private TextField ownerRegistrationZip;
    @FXML
    private TextField ownerRegistrationAcres;

    private ObservableList<String> propType = FXCollections.observableArrayList(
            "FARM",
            "GARDEN",
            "ORCHARD");

    @FXML
    private ComboBox ownerRegistrationPropType;

    private ObservableList<String> farmlist = FXCollections.observableArrayList(createCropList("FARM"));

    private ObservableList<String> orchardlist = FXCollections.observableArrayList(createCropList("ORCHARD"));

    private ObservableList<String> gardenlist = FXCollections.observableArrayList(createCropList("GARDEN"));

    private ObservableList<String> animals = FXCollections.observableArrayList(createAnimalList());

    @FXML
    private ComboBox ownerRegistrationCrop;

    @FXML
    private ComboBox ownerRegistrationPublic;

    @FXML
    private ComboBox ownerRegistrationAnimal;

    @FXML
    private ComboBox ownerRegistrationCommercial;
    @FXML
    private Button ownerRegistrationRegisterButton;
    @FXML
    private Button ownerRegistrationCancelButton;
    @FXML
    private Label owner_ErrorMessage;
    @FXML
    private Label ownerRegistrationAnimalLabel;

    public NewOwnerRegistration() throws SQLException {
    }

    public void createMenu() {
        ownerRegistrationPropType.setValue("GARDEN");
        ownerRegistrationAnimalLabel.setVisible(false);
        ownerRegistrationAnimal.setVisible(false);
        ownerRegistrationPropType.getItems().addAll(propType);
        ownerRegistrationCrop.getItems().addAll(gardenlist);
        ownerRegistrationPublic.getItems().addAll(true, false);
        ownerRegistrationAnimal.getItems().addAll(animals);
        ownerRegistrationCommercial.getItems().addAll(true, false);
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

    @FXML
    private void selectPropertyType(ActionEvent actionEvent) {
        if (ownerRegistrationPropType.getValue().toString().equals("FARM")) {
            ownerRegistrationAnimalLabel.setVisible(true);
            ownerRegistrationAnimal.setVisible(true);
            ownerRegistrationCrop.getItems().clear();
            ownerRegistrationCrop.getItems().addAll(farmlist);
        } else if (ownerRegistrationPropType.getValue().toString().equals("ORCHARD")){
            ownerRegistrationAnimalLabel.setVisible(false);
            ownerRegistrationAnimal.setVisible(false);
            ownerRegistrationCrop.getItems().clear();
            ownerRegistrationCrop.getItems().addAll(orchardlist);
        } else {
            ownerRegistrationAnimalLabel.setVisible(false);
            ownerRegistrationAnimal.setVisible(false);
            ownerRegistrationCrop.getItems().clear();
            ownerRegistrationCrop.getItems().addAll(gardenlist);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createMenu();
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

    public void pressCancelButtonOwner(ActionEvent actionEvent) throws IOException {
        sceneChanger(ownerRegistrationCancelButton, "page_login.fxml");
    }

    public void pressRegisterOwner(ActionEvent actionEvent) throws IOException {
        owner_ErrorMessage.setText("");
        boolean registered = false;
        boolean passed = false;
        boolean matches = Pattern.matches("^[A-Za-z0-9]+@[A-Za-z0-9]+(\\.[A-Za-z]{3,})$",
                ownerRegistrationEmail.getText());

        if (ownerRegistrationEmail.getText().length() == 0) {
            owner_ErrorMessage.setText("You must supply an Email.");
            passed = true;
        } else if (!matches) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nEmail is not valid.");
            passed = true;
        }

        if (ownerRegistrationUser.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must supply an Username.");
            passed = true;
        }

        if (ownerRegistrationPassword.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must supply a Password.");
            passed = true;
        } else if (ownerRegistrationConfirmPassword.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must confirm the password.");
            passed = true;
        } else if (!(ownerRegistrationPassword.getText().length() > 7)) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nPassword must be 8 characters or longer.");
            passed = true;
        } else if (!(ownerRegistrationPassword.getText()
                .equals(ownerRegistrationConfirmPassword.getText()))) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nPasswords do not match.");
            passed = true;
        }

        if (ownerRegistrationCity.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must supply a City.");
            passed = true;
        }
        if (ownerRegistrationAddress.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must supply an Address.");
            passed = true;
        }
        if (ownerRegistrationPropName.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must supply a Property Name.");
            passed = true;
        }
        try {
            Integer.parseInt(ownerRegistrationZip.getText());
        }catch (NumberFormatException e) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nZipcode only contains numbers.");
            passed = true;
        }
        if (ownerRegistrationZip.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must supply a ZipCode.");
            passed = true;
        } else if (ownerRegistrationZip.getText().length() != 5) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nZipCode must be 5 digits.");
            passed = true;
        }
        try {
            Integer.parseInt(ownerRegistrationAcres.getText());
        }catch (NumberFormatException e) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nAcres only contains numbers.");
            passed = true;
        }
        if (ownerRegistrationAcres.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must supply Acres.");
            passed = true;
        }

        if (!passed) {
            try {
                Connection server = SQLConnecter.connect();
                if (!server.isClosed()) {
                    owner_ErrorMessage.setText("Server is Closed or not Connected to it.");
                }

                String selectStatement = "SELECT Email, Username FROM USER WHERE Email = '"
                        + ownerRegistrationEmail.getText() + "' AND Username = '"
                        + ownerRegistrationUser.getText() + "'";
                System.out.println(selectStatement);

                ResultSet val = server.createStatement().executeQuery(selectStatement);

                if (val.next()) {
                    boolean notUnique = false;
                    if (val.getString(1).equals(ownerRegistrationEmail.getText())) {
                        owner_ErrorMessage.setText("Email must be unique.");
                        notUnique = true;
                    }
                    if (val.getString(2).equals(ownerRegistrationUser.getText())) {
                        owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                                + "\n User must be unique.");
                        notUnique = true;
                    }
                    if (notUnique) {
                        return;
                    }
                }

                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] bytesOfPass = ownerRegistrationPassword.getText().getBytes("UTF-8");
                byte[] digest = md.digest(bytesOfPass);
                String pass;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < digest.length; i++) {
                    stringBuffer.append(Integer.toString((digest[i] & 0xff) + 0x100, 16)
                            .substring(1));
                }
                pass = stringBuffer.toString();
                String email = ownerRegistrationEmail.getText();
                String username = ownerRegistrationUser.getText();
                String insert = "INSERT INTO USER (Password, Email, Username, U_type) "
                        + "VALUES('" + pass + "', '" + email + "', '" + username + "', 'OWNER')";
                System.out.println(insert);
                server.createStatement().execute(insert);
                val = server.createStatement().executeQuery("SELECT MAX(ID) FROM PROPERTY");
                int propID = 0;
                if (val.next()) {
                    propID = val.getInt(1) + 1;
                }
                if (ownerRegistrationPropType.getValue() != null
                        || ownerRegistrationCrop.getValue() != null
                        || ownerRegistrationPublic.getValue() != null
                        || ownerRegistrationAnimal.getValue() != null
                        || ownerRegistrationCommercial.getValue() != null) {
                    insert = "INSERT INTO PROPERTY (ID, Name, Acres, IsCommercial, IsPublic, Address, City, "
                            + "Zip, P_type, Owner, ApprovedBy) "
                            + "VALUES(" + propID + ", '"
                            + ownerRegistrationPropName.getText() + "', "
                            + ownerRegistrationAcres.getText() + ", "
                            + ownerRegistrationCommercial.getValue().toString().toUpperCase() + ", "
                            + ownerRegistrationPublic.getValue().toString().toUpperCase() + ", '"
                            + ownerRegistrationAddress.getText() + "', '"
                            + ownerRegistrationCity.getText() + "', "
                            + ownerRegistrationZip.getText() + ", '"
                            + ownerRegistrationPropType.getValue().toString() + "', '"
                            + ownerRegistrationUser.getText() + "', "
                            + "null)";
                    System.out.println(insert);
                    String insert2, insert3, insert4;
                    if (ownerRegistrationPropType.getValue().toString() == "FARM") {
                        insert2 = "INSERT INTO HAS (P_id, Item) VALUES("
                                + propID + ", '" + ownerRegistrationAnimal.getValue().toString()
                                + "')";
                        insert3 = "INSERT INTO HAS (P_id, Item) VALUES("
                                + propID + ", '" + ownerRegistrationCrop.getValue().toString()
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
                                + propID + ", '" + ownerRegistrationCrop.getValue().toString()
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
                    owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                            + "\nPlease complete all the fields.");
                }

            } catch (Exception e) {
                System.out.println("A SQL Statement could not be executed.");
                return;
            }
        }


        if (registered) {
            System.out.println("Visitor Registration Successful");
            sceneChanger(ownerRegistrationRegisterButton, "page_login.fxml");
        }
    }
}
