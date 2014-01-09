/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.interfaces.IMember;
import gcom.interfaces.IMessage;
import gcom.interfaces.MESSAGE_TYPE;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author SamTube405
 */
public class Message implements IMessage {

    private String group_name;
    private IMember source;
    private IMember destination;
    private String message;
    private int messageID;
    private int position;
    ArrayList<String> params;
    private MESSAGE_TYPE multicastType;
    private MESSAGE_TYPE orderType;
    private HashMap<String, Integer> vectorClock;
    private long timeStamp;

    public Message(String group_name, IMember source, String message, MESSAGE_TYPE type) {
        timeStamp = new Date().getTime();
        this.group_name = group_name;
        this.source = source;
        this.message = message;
        this.orderType = type;
    }

    public Message(String group_name, IMember source, ArrayList<String> message, MESSAGE_TYPE type) {
        timeStamp = new Date().getTime();
        this.group_name = group_name;
        this.source = source;
        this.params = message;
        this.orderType = type;
    }

    public Message(String group_name, int pos, int message, MESSAGE_TYPE type) {
        timeStamp = new Date().getTime();
        this.group_name = group_name;
        this.position = pos;
        this.messageID = message;
        this.orderType = type;
    }

    public Message(String group_name, IMember source, HashMap<String, Integer> vectorClock, String message, MESSAGE_TYPE multicastType, MESSAGE_TYPE orderType) {
        timeStamp = new Date().getTime();
        this.group_name = group_name;
        this.source = source;
        this.vectorClock = vectorClock;
        this.message = message;
        this.multicastType = multicastType;
        this.orderType = orderType;
    }

    public String getMessage() {
        return this.message;
    }

    public ArrayList<String> getParams() {
        return this.params;
    }

    public MESSAGE_TYPE getMessageType() {
        return this.getMessageOrderType();
    }

    public String getGroupName() {
        return group_name;
    }

    public IMember getSource() {
        return source;
    }

    public void setDestination(IMember destination) {
        this.destination = destination;
    }

    public IMember getDestination() {
        return this.destination;
    }

    /**
     * @return the messageID
     */
    public int getMessageID() {
        return messageID;
    }

    /**
     * @param messageID the messageID to set
     */
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    /**
     * @return the orderType
     */
    public MESSAGE_TYPE getMessageOrderType() {
        return orderType;
    }

    /**
     * @param orderType the orderType to set
     */
    public void setType(MESSAGE_TYPE type) {
        this.orderType = type;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the vectorClock
     */
    public HashMap<String, Integer> getVectorClock() {
        return vectorClock;
    }

    /**
     * @param vectorClock the vectorClock to set
     */
    public void setVectorClock(HashMap<String, Integer> vectorClock) {
        this.vectorClock = vectorClock;
    }

    /**
     * @param source the source to set
     */
    public void setSource(IMember source) {
        this.source = source;
    }

    public static MESSAGE_TYPE getMessageOrderingMode(String mode) throws Exception {
        if (mode.equalsIgnoreCase("unordered")) {
            return MESSAGE_TYPE.UNORDERED;
        } else if (mode.equalsIgnoreCase("causal")) {
            return MESSAGE_TYPE.CAUSAL;
        } else {
            throw new Exception("Invalid Message Ordering Mode..");
        }
    }

    public static MESSAGE_TYPE getMulticastMode(String mode) throws Exception {
        if (mode.equalsIgnoreCase("basic")) {
            return MESSAGE_TYPE.BASIC;
        } else if (mode.equalsIgnoreCase("reliable")) {
            return MESSAGE_TYPE.RELIABLE;
        } else {
            throw new Exception("Invalid Message Ordering Mode..");
        }
    }

    /**
     * @return the multicastType
     */
    public MESSAGE_TYPE getMulticastType() {
        return multicastType;
    }

    /**
     * @return the timeStamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }
}
