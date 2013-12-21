/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.member;

import java.util.Date;

/**
 *
 * @author Praneeth
 */
public class Contact {

    private String id;
    private String name;
    private Date lastSeen;
    private Status status;
}

enum Status {

    Online, Busy, Away, Offline
}
