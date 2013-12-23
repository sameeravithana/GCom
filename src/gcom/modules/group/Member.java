/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.RMIServer;
import gcom.interfaces.IGroupManagement;
import gcom.interfaces.IMember;
import gcom.interfaces.MESSAGE_TYPE;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author praneeth
 */
public class Member extends UnicastRemoteObject implements IMember {

    private String name;
    private int identifier;
    private Group parentGroup;
    private boolean isElectionParticipant = false;
    private boolean isGroupLeader;
    private LinkedList<IMember> members;
    private Election election;
    private RMIServer srv;
    protected PropertyChangeSupport propertyChangeSupport;
    private int signal = 0;
    private LinkedList<Message> holdingQueue;
    private HashMap<String, Integer> vectorClock;//*

    public Member(String name, Group parent) throws RemoteException {
        this.parentGroup = parent;
        this.name = name;
        members = new LinkedList<IMember>();
        identifier = new Random().nextInt(100) + 1;
        holdingQueue = new LinkedList<Message>();
        setVectorClock(new HashMap<String, Integer>());
        propertyChangeSupport = new PropertyChangeSupport(this);
        //election = new Election(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void start() {
    }

    public void memberAdded(IMember member) throws RemoteException {
        signal = (int) signal + 1;
        propertyChangeSupport.firePropertyChange("Signal", signal - 1, member);
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
        } else if (message.getType() == MESSAGE_TYPE.MEMBER_LEAVES) {

            HashMap<String, IMember> membersList = this.parentGroup.getMembersList();

            IMember source = membersList.get(message.getMessage());

            System.out.println("Member leaves : " + message.getMessage());

            try {
                LinkedList<IMember> llist = this.getMembers();
                int position = llist.indexOf(this.parentGroup.getMembersList().get(this.getName()));
                IMember neighbour = source.getNeighbour(position);
                parentGroup.removeMember(source.getName());

                Message emessage = new Message(parentGroup.getGroupName(), members.indexOf(neighbour), neighbour.getIdentifier(), MESSAGE_TYPE.ELECTION);

                for (String key : membersList.keySet()) {

                    IMember mem = membersList.get(key);

                    mem.updateGroup(this.parentGroup);
                    mem.removeMember(source);

                }
                if (source.isGroupLeader()) {
                    if (!source.getName().equals(neighbour.getName())) {
                        neighbour.callElection(emessage);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {
            IGroupManagement igm = srv.regLookUp("IGroupManagement");
            igm.updateGroup(parentGroup);
        } catch (Exception e) {
            e.printStackTrace();
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
            m.memberAdded(newmember);

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
        //printJoinOrder();
    }

    public void removeMember(IMember member) throws RemoteException {
        if (this.getMembers().contains(member)) {
            this.getMembers().remove(member);
            vectorClock.remove(member.getName());
            propertyChangeSupport.firePropertyChange("MemberLeft", parentGroup, member);
        }
        System.out.println("Members size " + members.size());
        //printJoinOrder();
    }

    public IMember getNeighbour(int pos) {
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
            //System.out.println("#CALL ELECTION: " + messageType + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessage());
            // System.out.println("Neighbour " + this.getNeighbour(position).getName() + " ID: " + this.getNeighbour(position).getIdentifier());
            this.getNeighbour(position).voteElection(emessage);
        }
    }

    public void voteElection(Message emessage) throws RemoteException {
        LinkedList<IMember> llist = this.getMembers();
        int position = llist.indexOf(this.parentGroup.getMembersList().get(this.getName()));
        if (emessage.getMessageType() == MESSAGE_TYPE.ELECTION) {
            if (this.getIdentifier() <= emessage.getMessageID()) {
                this.setElectionParticipant(true);
                //System.out.println("*I FORWARD ELECTION: " + emessage.getType() + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessageID());
                //System.out.println("Neighbour " + this.getNeighbour(position).getName() + " ID: " + this.getNeighbour(position).getIdentifier());
                if (emessage.getMessageID() == this.getNeighbour(position).getIdentifier()) {
                    this.getNeighbour(position).stopElection(emessage);
                } else {
                    this.getNeighbour(position).callElection(emessage);
                }

            } else {
                if (!this.isElectionParticipant()) {
                    this.setElectionParticipant(true);
                    emessage.setMessageID(this.getIdentifier());
                    //System.out.println("*I MODIFY ELECTION: " + emessage.getType() + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessageID());
                    this.callElection(emessage);
                }
            }
        }

        if (emessage.getMessageType() == MESSAGE_TYPE.ELECTED && this.getIdentifier() != emessage.getMessageID()) {
            this.setElectionParticipant(false);
            this.setGroupLeader(false);
            this.getParentGroup().setLeader(emessage.getSource());
            electionCompleted(emessage.getSource());
            // System.out.println("*ELECTED MSG: " + emessage.getType() + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessageID());
            this.getNeighbour(position).voteElection(emessage);
        }

    }

    public void stopElection(Message emessage) throws RemoteException {
        LinkedList<IMember> llist = this.getMembers();
        IMember leader = this.parentGroup.getMembersList().get(this.getName());

        int position = llist.indexOf(leader);

        this.setElectionParticipant(false);

        this.setGroupLeader(true);
        this.getParentGroup().setLeader(leader);
        emessage.setType(MESSAGE_TYPE.ELECTED);
        emessage.setSource(leader);
        System.out.println("#NEW LEADER: " + emessage.getType() + " " + this.getName() + " [" + this.getIdentifier() + "] " + isElectionParticipant() + " EMSG_ID: " + emessage.getMessageID());
        electionCompleted(leader);
        this.getNeighbour(position).voteElection(emessage);
    }

    public void electionCompleted(IMember member) throws RemoteException {
        propertyChangeSupport.firePropertyChange("ElectionFinished", signal - 1, member);
    }

    public void multicastCausal(Message message) throws RemoteException {
        System.out.println(message.getType() + " " + message.getMessage() + " ");
        initVectorClock();
        updateVectorCell(this.getName());
        message.setVectorClock(this.getVectorClock());
        HashMap<String, IMember> membersList = this.parentGroup.getMembersList();
        for (String key : membersList.keySet()) {
            IMember m = membersList.get(key);
            m.deliverCausal(message);
            System.out.print(key + " ");
        }
        System.out.print(" multicasted\n");
    }

    public void deliverCausal(Message message) throws RemoteException {
        boolean flag = false;
        holdingQueue.add(message);
        System.out.println("Message hold: " + message.getMessage());
        messageReceived(message);
        //this.releaseMessages(message);
    }

    public void messageReceived(Message message) {
        propertyChangeSupport.firePropertyChange("MessageReceived", signal - 1, message);
    }

    public void releaseMessages(Message message) throws RemoteException {
        if (getVectorClock().get(getName()) == getVectorClock().get(message.getSource().getName()) && compareClock(message.getVectorClock())) {
            holdingQueue.remove(message);
            System.out.println("Message released: " + message.getMessage());

            propertyChangeSupport.firePropertyChange("MessageReleased", signal - 1, message);

            Message rmessage = new Message(this.getParentGroup().getGroupName(), this.parentGroup.getMembersList().get(this.getName()), "Acknowledgement", MESSAGE_TYPE.ACKNOWLEDGEMENT);
            message.getSource().getAcknowledgement(rmessage);
        }
    }

    private boolean compareClock(HashMap<String, Integer> vectorClock) {
        boolean flag = true;
        for (String memName : this.getVectorClock().keySet()) {
            if (!(memName.equals(this.getName()))) {
                Integer k1 = this.getVectorClock().get(memName);
                Integer k2 = vectorClock.get(memName);
                if (k1 > k2) {
                    flag &= false;
                    return flag;
                }
            }
        }
        return flag;
    }

    public void getAcknowledgement(Message message) throws RemoteException {
        propertyChangeSupport.firePropertyChange("AckReceived", signal - 1, message);
    }

    public void initVectorClock() throws RemoteException {
        HashMap<String, IMember> membersList = this.parentGroup.getMembersList();
        for (String key : membersList.keySet()) {
            IMember m = membersList.get(key);
            getVectorClock().put(m.getName(), 0);
        }
    }

    public void updateVectorCell(String memName) {
        Integer pi = getVectorClock().get(memName);
        pi += 1;
        getVectorClock().put(memName, pi);
    }

    /**
     * @return the vectorClock
     */
    public HashMap<String, Integer> getVectorClock() {
        return vectorClock;
    }

    /**
     * @param vectorClock the vectorClock to set
     */
    public void setVectorClock(HashMap<String, Integer> vectorClock) {
        this.vectorClock = vectorClock;
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
        election = new Election(this);
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

    /**
     * @return the holdingQueue
     */
    public LinkedList<Message> getHoldingQueue() {
        return holdingQueue;
    }

    /**
     * @param holdingQueue the holdingQueue to set
     */
    public void setHoldingQueue(LinkedList<Message> holdingQueue) {
        this.holdingQueue = holdingQueue;
    }

    /**
     * @return the srv
     */
    public RMIServer getSrv() {
        return srv;
    }

    /**
     * @param srv the srv to set
     */
    public void setSrv(RMIServer srv) {
        this.srv = srv;
    }

    public void killProcess() throws RemoteException {
        HashMap<String, IMember> membersList = this.parentGroup.getMembersList();
        for (String key : membersList.keySet()) {
            IMember m = membersList.get(key);
            if (!m.isGroupLeader()) {
                m.kill();
            }
        }
        propertyChangeSupport.firePropertyChange("Kill", null, null);
    }

    public void kill() throws RemoteException {
        propertyChangeSupport.firePropertyChange("Kill", null, null);
    }

}
