/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.interfaces;

import gcom.modules.com.CommunicationMode;
import gcom.modules.group.GroupManagementException;
import gcom.modules.group.Member;
import gcom.modules.group.Message;
import java.io.Serializable;
import java.rmi.Remote;
import java.util.HashMap;

/**
 *
 * @author ens13pps
 */
public interface IGroup extends Remote,Serializable{

    /**
     * @return the comMode
     */
    public CommunicationMode getComMode();

    /**
     * @return the groupID
     */
    public String getGroupName();

    /**
     * @return the maxMembers
     */
    public int getMaxMembers();

    public IMember getMember(String memberId) throws GroupManagementException;

    public int getMemberCount();

    public HashMap<String, IMember>  getMembersList();

    public void initiateElection(Member initiater);

    public void send(Message message);
}
