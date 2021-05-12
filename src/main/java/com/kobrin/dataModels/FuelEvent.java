/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin.dataModels;

import java.sql.Timestamp;
import java.util.Date;

import com.kobrin.Setable;
import javafx.beans.property.*;


/**
 *
 * @author shdwk
 */
public class FuelEvent implements Comparable<FuelEvent>, Setable<FuelEvent> {
    private final StringProperty vin = new SimpleStringProperty(this,"vin");
    private final ObjectProperty<Timestamp> eventTimestamp = new SimpleObjectProperty<>(this,"eventTimestamp");
    private final LongProperty eventTime = new SimpleLongProperty(this,"eventTime");
    private final IntegerProperty odometer = new SimpleIntegerProperty(this,"odometer");
    private final FloatProperty totalPrice = new SimpleFloatProperty(this,"totalPrice");
    private final FloatProperty numGallons = new SimpleFloatProperty(this,"numGallons");
    private final BooleanProperty filledTank = new SimpleBooleanProperty(this,"filledTank");
    private final FloatProperty pricePerGal = new SimpleFloatProperty(this,"pricePerGal");
    private Boolean valid;

    public FuelEvent(String v, Timestamp tS, int o, float tP, float nG, Boolean fT){
        this(v, tS.getTime(), o, tP, nG, fT);
    }
    public FuelEvent(String v, long eT, int o, float tP, float nG, Boolean fT) {
        vin.set(v);
        eT = Math.min(eT, new Date().getTime());
        eventTimestamp.set(new Timestamp(eT));   
        eventTime.set(eT);
        //must be positive and less than 1M miles
        odometer.set((o >= 0 && o <= 999_999) ? o : -1);
        totalPrice.set((tP > 0.0f && tP < 150.0f) ? tP : -1.0f);
        numGallons.set((nG > 0.0f && nG < 50.0f) ? nG : -1.0f);
        filledTank.set(fT);
        setPricePerGal();
        setValid();
    }
    
    public FuelEvent(){
        this("", new Date().getTime(), -1, -1.0f, -1.0f, false);
    }
    
    public FuelEvent(FuelEvent fE){
        this(fE.getVin(), fE.getEventTime(), fE.getOdometer() ,fE.getTotalPrice(), fE.getNumGallons(), fE.isFilledTank());
    }
    public void setFuelEvent() {
        this.setFuelEvent("", new Date().getTime(), -1, -1.0f, -1.0f, false);
    }
    public void setFuelEvent(String v, long eT, int o, float tP, float nG, Boolean fT) {
        this.setVin(v);
        this.setEventTime(eT);
        this.setOdometer(o);
        this.setTotalPrice(tP);
        this.setNumGallons(nG);
        this.setFilledTank(fT);
        this.setValid();
    } 
    
    private void setValid(){
        valid = eventTime.get() <= (new Date().getTime()) && odometer.get() != -1 && totalPrice.get() != -1.0 && numGallons.get() != -1.0;
    }
    
    private void setPricePerGal(){        
        pricePerGal.set( (numGallons.get() == 0.0f) ? 0.0f : totalPrice.get()/numGallons.get() );
    }
    
    public void setEventTimestamp(Timestamp t){
        eventTimestamp.set(t);
        eventTime.set(t.getTime());
        setValid();
    }
    
    public void setEventTime(long eT){
        eventTimestamp.set(new Timestamp(eT));
        eventTime.set(eT);
        setValid();
    }

    public void setOdometer(int o) {
        //must be positive and less than 1M miles
        odometer.set((o >= 0 && o <= 999_999) ? o : -1);
        setValid();
    }
    public void setTotalPrice(float tP) {
        totalPrice.set((tP > 0.0f && tP < 150.0f) ? tP : -1.0f);
        this.setPricePerGal();
        setValid();
    }
  
    public void setNumGallons(float nG){
        numGallons.set((nG > 0.0f && nG < 50.0f) ? nG : -1.0f);
        this.setPricePerGal();
        setValid();
    }
    
    public void setFilledTank(Boolean fT){
        filledTank.set(fT);
        setValid();
    }

    public void setVin(String v) { vin.set(v);}

    public String getVin() { return vin.get();}
    public Timestamp getEventTimestamp() { return eventTimestamp.get();}
    public long getEventTime() { return eventTime.get();}
    public int getOdometer() {return odometer.get();}
    public float getTotalPrice() {return totalPrice.get();}
    public float getNumGallons() {return numGallons.get();}
    public Boolean isFilledTank() {return filledTank.get();}
    public float getPricePerGal() {return pricePerGal.get();}
    public Boolean isValid(){return valid;}

    public StringProperty vinProperty() {return vin;}
    public ObjectProperty<Timestamp> eventTimestampProperty() {return eventTimestamp;}
    public LongProperty eventTimeProperty() {return eventTime;}
    public IntegerProperty odometerProperty() {return odometer;}
    public FloatProperty totalPriceProperty() {return totalPrice;}
    public FloatProperty numGallonsProperty() {return numGallons;}
    public BooleanProperty filledTankProperty() {return filledTank;}
    public FloatProperty pricePerGalProperty() {return pricePerGal;}
    
    
    @Override
    public int compareTo(FuelEvent fE){
        //field by field compare return + if 'this' has higher val than 'fE'
        // return - if 'fE' has higher val than 'this or 0 if all fields same val
        int result = (this.getEventTime() - fE.getEventTime()) == 0 ? 0 : ((this.getEventTime() - fE.getEventTime()) < 0) ? -1 : 1;
        if (result != 0)
            return result;
        result = this.getOdometer() - fE.getOdometer();
        if(result != 0)
            return result;
        result = Float.compare(this.getTotalPrice() - fE.getTotalPrice(), 0.0f);
        if (result != 0)
            return result;
        result = Float.compare(this.getNumGallons() - fE.getNumGallons(), 0.0f);
        if (result != 0)
            return result;
        //considering a true val higher than false first XOR to check for same
        //'true' 'true' or 'false''false' then pick the +/- 
        return (this.isFilledTank() ^ fE.isFilledTank() ? (fE.isFilledTank() ? -1 : 1) : 0);
    }

    @Override
    public void Set(Object[] args){
        if (args.length == 6) {
            setFuelEvent((String) args[0], ((Timestamp) args[1]).getTime(), (int) args[2], (Float) args[3], (Float) args[4], (Boolean) args[5]);
        } else {
            setFuelEvent();
        }
    }

    @Override
    public FuelEvent newInstance(){
        return new FuelEvent();
    }
}
