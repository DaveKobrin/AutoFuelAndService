/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin;

import java.sql.Timestamp;
import java.util.Date;
import javafx.beans.property.*;


/**
 *
 * @author shdwk
 */
public class FuelEvent implements Comparable<FuelEvent>, Setable {
    private final ObjectProperty<Timestamp> eventTimestamp = new SimpleObjectProperty<>(this,"eventTimestamp");
    private final LongProperty eventTime = new SimpleLongProperty(this,"eventTime");
    private final IntegerProperty odometer = new SimpleIntegerProperty(this,"odometer");
    private final DoubleProperty totalPrice = new SimpleDoubleProperty(this,"totalPrice");
    private final DoubleProperty numGallons = new SimpleDoubleProperty(this,"numGallons");
    private final BooleanProperty filledTank = new SimpleBooleanProperty(this,"filledTank");
    private final DoubleProperty pricePerGal = new SimpleDoubleProperty(this,"pricePerGal");
    private Boolean valid;

    public FuelEvent(Timestamp tS, int o, double tP, double nG, Boolean fT){
        this(tS.getTime(), o, tP, nG, fT);
    }
    public FuelEvent(long eT, int o, double tP, double nG, Boolean fT) {
        eT = (eT > new Date().getTime())? new Date().getTime() : eT;
        eventTimestamp.set(new Timestamp(eT));   
        eventTime.set(eT);
        //must be positive and less than 1M miles
        odometer.set((o >= 0 && o <= 999_999) ? o : -1);
        totalPrice.set((tP > 0.0 && tP < 150.0) ? tP : -1.0);
        numGallons.set((nG > 0.0 && nG < 50.0) ? nG : -1.0);
        filledTank.set(fT);
        setPricePerGal();
        setValid();
    }
    
    public FuelEvent(){
        this(new Date().getTime(), -1, -1.0d, -1.0d, false);
    }
    
    public FuelEvent(FuelEvent fE){
        this(fE.getEventTime(),fE.getOdometer(),fE.getTotalPrice(),fE.getNumGallons(),fE.isFilledTank());
    }
    public void setFuelEvent() {
        this.setFuelEvent(new Date().getTime(), -1, -1.0d, -1.0d, false);
    }
    public void setFuelEvent(long eT, int o, double tP, double nG, Boolean fT) {
        this.setEventTime(eT);
        this.setOdometer(o);
        this.setTotalPrice(tP);
        this.setNumGallons(nG);
        this.setFilledTank(fT);
        this.setValid();
    } 
    
    private void setValid(){
        if(eventTime.get() > (new Date().getTime()) || odometer.get() == -1 || totalPrice.get() == -1.0 || numGallons.get() == -1.0){
            valid = false;
        } else {
            valid = true;
        }
    }
    
    private void setPricePerGal(){        
        pricePerGal.set( (numGallons.get() == 0.0d) ? 0.0d : totalPrice.get()/numGallons.get() );
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
    public void setTotalPrice(double tP) {
        totalPrice.set((tP > 0.0 && tP < 150.0) ? tP : -1.0);
        this.setPricePerGal();
        setValid();
    }
  
    public void setNumGallons(double nG){
        numGallons.set((nG > 0.0 && nG < 50.0) ? nG : -1.0);
        this.setPricePerGal();
        setValid();
    }
    
    public void setFilledTank(Boolean fT){
        filledTank.set(fT);
        setValid();
    }
    
    public Timestamp getEventTimestamp() { return eventTimestamp.get();}
    public long getEventTime() { return eventTime.get();}
    public int getOdometer() {return odometer.get();}
    public double getTotalPrice() {return totalPrice.get();}
    public double getNumGallons() {return numGallons.get();}
    public Boolean isFilledTank() {return filledTank.get();}
    public double getPricePerGal() {return pricePerGal.get();}
    public Boolean isValid(){return valid;}
   
    public ObjectProperty<Timestamp> eventTimestampProperty() {return eventTimestamp;}
    public LongProperty eventTimeProperty() {return eventTime;}
    public IntegerProperty odometerProperty() {return odometer;}
    public DoubleProperty totalPriceProperty() {return totalPrice;}
    public DoubleProperty numGallonsProperty() {return numGallons;}
    public BooleanProperty filledTankProperty() {return filledTank;}
    public DoubleProperty pricePerGalProperty() {return pricePerGal;}
    
    
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
        result = Double.compare(this.getTotalPrice() - fE.getTotalPrice(), 0.0);
        if (result != 0)
            return result;
        result = Double.compare(this.getNumGallons() - fE.getNumGallons(), 0.0);
        if (result != 0)
            return result;
        //considering a true val higher than false first XOR to check for same
        //'true' 'true' or 'false''false' then pick the +/- 
        return (this.isFilledTank() ^ fE.isFilledTank() ? (fE.isFilledTank() ? -1 : 1) : 0);
    }

    @Override
    public void Set(Object[] args){
        switch(args.length){
            case 5:
                setFuelEvent(((Timestamp)args[0]).getTime(), (int)args[1], (Double)args[2], (Double)args[3], (Boolean)args[4]);
                break;
            default:
                setFuelEvent();
        }
    }

    @Override
    public FuelEvent newInstance(){
        return new FuelEvent();
    }
}
