/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.RMIServer;
import gcom.interfaces.IGroupManagement;
import gcom.interfaces.IMember;
import gcom.interfaces.IMessage;
import gui.GComWindow;
import gui.member.NewMember;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author praneeth
 */
public class GroupManagement implements IGroupManagement {

    private static HashMap<String, Group> groups;
    private static String defaultGroupIdPrefix = "G";
    public static final int DEFAULT_MAX_MEMBERS = 1000;
    private static GComWindow gCom;

    static {
        groups = new HashMap<String, Group>();
    }

    public static void setGComWindow(GComWindow gComWindow) {
        gCom = gComWindow;
    }

    public static Group createGroup(GroupDef gDef) throws GroupManagementException {
        String gName = gDef.getGroupName();
        if (groups.containsKey(gName)) {
            throw new GroupManagementException("Duplicate Group Name : " + gName + " already exists.");
        } else {
            Group g = new Group(gDef);
            groups.put(gName, g);
            return g;
        }
    }

    public static void deleteGroup(String groupId) throws GroupManagementException {
        if (!groups.containsKey(groupId)) {
            throw new GroupManagementException("Invalid GroupID : " + groupId + " doeas not exist.");
        } else {
            groups.remove(groupId);
        }
    }

//    public void addMember(String groupId, Member member) throws GroupManagementException {
//        if (!groups.containsKey(groupId)) {
//            throw new GroupManagementException("Invalid GroupID : " + groupId + " doeas not exist.");
//        } else {
//            Group group = groups.get(groupId);
//            group.addMember(member);
//        }
//    }
//
//    public Member getMember(String groupId, String memberId) throws GroupManagementException {
//        if (!groups.containsKey(groupId)) {
//            throw new GroupManagementException("Invalid GroupID : " + groupId + " doeas not exist.");
//        } else {
//            Group group = groups.get(groupId);
//            return group.getMember(memberId);
//        }
//    }

    public void removeMember(String groupId, String memberId) throws GroupManagementException {
        if (!groups.containsKey(groupId)) {
            throw new GroupManagementException("Invalid GroupID : " + groupId + " doeas not exist.");
        } else {
            Group group = groups.get(groupId);
            group.removeMember(memberId);
        }
    }

    public static void sendMessage(String groupId, Message message) throws GroupManagementException {
        if (groups.containsKey(groupId)) {
            Group group = groups.get(groupId);
            group.send(message);
        } else {
            throw new GroupManagementException("Invalid GroupID : " + groupId + " doeas not exist.");
        }
    }

    private static HashMap<String, Integer> getGroups() {
        HashMap<String, Integer> gs = new HashMap<String, Integer>();
        for (String key : groups.keySet()) {
            gs.put(key, groups.get(key).getMemberCount());
        }
        return gs;
    }

    public HashMap<String, Integer> getGroupDetails() {
        return getGroups();
    }

    public IMember sendRequest(gcom.modules.group.Message message) throws RemoteException {

        Group parent = groups.get(message.getParams().get(0));        
        IMember m = message.getSource();
        try {       
            parent.addMember(m);
            
            m.addMember(m);
            
            
        } catch (GroupManagementException ex) {
            Logger.getLogger(GroupManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
            m.setParentGroup(parent);            
            //updateStatus(m, IMessage.TYPE_MESSAGE.UPDATESTATUS);        
            m.setGroupLeader(true);

       
        
        return m;

    }

    public void updateStatus(IMessage message) throws RemoteException, GroupManagementException {
        if (message.getMessageType() == IMessage.TYPE_MESSAGE.UPDATESTATUS) {
            gCom.updateStatus(message.getParams());
        } else {
            throw new GroupManagementException("Invalid message type.");
        }
    }

    public void updateStatus(Member member, IMessage.TYPE_MESSAGE type) throws RemoteException, GroupManagementException {
        gCom.updateStatus(member, type);
    }
}
