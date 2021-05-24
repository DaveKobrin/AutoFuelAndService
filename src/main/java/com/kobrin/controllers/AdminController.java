package com.kobrin.controllers;

import com.kobrin.DBQueries;
import com.kobrin.UIStateEngine;
import com.kobrin.dataModels.FuelEvent;
import com.kobrin.dataModels.User;
import com.kobrin.dataModels.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

public class AdminController {

    private final String[] validUserTypes = {"admin", "normal"};
    private ObservableList<String> userTypes;
    UIStateEngine uiState;
    private User currUser;
    private final DBQueries dbQueries = DBQueries.get();
    private final ArrayList<Object> data = new ArrayList<>();
    private ObservableList<User> userData;

    @FXML private BorderPane borderPane;
    @FXML private ChoiceBox<String> userTypeChoiceBoxID;;
    @FXML private TextField userIDTextID;
    @FXML private TextField passwordTextID;
    @FXML private TextField userTypeTextID;
    @FXML private TextField firstNameTextID;
    @FXML private TextField lastNameTextID;

    private TableView<User> userTableView;
    private TableColumn<User, String> userIDCol;
    private TableColumn<User, String> passwordCol;
    private TableColumn<User, String> userTypeCol;
    private TableColumn<User, String> firstNameCol;
    private TableColumn<User, String> lastNameCol;

    private static final String SQL_SELECT_USERS = "SELECT * FROM USER_TABLE ORDER BY USER_ID";
    private static final String SQL_INSERT_USER = "INSERT INTO USER_TABLE (USER_ID, USER_PASS, USER_TYPE, FIRST_NAME, LAST_NAME) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_USER = "UPDATE USER_TABLE SET USER_PASS = ?, USER_TYPE = ?, FIRST_NAME = ?, LAST_NAME = ? WHERE USER_ID = ?";
    private static final String SQL_DELETE_USER = "DELETE FROM USER_TABLE WHERE USER_ID = ?";



    @FXML
    void initialize() {
        uiState = UIStateEngine.getInstance();
        userTableView = new TableView<>();

        userData = FXCollections.observableArrayList();
        currUser = new User();

        userTypes = FXCollections.observableArrayList(validUserTypes);

        userTypeChoiceBoxID.setItems(userTypes);

        LoadData();
    }

    private void LoadData() {
        String sql = SQL_SELECT_USERS;
        data.clear();

        userData.clear();
        userData.addAll(dbQueries.DBSelect(sql, data.toArray(), currUser));

        userTableView.getColumns().clear();

        userIDCol = new TableColumn<>("User ID");
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userTableView.getColumns().add(userIDCol);

//        passwordCol = new TableColumn<>("Password");
//        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
//        userTableView.getColumns().add(passwordCol);

        userTypeCol = new TableColumn<>("User Type");
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));
        userTableView.getColumns().add(userTypeCol);

        firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        userTableView.getColumns().add(firstNameCol);

        lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        userTableView.getColumns().add(lastNameCol);

        userTableView.setItems(userData);
        userTableView.getSelectionModel().selectedItemProperty().addListener((e) -> {
            var newUser = new User(userTableView.getSelectionModel().getSelectedItem());
            DisplayUser(newUser);});
        borderPane.setCenter(userTableView);
    }

    // display an Alert dialog
    private void displayAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void DisplayUser(User user) {
        userIDTextID.setText(user.getUserId());
        passwordTextID.setText(user.getPassword());
        userTypeChoiceBoxID.setValue(user.getUserType());
        firstNameTextID.setText(user.getFirstName());
        lastNameTextID.setText(user.getLastName());
        currUser.setUser(user.getUserId(), user.getPassword(), user.getUserType(),
                user.getFirstName(), user.getLastName());
    }

    @FXML
    void AddUserButtonPressed(ActionEvent event){
        User newUser = new User(userIDTextID.getText(),
                        passwordTextID.getText(),
                        userTypeChoiceBoxID.getValue(),
                        firstNameTextID.getText(),
                        lastNameTextID.getText());

        if (newUser.compareTo(currUser) != 0 && !newUser.getUserId().equals(currUser.getUserId())){
            data.clear();
            data.add(newUser.getUserId());
            data.add(newUser.getPassword());
            data.add(newUser.getUserType());
            data.add(newUser.getFirstName());
            data.add(newUser.getLastName());

            int result = dbQueries.DBUpdate(SQL_INSERT_USER, data.toArray());
            if (result == 1) {
                displayAlert(Alert.AlertType.INFORMATION,"User Added", "User added successfully!");
                LoadData();
            } else {
                displayAlert(Alert.AlertType.WARNING, "Error Adding User", "Could not add user!");
            }
        }
    }

    @FXML
    void UpdateUserButtonPressed(ActionEvent event) {
        User newUser = new User(userIDTextID.getText(),
                passwordTextID.getText(),
                userTypeChoiceBoxID.getValue(),
                firstNameTextID.getText(),
                lastNameTextID.getText());

        if (newUser.compareTo(currUser) != 0 && newUser.getUserId().equals(currUser.getUserId())){
            data.clear();
            data.add(newUser.getPassword());
            data.add(newUser.getUserType());
            data.add(newUser.getFirstName());
            data.add(newUser.getLastName());
            data.add(newUser.getUserId());

            int result = dbQueries.DBUpdate(SQL_UPDATE_USER, data.toArray());
            if (result == 1) {
                displayAlert(Alert.AlertType.INFORMATION,"User Added", "User added successfully!");
                LoadData();
            } else {
                displayAlert(Alert.AlertType.WARNING, "Error Adding User", "Could not add user!");
            }
        }
    }

    @FXML
    void FuelButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.INS_FUEL_EVENT);
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

    @FXML
    void VehicleButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.INS_VEHICLE);
        uiState.DisplayUI();
    }

    @FXML
    void HomeButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.SPLASH);
        uiState.DisplayUI();
    }
}
