import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class PendingCrops implements Initializable {
    @FXML
    public TableView CropTable;

    @FXML
    public TableColumn Name_Col;

    @FXML
    public TableColumn Type_Col;

    @FXML
    public Button Back_Button;

    @FXML
    public Button Approve_Button;

    @FXML
    public Button Delete_Button;

    private String currentSelection;


    private ObservableList<pendingCropDetails> data;


    @Override

    public void initialize(URL location, ResourceBundle resources) {

        loadDataFromDatabase();
        Back_Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage stage;
                    Parent root;
                    stage = (Stage) Back_Button.getScene().getWindow();
                    root = FXMLLoader.load(getClass().getResource("welcome_owner.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    System.out.println("wrong");
                }
            }
        });

        Delete_Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentSelection == null) {

                } else {
                    try {
                        data = FXCollections.observableArrayList();

                        Connection server = Connect.SQLConnecter.connect();

                        server.createStatement().executeQuery("Delete Name, Type FROM FARM_ITEM WHERE Name = '"+ currentSelection.toString() +"'");
                        loadDataFromDatabase();
                    } catch(Exception e) {}
                }
            }
        });

        Approve_Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentSelection == null) {

                } else {
                    try {
                        data = FXCollections.observableArrayList();

                        Connection server = Connect.SQLConnecter.connect();

                        ResultSet rs = server.createStatement().executeQuery("Update FARM_ITEM set isAppoved = 1 WHERE Name = '" + currentSelection + "'");
                        loadDataFromDatabase();
                    } catch(Exception e) {}
                }
            }
        });



        CropTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(CropTable.getSelectionModel().getSelectedItem() != null)
                {
                    TableView.TableViewSelectionModel selectionModel = CropTable.getSelectionModel();


                    Object selectedItems = CropTable.getSelectionModel().getSelectedItems().get(0);
                    String first_Column = selectedItems.toString().split(",")[0];
                    //System.out.println(first_Column);
                    currentSelection = first_Column;
                }
            }
        });


    }


    @FXML

    private void loadDataFromDatabase() {

        try {

            System.out.println("WORKING");

            Connection server = Connect.SQLConnecter.connect();

            data = FXCollections.observableArrayList();


            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Type FROM FARM_ITEM WHERE isApproved = 0");

            while (rs.next()) {

                //int id = rs.getInt(2);

                //ResultSet ra = server.createStatement().executeQuery("SELECT COUNT(P_id) FROM VISITS WHERE P_id = 5");

                //System.out.println("GETTTINGGG" + ra.getInt(1));


                data.add(new pendingCropDetails(rs.getString(1), rs.getString(2)));

            }


        } catch (Exception e) {

            System.out.println("something went wrong + " + e.getMessage());


        }

        //Set cell value factory to tableview.

        //NB.PropertyValue Factory must be the same with the one set in model class.

        Name_Col.setCellValueFactory(new PropertyValueFactory<>("name"));

        Type_Col.setCellValueFactory(new PropertyValueFactory<>("type"));


        CropTable.setItems(null);

        System.out.println("Should be adding");

        System.out.println(data.size());

        CropTable.setItems(data);


    }


}