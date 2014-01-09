/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.member;

import gcom.interfaces.IMember;
import gcom.modules.group.Group;
import gcom.modules.group.Message;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignalListener implements PropertyChangeListener {

    private MemberWindow memWindow;

    public SignalListener(MemberWindow memWindow) {
        this.memWindow = memWindow;
    }

    public SignalListener() {
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("MemberAdded")) {
            try {
                memWindow.updateMembers((IMember) evt.getNewValue());
            } catch (RemoteException ex) {
                Logger.getLogger(SignalListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (evt.getPropertyName().equals("ElectionFinished")) {
            try {
                memWindow.electionCompleted((IMember) evt.getNewValue());
            } catch (RemoteException ex) {
                Logger.getLogger(SignalListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (evt.getPropertyName().equals("MessageReceived")) {
            try {
                memWindow.messageReceived((Message) evt.getNewValue());
            } catch (Exception ex) {
                Logger.getLogger(SignalListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (evt.getPropertyName().equals("MessageReleased")) {
            try {
                memWindow.messageReleased((Message) evt.getNewValue());
            } catch (Exception ex) {
                Logger.getLogger(SignalListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (evt.getPropertyName().equals("MyOwnMessageReleased")) {
            try {
                memWindow.myOwnMessageReleased((Message) evt.getNewValue());
            } catch (Exception ex) {
                Logger.getLogger(SignalListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if (evt.getPropertyName().equals("VectorReceived")) {
            try {
                memWindow.vectorReceived(evt.getOldValue(),evt.getNewValue());
            } catch (Exception ex) {
                Logger.getLogger(SignalListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (evt.getPropertyName().equals("AckReceived")) {
            try {
                memWindow.ackReceived((Message) evt.getNewValue());
            } catch (Exception ex) {
                Logger.getLogger(SignalListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (evt.getPropertyName().equals("MemberLeft")) {
            try {
                memWindow.updateMembers((Group) evt.getOldValue(), (IMember) evt.getNewValue());
            } catch (Exception ex) {
                Logger.getLogger(SignalListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (evt.getPropertyName().equals("Kill")) {
            try {
                memWindow.killProcess();
            } catch (Exception ex) {
                Logger.getLogger(SignalListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
