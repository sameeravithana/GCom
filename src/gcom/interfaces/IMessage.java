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
public interface IMessage extends Serializable {

    public String getMessage();

    public MESSAGE_TYPE getMessageType();

    public String getGroupName();

    public IMember getSource();

    public void setDestination(IMember destination);

    public IMember getDestination();

    public ArrayList<String> getParams();

}
