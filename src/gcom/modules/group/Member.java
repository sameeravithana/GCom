/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import java.rmi.Remote;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author praneeth
 */
public class Member implements Remote {

    private String id;
    private int identifier;
    private Group parentGroup;
    private boolean isElectionParticipant = false;
    private boolean isGroupLeader = false;
    private HashMap<String, Member> members;

    public Member(Group parent) {
        this.parentGroup = parent;
        identifier = new Random().nextInt(100) + 1;
    }

    public Member() {
        this(null);
    }

    public void callElection() {
        parentGroup.initiateElection(this);
        ElectionMessage em = new ElectionMessage(this);
    }

    public void vote() {
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
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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
    public boolean isIsElectionParticipant() {
        return isElectionParticipant;
    }

    /**
     * @param isElectionParticipant the isElectionParticipant to set
     */
    public void setIsElectionParticipant(boolean isElectionParticipant) {
        this.isElectionParticipant = isElectionParticipant;
    }
}
