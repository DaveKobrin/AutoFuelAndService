/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin;

import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;           
import javax.swing.table.TableModel;    
import javax.swing.table.TableRowSorter;


/**
 * FXML Controller class
 *
 * @author shdwk
 */
public class InsServiceEventController  {
    private UIStateEngine uiState;
   @FXML private BorderPane borderPane;
   @FXML private TextArea queryTextArea;
   @FXML private TextField filterTextField;

   // database URL, username and password
   private static final String DATABASE_URL = "jdbc:derby://localhost:1527/%DERBY_DB_HOME%/FuelEcoService";
   private static final String USERNAME = "DMK";
   private static final String PASSWORD = "pDaMsKs";
   
   // default query retrieves all data from Authors table
   private static final String DEFAULT_QUERY = "SELECT * FROM VEHICLE_TABLE";
   
   // used for configuring JTable to display and sort data
   private ResultSetTableModel tableModel;
   private TableRowSorter<TableModel> sorter;

   public void initialize() {
       uiState = UIStateEngine.getInstance();
      queryTextArea.setText(DEFAULT_QUERY);

      // create ResultSetTableModel and display database table
      try {
         // create TableModel for results of DEFAULT_QUERY
         tableModel = new ResultSetTableModel(DATABASE_URL,            
            USERNAME, PASSWORD);
         
         tableModel.setQuery(DEFAULT_QUERY);
         
         // create JTable based on the tableModel    
         JTable resultTable = new JTable(tableModel);

         // set up row sorting for JTable
         sorter = new TableRowSorter<TableModel>(tableModel);
         resultTable.setRowSorter(sorter);             

         // configure SwingNode to display JTable, then add to borderPane
         SwingNode swingNode = new SwingNode();
         swingNode.setContent(new JScrollPane(resultTable));
         borderPane.setCenter(swingNode);
         
      }
      catch (SQLException sqlException) {
         displayAlert(AlertType.ERROR, "Database Error", 
            sqlException.getMessage());
         tableModel.disconnectFromDatabase(); // close connection  
         System.exit(1); // terminate application
      } 
   }

   // query the database and display results in JTable
   @FXML
   void submitQueryButtonPressed(ActionEvent event) {
      // perform a new query
      try {
         tableModel.setQuery(queryTextArea.getText());
      } 
      catch (SQLException sqlException) {
         displayAlert(AlertType.ERROR, "Database Error", 
            sqlException.getMessage());
         
         // try to recover from invalid user query 
         // by executing default query
         try {
            tableModel.setQuery(DEFAULT_QUERY);
            queryTextArea.setText(DEFAULT_QUERY);
         } 
         catch (SQLException sqlException2) {
            displayAlert(AlertType.ERROR, "Database Error", 
               sqlException2.getMessage());
            tableModel.disconnectFromDatabase(); // close connection  
            System.exit(1); // terminate application
         } 
      } 
   }

   // apply specified filter to results
   @FXML
   void applyFilterButtonPressed(ActionEvent event) {
      String text = filterTextField.getText();

      if (text.length() == 0) {
         sorter.setRowFilter(null);
      }
      else {
         try {
            sorter.setRowFilter(RowFilter.regexFilter(text));
         } 
         catch (PatternSyntaxException pse) {
            displayAlert(AlertType.ERROR, "Regex Error", 
               "Bad regex pattern");
         }
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
