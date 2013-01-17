/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.exceptions;

/**
 *
 * @author Signalrunner
 */
public class FTPConectionRefusedException extends GlobalException{
    
    public FTPConectionRefusedException(){
        super();
    }
    
    public FTPConectionRefusedException(String s){
        super(s);
    }
}
