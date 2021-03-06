


import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import javafx.collections.transformation.FilteredList;

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



import java.io.IOException;

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

    public Button logo_btn;

    @FXML

    public Label welO;

    @FXML

    private Button welcomeOwnerAdd;

    @FXML

    public TextField searchField;

    @FXML

    public Button manageButton;







    //Initialize observable list to hold out database data

    private ObservableList<userPropDetails> data;

    public static userPropDetails  selectedOwnerProp = null;

    User user = User.getInstance();



    @Override

    public void initialize(URL location, ResourceBundle resources) {

        welO.setText("Welcome " + user.getUsername());

        loadDataFromDatabase();

        createMenu();

        filtering();
    }



    private void loadDataFromDatabase() {

        try {

            Connection server = Connect.SQLConnecter.connect();

            data = FXCollections.observableArrayList();

            ResultSet rs = server.createStatement().executeQuery("SELECT Name, Address, City, Zip, Acres, P_type, IsPublic, IsCommercial , ID, ApprovedBy FROM PROPERTY WHERE Owner = '" + user.getUsername() + "'");

            while (rs.next()) {

                int id = rs.getInt(9);

                ResultSet ra = server.createStatement().executeQuery("SELECT COUNT(P_id) FROM VISITS WHERE P_id = " + id);

                int pid = 0;

                if (ra.next()) {

                    pid = ra.getInt(1);

                }



                ResultSet rb = server.createStatement().executeQuery("SELECT avg(Rating) FROM VISITS WHERE P_id = " + id);

                double avgRating = 0.0;

                if (rb.next()) {

                    avgRating = Math.round((rb.getDouble(1)) * 10.0) / 10.0;

                }



                boolean isValid = true;

                if (rs.getString("ApprovedBy") == null) {

                    isValid = false;

                }

                data.add(new userPropDetails(rs.getString(1), rs.getString(2), rs.getString(3),

                        rs.getString(4), rs.getString(5), rs.getString(6),rs.getBoolean(7), rs.getBoolean(8),Integer.toString(rs.getInt(9) + 100000).substring(1),isValid, pid,  avgRating));

            }




            server.close();
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


        table.setItems(data);



    }



    public void createMenu() {

        searchMenu.setValue("Search by..");



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



    public static userPropDetails getSelectedOwnerProp() {

        return selectedOwnerProp;

    }



    public void openManage(ActionEvent actionEvent) {

        try {

            if (table.getSelectionModel().getSelectedItem() == null) {

                return;

            }





            selectedOwnerProp = (userPropDetails) table.getSelectionModel().getSelectedItem();

            String fxml = "property_management.fxml";

            if (selectedOwnerProp.getType().equals("FARM")) {

                fxml = "property_farm_management.fxml";

            }

            Parent root = FXMLLoader.load(getClass().getResource(fxml));

            Stage stage = (Stage) manageButton.getScene().getWindow();

            Scene scene = new Scene(root);



            stage.setScene(scene);

            stage.show();



        } catch(Exception e) {

            System.out.println("something went wrong + " + e.getMessage());



        }





    }



    public void showOther(ActionEvent actionEvent) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("other_owner_properties.fxml"));

            Stage stage = (Stage) manageButton.getScene().getWindow();

            Scene scene = new Scene(root);



            stage.setScene(scene);

            stage.show();



        } catch(Exception e) {

            System.out.println("something went wrong + " + e.getMessage());



        }

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



    public void pressAddProperty (ActionEvent actionEvent) throws IOException {

        sceneChanger(welcomeOwnerAdd, "add_new_property.fxml");

    }



    public void logOut(ActionEvent actionEvent) {

        try {

            sceneChanger(logo_btn, "page_login.fxml");

            user.setType(null);

            user.setUsername(null);

            user.setEmail(null);



        } catch(Exception e) {

            System.out.println("something went wrong + " + e.getMessage());



        }

    }
    public void sort(ActionEvent actionEvent) {
        loadDataFromDatabase();
    }


}