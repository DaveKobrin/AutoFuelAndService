/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin;


import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Timestamp;
import java.util.Date;



/**
 * FXML Controller class
 *
 * @author shdwk
 */
public class InsVehicleController  {
    private UIStateEngine uiState;
    private Vehicle currVehicle;
    private Vehicle newVehicle;
    private ObservableList<Vehicle> vehicleData;
    private final ArrayList<Object> data = new ArrayList<>();
    
    private static final String SQL_SELECT_ACTIVE_VEHICLES = "SELECT * FROM VEHICLE_TABLE WHERE IS_ACTIVE=TRUE";
    private static final String SQL_SELECT_ALL_VEHICLES = "Select * FROM VEHICLE_TABLE";
    private static final String SQL_INSERT_VEHICLE = "INSERT INTO VEHICLE_TABLE (VIN, MODEL_YEAR, MAKER, MODEL_NAME, TRIM_LEVEL, CURRENT_ODO, TIRE_SIZE, DISPLAY_NAME, IS_ACTIVE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_FUEL_EVENT = "INSERT INTO FUEL_EVENT (EVENT_TIME, ODOMETER, TOTAL_PRICE, NUM_GAL, IS_FULL_TANK) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_VEHICLE_FUEL = "INSERT INTO VEHICLE_FUEL_TABLE (VIN, EVENT_TIME) VALUES (?, ?)";
    private static final String SQL_UPDATE_VEHICLE = "UPDATE VEHICLE_TABLE SET MODEL_YEAR = ?, MAKER = ?, MODEL_NAME = ?, TRIM_LEVEL = ?, CURRENT_ODO = ?, TIRE_SIZE = ?, DISPLAY_NAME = ?, IS_ACTIVE = ? WHERE VIN=?";
    private static final String SQL_DELETE_VEHICLE = "UPDATE VEHICLE_TABLE SET IS_ACTIVE = FALSE WHERE VIN=?";
    private static final String SQL_RESTORE_VEHICLE = "UPDATE VEHICLE_TABLE SET IS_ACTIVE = TRUE WHERE VIN=?";

    //class to interact with db
    private final DBQueries dbQueries = new DBQueries();

    @FXML private BorderPane borderPane;
    @FXML private TextField textIDVIN;
    @FXML private TextField textIDYear;
    @FXML private TextField textIDMaker;
    @FXML private TextField textIDModel;
    @FXML private TextField textIDTrim;
    @FXML private TextField textIDOdometer;
    @FXML private TextField textIDTireSize;
    @FXML private TextField textIDDisplayName;
    
    private TableView<Vehicle> vehicleTableView;
    private TableColumn<Vehicle, String> vinCol;
    private TableColumn<Vehicle, Short> modelYearCol;
    private TableColumn<Vehicle, String> makerCol;
    private TableColumn<Vehicle, String> modelNameCol;
    private TableColumn<Vehicle, String> trimCol;
    private TableColumn<Vehicle, Integer> odometerCol;
    private TableColumn<Vehicle, String> tireCol;
    private TableColumn<Vehicle, String> nameCol;
    private TableColumn<Vehicle, Boolean> activeCol;


    public void initialize() {
        uiState = UIStateEngine.getInstance();
        vehicleTableView = new TableView<>();
        
        vehicleData = FXCollections.observableArrayList();
        currVehicle = new Vehicle();
        
        data.clear();
        LoadData(SQL_SELECT_ACTIVE_VEHICLES);
    }
    
    private void LoadData(String sql) {
        
        //testing
        Vehicle v = new Vehicle();

        vehicleData.clear();
        vehicleData.addAll(dbQueries.DBSelect(sql,data.toArray(),v));

        vehicleTableView = new TableView<>();
        
        nameCol = new TableColumn<>("Display Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        vehicleTableView.getColumns().add(nameCol);

        vinCol = new TableColumn<>("VIN");
        vinCol.setCellValueFactory(new PropertyValueFactory<>("vin"));
        vehicleTableView.getColumns().add(vinCol);
   
        modelYearCol = new TableColumn<>("Model Year");
        modelYearCol.setCellValueFactory(new PropertyValueFactory<>("modelYear"));
        vehicleTableView.getColumns().add(modelYearCol);
        
        makerCol = new TableColumn<>("Maker");
        makerCol.setCellValueFactory(new PropertyValueFactory<>("maker"));
        vehicleTableView.getColumns().add(makerCol);
        
        modelNameCol = new TableColumn<>("Model Name");
        modelNameCol.setCellValueFactory(new PropertyValueFactory<>("modelName"));
        vehicleTableView.getColumns().add(modelNameCol);
        
        trimCol = new TableColumn<>("Trim Level");
        trimCol.setCellValueFactory(new PropertyValueFactory<>("trimLevel"));
        vehicleTableView.getColumns().add(trimCol);
        
        odometerCol = new TableColumn<>("Odometer");
        odometerCol.setCellValueFactory(new PropertyValueFactory<>("odometer"));
        vehicleTableView.getColumns().add(odometerCol);
        
        tireCol = new TableColumn<>("Tire Size");
        tireCol.setCellValueFactory(new PropertyValueFactory<>("tireSize"));
        vehicleTableView.getColumns().add(tireCol);
        
        activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        vehicleTableView.getColumns().add(activeCol);
        
        vehicleTableView.setItems(vehicleData);
        vehicleTableView.getSelectionModel().selectedItemProperty().addListener((e) -> {
             var newV = new Vehicle(vehicleTableView.getSelectionModel().getSelectedItem());
             DisplayVehicle(newV);});
        this.borderPane.setCenter(this.vehicleTableView);
    }

   // display an Alert dialog
   private void displayAlert(
      AlertType type, String title, String message) {
      Alert alert = new Alert(type);
      alert.setTitle(title);
      alert.setContentText(message);
      alert.showAndWait();
   }
   
   private void DisplayVehicle(Vehicle v){
       textIDVIN.setText(v.getVin());
       textIDYear.setText(String.valueOf(v.getYear()));
       textIDMaker.setText(v.getMaker());
       textIDModel.setText(v.getModel());
       textIDTrim.setText(v.getTrim());
       textIDOdometer.setText(String.valueOf(v.getOdometer()));
       textIDTireSize.setText(v.getTireSize());
       textIDDisplayName.setText(v.getDisplayName());
       currVehicle.setVehicle(v.getVin(), v.getYear(), v.getMaker(), v.getModel(), 
               v.getTrim(),v.getOdometer(),v.getTireSize(),v.getDisplayName(), v.isActive());
   }
   
    @FXML
    void LoadDataButtonPressed(ActionEvent event){
        /**
         * Sets up the TableView to display the data from the VEHICLE_TABLE 
         */
        data.clear();
        LoadData(SQL_SELECT_ACTIVE_VEHICLES);

    }
    
    @FXML
    void InsertButtonPressed(ActionEvent event) {
        //validate data in text fields and insert into DB

        newVehicle = new Vehicle(textIDVIN.getText(),
                                (short)Integer.parseInt(textIDYear.getText()),
                                textIDMaker.getText(), 
                                textIDModel.getText(), 
                                textIDTrim.getText(), 
                                Integer.parseInt(textIDOdometer.getText()), 
                                textIDTireSize.getText(),
                                textIDDisplayName.getText(),
                                true);
        if(newVehicle.compareTo(currVehicle) != 0 && newVehicle.getVin().compareTo(currVehicle.getVin()) != 0) {
            data.clear();
            data.add(newVehicle.getVin());
            data.add(newVehicle.getYear());
            data.add(newVehicle.getMaker());
            data.add(newVehicle.getModel());
            data.add(newVehicle.getTrim());
            data.add(newVehicle.getOdometer());
            data.add(newVehicle.getTireSize());
            data.add(newVehicle.getDisplayName());
            data.add(newVehicle.isActive());
            int result = dbQueries.DBUpdate(SQL_INSERT_VEHICLE, data.toArray());
            if (result == 1) {
                Timestamp ts = new Timestamp(new Date().getTime());
                data.clear();
                data.add(ts);
                data.add(newVehicle.getOdometer());
                data.add(0.0d);
                data.add(0.0d);
                data.add(false);
                result = dbQueries.DBUpdate(SQL_INSERT_FUEL_EVENT, data.toArray());
                if (result == 1){
                    data.clear();
                    data.add(newVehicle.getVin());
                    data.add(ts);
                    result = dbQueries.DBUpdate(SQL_INSERT_VEHICLE_FUEL, data.toArray());
                    if (result == 1){
                        displayAlert(AlertType.INFORMATION, "Entry Added", "New vehicle successfully added.");
                    } else {
                        displayAlert(AlertType.ERROR, "Entry Partially Added", "Failed to insert VEHICLE_FUEL");
                    }
                } else {
                        displayAlert(AlertType.ERROR, "Entry Partially Added", "Failed to insert FUEL_EVENT");
                }
            } else {
                displayAlert(AlertType.ERROR, "Entry Not Added", "Unable to add entry");
            }
        } else {
            displayAlert(AlertType.INFORMATION, "Entry not Added", "Entry already exists");
        }
    }
    
    @FXML
    void UpdateButtonPressed(ActionEvent event){
        //validate data in text fields and insert into DB
        
        newVehicle = new Vehicle(textIDVIN.getText(),
                                (short)Integer.parseInt(textIDYear.getText()),
                                textIDMaker.getText(), 
                                textIDModel.getText(), 
                                textIDTrim.getText(), 
                                Integer.parseInt(textIDOdometer.getText()), 
                                textIDTireSize.getText(),
                                textIDDisplayName.getText(),
                                true);
        if(newVehicle.compareTo(currVehicle) != 0 && newVehicle.getVin().compareTo(currVehicle.getVin()) == 0) {
            data.clear();
            data.add(newVehicle.getYear());
            data.add(newVehicle.getMaker());
            data.add(newVehicle.getModel());
            data.add(newVehicle.getTrim());
            data.add(newVehicle.getOdometer());
            data.add(newVehicle.getTireSize());
            data.add(newVehicle.getDisplayName());
            data.add(newVehicle.isActive());
            data.add(currVehicle.getVin());
            int result = dbQueries.DBUpdate(SQL_UPDATE_VEHICLE, data.toArray());
            if (result == 1) {
                displayAlert(AlertType.INFORMATION, "Entry Updated", "Vehicle successfully updated.");
            } else {
                displayAlert(AlertType.ERROR, "Entry Not Updated", "Unable to update entry");
            }
        } else {
            displayAlert(AlertType.INFORMATION, "Entry not Updated", "No data were changed");
        }        
    }
    
    @FXML
    void DeleteButtonPressed(ActionEvent event){
            //validate data in text fields and insert into DB
        
        newVehicle = new Vehicle(textIDVIN.getText(),
                                (short)Integer.parseInt(textIDYear.getText()),
                                textIDMaker.getText(), 
                                textIDModel.getText(), 
                                textIDTrim.getText(), 
                                Integer.parseInt(textIDOdometer.getText()), 
                                textIDTireSize.getText(),
                                textIDDisplayName.getText(),
                                false);
        if(newVehicle.getVin().compareTo(currVehicle.getVin()) == 0) {
            data.clear();
            data.add(newVehicle.getVin());
            int result = dbQueries.DBUpdate(SQL_DELETE_VEHICLE, data.toArray());
            if (result == 1) {
                displayAlert(AlertType.INFORMATION, "Entry Deleted", "Vehicle successfully deleted.");
                ClearButtonPressed(event);
                LoadDataButtonPressed(event);
            } else {
                displayAlert(AlertType.ERROR, "Entry Not Deleted", "Unable to delete entry");
            }
        } else {
            displayAlert(AlertType.INFORMATION, "Entry not Added", "Entry already exists");
        }        
    }

    @FXML
    void ClearButtonPressed(ActionEvent event){
        textIDVIN.setText("");
        textIDYear.setText("");
        textIDMaker.setText("");
        textIDModel.setText("");
        textIDTrim.setText("");
        textIDOdometer.setText("");
        textIDTireSize.setText("");
        textIDDisplayName.setText("");
        currVehicle.setVehicle();
        textIDVIN.requestFocus();
    }

    @FXML
    void RestoreButtonPressed(ActionEvent event){
        //validate data in text fields and insert into DB  
        newVehicle = new Vehicle(textIDVIN.getText(),
                                (short)Integer.parseInt(textIDYear.getText()),
                                textIDMaker.getText(), 
                                textIDModel.getText(), 
                                textIDTrim.getText(), 
                                Integer.parseInt(textIDOdometer.getText()), 
                                textIDTireSize.getText(),
                                textIDDisplayName.getText(),
                                true);
        if(newVehicle.getVin().compareTo(currVehicle.getVin()) == 0 && !currVehicle.isActive()) {
            data.clear();
            data.add(newVehicle.getVin());
            int result = dbQueries.DBUpdate(SQL_RESTORE_VEHICLE, data.toArray());
            if (result == 1) {
                displayAlert(AlertType.INFORMATION, "Entry Restored", "Vehicle successfully restored.");
            } else {
                displayAlert(AlertType.ERROR, "Entry Not Restored", "Unable to restore entry");
            }
        } else {
            displayAlert(AlertType.INFORMATION, "Entry not Restored", "Vehicle already active");
        }
    }
    
    @FXML
    void LoadAllButtonPressed(ActionEvent event){
        data.clear();
        LoadData(SQL_SELECT_ALL_VEHICLES);
    }
    
    @FXML
    void FuelButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.INS_FUEL_EVENT);
        uiState.DisplayUI();
    }

    @FXML
    void HomeButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.SPLASH);
        uiState.DisplayUI();
    }

    @FXML
    void ServiceButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.INS_SERVICE_EVENT);
        uiState.DisplayUI();
    }

    @FXML
    void QueryButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.USER_QUERY);
        uiState.DisplayUI();
    }
}