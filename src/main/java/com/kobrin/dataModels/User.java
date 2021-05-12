package com.kobrin.dataModels;

import com.kobrin.Setable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User implements Comparable<User>, Setable<User> {

    private final StringProperty userId = new SimpleStringProperty(this,"userId");
    private final StringProperty password = new SimpleStringProperty(this,"password");
    private final StringProperty userType = new SimpleStringProperty(this, "userType");
    private final StringProperty firstName = new SimpleStringProperty(this,"firstName");
    private final StringProperty lastName = new SimpleStringProperty(this,"lastName");
    private boolean valid;

    public User(String id, String pw, String uT, String fN, String lN) {
        userId.setValue(id);
        password.setValue(pw);
        userType.setValue(uT);
        firstName.setValue(fN);
        lastName.setValue(lN);
        setValid();
    }

    public User(User user){
        this(user.getUserId(), user.getPassword(), user.getUserType(), user.getFirstName(), user.getLastName());
    }

    public User(){
        this("","","INVALID","","");
    }

    public String getUserId() {return userId.get();}
    public String getPassword() {return password.get();}
    public String getUserType() {return userType.get();}
    public String getFirstName() {return firstName.get();}
    public String getLastName() {return lastName.get();}
    public boolean isValid() {return valid;}

    public StringProperty UserIdProperty() {return userId;}
    public StringProperty PasswordProperty() {return password;}
    public StringProperty userTypeProperty() {return userType;}
    public StringProperty firstNameProperty() {return firstName;}
    public StringProperty lastNameProperty() {return lastName;}

    private void setValid() {
        valid = getUserId().compareTo("") != 0 && getPassword().compareTo("") != 0 && getUserType().compareTo("INVALID") != 0;
    }

    /**
     * Takes Object array containing all data fields returned from
     * SQL SELECT statement. use to set fields in the relevant data
     * model object
     *
     * @param args array of Objects read from SQL passed here to be assigned correct types
     */
    @Override
    public void Set(Object[] args) {
        if (args.length == 5)
            setUser((String)args[0], (String) args[1], (String) args[2], (String) args[3], (String) args[4]);
        else
            setUser();
    }

    public void setUser(){
        setUser("","","INVALID","","");
    }

    public void setUser(String id, String pw, String uT, String fN, String lN) {
        userId.setValue(id);
        password.setValue(pw);
        userType.setValue(uT);
        firstName.setValue(fN);
        lastName.setValue(lN);
        setValid();
    }

    /**
     * Returns a new instance of User
     * allows for creating a new instance of generic type
     */
    @Override
    public User newInstance() {
        return new User();
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
     * @param u the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(User u) {
        int result = this.getUserId().compareTo(u.getUserId());
        if (result != 0)
            return result;
        result = this.getPassword().compareTo(u.getPassword());
        if (result != 0)
            return result;
        result = this.getUserType().compareTo(u.getUserType());
        if (result != 0)
            return result;
        result = this.getFirstName().compareTo(u.getFirstName());
        if (result != 0)
            return result;
        return this.getLastName().compareTo(u.getLastName());
    }
}
