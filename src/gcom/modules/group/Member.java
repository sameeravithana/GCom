/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author praneeth
 */
public class Member implements Remote, Serializable {

    private String name;
    private int identifier;
    private Group parentGroup;
    private boolean isElectionParticipant;
    private boolean isGroupLeader;
    private HashMap<String, Member> members;

    public Member(String name, Group parent) {
        this.parentGroup = parent;
        this.name = name;
        identifier = new Random().nextInt(100) + 1;
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
}
