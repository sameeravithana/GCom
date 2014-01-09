/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.interfaces.IGroupManagement;
import gcom.interfaces.IMember;
import gcom.interfaces.IMessage;
import gcom.interfaces.MESSAGE_TYPE;
import gui.GComWindow;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author praneeth
 */
public class GroupManagement implements IGroupManagement {

    private static HashMap<String, Group> groups;
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

    @Override
    public IMember sendRequest(gcom.modules.group.Message message) throws RemoteException {

        Group parent = groups.get(message.getParams().get(0));
        if (parent.getGroupType() == Group.STATIC_GROUP && parent.isFilled()) {
            return null;
        }
        IMember m = message.getSource();
        try {
            parent.addMember(m);
            m.addMember(m);

        } catch (GroupManagementException ex) {
            Logger.getLogger(GroupManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        m.setParentGroup(parent);
        parent.setLeader(m);
        groups.put(parent.getGroupName(), parent);
        m.setGroupLeader(true);
        m.initVectorClock();
        return m;

    }

    public void updateStatus(IMessage message) throws RemoteException, GroupManagementException {
        if (message.getMessageType() == MESSAGE_TYPE.UPDATE_STATUS) {
            gCom.updateStatus(message.getParams());
        } else {
            throw new GroupManagementException("Invalid message type.");
        }
    }

    public void updateStatus(Message member, MESSAGE_TYPE type) throws RemoteException, GroupManagementException {
        gCom.updateStatus(member, type);
    }

    public void updateGroup(Group group) throws RemoteException, GroupManagementException {
        groups.put(group.getGroupName(), group);
    }

    public void addMember(Group group, IMember member) throws RemoteException, GroupManagementException {
        gCom.addMember(group, member);
    }

    public static void removeGroupMulticast(String groupName) throws RemoteException, GroupManagementException {

        try {
            gCom.getServer().regMemLookUp(groupName).killProcess();
        } catch (Exception e) {
            Logger.getLogger(GroupManagement.class.getName()).log(Level.INFO, "Group Remove Success!", "");
        }

        groups.remove(groupName);
    }

}
