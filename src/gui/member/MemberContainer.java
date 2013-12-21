/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.member;

import gcom.interfaces.IMember;

/**
 *
 * @author praneeth
 */
public class MemberContainer {

    private IMember member;
    private MemberWindow chatWindow;
    private IMember stub;
    private int counter;

    /**
     * @return the member
     */
    public IMember getMember() {
        return member;
    }

    /**
     * @param member the member to set
     */
    public void setMember(IMember member) {
        this.member = member;
    }

    /**
     * @return the chatWindow
     */
    public MemberWindow getChatWindow() {
        return chatWindow;
    }

    /**
     * @param chatWindow the chatWindow to set
     */
    public void setChatWindow(MemberWindow chatWindow) {
        this.chatWindow = chatWindow;
    }

    /**
     * @return the stub
     */
    public IMember getStub() {
        return stub;
    }

    /**
     * @param stub the stub to set
     */
    public void setStub(IMember stub) {
        this.stub = stub;
    }
}
