package com.kobrin.controllers;

import com.kobrin.DBQueries;
import com.kobrin.DBUtility;
import com.kobrin.UIStateEngine;
import com.kobrin.dataModels.FuelEvent;
import com.kobrin.dataModels.ServiceEvent;
import com.kobrin.dataModels.ServiceItem;
import com.kobrin.dataModels.Vehicle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.sql.Date;
import java.util.ArrayList;

public class InsServiceEventController {
    private UIStateEngine uiState;
    private ServiceEvent currServEvent;
    private ServiceItem currServItem;
    private Vehicle currVehicle;
    private ObservableList<String> displayNames;
    private final DBQueries dbQueries = DBQueries.get();
    private final ArrayList<Object> data = new ArrayList<>();
    private final ArrayList<Vehicle> vehicles = new ArrayList<>();
    private ObservableList<ServiceEvent> serviceEventData;
    private ObservableList<ServiceItem> serviceItemData;

    @FXML private BorderPane borderPane;
    @FXML private ComboBox<String> vehicleComboID;
    @FXML private DatePicker eventDatePickID;
    @FXML private TextField odometerTextID;
    @FXML private TextField locationTextID;
    @FXML private Pane serviceItemPaneID;
    @FXML private TextField servItemDescTextID;
    @FXML private TextField servItemCostTextID;
    @FXML private TextField totalCostTextID;
    @FXML private Pane serviceEventPaneID;

    private TableView<ServiceEvent> serviceEventTableView;
    private TableColumn<ServiceEvent, String> eventDateCol;

    private TableView<ServiceItem> serviceItemTableView;
    private TableColumn<ServiceItem, String> itemDescriptionCol;
    private TableColumn<ServiceItem, Float> costCol;


    private static final String SQL_SELECT_USER_VEHICLES = "SELECT * FROM VEHICLE_TABLE WHERE IS_ACTIVE=TRUE AND USER_ID = ? ORDER BY VIN";
    private static final String SQL_SELECT_ALL_VEHICLES = "SELECT * FROM VEHICLE_TABLE WHERE IS_ACTIVE=TRUE ORDER BY VIN";
    private static final String SQL_SELECT_SERV_EVENTS = "SELECT * FROM SERVICE_EVENT WHERE VIN = ? ORDER BY SERV_DATE";
    private static final String SQL_INSERT_SERV_EVENT = "INSERT INTO SERVICE_EVENT (ODOMETER, SERV_DATE, SERV_LOCATION, VIN) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_SERV_EVENT = "UPDATE SERVICE_EVENT SET ODOMETER = ?,SERV_DATE = ? SERV_LOCATION = ? WHERE SERV_EVENT_ID = ? AND VIN = ?";
    private static final String SQL_SELECT_SERV_ITEMS = "SELECT * FROM SERVICE_TABLE WHERE SERV_EVENT_ID = ? ORDER BY SERV_ID";
    private static final String SQL_INSERT_SERV_ITEM = "INSERT INTO SERVICE_TABLE (SERV_DESC, SERV_COST, SERV_EVENT_ID) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_SERV_ITEM = "UPDATE SERVICE_TABLE SET SERV_DESC = ?, SERV_COST = ? WHERE SERV_EVENT_ID = ?";

    @FXML
    public void initialize() {
        uiState = UIStateEngine.getInstance();
        data.clear();
        currVehicle = new Vehicle();
        currServEvent = new ServiceEvent();
        currServItem = new ServiceItem();
        displayNames = FXCollections.observableArrayList();
        serviceEventData = FXCollections.observableArrayList();
        serviceItemData = FXCollections.observableArrayList();

        vehicles.clear();
        String sql = SQL_SELECT_ALL_VEHICLES;
        if (!uiState.isAdmin()) {
            data.add(uiState.getUserID());
            sql = SQL_SELECT_USER_VEHICLES;
        }
        vehicles.addAll(dbQueries.DBSelect(sql, data.toArray(), currVehicle));

        for(Vehicle v : vehicles) {
            displayNames.add(v.getDisplayName());
        }
        vehicleComboID.setItems(displayNames);
        vehicleComboID.valueProperty().addListener((ObservableValue<? extends String> ov, String old_v, String new_v)->{
            currVehicle = vehicles.get(vehicleComboID.getSelectionModel().getSelectedIndex());

            LoadServEvents();
        });
        vehicleComboID.getSelectionModel().selectFirst();
    }

    private void LoadServEvents() {
        String sql = SQL_SELECT_SERV_EVENTS;
        data.clear();
        data.add(currVehicle.getVin());

        serviceEventData.clear();
        serviceEventData.addAll(dbQueries.DBSelect(sql, data.toArray(), currServEvent));

        serviceEventTableView = new TableView<>();
        serviceEventTableView.setPrefWidth(152.0);

        eventDateCol = new TableColumn<>("Date of Service");
        eventDateCol.setPrefWidth(150.0);
        eventDateCol.setCellValueFactory(new PropertyValueFactory<>("servDate"));
        serviceEventTableView.getColumns().add(eventDateCol);

        serviceEventTableView.setItems(serviceEventData);
        serviceEventTableView.getSelectionModel().selectedItemProperty().addListener((e) -> {
            var newSE = new ServiceEvent(serviceEventTableView.getSelectionModel().getSelectedItem());
            DisplayServEvent(newSE);
        });
        serviceEventPaneID.getChildren().add(serviceEventTableView);
        serviceEventTableView.getSelectionModel().selectFirst();
    }

    private void LoadServItems(){
        String sql = SQL_SELECT_SERV_ITEMS;
        data.clear();
        data.add(currServEvent.getEventId());

        serviceItemData.clear();
        serviceItemData.addAll(dbQueries.DBSelect(sql,data.toArray(),currServItem));

        serviceItemTableView = new TableView<>();
        serviceItemTableView.setPrefWidth(702.0);

        itemDescriptionCol = new TableColumn<>("Service Performed");
        itemDescriptionCol.setPrefWidth(575.0);
        itemDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        serviceItemTableView.getColumns().add(itemDescriptionCol);

        costCol = new TableColumn<>("Cost of Service");
        costCol.setPrefWidth(125.0);
        costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        serviceItemTableView.getColumns().add(costCol);

        serviceItemTableView.setItems(serviceItemData);
        serviceItemTableView.getSelectionModel().selectedItemProperty().addListener((e) -> {
            var newSI = new ServiceItem(serviceItemTableView.getSelectionModel().getSelectedItem());
            DisplayServItem(newSI);});
        this.serviceItemPaneID.getChildren().add(this.serviceItemTableView);
        //  any time service item data changes re-calculate total cost
        CalcServCost();
    }


    private void DisplayServEvent(ServiceEvent sE){
        eventDatePickID.setValue(sE.getServDate().toLocalDate());
        odometerTextID.setText(((Integer)sE.getOdometer()).toString());
        locationTextID.setText(sE.getLocation());

        currServEvent.setServiceEvent(sE.getEventId(), sE.getOdometer(), sE.getServDate(), sE.getLocation(), sE.getVin());
        LoadServItems();
    }

    private void DisplayServItem(ServiceItem sI){
        servItemDescTextID.setText(sI.getDescription());
        servItemCostTextID.setText(((Float)sI.getCost()).toString());

        currServItem.setServiceItem(sI.getServId(), sI.getDescription(), sI.getCost(), sI.getServEventId());
    }

   // display an Alert dialog
   private void displayAlert(Alert.AlertType type, String title, String message) {
      Alert alert = new Alert(type);
      alert.setTitle(title);
      alert.setContentText(message);
      alert.showAndWait();
   }

    private void CalcServCost(){
        float totCost = 0f;

        for (ServiceItem e : serviceItemData) {
            totCost += e.getCost();
        }
        totalCostTextID.setText(Float.toString(totCost));
    }

    /**
     * find the next chronologically later fuel event and returns its index
     * if it does not exist returns -1
     * @param sE ServiceEvent of interest
     * @return index of next chronologically later FuelEvent or -1 if not found
     */
    private int getUpperIdx(ServiceEvent sE) {
        for(int i = 0; i < serviceEventData.size(); ++i) {
            if(serviceEventData.get(i).getServDate().compareTo(sE.getServDate()) > 0)
                return i;
        }
        return (-1);
    }

    /**
     * find the next chronologically earlier fuel event and return its index
     * if it does not exist returns -1
     * @param sE ServiceEvent of interest
     * @return index of next chronologically earlier FuelEvent or -1 if not found
     */
    private int getLowerIdx(ServiceEvent sE) {
        for(int i = serviceEventData.size() -1 ; i >= 0 ; --i) {
            if(serviceEventData.get(i).getServDate().compareTo(sE.getServDate()) < 0)
                return i;
        }
        return (-1);
    }

    @FXML
    void addServiceItemPressed(ActionEvent event) {
        ServiceItem newServItem = new ServiceItem(
                currServItem.getServId(),
                servItemDescTextID.getText(),
                Float.parseFloat(servItemCostTextID.getText()),
                currServEvent.getEventId()
        );

        if (newServItem.compareTo(currServItem) != 0) {
            data.clear();
            data.add(newServItem.getDescription());
            data.add(newServItem.getCost());
            data.add(newServItem.getServEventId());

            int result = dbQueries.DBUpdate(SQL_INSERT_SERV_ITEM, data.toArray());

            if (result == 1) {
                displayAlert(Alert.AlertType.INFORMATION, "Entry Added", "New service item successfully added.");
                LoadServItems();
            } else {
                displayAlert(Alert.AlertType.ERROR, "Entry Not Added", "Unable to add entry");
            }
        } else {
            displayAlert(Alert.AlertType.INFORMATION, "Entry not Added", "Entry already exists");
        }
    }

    @FXML
    void newServEventButtonPressed(ActionEvent event) {
        ServiceEvent newServEvent = new ServiceEvent(
                currServEvent.getEventId(),
                Integer.parseInt(odometerTextID.getText()),
                new Date(eventDatePickID.getValue().toEpochDay() * DBUtility.millisPerDay()),
                locationTextID.getText(),
                currVehicle.getVin()
        );

        if (newServEvent.compareTo(currServEvent) != 0) {
            int i = getUpperIdx(newServEvent);
            if((i != -1) && serviceEventData.get(i).getOdometer() < newServEvent.getOdometer()){
                displayAlert(Alert.AlertType.ERROR, "Entry Not Added", "Error in input - event date or mileage incorrect");
                return;
            }
            i = getLowerIdx(newServEvent);
            if((i != -1) && serviceEventData.get(i).getOdometer() > newServEvent.getOdometer()){
                displayAlert(Alert.AlertType.ERROR, "Entry Not Added", "Error in input - event date or mileage incorrect");
                return;
            }

            data.clear();
            data.add(newServEvent.getOdometer());
            data.add(newServEvent.getServDate());
            data.add(newServEvent.getLocation());
            data.add(newServEvent.getVin());

            int result = dbQueries.DBUpdate(SQL_INSERT_SERV_EVENT, data.toArray());

            if (result == 1) {
                displayAlert(Alert.AlertType.INFORMATION, "Entry Added", "New service event successfully added.");
                LoadServEvents();
            } else {
                displayAlert(Alert.AlertType.ERROR, "Entry Not Added", "Unable to add entry");
            }
        } else {
            displayAlert(Alert.AlertType.INFORMATION, "Entry not Added", "Entry already exists");
        }
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
    void QueryButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.USER_QUERY);
        uiState.DisplayUI();
    }

    @FXML
    void VehicleButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.INS_VEHICLE);
        uiState.DisplayUI();
    }
}

