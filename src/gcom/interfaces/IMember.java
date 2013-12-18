/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.interfaces;

import gcom.modules.group.Group;
import gui.member.NewMember;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author samtube405
 */
public interface IMember extends Remote {

    public Group sendRequest(gcom.modules.group.Message message) throws RemoteException;

    public void multicast() throws RemoteException, AccessException, NotBoundException;

    public void updateGroup(Group parentGroup) throws RemoteException;

    public Group getParentGroup() throws RemoteException;

    
}
