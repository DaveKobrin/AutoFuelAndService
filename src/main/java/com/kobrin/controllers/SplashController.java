/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin.controllers;


import com.kobrin.UIStateEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 *
 * @author David Kobrin
 */

public class SplashController {
    private UIStateEngine uiState;
    @FXML private HBox splashHBoxID;
    @FXML private Button adminButtonID;

    @FXML
    void AdminButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.ADMIN);
        uiState.DisplayUI();
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
    void LogoutButtonPressed(ActionEvent event) throws Exception {
        uiState.setState(UIStateEngine.StateType.LOGIN);
        uiState.setLoggedIn(false);
        uiState.DisplayUI();
    }

    @FXML
    void initialize() {
        uiState = UIStateEngine.getInstance();
        if (uiState.isAdmin()){
            adminButtonID.setVisible(true);
            adminButtonID.setFocusTraversable(true);
        } else {
            adminButtonID.setVisible(false);
            adminButtonID.setFocusTraversable(false);
        }
    }
}
