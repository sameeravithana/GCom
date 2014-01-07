/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.interfaces.MESSAGE_TYPE;
import java.io.Serializable;

/**
 *
 * @author ens13pps
 */
public class GroupDef implements Serializable {

    private String groupName;
    private int groupType;
    private MESSAGE_TYPE ordType;
    private MESSAGE_TYPE comType;
    private int maxMembers;

    public GroupDef() {
    }

    public GroupDef(String groupName, String groupType, int membersCount, MESSAGE_TYPE comType, MESSAGE_TYPE ordType) throws GroupManagementException {
        this.groupName = groupName;
        if (groupType.equalsIgnoreCase("static")) {
            this.groupType = Group.STATIC_GROUP;
        } else if (groupType.equalsIgnoreCase("dynamic")) {
            this.groupType = Group.DYNAMIC_GROUP;
        } else {
            throw new GroupManagementException("Invalid group type : " + groupType);
        }
        this.ordType = ordType;
        this.comType = comType;
        this.maxMembers = membersCount;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the groupType
     */
    public int getGroupType() {
        return groupType;
    }

    /**
     * @param groupType the groupType to set
     */
    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    /**
     * @return the comType
     */
    public MESSAGE_TYPE getComType() {
        return comType;
    }

    /**
     * @param comType the comType to set
     */
    public void setComType(MESSAGE_TYPE comType) {
        this.comType = comType;
    }

    /**
     * @return the ordType
     */
    public MESSAGE_TYPE getOrdType() {
        return ordType;
    }

    /**
     * @param ordType the ordType to set
     */
    public void setOrdType(MESSAGE_TYPE ordType) {
        this.ordType = ordType;
    }

    /**
     * @return the comType
     */
    public MESSAGE_TYPE getMultType() {
        return comType;
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
}
