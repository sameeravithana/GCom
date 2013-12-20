/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.interfaces;

import gcom.modules.group.Group;
import gcom.modules.group.GroupManagementException;
import gcom.modules.group.Member;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author samtube405
 */
public interface IGroupManagement extends Remote {

    public HashMap<String, Integer> getGroupDetails() throws RemoteException;

    public IMember sendRequest(gcom.modules.group.Message message) throws RemoteException;

    public void updateStatus(IMessage message) throws RemoteException, GroupManagementException;

    public void updateStatus(Member member, IMessage.TYPE_MESSAGE type) throws RemoteException, GroupManagementException;
}
