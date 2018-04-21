
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class OwnerWelcome implements Initializable{

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
    public TableColumn colIsValid;
    @FXML
    public TableColumn colVisits;
    @FXML
    public TableColumn colRating;
    @FXML
    public TableView table;
    @FXML
    public ComboBox searchMenu;
    @FXML
    public Button searchButton;
    @FXML
    public TextField searchField;


    //Initialize observable list to hold out database data
    private ObservableList<userPropDetails> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDataFromDatabase();
        createMenu();
        filtering();


    }

    @FXML
    private void loadDataFromDatabase() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            data = FXCollections.observableArrayList();

            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Address, City, Zip, Acres, P_type, IsPublic, IsCommercial , ID, ApprovedBy FROM PROPERTY WHERE Owner = '" + "farmowner" +"'");
            while (rs.next()) {
               int id = rs.getInt(9);
                ResultSet ra = server.createStatement().executeQuery("SELECT COUNT(P_id) FROM VISITS WHERE P_id = " + id);
                int pid = 0;
                if(ra.next()) {
                    pid = ra.getInt(1);
                }

                ResultSet rb = server.createStatement().executeQuery("SELECT avg(Rating) FROM VISITS WHERE P_id = " + id);
                double avgRating = 0.0;
                if(rb.next()) {
                    avgRating = Math.round((rb.getDouble(1)) * 10.0) / 10.0;
                }

                String isValid = ((rs.getString(10)) != "NULL") ? "True": "False";
                data.add(new userPropDetails(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6),rs.getBoolean(7), rs.getBoolean(8),rs.getInt(9),isValid, pid,  avgRating));
            }


        } catch(Exception e) {
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
        colVisits.setCellValueFactory(new PropertyValueFactory<>("visits"));
        colIsValid.setCellValueFactory(new PropertyValueFactory<>("valid"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));


        table.setItems(null);
        System.out.println("Should be adding");
        System.out.println(data.size());
        table.setItems(data);

    }

    public void createMenu() {
        searchMenu.setValue("Search by..");
//        MenuItem mName = new MenuItem("Name");
//        mName.setId("propName");
//        MenuItem mAdd = new MenuItem("Address");
//        mName.setId("address");
//        MenuItem mCity = new MenuItem("City");
//        mName.setId("city");
//        MenuItem mType = new MenuItem("Type");
//        mName.setId("type");
//        MenuItem mVisits= new MenuItem("Visits");
//        mName.setId("visits");
//        MenuItem mRating = new MenuItem("Avg. Rating");
//        mName.setId("rating");
//
//        searchMenu.getItems().addAll(mName, mAdd, mCity, mType, mVisits, mRating);
        searchMenu.getItems().addAll("Name", "City", "Type", "Visits", "Avg. Rating");

    }


    public void filtering() {
        FilteredList<userPropDetails> filteredData = new FilteredList<>(data, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tuple -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (tuple.getPropName().toLowerCase().contains(newValue.toLowerCase())
                        && searchMenu.getValue().toString().equals("Name")) {

                    return true;
                } else if (tuple.getCity().toLowerCase().contains(newValue.toLowerCase())
                        && searchMenu.getValue().toString().equals("City")) {
                    return true;
                } else if (tuple.getType().toLowerCase().contains(newValue.toLowerCase())
                        && searchMenu.getValue().toString().equals("Type")) {
                    return true;
                } else if ((tuple.getVisits() + "").contains(newValue)
                        && searchMenu.getValue().toString().equals("Visits")) {
                    return true;
                } else if ((tuple.getRating() + "").contains(newValue)
                        && searchMenu.getValue().toString().equals("Avg. Rating")) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        table.setItems(filteredData);
    }
}
