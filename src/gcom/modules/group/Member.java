/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.RMIServer;
import gcom.interfaces.IMember;
import gcom.interfaces.MESSAGE_TYPE;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author praneeth
 */
public class Member extends JLabel implements IMember {

    private String name;
    private int identifier;
    private Group parentGroup;
    private boolean isElectionParticipant = false;
    private boolean isGroupLeader;
    private LinkedList<IMember> members;
    private Election election;
    private RMIServer srv;

    public Member(String name, Group parent) throws RemoteException {
        this.parentGroup = parent;
        this.name = name;
        members = new LinkedList<IMember>();
        identifier = new Random().nextInt(100) + 1;
        //election = new Election(this);
    }

    public void start() {
    }

    /**
     * @return the parentGroup
     */
    public Group getParentGroup() {
        return parentGroup;
    }

    /**
     * @param parentGroup the parentGroup to set
     */
    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
        System.out.println("MEMBER COUNT : " + parentGroup.getMemberCount());
        election = new Election(this);
        //System.out.println(this.getName()+" Mem count: "+this.parentGroup.getMemberCount());
    }

    /**
     * @return the id
     */
    public String getName() {
        return name;
    }

    /**
     * @param id the id to set
     */
    public void setName(String id) {
        this.name = id;
    }

    /**
     * @return the identifier
     */
    public int getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the isElectionParticipant
     */
    public boolean isElectionParticipant() {
        return isElectionParticipant;
    }

    /**
     * @param isElectionParticipant the isElectionParticipant to set
     */
    public void setElectionParticipant(boolean isElectionParticipant) {
        this.isElectionParticipant = isElectionParticipant;
    }

    /**
     * @return the isGroupLeader
     */
    public boolean isGroupLeader() {
        return isGroupLeader;
    }

    /**
     * @param isGroupLeader the isGroupLeader to set
     */
    public void setGroupLeader(boolean isGroupLeader) {
        this.isGroupLeader = isGroupLeader;
    }

    public IMember sendRequest(Message message) throws RemoteException, AccessException {
        IMember m = message.getSource();

        if (message.getMessageType() == MESSAGE_TYPE.JOIN_REQUEST) {
            try {
                this.parentGroup.addMember(m);
                this.addMember(m);
                System.out.println("LEADER ADDED MEMBER: " + m.getName());
                m.setParentGroup(this.parentGroup);
                updateMembers(m);
                multicast(m);
            } catch (GroupManagementException ex) {
                Logger.getLogger(Member.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(Member.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return m;
    }

    public void multicast(IMember newmember) throws RemoteException, AccessException, NotBoundException {
        HashMap<String, IMember> membersList = this.parentGroup.getMembersList();
        for (String key : membersList.keySet()) {

            IMember m = membersList.get(key);
            m.updateGroup(this.parentGroup);

            if (!(key.equals(this.getName()) || key.equals(newmember.getName()))) {
                m.addMember(newmember);
            }

        }
    }

    public void updateGroup(Group parentGroup) throws RemoteException {
        this.parentGroup = parentGroup;
        System.out.println(this.getName() + " -> Update Group Message: New member count: " + this.parentGroup.getMemberCount());
    }

    public void updateMembers(IMember member) throws RemoteException {
        for (IMember m : this.getMembers()) {
            member.addMember(m);
        }

    }

    /**
     * @return the election
     */
    public Election getElection() {
        return election;
    }

    /**
     * @param election the election to set
     */
    public void setElection(Election election) {
        this.election = election;
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

    public void addMember(IMember member) throws RemoteException {
        if (!this.getMembers().contains(member)) {
            this.getMembers().add(member);
        }
        printJoinOrder();
    }

    public IMember getNeighbour(int pos) {
//        if (getMembers().indexOf(this) > getMembers().size() - 1) {
//            return getMembers().getFirst();
//        }
//        return getMembers().get((getMembers().indexOf(this) + 1));
        //System.out.println(""+this.getMembers().indexOf(this));
        return this.getMembers().get((pos + 1) % this.getMembers().size());
    }

    public void printJoinOrder() throws RemoteException {
        System.out.print("Newbie Joined: Order: ");
        for (IMember m : this.getMembers()) {
            System.out.print(m.getName() + "->");
        }
        System.out.println("");
    }

    public void callElection(Message emessage) throws RemoteException {
        LinkedList<IMember> llist = this.getMembers();
        int position = llist.indexOf(this.parentGroup.getMembersList().get(this.getName()));
        MESSAGE_TYPE messageType = emessage.getMessageType();

        if (messageType == MESSAGE_TYPE.ELECTION) {
            System.out.println("#CALL ELECTION: " + messageType + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessage());
            System.out.println("Neighbour " + this.getNeighbour(position).getName() + " ID: " + this.getNeighbour(position).getIdentifier());
            this.getNeighbour(position).voteElection(emessage);
        }
    }

    public void voteElection(Message emessage) throws RemoteException {
        LinkedList<IMember> llist = this.getMembers();
        int position = llist.indexOf(this.parentGroup.getMembersList().get(this.getName()));
        if (emessage.getMessageType() == MESSAGE_TYPE.ELECTION) {
            if (this.getIdentifier() <= emessage.getMessageID()) {
                this.setElectionParticipant(true);
                System.out.println("*I FORWARD ELECTION: " + emessage.getType() + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessageID());
                System.out.println("Neighbour " + this.getNeighbour(position).getName() + " ID: " + this.getNeighbour(position).getIdentifier());
                if (emessage.getMessageID() == this.getNeighbour(position).getIdentifier()) {
                    this.getNeighbour(position).stopElection(emessage);
                } else {
                    this.getNeighbour(position).callElection(emessage);
                }

            } else {
                if (!this.isElectionParticipant()) {
                    this.setElectionParticipant(true);
                    emessage.setMessageID(this.getIdentifier());
                    System.out.println("*I MODIFY ELECTION: " + emessage.getType() + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessageID());
                    this.callElection(emessage);
                }
            }
        }

        if (emessage.getMessageType() == MESSAGE_TYPE.ELECTED && this.getIdentifier() != emessage.getMessageID()) {
            this.setElectionParticipant(false);
            this.setGroupLeader(false);
            //this.setIdentifier(emessage.getImessage());
            System.out.println("*ELECTED MSG: " + emessage.getType() + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessageID());
            this.getNeighbour(position).voteElection(emessage);
        }

    }

    public void stopElection(Message emessage) throws RemoteException {
        LinkedList<IMember> llist = this.getMembers();
        int position = llist.indexOf(this.parentGroup.getMembersList().get(this.getName()));

        this.setElectionParticipant(false);

        this.setGroupLeader(true);

//        srv = new RMIServer("localhost", 1099);
//        srv.start();
//        IMember stub = (IMember) UnicastRemoteObject.exportObject(this, 0);
//        srv.rebind(this.getParentGroup().getGroupName(), stub);
        emessage.setType(MESSAGE_TYPE.ELECTED);
        System.out.println("#NEW LEADER: " + emessage.getType() + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessageID());
        this.getNeighbour(position).voteElection(emessage);
    }
}
