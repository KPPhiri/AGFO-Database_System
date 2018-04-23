import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
public class ConfirmedProperties implements Initializable {
    @FXML
    public TableView confirmedtable;
    @FXML
    private TableColumn namecol;
    @FXML
    private TableColumn addresscol;
    @FXML
    private TableColumn citycol;
    @FXML
    private TableColumn usernamecol;
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
    private TableColumn verifiedcol;
    @FXML
    private TableColumn avgcol;
    @FXML
    private Button backbut;
    @FXML
    private Button managebut;
    @FXML
    private ComboBox searchcombo;
    @FXML
    private TextField searchbar;

    public static confirmedPropDetails  selectedOwnerPropConfirmed = null;

    private ObservableList<confirmedPropDetails> data;

    public void initialize(URL location, ResourceBundle resources) {
        loadDataFromDatabase();
        createMenu();
        filtering();
    }

    public void createMenu() {
        searchcombo.setValue("Search by..");
        searchcombo.getItems().addAll("Name","Zip","Type","Verified by","Avg.Rating");
    }

    @FXML
    private void loadDataFromDatabase()
    {
        try {
            System.out.println("WORKING");
            selectedOwnerPropConfirmed = null;
            Connection server = Connect.SQLConnecter.connect();
            data = FXCollections.observableArrayList();
<<<<<<< HEAD
            System.out.println("jbakjaeljaerng");
            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Address, City, Zip , Acres, P_type, IsPublic, IsCommercial, ID, ApprovedBy, AVG(Rating) FROM PROPERTY JOIN VISITS WHERE ID = P_ID");

            while (rs.next()) {
                System.out.println("broke");
                confirmedPropDetails a =  new confirmedPropDetails(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4), rs.getDouble(5),rs.getString(6),rs.getBoolean(7),
                        rs.getBoolean(8),(Integer.toString(rs.getInt(9) + 100000)).substring(1), rs.getString(10), rs.getDouble(11));
=======

            //ResultSet rs = server.createStatement().executeQuery("SELECT Name,Address,City,Zip ,Acres, P_type, IsPublic, IsCommercial, ID, ApprovedBy, AVG(Rating) FROM PROPERTY JOIN VISITS WHERE ApprovedBy != '(null)' AND ID = P_ID GROUP BY P_ID");
            ResultSet rs = server.createStatement().executeQuery("SELECT Name,Address,City,Zip ,Acres, P_type, IsPublic, IsCommercial, ID, ApprovedBy FROM PROPERTY WHERE ApprovedBy != '(null)'");
            System.out.println("ABOUT TO ENTER LOOP");
            while (rs.next()) {
                int id = rs.getInt(9);
                ResultSet rb = server.createStatement().executeQuery("SELECT avg(Rating) FROM VISITS WHERE P_id = " + id);
                double avgRating = 0.0;
                if(rb.next()) {
                    avgRating = Math.round((rb.getDouble(1)) * 10.0) / 10.0;
                }
                System.out.println("Running in the LOOP");
                confirmedPropDetails a =  new confirmedPropDetails(
                        rs.getString(1),//Name
                        rs.getString(2),//Address
                        rs.getString(3),//City
                        rs.getString(4),//Zip
                        rs.getDouble(5),//Acres
                        rs.getString(6),//P_type
                        rs.getBoolean(7),//Ispublic
                        rs.getBoolean(8),//iscommerical
                        (Integer.toString(rs.getInt(9) + 100000)).substring(1),//ID
                        rs.getString(10),//Approved By
                        avgRating); //Avg.rating
>>>>>>> 8cdec5b008a9ec5d0a5915494e67c138e1ade2cb
                data.add(a);
            }

            server.close();
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
        verifiedcol.setCellValueFactory(new PropertyValueFactory<>("verifiedby"));
        avgcol.setCellValueFactory(new PropertyValueFactory<>("avgrating"));


        confirmedtable.setItems(null);
        confirmedtable.setItems(data);
    }


    public void filtering() {
        FilteredList<confirmedPropDetails> filteredData = new FilteredList(data, p -> true);

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
                else if (tuple.getZip().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Zip")) {
                    return true;
                }
                else if (tuple.getType().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Type")) {
                    return true;
                }
                else if (tuple.getVerifiedby().toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Verified by")) {
                    return true;
                }
                else if (String.valueOf(tuple.getAvgrating()).toLowerCase().contains(newValue.toLowerCase())
                        && searchcombo.getValue().toString().equals("Avg.Rating")) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        confirmedtable.setItems(filteredData);
    }

    private void sceneChanger(Button button, String fxmlName) throws IOException {
        Stage stage;
        Parent root;
        stage = (Stage) button.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(fxmlName));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void pressManageProperty (ActionEvent actionEvent) throws IOException {
        if (confirmedtable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        selectedOwnerPropConfirmed = (confirmedPropDetails)confirmedtable.getSelectionModel().getSelectedItem();
        sceneChanger(managebut, "admin_manage_property.fxml");
    }

    public void openManage(ActionEvent actionEvent) {
        try {
            if (confirmedtable.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            selectedOwnerPropConfirmed = (confirmedPropDetails) confirmedtable.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(getClass().getResource("admin_manage_property.fxml"));
            Stage stage = (Stage) managebut.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            System.out.println("something went wrong + " + e.getMessage());

        }


    }



    public static confirmedPropDetails getselectedOwnerPropConfirmed() {
        return selectedOwnerPropConfirmed;
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
