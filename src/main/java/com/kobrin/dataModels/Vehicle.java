/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin.dataModels;

import java.time.Year;

import com.kobrin.Setable;
import javafx.beans.property.*;
/**
 *
 * @author shdwk
 */


public class Vehicle implements Comparable<Vehicle>, Setable<Vehicle> {
    private final StringProperty vin = new SimpleStringProperty(this,"vin");
    private final IntegerProperty modelYear = new SimpleIntegerProperty(this,"modelYear");
    private final StringProperty maker = new SimpleStringProperty(this,"maker");
    private final StringProperty modelName = new SimpleStringProperty(this,"modelName");
    private final StringProperty trimLevel = new SimpleStringProperty(this,"trimLevel");
    private final IntegerProperty odometer = new SimpleIntegerProperty(this,"odometer");
    private final StringProperty tireSize = new SimpleStringProperty(this,"tireSize");
    private final StringProperty displayName = new SimpleStringProperty(this,"displayName");
    private final BooleanProperty active = new SimpleBooleanProperty(this,"active");
    private final StringProperty userId = new SimpleStringProperty(this, "userId");
    private Boolean valid;
    
    public Vehicle(String v, short mY, String m, String mN, String tL, int o, String tS, String dN, Boolean a, String uId) {
//        System.out.printf("%s\t%d\t%s\t%s\t%s\t%d\t%s\t%s\t%s\n",v,mY,m,mN,tL,o,tS,dN,a.toString());
        //vin 17 digit [0-9][A-H][J-N][P][R-Z]
        vin.setValue(v.toUpperCase().matches("[0-9A-HJ-NPR-Z]{17}") ? v.toUpperCase() : "INVALID");
        //modelyear between 1900 and current year +1
        modelYear.set((mY > 1900 && mY < (short)Year.now().getValue()+1) ? mY : -1);
        //must be alpha-numeric may contain spaces trim limited to 8 chars
        maker.setValue(m.matches("[0-9A-Za-z]+|[0-9A-Za-z]+\\s[0-9A-Za-z]+") ? m : "INVALID");
        modelName.setValue(mN.matches("[0-9A-Za-z]+|[0-9A-Za-z]+\\s[0-9A-Za-z]+") ? mN : "INVALID");     
        trimLevel.setValue(tL.matches("[0-9A-Za-z]+|[0-9A-Za-z]+\\s[0-9A-Za-z]+")&&(tL.length() <= 8) ? tL : "");
        //must be positive and less than 1M miles
        odometer.set((o >= 0 && o <= 999_999) ? o : -1);
        //format like 215/65R16 (width in mm '/' ratio between width and sidewall thickness 'R' rim diameter in inches)
        tireSize.setValue(tS.matches("[1-4]\\d[5]/\\d[05][rR][1-3]\\d") ? tS : "");
        displayName.setValue(dN.matches("(\\w|['\"])+|(\\w|['\"])+(\\s(\\w|['\"])+)+") ? dN : (modelYear.get() + " " + maker.get() + " " + modelName.get()));
        active.set(a);
        userId.setValue(uId);
        
        setValid();
    }
    
    public Vehicle(){
        this("INVALID", (short)0, "INVALID", "INVALID", "INVALID", -1, "INVALID", "INVALID", false, "");
    }
    
    public Vehicle(Vehicle v){
        this(v.getVin(), v.getYear(), v.getMaker(), v.getModel(), v.getTrim(), v.getOdometer(), v.getTireSize(), 
                v.getDisplayName(), v.isActive(), v.getUserId());
    }
    
    @Override
    public void Set(Object[] args){
        if (args.length == 10) {
            setVehicle((String) args[0], (short) ((int) args[1]), (String) args[2], (String) args[3],
                    (String) args[4], (Integer) args[5], (String) args[6], (String) args[7], (Boolean) args[8], (String) args[9]);
        } else {
            setVehicle();
        }
    }
    @Override
    public Vehicle newInstance(){
        return new Vehicle();
    }
    public void setVehicle(){
        setVehicle("INVALID", (short)0, "INVALID", "INVALID", "INVALID", -1, "INVALID", "INVALID", false, "");
    }
    public void setVehicle(String v, short mY, String m, String mN, String tL, int o, String tS, String dN, Boolean a, String uId) {
//        System.out.printf("%s\t%d\t%s\t%s\t%s\t%d\t%s\t%s\t%s\n",v,mY,m,mN,tL,o,tS,dN,a.toString());
        this.setVin(v);
        this.setYear(mY);
        this.setMaker(m);
        this.setModel(mN);
        this.setTrim(tL);
        this.setOdometer(o);
        this.setTireSize(tS);
        this.setDisplayName(dN);
        this.setActive(a);
        this.setUserId(uId);
        this.setValid();
    }
    
    private void setValid(){
        valid = (vin.getValue().compareTo("INVALID") != 0) && (modelYear.getValue() != -1) && (maker.getValue().compareTo("INVALID") != 0) &&
                (modelName.getValue().compareTo("INVALID") != 0) && (odometer.getValue() != -1);
    }
  
    public void setVin(String v){
        //vin 17 digit [0-9][A-H][J-N][P][R-Z]
        vin.set(v.toUpperCase().matches("[0-9A-HJ-NPR-Z]{17}") ? v.toUpperCase() : "INVALID");
        setValid();
    }

    public void setYear(short mY) {
        //modelyear between 1900 and current year +1
        modelYear.set((mY > 1900 && mY < (short)Year.now().getValue()+1) ? mY : -1);
        setValid();
    }
    
    public void setMaker(String m) {
        //must be alpha-numeric may contain spaces trim limited to 8 chars
        maker.set(m.matches("[0-9A-Za-z]+|[0-9A-Za-z]+\\s[0-9A-Za-z]+") ? m : "INVALID");
        setValid();
    }
    public void setModel(String mN) {
        //must be alpha-numeric may contain spaces trim limited to 8 chars
        modelName.set(mN.matches("[0-9A-Za-z]+|[0-9A-Za-z]+\\s[0-9A-Za-z]+") ? mN : "INVALID");
        setValid();
    }
    public void setTrim(String tL) {
        //must be alpha-numeric may contain spaces trim limited to 8 chars
        trimLevel.set(tL.matches("[0-9A-Za-z]+|[0-9A-Za-z]+\\s[0-9A-Za-z]+")&&(tL.length() <= 8) ? tL : "");
        setValid();
    }
    public void setOdometer(int o) {
        //must be positive and less than 1M miles
        odometer.set((o >= 0 && o <= 999_999) ? o : -1);
        setValid();
    }
    public void setTireSize(String tS) {
        //format like 215/65R16 (width in mm '/' ratio between width and sidewall thickness 'R' rim diameter in inches)
        tireSize.set(tS.matches("[1-4]\\d[5]/\\d[05][rR][1-3]\\d") ? tS : "");
        setValid();
    }
  
    public void setDisplayName(String dN){
        displayName.set(dN.matches("\\w+|\\w+(\\s\\w+)+") ? dN : (modelYear + " " + maker + " " + modelName));
        setValid();
    }
    
    public void setActive(Boolean active){ this.active.set(active);}

    public void setUserId(String uId) { userId.set(uId);}
    
    public String getVin(){return vin.get();}
    public short getYear() {return (short)modelYear.get();}
    public String getMaker() {return maker.get();}
    public String getModel() {return modelName.get();}
    public String getTrim() {return trimLevel.get();}
    public int getOdometer() {return odometer.get();}
    public String getTireSize() {return tireSize.get();}
    public Boolean isValid(){return valid;}
    public String getDisplayName() {return displayName.get();}
    public Boolean isActive() {return active.get();}
    public String getUserId() {return userId.get();}
    
    public StringProperty vinProperty(){ 
        return vin;
    }
    public IntegerProperty modelYearProperty(){ return modelYear;}
    public StringProperty makerProperty(){ return maker;}
    public StringProperty modelNameProperty(){ return modelName;}
    public StringProperty trimLevelProperty(){ return trimLevel;}
    public IntegerProperty odometerProperty(){ return odometer;}
    public StringProperty tireSizeProperty(){ return tireSize;}
    public StringProperty displayNameProperty(){ return displayName;}
    public BooleanProperty activeProperty(){ return active;}
    public StringProperty userIdProperty(){ return userId;}
    
    @Override
    public int compareTo(Vehicle v){
        //field by field compare return + if 'this' has higher val than 'v'
        // return - if 'v' has higher val than 'this or 0 if all fields same val
        int result = this.getVin().compareTo(v.getVin());
        if (result != 0)
            return result;
        result = this.getYear() - v.getYear();
        if(result != 0)
            return result;
        result = this.getMaker().compareTo(v.getMaker());
        if (result != 0)
            return result;
        result = this.getModel().compareTo(v.getModel());
        if (result != 0)
            return result;
        result = this.getTrim().compareTo(v.getTrim());
        if (result != 0)
            return result;
        result = this.getOdometer() - v.getOdometer();
        if (result != 0)
            return result;
        result = this.getTireSize().compareTo(v.getTireSize());
        if (result != 0)
            return result;
        result = this.getDisplayName().compareTo(v.getDisplayName());
        if (result != 0)
            return result;
        //considering a true val higher than false first XOR to check for same
        //'true' 'true' or 'false''false' then pick the +/- 
        return (this.isActive() ^ v.isActive() ? (v.isActive() ? -1 : 1) : 0);
    }
    
    @Override
    public String toString() {return getDisplayName();}
}
