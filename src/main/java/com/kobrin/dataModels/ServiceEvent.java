package com.kobrin.dataModels;

import com.kobrin.Setable;
import javafx.beans.property.*;

//import java.util.Date;
import java.sql.Date;

public class ServiceEvent implements Comparable<ServiceEvent>, Setable<ServiceEvent> {

    private final IntegerProperty eventId = new SimpleIntegerProperty(this, "eventId");
    private final IntegerProperty odometer = new SimpleIntegerProperty(this, "odometer");
    private final ObjectProperty<Date> servDate = new SimpleObjectProperty<>(this,"servDate");
    private final StringProperty location = new SimpleStringProperty(this,"location");
    private final StringProperty vin = new SimpleStringProperty(this,"vin");
    private boolean valid;

    public ServiceEvent(int id, int odo, Date servDate, String loc, String v){
        this.eventId.set(id > 0 ? id : -1);
        this.odometer.set(odo >= 0 ? odo : -1);
        this.servDate.set(servDate);
        this.location.set(loc);
        this.vin.set(v.toUpperCase().matches("[0-9A-HJ-NPR-Z]{17}") ? v.toUpperCase() : "INVALID");
        setValid();
    }

    public ServiceEvent(ServiceEvent sE){
        this(sE.getEventId(), sE.getOdometer(), sE.getServDate(), sE.getLocation(), sE.getVin());
    }

    public ServiceEvent(){
        this(-1,-1,null, "INVALID","INVALID");
    }

    public int getEventId() {return eventId.get();}
    public int getOdometer() {return odometer.get();}
    public Date getServDate() {return servDate.get();}
    public String getLocation() {return location.get();}
    public String getVin() {return vin.get();}
    public boolean isValid() {return valid;}

    public IntegerProperty eventIdProperty() {return eventId;}
    public IntegerProperty odometerProperty() {return odometer;}
    public ObjectProperty<Date> servDateProperty() {return servDate;}
    public StringProperty locationProperty() {return location;}
    public StringProperty vinProperty() {return vin;}

    private void setValid() {
        valid = getEventId() >= 1 && getOdometer() >= 0 && getServDate() != null && !getLocation().equals("INVALID") && !getVin().equals("INVALID");
    }

    public void setServiceEvent(int id, int odo, Date servDate, String loc, String v){
        this.eventId.set(id > 0 ? id : -1);
        this.odometer.set(odo >= 0 ? odo : -1);
        this.servDate.set(servDate);
        this.location.set(loc);
        this.vin.set(v.toUpperCase().matches("[0-9A-HJ-NPR-Z]{17}") ? v.toUpperCase() : "INVALID");
        setValid();
    }
    public void setServiceEvent(){
        setServiceEvent(-1,-1,null, "INVALID","INVALID");
    }

    /**
     * Takes Object array containing all data fields returned from
     * SQL SELECT statement. use to set fields in the relevant data
     * model object
     *
     * @param args array of Objects read from SQL and passed here to assign correct classes
     */
    @Override
    public void Set(Object[] args) {
        if (args.length == 5)
            setServiceEvent((int) args[0], (int) args[1], (Date) args[2], (String) args[3], (String) args[4]);
        else
            setServiceEvent();
    }

    /**
     * Returns a new instance of T
     * allows for creating a new instance of generic type
     */
    @Override
    public ServiceEvent newInstance() {
        return new ServiceEvent();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param sE the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(ServiceEvent sE) {
        int result = this.getEventId() - sE.getEventId();
        if (result != 0)
            return result;
        result = this.getOdometer() - sE.getOdometer();
        if (result != 0)
            return result;
        result = this.getServDate().compareTo(sE.getServDate());
        if (result != 0)
            return result;
        result = this.getLocation().compareTo(sE.getLocation());
        if (result != 0)
            return result;
        return this.getVin().compareTo(sE.getVin());
    }
}
