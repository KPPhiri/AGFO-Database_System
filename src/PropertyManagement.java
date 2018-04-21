import com.sun.org.apache.xml.internal.security.Init;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PropertyManagement implements Initializable {
    @FXML
    public Button save;
    @FXML
    public Button back;
    @FXML
    public Button delete;

    @Override
    public void initialize (URL location, ResourceBundle resources) {
    }

}
