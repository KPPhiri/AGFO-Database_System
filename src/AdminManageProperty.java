import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.beans.Visibility;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class AdminManageProperty implements Initializable {

    @FXML
    private TextField namefield;
    @FXML
    private TextField addressfield;
    @FXML
    private TextField cityfield;
    @FXML
    private TextField zipfield;
    @FXML
    private TextField sizefield;
    @FXML
    private ComboBox animalcombo;
    @FXML
    private ComboBox selectanimalcombo;
    @FXML
    private ComboBox cropcombo;
    @FXML
    private ComboBox selectcropcombo;
    @FXML
    private ComboBox publiccombo;
    @FXML
    private ComboBox commercialcombo;
    @FXML
    private Label manageproplabel;
    @FXML
    private Label typelabel;
    @FXML
    private Label idlabel;
    @FXML
    private Label animalslabel;
    @FXML
    private Label addanimalslabel;
    @FXML
    private Button deleteanimalbut;
    @FXML
    private Button addanimalbut;
    @FXML
    private Button deletecropbut;
    @FXML
    private Button addcropbut;
    @FXML
    private Button deletebut;
    @FXML
    private Button savechangebut;
    @FXML
    private Button backbut;

    User currentUser = User.getInstance();

    private ArrayList<String> approvedCropItems = new ArrayList<>();
    private ArrayList<String> approvedAnimalItems = new ArrayList<>();
    private ArrayList<String> hasAnimalItems = new ArrayList<>();
    private ArrayList<String> hasCroplItems = new ArrayList<>();
    private ArrayList<String> bothCropandAnimal = new ArrayList<>();

    public String propname;
    public String propaddress;
    public String propcity;
    public String propzip;
    public String propsize;
    public String proptype;
    public Boolean propispublic;
    public Boolean propiscommercial;
    public String propId;
    public int wheretheycame = 1;


    public void initialize(URL location, ResourceBundle resources) {
        setvariables();
        createSpinners();
        loadDataFields();
        hideDataFields();
        populatecropsandanimals();
        if (hasCroplItems.size() > 0) {
            cropcombo.setValue(hasCroplItems.get(0));
        }
        if (hasAnimalItems.size() > 0) {
            animalcombo.setValue(hasAnimalItems.get(0));
        }
    }

    public void createSpinners() {
        publiccombo.getItems().addAll(true,false);
        commercialcombo.getItems().addAll(true,false);
    }

    public void loadDataFields() {
        manageproplabel.setText("Manage " + propname);
        namefield.setText(propname);
        addressfield.setText(propaddress);
        cityfield.setText(propcity);
        zipfield.setText(propzip);
        sizefield.setText(propsize);
        typelabel.setText(proptype);
        publiccombo.setValue(propispublic);
        commercialcombo.setValue(propiscommercial);
        idlabel.setText(propId);
    }
    public void setvariables() {
        if(ConfirmedProperties.getselectedOwnerPropConfirmed()!=null){
            confirmedPropDetails current = ConfirmedProperties.getselectedOwnerPropConfirmed();
            wheretheycame = 1;
            propname=current.getName();
            propaddress=current.getAddress();
            propcity=current.getCity();
            propzip=current.getZip();
            propsize=String.valueOf(current.getSize());
            proptype=current.getType();
            propispublic = current.getIspublic();
            propiscommercial = current.getIscommercial();
            propId = current.getId();
        }
        if(UnconfirmedProperties.getselectedOwnerPropUnconfirmed() != null){
            unconfirmedPropDetails current = UnconfirmedProperties.getselectedOwnerPropUnconfirmed();
            wheretheycame = 0;
            propname=current.getName();
            propaddress=current.getAddress();
            propcity=current.getCity();
            propzip=current.getZip();
            propsize=String.valueOf(current.getSize());
            proptype=current.getType();
            propispublic = current.getIspublic();
            propiscommercial = current.getIscommercial();
            propId = current.getId();
        }
    }
    public void hideDataFields(){
        if((proptype.toLowerCase().contains("garden"))||(proptype.toLowerCase().contains("orchard"))){
            deleteanimalbut.setVisible(false);
            animalcombo.setVisible(false);
            addanimalbut.setVisible(false);
            animalslabel.setVisible(false);
            addanimalslabel.setVisible(false);
            selectanimalcombo.setVisible(false);
        }
    }
    public void populatecropsandanimals(){
        getExistingCrops();
        getExistingAnimals();
        loadApprovedCrops();
        if(proptype.toLowerCase().contains("farm")){
            loadApprovedAnimals();
        }
    }
    public void loadApprovedCrops(){
        try{
            System.out.println("WORKING ON GETTING CROPS");
            Connection server = Connect.SQLConnecter.connect();
            String query = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND (Type = 'FRUIT' OR Type = 'NUT' OR Type = 'FLOWER' OR Type = 'VEGETABLE')";
            if (proptype.toLowerCase().contains("garden")) {
                System.out.println("ITS A GARDEN!");
                query = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND (Type = 'FLOWER' OR Type = 'VEGETABLE')";
            }
            if (proptype.toLowerCase().contains("orchard")) {
                System.out.println("ITS A Orchard!");
                query = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND (Type = 'FRUIT' OR Type = 'NUT')";
            }
             ResultSet rs = server.createStatement().executeQuery(query);
            while (rs.next() && !approvedCropItems.contains(rs.getString("Name"))) {
                System.out.println("I'm Runnning in the RS LOOP");
                approvedCropItems.add(rs.getString("Name"));
            }
            selectcropcombo.setItems(FXCollections.observableArrayList(approvedCropItems));
        }
        catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }

    public void loadApprovedAnimals(){
        try{
            System.out.println("WORKING ON GETTING Animals");
            Connection server = Connect.SQLConnecter.connect();
            String query = "SELECT Name FROM FARM_ITEM WHERE isApproved = 1 AND TYPE = 'ANIMAL'";
            ResultSet rs = server.createStatement().executeQuery(query);
            while (rs.next() && !approvedAnimalItems.contains(rs.getString("Name"))) {
                System.out.println("I'm Runnning in the RS LOOP");
                approvedAnimalItems.add(rs.getString("Name"));
            }
            selectanimalcombo.setItems(FXCollections.observableArrayList(approvedAnimalItems));
        }
        catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }

    public void getExistingCrops() {
        try{
            System.out.println("Working on getting existing Crops");
            Connection server = Connect.SQLConnecter.connect();
            String query = "SELECT Item FROM HAS, FARM_ITEM WHERE Item=Name AND P_id = '" + propId + "'AND (Type = 'FRUIT' OR Type = 'NUT' OR Type = 'FLOWER' OR Type = 'VEGETABLE')";
            ResultSet rs = server.createStatement().executeQuery(query);
            while (rs.next() && !hasCroplItems.contains(rs.getString("Item"))) {
                hasCroplItems.add(rs.getString("Item"));
            }
            cropcombo.setItems(FXCollections.observableArrayList(hasCroplItems));
        }
        catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }

    public void getExistingAnimals() {
        try{
            System.out.println("Working on getting existing Animals");
            Connection server = Connect.SQLConnecter.connect();
            String query = "SELECT Item FROM HAS, FARM_ITEM WHERE Item=Name AND P_id = '" + propId + "'AND Type = 'ANIMAL' ";
            ResultSet rs = server.createStatement().executeQuery(query);
            while (rs.next() && !hasAnimalItems.contains(rs.getString("Item"))) {
                hasAnimalItems.add(rs.getString("Item"));
            }
            animalcombo.setItems(FXCollections.observableArrayList(hasAnimalItems));
        }
        catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }
    public void addNewApprovedCrop() {
        System.out.println("You Clicked the Add Crop Button");
        if (hasCroplItems.contains((selectcropcombo.getValue()))) {
            System.out.println("no Bueno");
            return;
        }
        hasCroplItems.add((String)selectcropcombo.getValue());
        cropcombo.getItems().addAll((String)selectcropcombo.getValue());
    }
    public void addNewApprovedAnimal() {
        System.out.println("You Clicked the Add Animal Button");
        if (hasAnimalItems.contains((selectanimalcombo.getValue()))) {
            System.out.println("no Bueno");
            return;
        }
        hasAnimalItems.add((String)selectanimalcombo.getValue());
        animalcombo.getItems().addAll((String)selectanimalcombo.getValue());
    }
    public void removeApprovedCrop() {
        System.out.println("You Clicked the Crop Delete Button");
        hasCroplItems.remove(cropcombo.getValue());
        cropcombo.getItems().remove(cropcombo.getValue());
    }
    public void removeApprovedAnimal() {
        System.out.println("You Clicked the Animal Delete Button");
        hasAnimalItems.remove(animalcombo.getValue());
        animalcombo.getItems().remove(animalcombo.getValue());
    }

    public void deleteProperty() {
        try {
            System.out.println("Deleting Property");
            Connection server = Connect.SQLConnecter.connect();
            server.createStatement().executeUpdate("DELETE FROM PROPERTY WHERE ID= '" + propId + "'");
            backfromwheretheycame();
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }

    }
    public void backfromwheretheycame() {
        if(wheretheycame == 1){
            goToConfirmed();
        }
            goToUnconfirmed();
    }
    private void goToConfirmed(){
        Stage stage;
        Parent root = null;
        stage = (Stage) backbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("confirmed_properties.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void goToUnconfirmed(){
        Stage stage;
        Parent root = null;
        stage = (Stage) backbut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("unconfirmed_properties.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void saveProperty() {
        try {
            System.out.println("Saving Property");
            Connection server = Connect.SQLConnecter.connect();
            server.createStatement().executeUpdate("DELETE FROM HAS WHERE P_id= '"+ propId +"'");


            bothCropandAnimal.addAll(hasCroplItems);
            bothCropandAnimal.addAll(hasAnimalItems);
            for (String s: bothCropandAnimal) {
                server.createStatement().executeUpdate("INSERT INTO HAS (P_id, Item) VALUES(" + propId + ", '" + s + "')");
            }

            server.createStatement().executeUpdate("UPDATE PROPERTY SET Name = '"+ namefield.getText() +"', Address = '" + addressfield.getText() + "', City = '" + cityfield.getText() + "', Zip = '" + zipfield.getText() + "',  Acres = '" + sizefield.getText() + "', IsPublic = " + publiccombo.getValue() + ", IsCommercial = " + commercialcombo.getValue() + " WHERE ID = "+ propId + "");
            server.createStatement().executeUpdate("UPDATE PROPERTY SET ApprovedBy = '" + currentUser.getUsername() + "' WHERE ID = "+ propId + "");
            goToConfirmed();
        } catch (Exception e) {
            System.out.println("something went wrong + " + e.getMessage());
        }
    }


}
