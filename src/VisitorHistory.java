import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class VisitorHistory implements Initializable {
    @FXML
    public TableView History_Table;

    @FXML
    public Label History;

    @FXML
    public TableColumn Name_Col;

    @FXML
    public TableColumn Logged_Col;

    @FXML
    public TableColumn Rating_Col;

    @FXML
    public Button Back_Button;

    @FXML
    public Button Property_Details;






    public static int pid;


    String selected = null;

    private ObservableList<visitorHitoryDetails> data;
    User user = User.getInstance();
    public static String userst;
    //String


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
                    root = FXMLLoader.load(getClass().getResource("welcome_visitor.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    System.out.println("wrong");
                }
            }
        });


        Property_Details.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (History_Table.getSelectionModel().getSelectedItem() == null) {
                    return;
                }
                    try {
                        //pid
                        Stage stage;
                        Parent root;
                        stage = (Stage) Property_Details.getScene().getWindow();
                        root = FXMLLoader.load(getClass().getResource("visit_details.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e) {
                        System.out.println("wrong");
                    }
                }

//                OtherOwnerProperties ab = new OtherOwnerProperties();
//                ab.selectedUser =  (userPropDetails) History_Table.getSelectionModel().getSelectedItem();
//                System.out.println(ab.selectedUser.getPropName());
//                Stage stage;
//                Parent root = null;
//                stage = (Stage) Property_Details.getScene().getWindow();
//                try {
//                    root = FXMLLoader.load(getClass().getResource("visit_details.fxml"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//            }
        });

        //MAKE SURE TO CONNECT TO VIEW PROPERTY DETAILS WITH SELECTED PULLED UP


        History_Table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
        @Override
        public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
            //Check whether item is selected and set value of selected item to Label
            if(History_Table.getSelectionModel().getSelectedItem() != null)
            {
                TableView.TableViewSelectionModel selectionModel = History_Table.getSelectionModel();
                Connection server = Connect.SQLConnecter.connect();


                Object selectedItems = History_Table.getSelectionModel().getSelectedItems().get(0);
                String first_Column = selectedItems.toString().split(",")[0];
                //System.out.println(first_Column);
                selected = first_Column;
                System.out.println(selected);


                //pid = selected;
                try {
                    ResultSet result2 = server.createStatement().executeQuery("SELECT ID FROM PROPERTY WHERE Name = '" + selected + "'");
                    if (result2.next()) {
                        selected = result2.getString(1);
                    }


                    ResultSet result = server.createStatement().executeQuery("SELECT P_id FROM VISITS WHERE  VISITS.P_id = '" + selected + "'");
                    if (result.next()) {
                        pid = result.getInt(1);
                    }
                    System.out.println(pid);
                } catch (Exception e) {e.printStackTrace();}




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

            ResultSet rs = server.createStatement().executeQuery("SELECT PROPERTY.Name,  VISITS.Date, VISITS.Rating FROM PROPERTY, VISITS WHERE VISITS.Rating >= 0 && PROPERTY.id = VISITS.P_id AND VISITS.Username =  '" + user.getUsername() + "'");
            userst = user.getUsername();
            //ResultSet result = server.createStatement().executeQuery("SELECT P_id FROM VISITS WHERE VISITS.Rating >= 0 && PROPERTY.id = VISITS.P_id AND VISITS.Username =  '" + user.getUsername() + "'");
            while (rs.next()) {

                int id = rs.getInt(3);

                //ResultSet ra = server.createStatement().executeQuery("SELECT COUNT(P_id) FROM VISITS WHERE P_id = 5");

                //System.out.println("GETTTINGGG" + ra.getInt(1));





                data.add(new visitorHitoryDetails(rs.getString(1), rs.getString(2), rs.getInt(3)));

            }







        } catch(Exception e) {

            System.out.println("something went wrong + " + e.getMessage());



        }

        //Set cell value factory to tableview.

        //NB.PropertyValue Factory must be the same with the one set in model class.

        Name_Col.setCellValueFactory(new PropertyValueFactory<>("name"));

        Logged_Col.setCellValueFactory(new PropertyValueFactory<>("date"));

        Rating_Col.setCellValueFactory(new PropertyValueFactory<>("rating"));






        History_Table.setItems(null);

        System.out.println("Should be adding");

        System.out.println(data.size());

        History_Table.setItems(data);



    }






}
