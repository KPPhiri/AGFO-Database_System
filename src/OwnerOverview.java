import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;

public class OwnerOverview implements Initializable {
    @FXML
    private TableColumn usernamecol;
    @FXML
    private TableColumn emailcol;
    @FXML
    private TableColumn numpropcol;
    @FXML
    private Button deletebut;
    @FXML
    private Button backbut;
    @FXML
    private TextField searchbar;
    @FXML
    private ComboBox searchcombo;
    @FXML
    public TableView ownertable;

    private ObservableList<ownerPropDetails> data;

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

        ResultSet rs = server.createStatement().executeQuery("" +
                "SELECT Username,Email,COUNT(*)" +
                "FROM USER JOIN PROPERTY WHERE U_type='OWNER' AND Username=Owner GROUP BY USERNAME");
         while (rs.next()) {
        data.add(new ownerPropDetails(rs.getString(1), rs.getString(2),  rs.getInt(3)));
                            }


        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());

        }
        usernamecol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        numpropcol.setCellValueFactory(new PropertyValueFactory<>("numProp"));


        ownertable.setItems(null);
        System.out.println("Should be adding");
        System.out.println(data.size());
        ownertable.setItems(data);
    }

    public void createMenu() {
        searchcombo.setValue("Search by..");
        searchcombo.getItems().addAll("Username","Email","Number of Properties");

    }

    public void filtering() {
        FilteredList<ownerPropDetails> filteredData = new FilteredList(data, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tuple -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (tuple.getUserName().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Username")) {

                    return true;
                } else if (tuple.getEmail().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Email")) {
                    return true;
                } else if (String.valueOf(tuple.getNumProp()).toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Number of Properties")) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        ownertable.setItems(filteredData);
    }
    public void delete() {
        try {
            if(ownertable.getSelectionModel().getSelectedItem()==null){
                return;
            }
            ownerPropDetails selectedOwner = (ownerPropDetails) ownertable.getSelectionModel().getSelectedItem();
            Connection server = Connect.SQLConnecter.connect();
            server.createStatement().executeUpdate("DELETE FROM USER WHERE Email= '"+ selectedOwner.getEmail() +"'");
            loadDataFromDatabase();
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
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
