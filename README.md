# AutoFuelAndService

This application allows a user to record and manage information related to their vehicles. 
Including fuel costs and usage, information about the vehicle, and service reminders.

Entries are stored in a database on the localhost, currently via the Apache Derby Client/Server JDBC interface.


NOTE: must add JVM runtime options to include javafx modules. 
--module-path "c:\PATH-TO\javafx-sdk-14\lib" --add-modules javafx.controls,javafx.swing,javafx.fxml --add-exports javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED
Add to the run configuration with the correct path to javafx.
