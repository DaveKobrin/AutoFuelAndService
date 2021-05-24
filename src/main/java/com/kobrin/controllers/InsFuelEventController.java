/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin.controllers;



import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;
import java.time.ZoneOffset;

import java.util.ArrayList;

import com.kobrin.DBQueries;
import com.kobrin.UIStateEngine;
import com.kobrin.dataModels.FuelEvent;
import com.kobrin.dataModels.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.RowFilter;           
//import javax.swing.table.TableModel;    
//import javax.swing.table.TableRowSorter;
//import java.util.regex.PatternSyntaxException;
//import javafx.scene.control.TextArea;
//import javafx.embed.swing.SwingNode;

/**
 * FXML Controller class
 *
 * @author shdwk
 */
public class InsFuelEventController  {
    private UIStateEngine uiState;
    private FuelEvent currFuelEvent;
    private Vehicle currVehicle;
    private ObservableList<String> displayNames;
    private final DBQueries dbQueries = DBQueries.get();
    private final ArrayList<Object> data = new ArrayList<>();
    private final ArrayList<Vehicle> vehicles = new ArrayList<>();
    private ObservableList<FuelEvent> fuelData;
    
    @FXML private BorderPane borderPane;
    @FXML private ComboBox<String> vehicleComboID;
    @FXML private DatePicker eventDatePickID;
    @FXML private TextField odometerTextID;
    @FXML private TextField totalPriceTextID;
    @FXML private TextField numGallonsTextID;
    @FXML private CheckBox filledTankCheckID;
    @FXML private TextField overallEcoID;
    @FXML private TextField lastFillEcoID;


    private TableView<FuelEvent> fuelTableView;
    private TableColumn<FuelEvent, String> eventTimeCol;
    private TableColumn<FuelEvent, Integer> odometerCol;
    private TableColumn<FuelEvent, Float> priceCol;
    private TableColumn<FuelEvent, Float> gallonsCol;
    private TableColumn<FuelEvent, Boolean> filledCol;
    private TableColumn<FuelEvent, Float> pricePerGalCol;

    private static final String SQL_SELECT_USER_VEHICLES = "SELECT * FROM VEHICLE_TABLE WHERE IS_ACTIVE=TRUE AND USER_ID = ? ORDER BY VIN";
    private static final String SQL_SELECT_ALL_VEHICLES = "SELECT * FROM VEHICLE_TABLE WHERE IS_ACTIVE=TRUE ORDER BY VIN";
    private static final String SQL_SELECT_FUEL_EVENTS = "SELECT * FROM FUEL_EVENT WHERE VIN = ? ORDER BY EVENT_TIME";
    private static final String SQL_INSERT_FUEL_EVENT = "INSERT INTO FUEL_EVENT (VIN, EVENT_TIME, ODOMETER, TOTAL_PRICE, NUM_GAL, IS_FULL_TANK) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_FUEL_EVENT = "UPDATE FUEL_EVENT SET ODOMETER = ?, TOTAL_PRICE = ?, NUM_GAL = ?, IS_FULL_TANK = ? WHERE EVENT_TIME = ? AND VIN = ?";
    private static final String SQL_DELETE_FUEL_EVENT = "DELETE FROM FUEL_EVENT WHERE EVENT_TIME = ? AND VIN = ?";

    @FXML
    public void initialize() {
        uiState = UIStateEngine.getInstance();
        data.clear();
        currVehicle = new Vehicle();
        currFuelEvent = new FuelEvent();
        vehicles.clear();
        String sql = SQL_SELECT_ALL_VEHICLES;
        if (!uiState.isAdmin()) {
            data.add(uiState.getUserID());
            sql = SQL_SELECT_USER_VEHICLES;
        }
        vehicles.addAll(dbQueries.DBSelect(sql, data.toArray(), currVehicle));
        displayNames = FXCollections.observableArrayList();
        fuelData = FXCollections.observableArrayList();
        for(Vehicle v : vehicles) {
            displayNames.add(v.getDisplayName());
        }
        vehicleComboID.setItems(displayNames);
        vehicleComboID.valueProperty().addListener((ObservableValue<? extends String> ov, String old_v, String new_v)->{
            currVehicle = vehicles.get(vehicleComboID.getSelectionModel().getSelectedIndex());
            data.clear();
            data.add(currVehicle.getVin());
            LoadData(SQL_SELECT_FUEL_EVENTS);
        });
        vehicleComboID.getSelectionModel().selectFirst();
        
    }
    
    private void LoadData(String sql) {
        fuelData.clear();
        fuelData.addAll(dbQueries.DBSelect(sql,data.toArray(),currFuelEvent));

        fuelTableView = new TableView<>();
        
        eventTimeCol = new TableColumn<>("Fuel Event Time");
        eventTimeCol.setCellValueFactory(new PropertyValueFactory<>("eventTimestamp"));
        fuelTableView.getColumns().add(eventTimeCol);
        
        odometerCol = new TableColumn<>("Odometer");
        odometerCol.setCellValueFactory(new PropertyValueFactory<>("odometer"));
        fuelTableView.getColumns().add(odometerCol);
        
        priceCol = new TableColumn<>("Total Cost");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        fuelTableView.getColumns().add(priceCol);
   
        gallonsCol = new TableColumn<>("Gallons");
        gallonsCol.setCellValueFactory(new PropertyValueFactory<>("numGallons"));
        fuelTableView.getColumns().add(gallonsCol);
        
        filledCol = new TableColumn<>("Full Tank");
        filledCol.setCellValueFactory(new PropertyValueFactory<>("filledTank"));
        fuelTableView.getColumns().add(filledCol);
        
        pricePerGalCol = new TableColumn<>("$/Gallon");
        pricePerGalCol.setCellValueFactory(new PropertyValueFactory<>("pricePerGal"));
        fuelTableView.getColumns().add(pricePerGalCol);
        
        fuelTableView.setItems(fuelData);
        fuelTableView.getSelectionModel().selectedItemProperty().addListener((e) -> {
             var newFE = new FuelEvent(fuelTableView.getSelectionModel().getSelectedItem());
             DisplayFuelEvent(newFE);});
        this.borderPane.setCenter(this.fuelTableView);
        //  any time fuel data changes recalc fuel eco
        CalcFuelEco();
    }
    
    private void DisplayFuelEvent(FuelEvent FE){
        eventDatePickID.setValue(FE.getEventTimestamp().toLocalDateTime().toLocalDate());   
        odometerTextID.setText(((Integer)FE.getOdometer()).toString());
        totalPriceTextID.setText(((Float)FE.getTotalPrice()).toString());
        numGallonsTextID.setText(((Float)FE.getNumGallons()).toString());
        filledTankCheckID.setSelected(FE.isFilledTank());

        currFuelEvent.setFuelEvent(FE.getVin(),FE.getEventTime(), FE.getOdometer(), FE.getTotalPrice(), FE.getNumGallons(), FE.isFilledTank());
   }

    private void CalcFuelEco(){
        //if there are at least 2 full tanks in fuelData then calc fuel economy info
        ArrayList<Integer> milesBetweenFullTanks = new ArrayList<>();
        ArrayList<Float> gallonsBetweenFullTanks = new ArrayList<>();
        boolean hasBeenFilled = false;

        int lastMiles = 0;
        int miles;
        float gallons = 0.0f;
        
        for (FuelEvent e : fuelData){
            gallons += e.getNumGallons();
           
            if(e.isFilledTank()) {
                if (hasBeenFilled){
                    miles = e.getOdometer() - lastMiles;
                    milesBetweenFullTanks.add(miles);
                    gallonsBetweenFullTanks.add(gallons);
                    lastMiles = e.getOdometer();
                    gallons = 0.0f;
                } else {
                    hasBeenFilled = true;
                    lastMiles = e.getOdometer();
                    gallons = 0.0f;
                }
            }
        }
        
        miles = 0;
        gallons = 0.0f;
        lastFillEcoID.setText("");
        overallEcoID.setText("");
        
        for(int i = 0; i < milesBetweenFullTanks.size(); ++i){
            miles += milesBetweenFullTanks.get(i);
            gallons += gallonsBetweenFullTanks.get(i);
            float result = (float)milesBetweenFullTanks.get(i) / gallonsBetweenFullTanks.get(i);
            lastFillEcoID.setText(Float.toString(result));
            result = (float)miles / gallons;
            overallEcoID.setText(Float.toString(result));
        }
    }

   // display an Alert dialog
   private void displayAlert(
      AlertType type, String title, String message) {
      Alert alert = new Alert(type);
      alert.setTitle(title);
      alert.setContentText(message);
      alert.showAndWait();
   }
   
    @FXML
    void ClearButtonPressed(ActionEvent event) {
        eventDatePickID.setValue(new Timestamp(new Date().getTime()).toLocalDateTime().toLocalDate());   
        odometerTextID.clear();
        totalPriceTextID.clear();
        numGallonsTextID.clear();
        filledTankCheckID.setSelected(true);
    }
    
    /**
     * find the next chronologically later fuel event and returns its index
     * if it does not exist returns -1
     * @param fe FuelEvent of interest
     * @return index of next chronologically later FuelEvent or -1 if not found
     */
    private int getUpperIdx(FuelEvent fe) {
        for(int i = 0; i < fuelData.size(); ++i) {
            if(fuelData.get(i).getEventTimestamp().compareTo(fe.getEventTimestamp()) > 0)
                return i;
        }
        return (-1);
    }
    
    /**
     * find the next chronologically earlier fuel event and return its index
     * if it does not exist returns -1
     * @param fe FuelEvent of interest
     * @return index of next chronologically earlier FuelEvent or -1 if not found
     */
    private int getLowerIdx(FuelEvent fe) {
        for(int i = fuelData.size() -1 ; i >= 0 ; --i) {
            if(fuelData.get(i).getEventTimestamp().compareTo(fe.getEventTimestamp()) < 0)
                return i;
        }
        return (-1);
    }
    
    @FXML
    void InsertButtonPressed(ActionEvent event) {
        //todo change dbqueries to allow transactional proccessing (external commit control)

//      entry into FUEL_EVENT table
        FuelEvent FE = new FuelEvent(
                currVehicle.getVin(),
                currFuelEvent.getEventTimestamp(),
                Integer.parseInt(odometerTextID.getText()),
                Float.parseFloat(totalPriceTextID.getText()),
                Float.parseFloat(numGallonsTextID.getText()),
                filledTankCheckID.isSelected());
       
        if(FE.compareTo(this.currFuelEvent) != 0 ){
            FE.setEventTimestamp(new Timestamp(1000*eventDatePickID.getValue().toEpochSecond(LocalDateTime.now().toLocalTime(), 
                        ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()))));
            
            int i = getUpperIdx(FE);
            if((i != -1) && fuelData.get(i).getOdometer() < FE.getOdometer()){
                displayAlert(AlertType.ERROR, "Entry Not Added", "Error in input - event date or mileage incorrect");
                return;
            }
            i = getLowerIdx(FE);
            if((i != -1) && fuelData.get(i).getOdometer() > FE.getOdometer()){
                displayAlert(AlertType.ERROR, "Entry Not Added", "Error in input - event date or mileage incorrect");
                return;
            }
            
            data.clear();
            data.add(FE.getVin());
            data.add(FE.getEventTimestamp());
            data.add(FE.getOdometer());
            data.add(FE.getTotalPrice());
            data.add(FE.getNumGallons());
            data.add(FE.isFilledTank());
            
            int result = dbQueries.DBUpdate(SQL_INSERT_FUEL_EVENT, data.toArray());

            if (result == 1) {
////              entry into VEHICLE_FUEL_TABLE
//                data.clear();
//                data.add(this.currVehicle.getVin());
//                data.add(FE.getEventTimestamp());
//
//                result = dbQueries.DBUpdate(SQL_INSERT_VEHICLE_FUEL, data.toArray());
//
//                if (result == 1){
                    //update fuel event table to include new entry
                    data.clear();
                    data.add(currVehicle.getVin());
                    LoadData(SQL_SELECT_FUEL_EVENTS);
                    displayAlert(AlertType.INFORMATION, "Entry Added", "New fuel event successfully added.");
//                } else {
//                    displayAlert(AlertType.ERROR, "Entry Not Added", "Entry added to fuel_event but failed in vehicle_fuel_table");
//                }
            } else {
                displayAlert(AlertType.ERROR, "Entry Not Added", "Unable to add entry");
            }
        } else {
            displayAlert(AlertType.INFORMATION, "Entry not Added", "Entry already exists");
        }
                

    }
    
    @FXML
    void UpdateButtonPressed(ActionEvent event) {
        //      entry into FUEL_EVENT table
        FuelEvent FE = new FuelEvent(
                currVehicle.getVin(),
                currFuelEvent.getEventTimestamp(),
                Integer.parseInt(odometerTextID.getText()),
                Float.parseFloat(totalPriceTextID.getText()),
                Float.parseFloat(numGallonsTextID.getText()),
                filledTankCheckID.isSelected());
        
        if(FE.compareTo(this.currFuelEvent) != 0 ){
            
            int i = getUpperIdx(FE);
            if((i != -1) && fuelData.get(i).getOdometer() < FE.getOdometer()){
                displayAlert(AlertType.ERROR, "Entry Not Updated", "Error in input - mileage incorrect");
                return;
            }
            i = getLowerIdx(FE);
            if((i != -1) && fuelData.get(i).getOdometer() > FE.getOdometer()){
                displayAlert(AlertType.ERROR, "Entry Not Updated", "Error in input - mileage incorrect");
                return;
            }
            
            data.clear();
            data.add(FE.getOdometer());
            data.add(FE.getTotalPrice());
            data.add(FE.getNumGallons());
            data.add(FE.isFilledTank());
            data.add(FE.getEventTimestamp());
            data.add(FE.getVin());

            int result = dbQueries.DBUpdate(SQL_UPDATE_FUEL_EVENT, data.toArray());

            if (result == 1) {
                //update fuel event table to include new entry
                data.clear();
                data.add(currVehicle.getVin());
                LoadData(SQL_SELECT_FUEL_EVENTS);
                displayAlert(AlertType.INFORMATION, "Entry Updated", "Fuel Event successfully updated.");
            } else {
                displayAlert(AlertType.ERROR, "Entry Not Updated", "Unable to update Fuel_Event");
            }
        } else {
            displayAlert(AlertType.INFORMATION, "Entry Not Updated", "No fields changed");
        }
    }
    
    @FXML
    void DeleteButtonPressed(ActionEvent event) {
        data.clear();
        data.add(currFuelEvent.getEventTimestamp());
        data.add(currFuelEvent.getVin());
//        int result = dbQueries.DBUpdate(SQL_DELETE_VEHICLE_FUEL, data.toArray());
//        if (result == 1) {
        int result = dbQueries.DBUpdate(SQL_DELETE_FUEL_EVENT, data.toArray());
        if (result == 1) {
            displayAlert(AlertType.INFORMATION, "Entry Deleted", "Fuel Event successfully deleted.");
//        }else
//            displayAlert(AlertType.ERROR, "Entry Partly Deleted", "Fuel Event deleted, but vehicle fuel failed");
        }else
                displayAlert(AlertType.ERROR, "Entry Not Deleted", "Fuel Event not deleted");
    }
  
    @FXML
    void QueryButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.USER_QUERY);
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
    void VehicleButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.INS_VEHICLE);
        uiState.DisplayUI();
    }
}
