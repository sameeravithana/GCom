/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom.interfaces;

/**
 *
 * @author praneeth
 */
public enum MESSAGE_TYPE {

    GROUP_CREATED, APPLICATION, JOIN_REQUEST, PARTIAL_REQUEST, PARTIAL_RESPONSE,
    REJECT, WELCOME, MEMBER_JOINED, MEMBER_LEFT,
    ELECTION, ELECTED, MEMBER_LEAVES, UPDATE_STATUS, ACKNOWLEDGEMENT, CAUSAL, UNORDERED, BASIC, RELIABLE
}
