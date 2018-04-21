import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    public ComboBox searchBy;
    @FXML
    public Button viewProperty;
    @FXML
    public Button back;

    //Initialize observable list to hold out database data
    private ObservableList<userPropDetails> original_data;
    public static userPropDetails  selectedUser = null;
    public TextField searchField;
    User user = User.getInstance();

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        loadDataFromDatabase();
        searchBy.setValue("Search By");
        searchBy.setItems(FXCollections.observableArrayList(
                "Name", "Address", "City", "Zip", "Type", "ID"
        ));
        searchField.setText("");
        viewProperty.setOnAction(e-> viewProperty());
        back.setOnAction(e -> backToWelcomePage());
        filtering();
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

    private void backToWelcomePage() {
        Stage stage;
        Parent root = null;
        stage = (Stage) back.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("welcome_owner.fxml"));
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

            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Address, City, Zip, Acres, P_type, IsPublic, IsCommercial , ID FROM PROPERTY WHERE ApprovedBy != '" + "NULL" + "' AND Owner != '" + user.getUsername() + "'");
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

    public void filtering() {
        FilteredList<userPropDetails> filteredData = new FilteredList<>(original_data, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tuple -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (tuple.getPropName().toLowerCase().contains(newValue.toLowerCase())
                        && searchBy.getValue().toString().equals("Name")) {
                    return true;
                } else if (tuple.getCity().toLowerCase().contains(newValue.toLowerCase())
                        && searchBy.getValue().toString().equals("City")) {
                    return true;
                } else if (tuple.getType().toLowerCase().contains(newValue.toLowerCase())
                        && searchBy.getValue().toString().equals("Type")) {
                    return true;
                } else if ((tuple.getVisits() + "").contains(newValue)
                        && searchBy.getValue().toString().equals("Visits")) {
                    return true;
                } else if ((tuple.getRating() + "").contains(newValue)
                        && searchBy.getValue().toString().equals("Avg. Rating")) {
                    return true;
                } else if ((tuple.getZip() + "").contains(newValue)
                        && searchBy.getValue().toString().equals("Zip")) {
                    return true;
                }  else if ((tuple.getAddress() + "").toLowerCase().contains(newValue.toLowerCase())
                        && searchBy.getValue().toString().equals("Address")) {
                    return true;
                }   else if ((tuple.getId() + "").contains(newValue)
                        && searchBy.getValue().toString().equals("ID")) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        table.setItems(filteredData);
    }
}
