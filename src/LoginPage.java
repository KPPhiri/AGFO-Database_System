
import Connect.SQLConnecter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LoginPage implements Initializable{
    @FXML
    public Label wrong;
    @FXML
    private TextField email_TextField;

    @FXML
    private PasswordField  password_PasswordField;

    @FXML
    private Button lgn_btn;

    @FXML
    private Button newOwnerRegistrationButton;
    @FXML
    private Button newVisitorRegistrationButton;
    @FXML
    private Label login_ErrorMessage;

    User user = User.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



    @FXML
    private void checkLogin(ActionEvent event) {
        boolean passed = false;
        boolean matches = Pattern.matches("^[A-Za-z0-9]+@[A-Za-z0-9]+(\\.[A-Za-z]{3,})$",
                email_TextField.getText());
        if (email_TextField.getText().length() == 0) {
            login_ErrorMessage.setText("You must supply an Email.");
            passed = true;
        } else if (!matches) {
            login_ErrorMessage.setText(login_ErrorMessage.getText()
                    + "\nEmail is not valid.");
            passed = true;
        }

        if (password_PasswordField.getText().length() == 0) {
            login_ErrorMessage.setText(login_ErrorMessage.getText()
                    + "\nYou must supply a Password.");
            passed = true;
        } else if (!(password_PasswordField.getText().length() > 7)) {
            login_ErrorMessage.setText(login_ErrorMessage.getText()
                    + "\nPassword must be 8 characters or longer.");
            passed = true;
        }

        if (!passed) {
            try {
                Connection server = SQLConnecter.connect();
                if (server.isClosed()) {
                    login_ErrorMessage.setText("Server is Closed or not Connected to it.");
                    return;
                }
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] bytesOfPass = password_PasswordField.getText().getBytes("UTF-8");
                byte[] digest = md.digest(bytesOfPass);
                String pass;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < digest.length; i++) {
                    stringBuffer.append(Integer.toString((digest[i] & 0xff) + 0x100, 16)
                            .substring(1));
                }
                pass = stringBuffer.toString();

                ResultSet val = server.createStatement().executeQuery("SELECT Username, U_type, Password FROM USER WHERE Username = '"
                        + email_TextField.getText() + "' AND Password = '" + pass + "'");

                if (val.next()) {
                    if (val.getString(3).equals(pass)) {
                        user.setUsername(val.getString(1));
                        user.setType(val.getString(2));
                        System.out.println(user.getType());
                        if (user.getType().equals("OWNER")) {
                            Parent root = FXMLLoader.load(getClass().getResource("welcome_owner.fxml"));
                            Stage stage = (Stage) lgn_btn.getScene().getWindow();
                            Scene scene = new Scene(root);

                            stage.setScene(scene);
                            stage.show();

                        }
                        if (user.getType().equals("VISITOR")) {
                            Parent root = FXMLLoader.load(getClass().getResource("welcome_visitor.fxml"));
                            Stage stage = (Stage) lgn_btn.getScene().getWindow();
                            Scene scene = new Scene(root);

                            stage.setScene(scene);
                            stage.show();
                        }
                    }
                } else {
                    wrong.setVisible(true);
                }
            } catch (Exception e) {
                System.out.println("something went wrong + " + e.getMessage());

            }
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

    public void pressOwnerRegistration(ActionEvent actionEvent) throws IOException {
        sceneChanger(newOwnerRegistrationButton, "new_owner_registration.fxml");
    }

    public void pressVisitorRegistration(ActionEvent actionEvent) throws IOException {
        sceneChanger(newVisitorRegistrationButton, "new_visitor_registration.fxml");
    }
}
