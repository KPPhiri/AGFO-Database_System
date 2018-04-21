import Connect.SQLConnecter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
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
    @FXML
    private MenuButton ownerRegistrationPropType = new MenuButton("Type", null,
            new MenuItem("FARM"),
            new MenuItem("GARDEN"),
            new MenuItem("ORCHARD"));
    @FXML
    private MenuButton ownerRegistrationCrop = new MenuButton("Crop", null,
            new MenuItem("Almond"),
            new MenuItem("Apple"),
            new MenuItem("Banana"),
            new MenuItem("Broccoli"),
            new MenuItem("Carrot"),
            new MenuItem("Cashew"),
            new MenuItem("Corn"),
            new MenuItem("Daffodil"),
            new MenuItem("Daisy"),
            new MenuItem("Fig"),
            new MenuItem("Garlic"),
            new MenuItem("Kiwi"),
            new MenuItem("Onion"),
            new MenuItem("Orange"),
            new MenuItem("Peach"),
            new MenuItem("Peanut"),
            new MenuItem("Peas"),
            new MenuItem("Peruvian Lily"),
            new MenuItem("Pineapple"),
            new MenuItem("Pineapple Sage"),
            new MenuItem("Rose"),
            new MenuItem("Salami"));
    @FXML
    private MenuButton ownerRegistrationPublic = new MenuButton("Public?", null,
            new MenuItem("TRUE"),
            new MenuItem("FALSE"));
    @FXML
    private MenuButton ownerRegistrationAnimal = new MenuButton("Animal", null,
            new MenuItem("Cheetah"),
            new MenuItem("Chicken"),
            new MenuItem("Cow"),
            new MenuItem("Goat"),
            new MenuItem("Mongoose"),
            new MenuItem("Monkey"),
            new MenuItem("Pete"),
            new MenuItem("Pig"));
    @FXML
    private MenuButton ownerRegistrationCommercial = new MenuButton("Commercial?", null,
            new MenuItem("TRUE"),
            new MenuItem("FALSE"));;
    @FXML
    private Button ownerRegistrationRegisterButton;
    @FXML
    private Button ownerRegistrationCancelButton;
    @FXML
    private Label owner_ErrorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        Boolean registered = false;
        Boolean passed = false;
        Boolean matches = Pattern.matches("^[A-Za-z0-9]+@[A-Za-z0-9]+(\\.[A-Za-z]{3,})$",
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
                    + "\nYou must supply an Username.");
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
        if (ownerRegistrationZip.getText().contains("[a-zA-Z]+") == false) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nZipcode only contains numbers.");
            passed = true;
        } else if (ownerRegistrationZip.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must supply a ZipCode.");
            passed = true;
        } else if (ownerRegistrationZip.getText().length() != 5) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nZipCode must be 5 digits.");
            passed = true;
        }
        if (ownerRegistrationAcres.getText().contains("[a-zA-Z]+") == false) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nAcres only contains numbers.");
            passed = true;
        } else if (ownerRegistrationAcres.getText().length() == 0) {
            owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                    + "\nYou must supply Acres.");
            passed = true;
        }

        if (!passed) {
            try {
                owner_ErrorMessage.setText("1");
                Connection server = SQLConnecter.connect();
                if (!server.isClosed()) {
                    owner_ErrorMessage.setText("Successfully connected to Server");
                }
                owner_ErrorMessage.setText("3");

                ResultSet val = server.createStatement().executeQuery("SELECT Email, Username FROM USER WHERE Email = '"
                        + ownerRegistrationEmail.getText() + "' AND Username = '"
                        + ownerRegistrationUser.getText() + "'");
                owner_ErrorMessage.setText("4");

                if (val.next()) {
                    if (val.getString(1) == ownerRegistrationEmail.getText()) {
                        owner_ErrorMessage.setText("Email must be unique.");
                    }
                    if (val.getString(2) == ownerRegistrationUser.getText()) {
                        owner_ErrorMessage.setText(owner_ErrorMessage.getText()
                                + "\n User must be unique.");
                    }

                }

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(ownerRegistrationPassword.getText().getBytes());
                owner_ErrorMessage.setText("6");
                byte[] digest = md.digest();
                String pass = DatatypeConverter.printHexBinary(digest);
                String email = ownerRegistrationEmail.getText();
                String username = ownerRegistrationUser.getText();
                String insert = "INSERT INTO USER Password, Email, Username, U_type,  "
                        + "VALUES('" + pass + "', '" + email + "', '" + username + "', \"OWNER\")";
                System.out.println(insert);
                server.createStatement().execute(insert);
                owner_ErrorMessage.setText("7");
                val = server.createStatement().executeQuery("SELECT MAX(ID) FROM PROPERTY");
                int propID = 0;
                if (val.next()) {
                    propID = val.getInt(1);
                }
                insert = "INSERT INTO PROPERTY ID, Name, Acres, IsCommercial, IsPublic, Address, City,"
                        + "Zip, P_type, Owner, ApprovedBy"
                        + "VALUES(" + propID + ", '"
                        + ownerRegistrationPropName + "', '"
                        + ownerRegistrationAcres + "', '"
                        + ownerRegistrationCommercial + "', '";
                System.out.println(insert);
                server.createStatement().execute(insert);
                server.close();

                registered = true;

            } catch (Exception e) {
                System.out.println("Something went wrong (KAPPA)");
                return;
            }
        }


        if (registered) {
            System.out.println("Visitor Registration Successful");
            sceneChanger(ownerRegistrationRegisterButton, "page_login.fxml");
        }
    }
}
