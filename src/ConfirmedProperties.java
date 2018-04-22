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

            ResultSet rs = server.createStatement().executeQuery("SELECT Name,Address,City,Zip ,Acres, P_type, IsPublic, IsCommercial, ID, ApprovedBy, AVG(Rating) FROM PROPERTY JOIN VISITS WHERE ApprovedBy != '(null)' AND ID = P_ID GROUP BY P_ID");

            while (rs.next()) {
                confirmedPropDetails a =  new confirmedPropDetails(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getDouble(5),rs.getString(6),rs.getBoolean(7),
                        rs.getBoolean(8),Integer.toString(Integer.parseInt(rs.getString(9)) + 100000),rs.getString(10),rs.getDouble(11));
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
