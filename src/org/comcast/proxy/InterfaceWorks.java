/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.proxy;

import org.comcast.logic.DateScheduler;
import org.comcast.router.Message;
import org.comcast.structures.SimpleList;

/**
 *
 * @author Quality of Service
 */
public interface InterfaceWorks {

    public void transferFiles(SimpleList<Message> toTransfer, DateScheduler date) throws Exception;
}
