/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.logic;

import org.comcast.router.InputChannel;

/**
 *
 * @author Quality of Service
 */
public class Client implements Comparable, InputChannel {

    private int aux;
    
   public Client(){
       aux = 0;
   }
   
   public int getAux(){
       return this.aux;
   }
   
    @Override
    public int compareTo(Object o) {
        if(o == null){
            // throw exception
        }
        
        Client aux = null;
        
        if(o instanceof Client){
            aux = (Client) o;
        }else{
            //throw exception
        }
        
        return this.aux - aux.getAux();
    }
    
}
