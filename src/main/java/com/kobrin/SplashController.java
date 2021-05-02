/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin;

/**
 *
 * @author shdwk
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SplashController {
    private UIStateEngine uiState;
    
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
    void initialize() {
        uiState = UIStateEngine.getInstance();
    }
}
