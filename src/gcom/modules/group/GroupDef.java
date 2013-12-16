/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

/**
 *
 * @author ens13pps
 */
public class GroupDef {

    private String groupName;
    private String groupType, comType, ordType;

    public GroupDef() {
    }

    public GroupDef(String groupName, String groupType, String comType, String ordType) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.comType = comType;
        this.ordType = ordType;
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
    public String getGroupType() {
        return groupType;
    }

    /**
     * @param groupType the groupType to set
     */
    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    /**
     * @return the comType
     */
    public String getComType() {
        return comType;
    }

    /**
     * @param comType the comType to set
     */
    public void setComType(String comType) {
        this.comType = comType;
    }

    /**
     * @return the ordType
     */
    public String getOrdType() {
        return ordType;
    }

    /**
     * @param ordType the ordType to set
     */
    public void setOrdType(String ordType) {
        this.ordType = ordType;
    }
}
