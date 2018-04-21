import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.ResourceBundle;


/**
 * Table showing Other Owner Properties
 * @author Kevin Nguyen
 */

public class OtherOwnerProperties implements Initializable {
    @FXML
    public TableColumn colName;
    @FXML
    public TableColumn colAddress;
    @FXML
    public TableColumn colCity;
    @FXML
    public TableColumn colZip;
    @FXML
    public TableColumn colSize;
    @FXML
    public TableColumn colType;
    @FXML
    public TableColumn colPublic;
    @FXML
    public TableColumn colCommercial;
    @FXML
    public TableColumn colID;
    @FXML
    public TableColumn colVisits;
    @FXML
    public TableColumn colRating;
    @FXML
    public TableView table;
    @FXML
    public ChoiceBox searchBy;
    @FXML
    public Button viewProperty;

    //Initialize observable list to hold out database data
    private ObservableList<userPropDetails> original_data;
    public static userPropDetails  selectedUser = null;

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        loadDataFromDatabase();
        searchBy.setItems(FXCollections.observableArrayList(
                "Search By", "Name", "Address", "City", "Zip", "Type", "Commercial", "ID"
        ));
        viewProperty.setOnAction(e-> viewProperty());
    }

    private void viewProperty() {
        if (table.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        selectedUser = (userPropDetails) table.getSelectionModel().getSelectedItem();
        Stage stage;
        Parent root = null;
        stage = (Stage) viewProperty.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("property_details.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static userPropDetails getSelectedUser() {
        return selectedUser;
    }

    @FXML
    private void loadDataFromDatabase() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            original_data = FXCollections.observableArrayList();

            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Address, City, Zip, Acres, P_type, IsPublic, IsCommercial , ID FROM PROPERTY WHERE ApprovedBy != '" + "NULL" + "'");
            while (rs.next()) {
                original_data.add(new userPropDetails(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getBoolean(7), rs.getBoolean(8),
                        rs.getInt(9), true, 9, 0.0));
            }


        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());

        }

        //Set cell value factory to tableview.
        //NB.PropertyValue Factory must be the same with the one set in model class.
        colName.setCellValueFactory(new PropertyValueFactory<>("propName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colZip.setCellValueFactory(new PropertyValueFactory<>("zip"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPublic.setCellValueFactory(new PropertyValueFactory<>("ipublic"));
        colCommercial.setCellValueFactory(new PropertyValueFactory<>("commercial"));
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colVisits.setCellValueFactory(new PropertyValueFactory<>("valid"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        table.setItems(null);
        System.out.println("Should be adding");
        System.out.println(original_data.size());
        table.setItems(original_data);
    }
}
