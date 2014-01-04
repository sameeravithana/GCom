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
    private String groupType, comType;
    MESSAGE_TYPE ordType;
    private MESSAGE_TYPE multType;

    public GroupDef() {
    }

    public GroupDef(String groupName, String groupType, String comType, MESSAGE_TYPE ordType) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.comType = comType;
        this.ordType = ordType;
    }
    
     public GroupDef(String groupName, String groupType, MESSAGE_TYPE multType, MESSAGE_TYPE ordType) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.comType = comType;
        this.ordType = ordType;
        this.multType = multType;
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
     * @return the multType
     */
    public MESSAGE_TYPE getMultType() {
        return multType;
    }
}
