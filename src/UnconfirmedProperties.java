import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
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
import java.util.ResourceBundle;
public class UnconfirmedProperties implements Initializable {
    @FXML
    public TableView unconfirmedtable;
    @FXML
    private TableColumn namecol;
    @FXML
    private TableColumn addresscol;
    @FXML
    private TableColumn citycol;
    @FXML
    private TableColumn zipcol;
    @FXML
    private TableColumn sizecol;
    @FXML
    private TableColumn typecol;
    @FXML
    private TableColumn publiccol;
    @FXML
    private TableColumn commercialcol;
    @FXML
    private TableColumn idcol;
    @FXML
    private TableColumn ownercol;
    @FXML
    private Button backbut;
    @FXML
    private Button managebut;
    @FXML
    private ComboBox searchcombo;
    @FXML
    private TextField searchbar;

    private ObservableList<unconfirmedPropDetails> data;

    public static unconfirmedPropDetails  selectedOwnerPropUnconfirmed = null;

    public void initialize(URL location, ResourceBundle resources) {
        loadDataFromDatabase();
        createMenu();
        filtering();
    }
    public void createMenu() {
        searchcombo.setValue("Search by..");
        searchcombo.getItems().addAll("Name","Size","Owner");
    }

    @FXML
    private void loadDataFromDatabase()
    {
        try {
            System.out.println("WORKING");
            selectedOwnerPropUnconfirmed = null;
            Connection server = Connect.SQLConnecter.connect();
            data = FXCollections.observableArrayList();

            ResultSet rs = server.createStatement().executeQuery("SELECT Name,Address,City,Zip ,Acres, P_type, IsPublic, IsCommercial, ID, Owner FROM PROPERTY WHERE ApprovedBy IS NULL");

            while (rs.next()) {
                unconfirmedPropDetails a =  new unconfirmedPropDetails(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),rs.getBoolean(7),
                        rs.getBoolean(8),rs.getString(9),rs.getString(10));
                System.out.println(rs.getString(10));
                data.add(a);
            }

        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
        namecol.setCellValueFactory(new PropertyValueFactory<confirmedPropDetails,String>("name"));
        addresscol.setCellValueFactory(new PropertyValueFactory<>("address"));
        citycol.setCellValueFactory(new PropertyValueFactory<>("city"));
        zipcol.setCellValueFactory(new PropertyValueFactory<>("zip"));
        sizecol.setCellValueFactory(new PropertyValueFactory<>("size"));
        typecol.setCellValueFactory(new PropertyValueFactory<>("type"));
        publiccol.setCellValueFactory(new PropertyValueFactory<>("ispublic"));
        commercialcol.setCellValueFactory(new PropertyValueFactory<>("iscommercial"));
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ownercol.setCellValueFactory(new PropertyValueFactory<>("owner"));


        unconfirmedtable.setItems(null);
        unconfirmedtable.setItems(data);
    }

    public void filtering() {
        FilteredList<unconfirmedPropDetails> filteredData = new FilteredList(data, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tuple -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (tuple.getName().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Name")) {
                    return true;
                }
                else if (tuple.getSize().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Size")) {
                    return true;
                }
                else if (tuple.getOwner().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Owner")) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        unconfirmedtable.setItems(filteredData);
    }


    public void openManage(ActionEvent actionEvent) {
        try {
            if (unconfirmedtable.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            selectedOwnerPropUnconfirmed = (unconfirmedPropDetails) unconfirmedtable.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(getClass().getResource("admin_manage_property.fxml"));
            Stage stage = (Stage) managebut.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            System.out.println("something went wrong + " + e.getMessage());

        }


    }



    public static unconfirmedPropDetails getselectedOwnerPropUnconfirmed() {
        return selectedOwnerPropUnconfirmed;
    }

    public void gobacktoOwnerWelcome(){
        Stage stage;
        Parent root = null;
        stage = (Stage) backbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("admin_welcome.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
