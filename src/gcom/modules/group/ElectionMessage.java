/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.modules.group;

/**
 *
 * @author Praneeth
 */
public class ElectionMessage {

    private Member sender;
    private Member currentLeader;

    public ElectionMessage(Member sender) {
        this.sender = sender;
    }

    /**
     * @return the currentLeader
     */
    public Member getCurrentLeader() {
        return currentLeader;
    }

    /**
     * @param currentLeader the currentLeader to set
     */
    public void setCurrentLeader(Member currentLeader) {
        this.currentLeader = currentLeader;
    }
}
