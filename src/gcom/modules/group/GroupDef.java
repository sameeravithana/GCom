/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author ens13pps
 */
public class GroupDef {

    private String groupName;
    private int groupType, comType, ordType;
    public static final int GROUPTYPE_STATIC = 0;
    public static final int GROUPTYPE_DYNAMIC = 1;
    public static final int COMTYPE_BASIC = 2;
    public static final int COMTYPE_RELIABLE = 3;
    public static final int ORDTYPE_UNORDERED = 4;
    public static final int ORDTYPE_CAUSAL = 5;

    public GroupDef() {
    }

    public GroupDef(String groupName, int groupType, int comType, int ordType) throws IOException {
        this.groupName = groupName;
        this.groupType = groupType;
        this.comType = comType;
        this.ordType = ordType;

    }

    public GroupDef(String groupName, String groupType, String comType, String ordType) {
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
    public int getComType() {
        return comType;
    }

    /**
     * @param comType the comType to set
     */
    public void setComType(int comType) {
        this.comType = comType;
    }

    /**
     * @return the ordType
     */
    public int getOrdType() {
        return ordType;
    }

    /**
     * @param ordType the ordType to set
     */
    public void setOrdType(int ordType) {
        this.ordType = ordType;
    }
}
