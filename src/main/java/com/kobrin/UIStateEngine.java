/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin;

import com.kobrin.dataModels.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 *
 * @author shdwk
 */
public class UIStateEngine {
    public enum StateType {
        SPLASH,
        LOGIN,
        ADMIN,
        INS_VEHICLE,
        INS_FUEL_EVENT,
        INS_SERVICE_EVENT,
        USER_QUERY
    } 
    private StateType currState;
    private StateType lastState;
    private boolean loggedIn;
    private boolean administrator;
    private User currUser;
    
    private static final String SPLASH_FXML = "/com.kobrin/FXML/splash.fxml";
    private static final String LOGIN_FXML = "/com.kobrin/FXML/login.fxml";
    private static final String ADMIN_FXML = "/com.kobrin/FXML/admin.fxml";
    private static final String INS_VEHICLE_FXML = "/com.kobrin/FXML/insertVehicle.fxml";
    private static final String INS_FUEL_EVENT_FXML = "/com.kobrin/FXML/insertFuelEvent.fxml";
    private static final String INS_SERVICE_EVENT_FXML = "/com.kobrin/FXML/insertServiceEvent.fxml";
    private static final String USER_QUERY_FXML = "/com.kobrin/FXML/userQuery.fxml";

    private Stage stage;
    private Parent root;
    private Scene scene;


    private static final UIStateEngine instance = new UIStateEngine();

    private UIStateEngine () { }

    public void Initialize(Stage stage) {
        loggedIn = false;
        administrator = false;
        currUser = null;
        this.stage = stage;
        currState = StateType.LOGIN;
        lastState = StateType.USER_QUERY;
    }

    public static UIStateEngine getInstance() { return instance; } 
    
    public boolean isAdmin() {return administrator;}
    public  void  setAdmin(boolean value) { administrator = value;}

    public boolean isLoggedIn() {return loggedIn;}
    public void  setLoggedIn(boolean value) { loggedIn = value;}

    public String getUserID() {return currUser.getUserId();}
    public User getCurrUser() {return currUser;}
    public void setCurrUser(User user) {currUser = new User(user);}

    public StateType getState() {return currState;}

    public void setState(StateType state) {

        if ((state != currState) && loggedIn) {
            lastState = currState;
            currState = state;
        }
    }
    
    public void DisplayUI() throws Exception {
        if (lastState != currState) {
            stage.hide();

            try {
                switch (currState) {
                    case SPLASH:
                        root = FXMLLoader.load(getClass().getResource(SPLASH_FXML));
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        break;

                    case LOGIN:
                        root = FXMLLoader.load(getClass().getResource(LOGIN_FXML));
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        break;

                    case INS_VEHICLE:
                        root = FXMLLoader.load(getClass().getResource(INS_VEHICLE_FXML));
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        break;

                    case INS_FUEL_EVENT:
                        root = FXMLLoader.load(getClass().getResource(INS_FUEL_EVENT_FXML));
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        break;

                    case INS_SERVICE_EVENT:
                        root = FXMLLoader.load(getClass().getResource(INS_SERVICE_EVENT_FXML));
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        break;

                    case USER_QUERY:
                        root = FXMLLoader.load(getClass().getResource(USER_QUERY_FXML));
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        break;

                    case ADMIN:
                        root = FXMLLoader.load(getClass().getResource(ADMIN_FXML));
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        break;

                }                     
            }catch (IOException e){
                    e.printStackTrace();
                    }
            lastState = currState;
        }
    }
}
