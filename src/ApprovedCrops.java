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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.collections.transformation.FilteredList;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ApprovedCrops implements Initializable {
    @FXML
    public TableView CropTable;

    @FXML
    public TableColumn Name_Col;

    @FXML
    public TableColumn Type_Col;

    @FXML
    public Button Back_Button;



    @FXML
    public Button Add_Button;

    @FXML
    public Button Delete_Selection;

    @FXML
    public ComboBox Search_Menu;

    @FXML
    public ComboBox Type_Menu;

    @FXML
    public TextField Name_Bar;

    @FXML
    public TextField Search_Bar;




    private String currentSelect = null;

    private String SearchBy = null;

    private String SearchTerm = null;

    private String Types = null;

    private String CropName = null;

    private ObservableList<approvedCropDetails> data;


    @Override

    public void initialize(URL location, ResourceBundle resources) {

        loadDataFromDatabase();

        filtering();

        Type_Menu.setValue("Type");

        Type_Menu.getItems().addAll("ANIMAL","FRUIT", "VEGETABLE", "FLOWER");

        Search_Menu.setValue("Search by..");

        Search_Menu.getItems().addAll("Name","Type");













        Back_Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage stage;
                    Parent root;
                    stage = (Stage) Back_Button.getScene().getWindow();
                    root = FXMLLoader.load(getClass().getResource("admin_welcome.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    System.out.println("wrong");
                }
            }
        });


        Delete_Selection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentSelect == null) {
                    return;
                } else {
                    try {
                        data = FXCollections.observableArrayList();

                        Connection server = Connect.SQLConnecter.connect();

                        System.out.println(currentSelect);


                        server.createStatement().executeUpdate("DELETE FROM FARM_ITEM WHERE Name = '"+ currentSelect +"'");

                        //ResultSet rs = server.createStatement().executeQuery("Delete Name, Type FROM FARM_ITEM WHERE Name = " + currentSelect);
                        loadDataFromDatabase();
                        server.close();
                    } catch(Exception e) {System.out.println("Error");}
                }
            }
        });

        Add_Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Types = Type_Menu.getValue().toString();
                CropName = Name_Bar.getText();
                System.out.println(Types + " " + CropName);
                if (Types == null || Types.equals("Type") || CropName == null || CropName.equals("SearchBy..")) {
                    return;
                } else {
                    try {
                        data = FXCollections.observableArrayList();

                        Connection server = Connect.SQLConnecter.connect();

                        server.createStatement().executeUpdate("INSERT INTO FARM_ITEM (Name, isApproved, Type) VALUES('" + CropName + "', 1, '" + Types + "')");
                        loadDataFromDatabase();
                        server.close();
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
                    currentSelect = first_Column;
                    System.out.println(currentSelect);
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


            ResultSet rs = server.createStatement().executeQuery("SELECT FARM_ITEM.Name, Type FROM FARM_ITEM WHERE isApproved = 1");

            while (rs.next()) {

                //int id = rs.getInt(2);

                //ResultSet ra = server.createStatement().executeQuery("SELECT COUNT(P_id) FROM VISITS WHERE P_id = 5");

                //System.out.println("GETTTINGGG" + ra.getInt(1));


                data.add(new approvedCropDetails(rs.getString(1), rs.getString(2)));

            }
            server.close();
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

    public void filtering() {
        FilteredList<approvedCropDetails> filteredData = new FilteredList(data, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        Search_Bar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tuple -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (Search_Menu.getValue().toString().equals("Type")
                        && tuple.getType().toLowerCase().contains(newValue.toLowerCase())
                       ) {

                    return true;
                } else if (tuple.getName().toLowerCase().contains(newValue.toLowerCase())
                        && Search_Menu.getValue().toString().equals("Name")) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        CropTable.setItems(filteredData);
    }

    public void sort(ActionEvent actionEvent) {
        try {
            loadDataFromDatabase();
        } catch(Exception e) {

            System.out.println("something went wrong + " + e.getMessage());



        }

    }


//    public void pressButton(ActionEvent actionEvent) {
//        try {
//            Stage stage;
//            Parent root;
//            stage = (Stage) Back_Button.getScene().getWindow();
//            root = FXMLLoader.load(getClass().getResource("welcome_owner.fxml"));
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        } catch (Exception e) {
//            System.out.println("wrong");
//        }
//    }



}