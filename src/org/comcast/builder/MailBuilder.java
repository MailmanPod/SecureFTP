/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.builder;

import java.util.Properties;
import javax.mail.Session;
import org.comcast.logic.Validator;

/**
 *
 * @author Quality of Service
 */
public class MailBuilder {
    
    public static final int PROPERTIES = 1;
    public static final int FROM = 2;
    public static final int RECIPIENTS = 4;
    public static final int SUBJECT = 8;
    public static final int MAIL_TEXT = 16;
    public static final int SEND_PROTOCOL = 32;
    public static final int USER_NAME = 64;
    public static final int USER_PASSWORD = 128;
    
    
    protected Mail mail;
    protected int requiredElements;
    
    public MailBuilder(){
        this.mail = new Mail();
    }
    
    public void buildProperties(Properties props){
        
        if(props != null && props.containsKey("mail.smtp.host")){
            this.mail.setProperties(props);
        }
    }
    
    public void buildFrom(String from){
        
        
    }
    
    public void buildRecipient(String recipient){
        
        
    }
    
    public void buildSubject(String subject){
        
    }
    
    public void buildMailText(String mailText){
        
    }
    
    public void buildSendProtocol(String protocol){
        
        if(Validator.isText(protocol)){
            this.mail.setSendProtocol(protocol);
        }
    }
    
    public void buildUserName(String userName){
        
        if(Validator.isUserName(userName)){
            this.mail.setUserName(userName);
        }
    }
}
