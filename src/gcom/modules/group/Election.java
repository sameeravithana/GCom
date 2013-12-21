/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.interfaces.IMember;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 *
 * @author Praneeth
 */
public class Election implements Remote{

    private IMember initiater;
    private LinkedList<IMember> members;   
    

    public Election(IMember initiater) throws RemoteException {
        //addNeighbour(initiater);
        members=new LinkedList<IMember>();
    }

    public void addNeighbour(IMember member) throws RemoteException{
        if(!members.contains(member)){
            getMembers().add(member);            
        }       
        printJoinOrder();
    }
    
    public void printJoinOrder() throws RemoteException{
        System.out.print("Joined order: ");
        for(IMember m:this.getMembers())
            System.out.print(m.getName()+"->");
        System.out.println("");
    }

    /**
     * @return the members
     */
    public LinkedList<IMember> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(LinkedList<IMember> members) {
        this.members = members;
    }
}
