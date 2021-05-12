/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin;
import java.sql.Types;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author shdwk
 */
public class DBQueries {
    private static final String DB = "//localhost:1527/FuelEcoService";
    private static final String URL = "jdbc:derby:" + DB;
    private static final String USERNAME = "DMK";
    private static final String PASSWORD = "pDaMsKs";
    private ResultSetMetaData vehicleMeta;
    private static DBQueries instance = null;

//    private HashMap<Integer, Class> vehicleColClassMap;

    private Connection connection;
//    private PreparedStatement insertVehicle;
//    private PreparedStatement insertFuelEvent;
//    private PreparedStatement insertVehicleFuel;
//    private PreparedStatement selectAllVehicles;
//    private PreparedStatement selectAllActiveVehicles;
//    private PreparedStatement selectAllFuelEvent;

    private void OpenConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println("connection opened");
    }

    private void CloseConnection() {
        try {
            if (!connection.isClosed()) {
                connection.close();
                System.out.println("connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static DBQueries get(){
        if (instance == null)
            instance = new DBQueries();
        return instance;
    }
    private DBQueries() {
//        vehicleColClassMap = new HashMap<>();
        vehicleMeta = null;
        try {
//            if (connection.isClosed())
            OpenConnection();

//            insertVehicle = connection.prepareStatement("INSERT INTO VEHICLE_TABLE " +
//                    "(VIN, MODEL_YEAR, MAKER, MODEL_NAME, TRIM_LEVEL, CURRENT_ODO, TIRE_SIZE, DISPLAY_NAME) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//
//            insertFuelEvent = connection.prepareStatement("INSERT INTO FUEL_EVENT " +
//                    "(EVENT_TIME, ODOMETER, TOTAL_PRICE, NUM_GAL, IS_FULL_TANK) " +
//                    "VALUES (?, ?, ?, ?, ?)");
//
//            insertVehicleFuel = connection.prepareStatement("INSERT INTO VEHICLE_FUEL_TABLE " +
//                    "(VIN, EVENT_TIME) VALUES (?, ?)");
//
//            selectAllActiveVehicles = connection.prepareStatement("SELECT * FROM VEHICLE_TABLE WHERE IS_ACTIVE=TRUE");
//
//            selectAllFuelEvent = connection.prepareStatement("SELECT * FROM FUEL_EVENT");

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            CloseConnection();
        }
    }

    /**
     * Generic select process -
     *
     * @param sql       - string that is the SQL prepared statement
     * @param data      - array of Objects that holds the parameters
     *                  for the statement
     * @param dataModel - variable of the type to return the data as.
     *                  requires that model implements Setable
     *                  to apply ResultSet data to dataModle type
     * @param <T>       class used to model the data from the select and as
     *                  the return type.
     * @return List  of elements of type  T
     */
    public <T extends Setable<T>> List<T> DBSelect(String sql, Object[] data, T dataModel) {


        List<T> results = new ArrayList<>();
        try {
            if (connection.isClosed())
                OpenConnection();

            PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < data.length; ++i) {
                switch (DBUtility.getSetParameterType(data[i].getClass())) {
                    case SET_ARRAY:
                        ps.setArray(i + 1, (java.sql.Array) data[i]);
                        break;
                    case SET_BIGDECIMAL:
                        ps.setBigDecimal(i + 1, (java.math.BigDecimal) data[i]);
                        break;
                    case SET_BLOB:
                        ps.setBlob(i + 1, (java.sql.Blob) data[i]);
                        break;
                    case SET_BOOLEAN:
                        ps.setBoolean(i + 1, (Boolean) data[i]);
                        break;
                    case SET_BYTE:
                        ps.setByte(i + 1, (byte) data[i]);
                        break;
                    case SET_BYTES:
                        ps.setBytes(i + 1, (byte[]) data[i]);
                        break;
                    case SET_CLOB:
                        ps.setClob(i + 1, (java.sql.Clob) data[i]);
                        break;
                    case SET_DATE:
                        ps.setDate(i + 1, (java.sql.Date) data[i]);
                        break;
                    case SET_DOUBLE:
                        ps.setDouble(i + 1, (Double) data[i]);
                        break;
                    case SET_FLOAT:
                        ps.setFloat(i + 1, (Float) data[i]);
                        break;
                    case SET_INT:
                        ps.setInt(i + 1, (int) data[i]);
                        break;
                    case SET_LONG:
                        ps.setLong(i + 1, (long) data[i]);
                        break;
                    case SET_NCLOB:
                        ps.setNClob(i + 1, (java.sql.NClob) data[i]);
                        break;
                    case SET_NSTRING:
                        ps.setNString(i + 1, (String) data[i]);
                        break;
                    case SET_SQLXML:
                        ps.setSQLXML(i + 1, (java.sql.SQLXML) data[i]);
                        break;
                    case SET_SHORT:
                        ps.setShort(i + 1, (short) data[i]);
                        break;
                    case SET_STRING:
                        ps.setString(i + 1, (String) data[i]);
                        break;
                    case SET_TIME:
                        ps.setTime(i + 1, (java.sql.Time) data[i]);
                        break;
                    case SET_TIMESTAMP:
                        ps.setTimestamp(i + 1, (Timestamp) data[i]);
                        break;
                    default:
                        ps.setObject(i + 1, data[i]);
                        break;
                }
            }

            ResultSet resultSet = ps.executeQuery();
            ResultSetMetaData meta = resultSet.getMetaData();
            ArrayList<Object> args = new ArrayList<>();

            while (resultSet.next()) {
                args.clear();
                for (int i = 0; i < meta.getColumnCount(); ++i) {
                    System.out.printf("%s\n", meta.getColumnClassName(i + 1));
                    switch (DBUtility.getSetParameterType(meta.getClass())) {
                        case SET_ARRAY:
                            args.add(resultSet.getArray(i + 1));
                            break;
                        case SET_BIGDECIMAL:
                            args.add(resultSet.getBigDecimal(i + 1));
                            break;
                        case SET_BLOB:
                            args.add(resultSet.getBlob(i + 1));
                            break;
                        case SET_BOOLEAN:
                            args.add(resultSet.getBoolean(i + 1));
                            break;
                        case SET_BYTE:
                            args.add(resultSet.getByte(i + 1));
                            break;
                        case SET_BYTES:
                            args.add(resultSet.getBytes(i + 1));
                            break;
                        case SET_CLOB:
                            args.add(resultSet.getClob(i + 1));
                            break;
                        case SET_DATE:
                            args.add(resultSet.getDate(i + 1));
                            break;
                        case SET_DOUBLE:
                            args.add(resultSet.getDouble(i + 1));
                            break;
                        case SET_FLOAT:
                            args.add(resultSet.getFloat(i + 1));
                            break;
                        case SET_INT:
                            args.add(resultSet.getInt(i + 1));
                            break;
                        case SET_LONG:
                            args.add(resultSet.getLong(i + 1));
                            break;
                        case SET_NCLOB:
                            args.add(resultSet.getNClob(i + 1));
                            break;
                        case SET_NSTRING:
                            args.add(resultSet.getNString(i + 1));
                            break;
                        case SET_SQLXML:
                            args.add(resultSet.getSQLXML(i + 1));
                            break;
                        case SET_SHORT:
                            args.add(resultSet.getShort(i + 1));
                            break;
                        case SET_STRING:
                            args.add(resultSet.getString(i + 1));
                            break;
                        case SET_TIME:
                            args.add(resultSet.getTime(i + 1));
                            break;
                        case SET_TIMESTAMP:
                            args.add(resultSet.getTimestamp(i + 1));
                            break;
                        default:
                            args.add(resultSet.getObject(i + 1));
                            break;
                    }
                }
                T element =  dataModel.newInstance();
                element.Set(args.toArray());
                results.add(element);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseConnection();
        }
        return results;
    }

    public int DBUpdate(String sql, Object[] data) {
        try {
            if (connection.isClosed())
                OpenConnection();

            PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < data.length; ++i) {
                switch (DBUtility.getSetParameterType(data[i].getClass())) {
                    case SET_ARRAY:
                        ps.setArray(i + 1, (java.sql.Array) data[i]);
                        break;
                    case SET_BIGDECIMAL:
                        ps.setBigDecimal(i + 1, (java.math.BigDecimal) data[i]);
                        break;
                    case SET_BLOB:
                        ps.setBlob(i + 1, (java.sql.Blob) data[i]);
                        break;
                    case SET_BOOLEAN:
                        ps.setBoolean(i + 1, (Boolean) data[i]);
                        break;
                    case SET_BYTE:
                        ps.setByte(i + 1, (byte) data[i]);
                        break;
                    case SET_BYTES:
                        ps.setBytes(i + 1, (byte[]) data[i]);
                        break;
                    case SET_CLOB:
                        ps.setClob(i + 1, (java.sql.Clob) data[i]);
                        break;
                    case SET_DATE:
                        ps.setDate(i + 1, (java.sql.Date) data[i]);
                        break;
                    case SET_DOUBLE:
                        ps.setDouble(i + 1, (Double) data[i]);
                        break;
                    case SET_FLOAT:
                        ps.setFloat(i + 1, (Float) data[i]);
                        break;
                    case SET_INT:
                        ps.setInt(i + 1, (int) data[i]);
                        break;
                    case SET_LONG:
                        ps.setLong(i + 1, (long) data[i]);
                        break;
                    case SET_NCLOB:
                        ps.setNClob(i + 1, (java.sql.NClob) data[i]);
                        break;
                    case SET_NSTRING:
                        ps.setNString(i + 1, (String) data[i]);
                        break;
                    case SET_SQLXML:
                        ps.setSQLXML(i + 1, (java.sql.SQLXML) data[i]);
                        break;
                    case SET_SHORT:
                        ps.setShort(i + 1, (short) data[i]);
                        break;
                    case SET_STRING:
                        ps.setString(i + 1, (String) data[i]);
                        break;
                    case SET_TIME:
                        ps.setTime(i + 1, (java.sql.Time) data[i]);
                        break;
                    case SET_TIMESTAMP:
                        ps.setTimestamp(i + 1, (Timestamp) data[i]);
                        break;
                    default:
                        ps.setObject(i + 1, data[i]);
                        break;
                }
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            CloseConnection();
        }
    }
}
//    public int AddVehicle(Vehicle v) {
//        //insert new entry return number of rows updated
//        if (v.isValid()){
//            try {
//                //set sql statement parameters
//                insertVehicle.setString(1, v.getVin());
//                insertVehicle.setShort(2, v.getYear());
//                insertVehicle.setString(3, v.getMaker());
//                insertVehicle.setString(4, v.getModel());
//                insertVehicle.setString(5, v.getTrim());
//                insertVehicle.setInt(6, v.getOdometer());
//                insertVehicle.setString(7, v.getTireSize());
//                insertVehicle.setString(8, v.getDisplayName());
//
//                return insertVehicle.executeUpdate();
//            } catch (SQLException e){
//                e.printStackTrace();
//                return 0;
//            }
//        } else
//            return -1;
//    }
//
//    public int AddFuelEvent(FuelEvent fE, Vehicle v) {
//        if(fE.isValid()) {
//            //insert new entry and return number of rows updated
//            try {
//                int result;
//                //set sql statement parameters
//                insertFuelEvent.setTimestamp(1, new Timestamp(fE.getEventTime()));    //EVENT_TIME
//                insertFuelEvent.setInt(2, fE.getOdometer());//ODOMETER
//                insertFuelEvent.setDouble(3, fE.getTotalPrice());//TOTAL_PRICE
//                insertFuelEvent.setDouble(4, fE.getNumGallons());//NUM_GAL
//                insertFuelEvent.setBoolean(5, fE.isFilledTank());//IS_FULL_TANK
//
//                result = insertFuelEvent.executeUpdate();
//                result += this.AddVehicleFuel(v, fE);
//                return result;
//
//            } catch (SQLException e){
//                e.printStackTrace();
//                return 0;
//            }
//        } else
//            return -1;
//    }
//
//    private int AddVehicleFuel(Vehicle v, FuelEvent fE){
//        if (v.isValid() && fE.isValid()) {
//            //insert new entry and return number of rows updated
//            try {
//                //set sql statement parameters
//                insertVehicleFuel.setString(1, v.getVin());
//                insertVehicleFuel.setTimestamp(2, new Timestamp(fE.getEventTime()));    //EVENT_TIME
//
//                return insertVehicleFuel.executeUpdate();
//
//            } catch (SQLException e) {
//                 e.printStackTrace();
//                return 0;
//            }
//        } else
//            return -1;
//    }
//
//    public List<Vehicle> getAllVehicles() {
//        //returns a list of all entries in VEHICLE_TABLE
//        try {
//            if (connection.isClosed())
//                OpenConnection();
//
//            selectAllActiveVehicles = connection.prepareStatement("SELECT * FROM VEHICLE_TABLE WHERE IS_ACTIVE=TRUE");
////            selectAllVehicles = connection.prepareStatement("SELECT * FROM VEHICLE_TABLE");
//
//            ResultSet resultSet = selectAllActiveVehicles.executeQuery();
//            vehicleMeta = resultSet.getMetaData();
//
////            vehicleColClassMap.clear();
////            for (int i = 1; i <= vehicleMeta.getColumnCount(); ++i){
////                vehicleColClassMap.put(i, Class.forName(vehicleMeta.getColumnClassName(i)));
////            }
//
//            List<Vehicle> results = new ArrayList<>();
//
//            while(resultSet.next()) {
//                results.add(new Vehicle(
//                        resultSet.getString("VIN"),
//                        resultSet.getShort("MODEL_YEAR"),
//                        resultSet.getString("MAKER"),
//                        resultSet.getString("MODEL_NAME"),
//                        resultSet.getString("TRIM_LEVEL"),
//                        resultSet.getInt("CURRENT_ODO"),
//                        resultSet.getString("TIRE_SIZE"),
//                        resultSet.getString("DISPLAY_NAME"),
//                        resultSet.getBoolean("IS_ACTIVE")));
//            }
//            resultSet.close();
//            return results;
//        } catch (SQLException e) {
//            e.printStackTrace();
////        } catch (ClassNotFoundException e) {
////            e.printStackTrace();
//        } finally {
//            CloseConnection();
//        }
//
//        return null;
//    }
//
//    public List<FuelEvent> getAllFuelEvents() {
//        //returns a ResultSet of all entries in VEHICLE_TABLE
//        try (ResultSet resultSet = selectAllVehicles.executeQuery()) {
//            List<FuelEvent> results = new ArrayList<>();
//
//            while(resultSet.next()) {
//                results.add(new FuelEvent(
//                        resultSet.getTimestamp("EVENT_TIME"),
//                        resultSet.getInt("ODOMETER"),
//                        resultSet.getDouble("TOTAL_PRICE"),
//                        resultSet.getDouble("NUM_GAL"),
//                        resultSet.getBoolean("IS_FULL_TANK")));
//            }
//            return results;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void close() {
//        try {
//            connection.close();
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//    }
//    public Class getVehicleColClass(int col) throws Exception{
//        if (vehicleMeta == null){
//            throw new Exception("vehicleMeta is null");
//        }
//        try {
//            String classname = vehicleMeta.getColumnClassName(col);
//            return Class.forName(classname);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return Object.class;
//    }
//
//    public String getVehicleColName(int col) throws Exception{
//        if (vehicleMeta == null){
//            throw new Exception("vehicleMeta is null");
//        }
//        try {
//            return vehicleMeta.getColumnName(col);
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//        return "";
//    }
//}
