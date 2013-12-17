/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author samtube405
 */
public interface IGroupManagement extends Remote {
    
    public HashMap<String, Integer> getGroupDetails() throws RemoteException;
    
}
