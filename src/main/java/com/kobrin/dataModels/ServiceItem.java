package com.kobrin.dataModels;

import com.kobrin.Setable;
import javafx.beans.property.*;

public class ServiceItem implements Comparable<ServiceItem>, Setable<ServiceItem> {

    private final IntegerProperty servId = new SimpleIntegerProperty(this, "servId");
    private final StringProperty description = new SimpleStringProperty(this, "description");
    private final FloatProperty cost = new SimpleFloatProperty(this, "cost");
    private final IntegerProperty servEventId = new SimpleIntegerProperty(this, "servEventId");
    private boolean valid;

    public ServiceItem(int id, String desc, float cost, int eventId){
        this.servId.set(id > 0? id : -1);
        this.description.set(desc);
        this.cost.set(cost >= 0f? cost : -1f);
        this.servEventId.set(eventId > 0 ? eventId : -1);
        setValid();
    }

    public ServiceItem(ServiceItem sI){
        this(sI.getServId(), sI.getDescription(), sI.getCost(), sI.getServEventId());
    }

    public ServiceItem(){
        this(-1, "INVALID",-1f,-1);
    }


    public int getServId() {return servId.get();}
    public String getDescription() {return description.get();}
    public float getCost() {return cost.get();}
    public int getServEventId() {return servEventId.get();}
    public boolean isValid() {return valid;}

    public IntegerProperty servIdProperty() {return servId;}
    public StringProperty descriptionProperty() {return description;}
    public FloatProperty costProperty() {return cost;}
    public IntegerProperty servEventIdProperty() {return servEventId;}

    public void setValid() {
        valid = getServId() >= 1 && getDescription().compareTo("INVALID") != 0 && !(getCost() < 0f) && getServEventId() >= 1;
    }

    public void setServiceItem(int id, String desc, float cost, int eventId){
        this.servId.set(id > 0? id : -1);
        this.description.set(desc);
        this.cost.set(cost >= 0f? cost : -1f);
        this.servEventId.set(eventId > 0 ? eventId : -1);
        setValid();
    }

    public void setServiceItem(){
        setServiceItem(-1, "INVALID",-1f,-1);
    }

    /**
     * Takes Object array containing all data fields returned from
     * SQL SELECT statement. use to set fields in the relevant data
     * model object
     *
     * @param args array of Objects read from SQL and passed here to assign correct types
     */
    @Override
    public void Set(Object[] args) {
        if (args.length == 4)
            setServiceItem((int) args[0], (String) args[1], (float) args[2], (int) args[3]);
        else
            setServiceItem();
    }

    /**
     * Returns a new instance of T
     * allows for creating a new instance of generic type
     */
    @Override
    public ServiceItem newInstance() {
        return new ServiceItem();
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
     * @param sI the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(ServiceItem sI) {
        int result = this.getServId() - sI.getServId();
        if (result != 0)
            return result;
        result = this.getDescription().compareTo(sI.getDescription());
        if(result != 0)
            return result;
        result = (int)(this.getCost() - sI.getCost());
        if(result != 0)
            return result;
        return this.getServEventId() - sI.getServEventId();
    }
}
