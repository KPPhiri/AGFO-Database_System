import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewVisitorRegistration implements Initializable {
    @FXML
    private TextField visitorRegistrationEmail;
    @FXML
    private TextField visitorRegistrationUser;
    @FXML
    private PasswordField visitorRegistrationPassword;
    @FXML
    private PasswordField visitorRegistrationConfirmPassword;
    @FXML
    private Button visitorRegistration_registerButton;
    @FXML
    private Button visitorRegistration_cancelButton;
    @FXML
    private Label visitor_ErrorMessage;



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

    public void pressCancelButton(ActionEvent actionEvent) throws IOException {
        sceneChanger(visitorRegistration_cancelButton, "page_login.fxml");
    }
    public void pressRegisterVisitor(ActionEvent actionEvent) throws IOException {
        visitor_ErrorMessage.setText("");
        Boolean registered = false;
        Boolean passed = false;
        Boolean matches = Pattern.matches("^[A-Za-z0-9]+@[A-Za-z0-9]+(\\.[A-Za-z]{3,})$",
                visitorRegistrationEmail.getText());

        if (visitorRegistrationEmail.getText().length() == 0) {
            visitor_ErrorMessage.setText("You must supply an Email.");
            passed = true;
        } else if (!matches) {
            visitor_ErrorMessage.setText(visitor_ErrorMessage.getText()
                    + "\nEmail is not valid.");
            passed = true;
        }

        if (visitorRegistrationUser.getText().length() == 0) {
            visitor_ErrorMessage.setText(visitor_ErrorMessage.getText()
                    + "\nYou must supply an Username.");
            passed = true;
        }

        if (visitorRegistrationPassword.getText().length() == 0) {
            visitor_ErrorMessage.setText(visitor_ErrorMessage.getText()
                    + "\nYou must supply a Password.");
            passed = true;
        } else if (visitorRegistrationConfirmPassword.getText().length() == 0) {
            visitor_ErrorMessage.setText(visitor_ErrorMessage.getText()
                    + "\nYou must supply an Username.");
            passed = true;
        } else if (!(visitorRegistrationPassword.getText().length() > 7)) {
            visitor_ErrorMessage.setText(visitor_ErrorMessage.getText()
                    + "\nPassword must be 8 characters or longer.");
            passed = true;
        } else if (!(visitorRegistrationPassword.getText()
                .equals(visitorRegistrationConfirmPassword.getText()))) {
            visitor_ErrorMessage.setText(visitor_ErrorMessage.getText()
                    + "\nPasswords do not match.");
            passed = true;
        }
        if (!passed) {
            try {
                visitor_ErrorMessage.setText("1");
                Connection server = Connect.SQLConnecter.connect();
                if (!server.isClosed()) {
                    visitor_ErrorMessage.setText("Successfully connected to Server");
                }
                visitor_ErrorMessage.setText("3");

                ResultSet val = server.createStatement().executeQuery("SELECT Email, Username FROM USER WHERE Email = '"
                        + visitorRegistrationEmail.getText() + "' AND Username = '"
                        + visitorRegistrationUser.getText() + "'");
                visitor_ErrorMessage.setText("4");

                if (val.next()) {
                    if (val.getString(1) == visitorRegistrationEmail.getText()) {
                        visitor_ErrorMessage.setText("Email must be unique.");
                    }
                    if (val.getString(2) == visitorRegistrationUser.getText()) {
                        visitor_ErrorMessage.setText(visitor_ErrorMessage.getText()
                                + "\n User must be unique.");
                    }

                }

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(visitorRegistrationPassword.getText().getBytes());
                visitor_ErrorMessage.setText("6");
                byte[] digest = md.digest();
                String pass = DatatypeConverter.printHexBinary(digest);
                String email = visitorRegistrationEmail.getText();
                String username = visitorRegistrationUser.getText();
                String insert = "INSERT INTO USER Password, Email, Username, U_type "
                        + "VALUES('" + pass + "', '" + email + "', '" + username + "', \"VISITOR\")";
                System.out.println(insert);
                server.createStatement().execute(insert);
                visitor_ErrorMessage.setText("7");
                server.close();

                registered = true;

            } catch (Exception e) {
                System.out.println("Something went wrong (KAPPA)");
                return;
            }
        }


        if (registered) {
            System.out.println("Visitor Registration Successful");
            sceneChanger(visitorRegistration_registerButton, "page_login.fxml");
        }
    }
}
