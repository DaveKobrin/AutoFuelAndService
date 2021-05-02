/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kobrin;

/**
 *  Interface used to enable generic dynamic select statements
 *  
 * @author shdwk
 */
public interface Setable<T> {
    
/**     Takes Object array containing all data fields returned from
 *      SQL SELECT statement. use to set fields in the relevant data
 *      model object
 */
    public abstract void Set(Object[] args); 
    
/**     Returns a new instance of T 
 *      allows for creating a new instance of generic type
*/
    public abstract T newInstance();    
}
