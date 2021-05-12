/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin.controllers;


import java.util.ArrayList;

import com.kobrin.DBQueries;
import com.kobrin.UIStateEngine;
import com.kobrin.dataModels.Vehicle;
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
    
    private static final String SQL_SELECT_ACTIVE_USER_VEHICLES = "SELECT * FROM VEHICLE_TABLE WHERE IS_ACTIVE=TRUE AND USER_ID = ?";
    private static final String SQL_SELECT_ALL_USER_VEHICLES = "Select * FROM VEHICLE_TABLE WHERE USER_ID = ?";
    private static final String SQL_SELECT_ACTIVE_ADMIN_VEHICLES = "SELECT * FROM VEHICLE_TABLE WHERE IS_ACTIVE=TRUE";
    private static final String SQL_SELECT_ALL_ADMIN_VEHICLES = "Select * FROM VEHICLE_TABLE";
    private static final String SQL_INSERT_VEHICLE = "INSERT INTO VEHICLE_TABLE (VIN, MODEL_YEAR, MAKER, MODEL_NAME, TRIM_LEVEL, CURRENT_ODO, TIRE_SIZE, DISPLAY_NAME, IS_ACTIVE, USER_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_FUEL_EVENT = "INSERT INTO FUEL_EVENT (VIN, EVENT_TIME, ODOMETER, TOTAL_PRICE, NUM_GAL, IS_FULL_TANK) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_VEHICLE = "UPDATE VEHICLE_TABLE SET MODEL_YEAR = ?, MAKER = ?, MODEL_NAME = ?, TRIM_LEVEL = ?, CURRENT_ODO = ?, TIRE_SIZE = ?, DISPLAY_NAME = ?, IS_ACTIVE = ?, USER_ID = ? WHERE VIN=?";
    private static final String SQL_DELETE_VEHICLE = "UPDATE VEHICLE_TABLE SET IS_ACTIVE = FALSE WHERE VIN = ? AND USER_ID = ?";
    private static final String SQL_RESTORE_VEHICLE = "UPDATE VEHICLE_TABLE SET IS_ACTIVE = TRUE WHERE VIN = ? AND USER_ID = ?";

    //class to interact with db
    private final DBQueries dbQueries = DBQueries.get();

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


    @FXML
    public void initialize() {
        uiState = UIStateEngine.getInstance();
        vehicleTableView = new TableView<>();
        
        vehicleData = FXCollections.observableArrayList();
        currVehicle = new Vehicle();
        

        LoadData(true);
    }
    
    private void LoadData(boolean activeOnly) {
        String sql;
        data.clear();

        if (uiState.isAdmin()){
            if (activeOnly){
                sql = SQL_SELECT_ACTIVE_ADMIN_VEHICLES;
            } else {
                sql = SQL_SELECT_ALL_ADMIN_VEHICLES;
            }
        } else {
            data.add(uiState.getUserID());
            if (activeOnly){
                sql = SQL_SELECT_ACTIVE_USER_VEHICLES;
            } else {
                sql = SQL_SELECT_ALL_USER_VEHICLES;
            }
        }
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
       currVehicle.setVehicle(v.getVin(), v.getYear(), v.getMaker(), v.getModel(), v.getTrim(),
               v.getOdometer(),v.getTireSize(),v.getDisplayName(), v.isActive(), v.getUserId());
   }

    /**
     * Sets up the TableView to display the data from the VEHICLE_TABLE
     */
    @FXML
    void LoadDataButtonPressed(ActionEvent event){
        data.clear();
        LoadData(true);

    }
    
    @FXML
    void InsertButtonPressed(ActionEvent event) {
        if (uiState.isAdmin()) {
            displayAlert(AlertType.WARNING, "Cannot Insert", "Admin accounts not permitted to insert new vehicles.");
            return;
        }

        //validate data in text fields and insert into DB

        newVehicle = new Vehicle(textIDVIN.getText(),
                                (short)Integer.parseInt(textIDYear.getText()),
                                textIDMaker.getText(), 
                                textIDModel.getText(), 
                                textIDTrim.getText(), 
                                Integer.parseInt(textIDOdometer.getText()), 
                                textIDTireSize.getText(),
                                textIDDisplayName.getText(),
                                true,
                                uiState.getUserID());

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
            data.add(newVehicle.getUserId());

            int result = dbQueries.DBUpdate(SQL_INSERT_VEHICLE, data.toArray());
            if (result == 1) {
                Timestamp ts = new Timestamp(new Date().getTime());
                data.clear();
                data.add(newVehicle.getVin());
                data.add(ts);
                data.add(newVehicle.getOdometer());
                data.add(0.0f);
                data.add(0.0f);
                data.add(false);
                result = dbQueries.DBUpdate(SQL_INSERT_FUEL_EVENT, data.toArray());
                if (result == 1){
                    displayAlert(AlertType.INFORMATION, "Entry Added", "New vehicle successfully added.");
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
                                true,
                                uiState.getUserID());

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
            data.add(currVehicle.getUserId());
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
                                false,
                                uiState.getUserID());

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
                                true,
                                uiState.getUserID());

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
        LoadData(false);
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