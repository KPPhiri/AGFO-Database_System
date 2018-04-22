import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.ResourceBundle;

public class VisitorOverview implements Initializable {
    @FXML
    private TableColumn usernamecol;
    @FXML
    private TableColumn emailcol;
    @FXML
    private TableColumn logviscol;
    @FXML
    private Button deleteaccbut;
    @FXML
    private Button deletelogbut;
    @FXML
    private Button backbut;
    @FXML
    private ComboBox searchcombo;
    @FXML
    private TextField searchbar;
    @FXML
    public TableView visitortable;

    private ObservableList<visitorPropDetails> data1;

    public void initialize(URL location, ResourceBundle resources) {
        loadDataFromDatabase();
        createMenu();
        filtering();
    }


    public void createMenu() {
        searchcombo.setValue("Search by..");
        searchcombo.getItems().addAll("Username","Email","Logged Visits");
    }

    @FXML
    private void loadDataFromDatabase()
    {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            data1 = FXCollections.observableArrayList();

             ResultSet rs = server.createStatement().executeQuery("SELECT Username,Email,IFNULL(Logged_Visits,0)FROM(SELECT *FROM (SELECT Username,Email FROM USER WHERE U_type='VISITOR') STUFF1 LEFT JOIN (SELECT Username AS VUsername, COUNT(*) AS 'Logged_Visits' FROM VISITS GROUP BY Username) STUFF2 ON Username=VUsername ) KILLME ");

            while (rs.next()) {
                visitorPropDetails a =  new visitorPropDetails(rs.getString(1),rs.getString(2),rs.getInt(3));
                data1.add(a);
                System.out.println(a.getLoggedvisit());
            }

        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
        usernamecol.setCellValueFactory(new PropertyValueFactory<visitorPropDetails,String>("username1"));
        emailcol.setCellValueFactory(new PropertyValueFactory<visitorPropDetails,String>("email1"));
        logviscol.setCellValueFactory(new PropertyValueFactory<visitorPropDetails,String>("loggedvisit"));
        visitortable.setItems(null);
        System.out.println(data1.size());
        visitortable.setItems(data1);
    }

    public void filtering() {
        FilteredList<visitorPropDetails> filteredData = new FilteredList(data1, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tuple -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (tuple.getUsername1().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Username")) {

                    return true;
                } else if (tuple.getEmail1().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Email")) {
                    return true;
                } else if (String.valueOf(tuple.getLoggedvisit()).toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Logged Visits")) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        visitortable.setItems(filteredData);
    }

    public void delete() {
        try {
            if(visitortable.getSelectionModel().getSelectedItem()==null){
                return;
            }
            visitorPropDetails selectedOwner = (visitorPropDetails) visitortable.getSelectionModel().getSelectedItem();
            Connection server = Connect.SQLConnecter.connect();
            server.createStatement().executeUpdate("DELETE FROM USER WHERE Email= '"+ selectedOwner.getEmail1() +"'");
            loadDataFromDatabase();
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }

    public void deletelog() {
        try {
            if(visitortable.getSelectionModel().getSelectedItem()==null){
                return;
            }
            visitorPropDetails selectedOwner = (visitorPropDetails) visitortable.getSelectionModel().getSelectedItem();
            Connection server = Connect.SQLConnecter.connect();
            server.createStatement().executeUpdate("DELETE FROM VISITS WHERE Username= '"+ selectedOwner.getUsername1() +"'");
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
