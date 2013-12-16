/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.modules.com.CommunicationMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author praneeth
 */
public class Group {
    
    private String groupID;
    private HashMap<String, Member> members;
    private CommunicationMode comMode;
    private int maxMembers = 1000;
    private static int counter;
    
    public Group() {
        this("G-" + counter++);
    }
    
    public Group(String groupID) {
        this.groupID = groupID;
        
        members = new HashMap<String, Member>();
    }
    
    public Group(String groupID, int maxMembers) {
        this(groupID);
        this.maxMembers = maxMembers;
    }
    
    public void addMember(Member member) throws GroupManagementException {
        String memberId = member.getId();
        if (members.containsKey(member.getId())) {
            throw new GroupManagementException("Duplicate Member Id : Member id" + memberId + " already exists in " + groupID);
        } else {
            members.put(memberId, member);
        }
    }
    
    public void removeMember(String memberId) throws GroupManagementException {
        if (members.containsKey(memberId)) {
            members.remove(memberId);
        } else {
            throw new GroupManagementException("Invalid Member Id : Member id" + memberId + " does not exist in " + groupID);
        }
    }
    
    public Member getMember(String memberId) throws GroupManagementException {
        if (members.containsKey(memberId)) {
            return members.get(memberId);
        } else {
            throw new GroupManagementException("Invalid Member Id : Member id" + memberId + " does not exist in " + groupID);
        }
    }
    
    public ArrayList<Member> getMembersList() {
        return new ArrayList(members.values());
    }

    /**
     * @return the groupID
     */
    public String getGroupID() {
        return groupID;
    }

    /**
     * @return the comMode
     */
    public CommunicationMode getComMode() {
        return comMode;
    }

    /**
     * @param comMode the comMode to set
     */
    public void setComMode(CommunicationMode comMode) {
        this.comMode = comMode;
    }

    /**
     * @return the maxMembers
     */
    public int getMaxMembers() {
        return maxMembers;
    }

    /**
     * @param maxMembers the maxMembers to set
     */
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }
    
    public void send(GroupMessage message) {
        Set<String> keySet = members.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            Member m = members.get(next);
            send(m, message);
        }
    }

    public void initiateElection(Member initiater) {
        ArrayList<Member> membersList = getMembersList();
        for (Member m : membersList) {
            m.setIsElectionParticipant(false);
        }
        initiater.setIsElectionParticipant(true);

        // make the initiater 1st in the list
        membersList.remove(initiater);
        membersList.add(0, initiater);
        
    }
    
    private void send(Member member, GroupMessage message) {
        // call members server method ?
    }
    
    @Override
    public void finalize() throws Throwable {
        super.finalize();
        System.out.println("Group " + groupID + " is destroyed.");
    }
}
