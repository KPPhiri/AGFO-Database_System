
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


    //Initialize observable list to hold out database data
    private ObservableList<userPropDetails> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDataFromDatabase();

    }

    @FXML
    private void loadDataFromDatabase() {
        try {
            System.out.println("WORKING");
            Connection server = Connect.SQLConnecter.connect();
            data = FXCollections.observableArrayList();

            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Address, City, Zip, Acres, P_type, IsPublic, IsCommercial , ID FROM PROPERTY WHERE Owner = '" + "farmowner" +"'");
            while (rs.next()) {
               int id = rs.getInt(9);
               //ResultSet ra = server.createStatement().executeQuery("SELECT COUNT(P_id) FROM VISITS WHERE P_id = 5");
                 //System.out.println("GETTTINGGG" + ra.getInt(1));


                data.add(new userPropDetails(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6),rs.getBoolean(7), rs.getBoolean(8),rs.getInt(9),true, 9,  0.0));
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
        colIsValid.setCellValueFactory(new PropertyValueFactory<>("visits"));
        colVisits.setCellValueFactory(new PropertyValueFactory<>("valid"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));


        table.setItems(null);
        System.out.println("Should be adding");
        System.out.println(data.size());
        table.setItems(data);

    }


}
