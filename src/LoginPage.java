
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
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginPage implements Initializable{
    @FXML
    public Label wrong;
    @FXML
    private TextField email_TextField;

    @FXML
    private PasswordField  password_PasswordField;

    @FXML
    private Button lgn_btn;

    User user = User.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



    @FXML
    private void checkLogin(ActionEvent event) {
        String email = "farmerJoe@gmail.com";
        String password = "d68fae04506bde7857ff4aa40ebad49c";
        String u_type = "";
        if(email_TextField.getText().toString().length() < 1 || password_PasswordField.getText().toString().length()< 1
                || email_TextField.getText().toString().length() > 50 || password_PasswordField.getText().toString().length() > 30) {
            wrong.setVisible(true);

        } else {
            try {
                Connection server = Connect.SQLConnecter.connect();
                ResultSet val = server.createStatement().executeQuery("SELECT Username, U_type, Email FROM USER WHERE Email = '" + email +  "' AND Password = '" + password + "'");

                // This is the MD5 hash function for the inserted password.
                // TODO: you need to replace the Select SQL statement's password variable in the WHERE portion to pass variable.
                // TODO: This will be done when you can actually sign in.
                    email = email_TextField.getText().toString();
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

                password = password_PasswordField.getText().toString();

                if(val.next()) {
                    user.setUsername(val.getString(1));
                    user.setType(val.getString(2));
                    System.out.println(user.getType());
                    user.setEmail(val.getString(3));
                    if(user.getType().equals("OWNER")){
                        Parent root = FXMLLoader.load(getClass().getResource("welcome_owner.fxml"));
                        Stage stage = (Stage) lgn_btn.getScene().getWindow();
                        Scene scene = new Scene(root);

                        stage.setScene(scene);
                        stage.show();

                    }
                    if(user.getType().equals("VISITOR")){
                        Parent root = FXMLLoader.load(getClass().getResource("welcome_visitor.fxml"));
                        Stage stage = (Stage) lgn_btn.getScene().getWindow();
                        Scene scene = new Scene(root);

                        stage.setScene(scene);
                        stage.show();

                    }

                } else {
                    wrong.setVisible(true);
                }

            } catch(Exception e) {
                System.out.println("something went wrong + " + e.getMessage());

            }

        }



    }

    public void pressOwnerRegistration(ActionEvent actionEvent) {
    }

    public void pressVisitorRegistration(ActionEvent actionEvent) {
    }


//    public void pressButton(ActionEvent actionEvent) {
//        checkLogin();
//    }
}
