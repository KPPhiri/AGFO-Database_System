
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginPage implements Initializable{
    @FXML
    private TextField email_TextField;

    @FXML
    private PasswordField  password_PasswordField;

    @FXML
    private Button lgn_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }




    @FXML
    private String checkLogin() {
        String email = "anthony.dinozzo@ncis.mil.gov";
        String password = "c67e443eaa780debf5ee2d71a2a7dc39";
        String u_type = "";
        if(email_TextField.toString().length() > 1) {
            if (password_PasswordField.toString().length() > 1) {
                email = email_TextField.toString();
                password = password_PasswordField.toString();
            }
        }

        System.out.println("Hello World");

        try {
            Connection server = Connect.SQLConnecter.connect();

            Statement statement = server.createStatement();

            String select = "SELECT Password, U_type FROM USER WHERE Email == " + email;  //put sql statement

            ResultSet val = statement.executeQuery(select);

            if(val.next()) {
                if(password.equals(val.getString("Password"))) {
                    System.out.println("working!");
                }

            }

        } catch(Exception e) {

        }
        return "";
    }


    public void pressButton(ActionEvent actionEvent) {
        checkLogin();
    }
}
