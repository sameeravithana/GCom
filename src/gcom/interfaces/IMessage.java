/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.interfaces;

import gcom.modules.group.Member;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author samtube405
 */
public interface IMessage extends Serializable{
    public static enum TYPE_MESSAGE {
		APPLICATION, JOINREQUEST, PARTREQUEST, PARTRESPONSE,
		REJECT, WELCOME, GOTMEMBER, LOSTMEMBER,
		ELECTION, CLOSE,UPDATESTATUS
	};
    public String getMessage();
    
    public TYPE_MESSAGE getMessageType();
    
    public String getGroupName();

    public IMember getSource();
    
    public void setDestination(Member destination);
    
    public Member getDestination();
    
    public ArrayList<String> getParams();
    
}
