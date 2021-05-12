/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin;
import java.sql.Types;
import java.util.Map;
/**
 *
 * @author shdwk
 */
public class DBUtility {
    private static final Map<Integer, Class<?>> sql2java = Map.ofEntries(
            Map.entry (Types.CHAR, String.class),
            Map.entry (Types.VARCHAR, String.class),
            Map.entry (Types.LONGVARCHAR,String.class),
            Map.entry (Types.NUMERIC, java.math.BigDecimal.class),
            Map.entry (Types.DECIMAL, java.math.BigDecimal.class),
            Map.entry (Types.BOOLEAN, Boolean.class),
            Map.entry (Types.BIT, Boolean.class),
            Map.entry (Types.BIGINT, Long.class),
            Map.entry (Types.BINARY, Byte[].class),
            Map.entry (Types.LONGVARBINARY, Byte[].class),
            Map.entry (Types.VARBINARY, Byte[].class),
            Map.entry (Types.TINYINT, Byte.class),
            Map.entry (Types.SMALLINT, Short.class),
            Map.entry (Types.INTEGER, Integer.class),
            Map.entry (Types.FLOAT, Float.class),
            Map.entry (Types.REAL, Float.class),
            Map.entry (Types.DOUBLE, Double.class),
            Map.entry (Types.DATE, java.sql.Date.class),
            Map.entry (Types.TIME, java.sql.Time.class),
            Map.entry (Types.TIMESTAMP, java.sql.Timestamp.class)
    );
    
    public enum SQLSetType {
        SET_ARRAY,
        SET_ASCIISTREAM,
        SET_BIGDECIMAL,
        SET_BINARYSTREAM,
        SET_BLOB,
        SET_BOOLEAN,
        SET_BYTE,
        SET_BYTES,
        SET_CHARACTERSTREAM,
        SET_CLOB,
        SET_DATE,
        SET_DOUBLE,
        SET_FLOAT,
        SET_INT,
        SET_LONG,
        SET_NCHARACTERSTREAM,
        SET_NCLOB,
        SET_NSTRING,
        SET_OBJECT,
        SET_SQLXML,
        SET_SHORT,
        SET_STRING,
        SET_TIME,
        SET_TIMESTAMP
    }
    
    public static Class<?> typeSQLtoJava(int type){

        return (sql2java.getOrDefault(type, Object.class));
    }
    
    public static int typeJavatoSQL(Class<?> jclass) {
        int result;
        if (sql2java.containsValue(jclass)){
             result = getKeyFromVal(jclass);
        } else 
            result = Types.OTHER;
    
        return result;
    }
    
    private static int getKeyFromVal(Class<?> classname) {
        for(Map.Entry<Integer, Class<?>> entry : sql2java.entrySet()) {
            if(classname.equals(entry.getValue()))
                return entry.getKey();
        }
        return Types.OTHER;
    }
    
    public static SQLSetType getSetParameterType(Class<?> classname) {
        return getSetParameterType(getKeyFromVal(classname));
    }
    
    public static SQLSetType getSetParameterType(int val) {
        switch (val){
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return SQLSetType.SET_STRING;
            case Types.NUMERIC:
            case Types.DECIMAL:
                return SQLSetType.SET_BIGDECIMAL;
            case Types.BIT:
            case Types.BOOLEAN:
                return SQLSetType.SET_BOOLEAN;
            case Types.TINYINT:
                return SQLSetType.SET_BYTE;
            case Types.SMALLINT:
                return SQLSetType.SET_SHORT;
            case Types.INTEGER:
                return SQLSetType.SET_INT;
            case Types.BIGINT:
                return SQLSetType.SET_LONG;
            case Types.REAL:
            case Types.FLOAT:
                return SQLSetType.SET_FLOAT;
            case Types.DOUBLE:
                return SQLSetType.SET_DOUBLE;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return SQLSetType.SET_BYTES;
            case Types.DATE:
                return SQLSetType.SET_DATE;
            case Types.TIME:
                return SQLSetType.SET_TIME;
            case Types.TIMESTAMP:
                return SQLSetType.SET_TIMESTAMP;
            }
        return SQLSetType.SET_OBJECT;
    }
}
