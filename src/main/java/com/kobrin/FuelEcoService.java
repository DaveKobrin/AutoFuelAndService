/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author shdwk
 */
public class FuelEcoService extends Application {
    
    private static UIStateEngine uiState;
    
    @Override
   public void start(Stage stage) throws Exception {
       stage.setTitle("Fuel Economy And Service Tracker");
       uiState.Initialize(stage);
       uiState.DisplayUI();
/*      Parent root = 
         FXMLLoader.load(getClass().getResource("FuelEcoService.fxml"));
      
      Scene scene = new Scene(root);
      stage.setTitle("Fuel Economy And Service Tracker");
      stage.setScene(scene);
      stage.show();
*/
    }

   public static void main(String[] args) {
       uiState = UIStateEngine.getInstance();
      launch(args);
   }
}
