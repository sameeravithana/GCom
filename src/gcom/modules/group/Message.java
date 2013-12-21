/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.interfaces.IMember;
import gcom.interfaces.IMessage;
import gcom.interfaces.MESSAGE_TYPE;
import java.util.ArrayList;

/**
 *
 * @author SamTube405
 */
public class Message implements IMessage {

    private String group_name;
    private IMember source;
    private Member destination;
    private String message;
    private int messageID;
    private int position;
    ArrayList<String> params;
    private MESSAGE_TYPE type;

    public Message(String group_name, Member source, String message, MESSAGE_TYPE type) {
        this.group_name = group_name;
        this.source = source;
        this.message = message;
        this.type = type;
    }

    public Message(String group_name, IMember source, ArrayList<String> message, MESSAGE_TYPE type) {
        this.group_name = group_name;
        this.source = source;
        this.params = message;
        this.type = type;
    }

    public Message(String group_name, int pos, int message, MESSAGE_TYPE type) {
        this.group_name = group_name;
        this.position = pos;
        this.messageID = message;
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public ArrayList<String> getParams() {
        return this.params;
    }

    public MESSAGE_TYPE getMessageType() {
        return this.getType();
    }

    public String getGroupName() {
        return group_name;
    }

    public IMember getSource() {
        return source;
    }

    public void setDestination(Member destination) {
        this.destination = destination;
    }

    public Member getDestination() {
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
     * @return the type
     */
    public MESSAGE_TYPE getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(MESSAGE_TYPE type) {
        this.type = type;
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

}
