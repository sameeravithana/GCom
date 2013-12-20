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
    
     

    public String getMessage() {
        return this.message;
    }
    
    public ArrayList<String> getParams(){
        return this.params;
    }

    public TYPE_MESSAGE getMessageType() {
        return this.type;
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

    

  
    
   
    
}
