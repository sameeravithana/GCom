/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author SamTube405
 */
public class Message implements gcom.interfaces.Message{
    private String group_name;
    private Member source;
    private Member destination;
    private Serializable message;
    private TYPE_MESSAGE type;
            
    public Message(String group_name, Member source, Serializable message, TYPE_MESSAGE type) {
        this.group_name=group_name;
        this.source=source;
        this.message=message;
        this.type=type;
    }
    
     public Message(Serializable message, TYPE_MESSAGE type) {        
        this.message=message;
        this.type=type;
    }

    public Serializable getMessage() {
        return this.message;
    }

    public TYPE_MESSAGE getMessageType() {
        return this.type;
    }

    public String getGroupName() {
        return group_name;
    }

    public Member getSource() {
        return source;
    }

    public void setDestination(Member destination) {
        this.destination=destination;
    }

    public Member getDestination() {
        return this.destination;
    }
    
    public HashMap<String, Integer> getGroups(){
        return GroupManagement.getGroups();
    }
    
}
