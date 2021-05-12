
package com.kobrin.controllers;

import com.kobrin.DBQueries;
import com.kobrin.UIStateEngine;
import com.kobrin.dataModels.User;
import com.kobrin.dataModels.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class LoginController {
    private UIStateEngine uiState;
    private User currUser;
    private ObservableList<User> userData;
    private final ArrayList<Object> data = new ArrayList<>();

    private static final String SQL_SELECT_USER_BY_ID = "SELECT * FROM USER_TABLE WHERE USER_ID=?";

    //class to interact with db
    private final DBQueries dbQueries = DBQueries.get();

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    public void initialize(){
        uiState = UIStateEngine.getInstance();

        userData = FXCollections.observableArrayList();
        currUser = new User();

        data.clear();
    }

    @FXML
    void processLogon(ActionEvent event) throws Exception {
        String uId = username.getText();
        String pass = password.getText();
        if ( uId == null || uId.equals("")) {
            displayAlert(Alert.AlertType.ERROR, "Error Logging On", "UserID is required!");
            return;
        }
        if (pass == null || pass.equals("")) {
            displayAlert(Alert.AlertType.ERROR, "Error Logging On", "Password is required!");
            return;
        }

        User u = new User();
        data.clear();
        data.add(uId);
        userData.clear();
        userData.addAll(dbQueries.DBSelect(SQL_SELECT_USER_BY_ID, data.toArray(), u));

        if (userData.size() != 1) {
            displayAlert(Alert.AlertType.ERROR, "Error Logging On", "UserID not found!");
            return;
        }

        currUser = new User(userData.get(0));

        if (pass.compareTo(currUser.getPassword()) != 0) {
            displayAlert(Alert.AlertType.ERROR, "Error Logging On", "UserID and Password do not match!");
            return;
        }

        if (currUser.isValid()) {
            uiState.setCurrUser(currUser);
            uiState.setLoggedIn(true);
            uiState.setAdmin(currUser.getUserType().compareTo("admin") == 0);
            displayAlert(Alert.AlertType.INFORMATION, "User Logged in", "Logged in successfully!");
            uiState.setState(UIStateEngine.StateType.SPLASH);
            uiState.DisplayUI();
        } else {
            displayAlert(Alert.AlertType.ERROR, "Error Logging On", "User record is invalid!");
        }
    }

    // display an Alert dialog
    private void displayAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
