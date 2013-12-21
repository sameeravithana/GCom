/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

import gcom.interfaces.IMember;
import java.util.ArrayList;

/**
 *
 * @author SamTube405
 */
public class Message implements gcom.interfaces.IMessage{
    private String group_name;
    private IMember source;  
    private Member destination;
    private String message;
    private int imessage;
    private int position;
    ArrayList<String> params;
    private TYPE_MESSAGE type;
            
    public Message(String group_name, Member source, String message, TYPE_MESSAGE type) {
        this.group_name=group_name;
        this.source=source;
        this.message=message;
        this.type=type;
    }
    
   
    
    public Message(String group_name, IMember source, ArrayList<String> message, TYPE_MESSAGE type) {
        this.group_name=group_name;
        this.source=source;
        this.params=message;
        this.type=type;
    }
    
    public Message(String group_name, int pos, int message, TYPE_MESSAGE type) {
        this.group_name=group_name;
        this.position=pos;
        this.imessage=message;
        this.type=type;
    }
    
     

    public String getMessage() {
        return this.message;
    }
    
    public ArrayList<String> getParams(){
        return this.params;
    }

    public TYPE_MESSAGE getMessageType() {
        return this.getType();
    }

    public String getGroupName() {
        return group_name;
    }

    public IMember getSource() {
        return source;
    }

    public void setDestination(Member destination) {
        this.destination=destination;
    }

    public Member getDestination() {
        return this.destination;
    }

    /**
     * @return the imessage
     */
    public int getImessage() {
        return imessage;
    }

    /**
     * @param imessage the imessage to set
     */
    public void setImessage(int imessage) {
        this.imessage = imessage;
    }

    /**
     * @return the type
     */
    public TYPE_MESSAGE getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TYPE_MESSAGE type) {
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
