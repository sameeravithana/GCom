/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import java.util.HashMap;

/**
 *
 * @author praneeth
 */
public class GroupManagement {

    private static HashMap<String, Group> groups;
    private static String defaultGroupIdPrefix = "G";
    public static final int DEFAULT_MAX_MEMBERS = 1000;

    static {
        groups = new HashMap<String, Group>();
    }

    public static void createGroup(String groupId) throws GroupManagementException {
        createGroup(groupId, DEFAULT_MAX_MEMBERS);
    }

    public static Group createGroup(String groupId, int maxMembers) throws GroupManagementException {
        if (groups.containsKey(groupId)) {
            throw new GroupManagementException("Duplicate GroupID : " + groupId + " already exists.");
        } else {
            Group g = new Group(groupId, maxMembers);
            groups.put(groupId, g);
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

    public void addMember(String groupId, Member member) throws GroupManagementException {
        if (!groups.containsKey(groupId)) {
            throw new GroupManagementException("Invalid GroupID : " + groupId + " doeas not exist.");
        } else {
            Group group = groups.get(groupId);
            group.addMember(member);
        }
    }

    public Member getMember(String groupId, String memberId) throws GroupManagementException {
        if (!groups.containsKey(groupId)) {
            throw new GroupManagementException("Invalid GroupID : " + groupId + " doeas not exist.");
        } else {
            Group group = groups.get(groupId);
            return group.getMember(memberId);
        }
    }

    public void removeMember(String groupId, String memberId) throws GroupManagementException {
        if (!groups.containsKey(groupId)) {
            throw new GroupManagementException("Invalid GroupID : " + groupId + " doeas not exist.");
        } else {
            Group group = groups.get(groupId);
            group.removeMember(memberId);
        }
    }

    public static void sendMessage(String groupId, GroupMessage message) throws GroupManagementException {
        if (groups.containsKey(groupId)) {
            Group group = groups.get(groupId);
            group.send(message);
        } else {
            throw new GroupManagementException("Invalid GroupID : " + groupId + " doeas not exist.");
        }
    }
}
