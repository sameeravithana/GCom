/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.interfaces;

import gcom.modules.group.Election;
import gcom.modules.group.Group;
import gcom.modules.group.Member;
import gcom.modules.group.Message;
import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 *
 * @author samtube405
 */
public interface IMember extends Remote,Serializable{

    public IMember sendRequest(Message message) throws RemoteException;

    public void multicast(IMember newmember) throws RemoteException, AccessException, NotBoundException;

    public void updateGroup(Group parentGroup) throws RemoteException;
    
    public void updateMembers(IMember member) throws RemoteException;

    public Group getParentGroup() throws RemoteException;
    
    public void setParentGroup(Group parentGroup) throws RemoteException;
    
    public boolean isGroupLeader() throws RemoteException;
    
    public void setGroupLeader(boolean isGroupLeader) throws RemoteException;
    
    public String getName() throws RemoteException;

     public Election getElection() throws RemoteException;
     
     public void setElection(Election election) throws RemoteException;
     
     public boolean isElectionParticipant() throws RemoteException;
     
     public void setElectionParticipant(boolean isElectionParticipant) throws RemoteException;
     
     public LinkedList<IMember> getMembers() throws RemoteException;
     
     public void setMembers(LinkedList<IMember> members) throws RemoteException;
     
     public void addMember(IMember member) throws RemoteException;
     
     public IMember getNeighbour(int pos) throws RemoteException;
     
     public void callElection(Message emessage) throws RemoteException;
     
     public void voteElection(Message emessage) throws RemoteException;
     
     public void stopElection(Message emessage) throws RemoteException;
     
     public int getIdentifier() throws RemoteException;
             
     public void setIdentifier(int identifier) throws RemoteException;
     
     public void multicastCausal(Message message) throws RemoteException;
     
     public void delivercasual(Message message) throws RemoteException;
     
     public void getAcknowledgement(Message message) throws RemoteException;
    
}
