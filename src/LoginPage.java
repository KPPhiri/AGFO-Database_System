
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

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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

    User user;

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
                ResultSet val = server.createStatement().executeQuery("SELECT Username, U_type FROM USER WHERE Email = '" + email +  "' AND Password = '" + password + "'");


    //            Statement statement = server.createStatement();
    //            String select = "SELECT Username, U_type FROM USER WHERE Email = '" + email +  "' AND Password = '" + password + "'";  //put sql statement
    //            ResultSet val = statement.executeQuery(select);

                if(val.next()) {
                    System.out.println("Successful Login.");
                    user = new User(val.getString(1), val.getString(2));
                    if(user.getType().equals("OWNER")){

                        Parent root = FXMLLoader.load(getClass().getResource("welcome_owner.fxml"));
                        Stage stage = new Stage();
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


//    public void pressButton(ActionEvent actionEvent) {
//        checkLogin();
//    }
}
