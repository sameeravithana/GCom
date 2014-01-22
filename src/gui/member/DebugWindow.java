/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DebugWindow.java
 *
 * Created on Dec 20, 2013, 3:23:07 PM
 */
package gui.member;

import gcom.interfaces.IMember;
import gcom.interfaces.MESSAGE_TYPE;
import gcom.modules.group.Group;
import gcom.modules.group.Member;
import gcom.modules.group.Message;
import gui.GComWindow;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ens13pps
 */
public class DebugWindow extends javax.swing.JFrame {

    /**
     * Creates new form DebugWindow
     */
    private IMember member;
    private MemberWindow memWindow;
    private String memName;
    //private Member member;
    private DefaultTableModel dtm;
    private IMember stub;
    private boolean autoRelease = false;
    private LinkedList<Message> holdback, messages;
    private SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");

    public DebugWindow(MemberWindow memWindow, Member member, IMember stub) {
        this.stub = stub;
        initComponents();
        this.member = member;
        setIconImage(new ImageIcon(GComWindow.class.getResource("/pics/logo.png")).getImage());
        this.memWindow = memWindow;
        messages = new LinkedList<Message>();
        try {
            memName = member.getName();
        } catch (Exception ex) {
            Logger.getLogger(DebugWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties p = new Properties();
        try {
            p.load(new FileReader("./client.properties"));
            autoRelease = Boolean.valueOf(p.getProperty("autoRelease"));
            chkHold.setSelected(!autoRelease);
        } catch (IOException ex) {
            Logger.getLogger(MemberWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initialize() {
        try {
            setTitle("Debug : " + memName + " of " + member.getParentGroup().getGroupName());
        } catch (RemoteException ex) {
            Logger.getLogger(DebugWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void messageReceived(Message message) throws RemoteException {
        updateStatus(message.getMulticastType() + " Multicast message received from " + message.getSource().getName());
        if (!autoRelease) {
            holdback = member.getHoldingQueue();
            fillHoldingQueue(holdback);
        } else {
            member.releaseMessages(message);
            releaseHoldback();
            updateStatus(message.getMulticastType() + " Multicast message from " + message.getSource().getName() + " released.");
        }        
    }
    
    //Try to release blocked messages
    public void releaseHoldback() throws RemoteException{        
        for(Message hmessage:member.getHoldingQueue()){
            member.releaseMessages(hmessage);
        }
    }

    public void messageReleased(Message message) throws RemoteException {
        holdback = member.getHoldingQueue();
        fillHoldingQueue(holdback);
        messages.add(message);
        dtm = (DefaultTableModel) tblMessages.getModel();
        dtm.addRow(new Object[]{message.getSource().getName(), message.getDestination().getName(), message.getMessage(), new SimpleDateFormat("HH:mm:ss").format(new Date(message.getTimeStamp()))});
        fillHoldingQueue(holdback);
        updateStatus("Message from " + message.getSource().getName() + " released.");
        //JOptionPane.showMessageDialog(this, message.getMessage());
    }

    private void fillHoldingQueue(LinkedList<Message> holdback) throws RemoteException {
        dtm = (DefaultTableModel) tblHoldMessages.getModel();
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        for (Message message : holdback) {
            String format = sd.format(new Date(message.getTimeStamp()));
            Object row[] = new Object[]{message.getSource().getName(), message.getDestination().getName(), message.getMessage(), format};
            dtm.addRow(row);
        }
    }

    public void updateStatus(String message) {
        txtLog.setText(txtLog.getText() + message + "\n");
    }

    public void updateMemberTable() throws RemoteException {
        dtm = (DefaultTableModel) tblMembers.getModel();
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        Collection<IMember> members = member.getParentGroup().getMembersList().values();
        for (IMember m : members) {
            dtm.addRow(new Object[]{m.getName(), (m.isGroupLeader() ? "Leader" : "Member"), sd.format(m.getJoined()), m.getIdentifier()});
        }
    }

    /**
     *
     * @param leader
     * @throws RemoteException
     */
    public void updateLeaderInTable(IMember leader) throws RemoteException {
        dtm = (DefaultTableModel) tblMembers.getModel();
        Vector<Object[]> rows = new Vector<Object[]>();
        for (int i = 0; i < dtm.getRowCount(); i++) {
            String role = "Member";

            if (dtm.getValueAt(i, 0).toString().equals(leader.getName())) {
                role = "Leader";
            }
            rows.add(new Object[]{dtm.getValueAt(i, 0), role, dtm.getValueAt(i, 2), dtm.getValueAt(i, 3)});
        }
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        for (Object[] o : rows) {
            dtm.addRow(o);
        }
    }

    /**
     *
     * @param leader
     * @throws RemoteException
     */
    public void updateLeaderInTable(String leader) throws RemoteException {
        dtm = (DefaultTableModel) tblMembers.getModel();
        for (int i = 0; i < dtm.getRowCount(); i++) {

            String role = "Member";

            String tableValue = dtm.getValueAt(i, 0).toString();

            if (tableValue.equals(leader)) {
                role = "Leader";
            }
            dtm.setValueAt(role, i, 1);
        }
    }

    public void startElection() {
        try {
            if (member.getParentGroup().getGroupType() == Group.DYNAMIC_GROUP || member.getParentGroup().isFilled()) {
                Message emessage = new Message(member.getParentGroup().getGroupName(), member.getMembers().indexOf(member), member.getIdentifier(), MESSAGE_TYPE.ELECTION);
                if (member.getParentGroup().getMemberCount() > 1) {
                    Logger.getLogger(NewMember.class.getName()).log(Level.INFO, "{0} starting the election.", member.getName());
                    member.setElectionParticipant(true);
                    member.callElection(emessage);
                } else {
                    member.callElection(emessage);
                    updateStatus("This process was selected as the group leader.");
                    Logger.getLogger(NewMember.class.getName()).log(Level.INFO, "{0} : You have no neighbours..So you're the leader.", member.getName());
                }

            } else {
                JOptionPane.showMessageDialog(this, "You cannot call for election until group is filled.", "Group is not stable.", JOptionPane.WARNING_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(NewMember.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void releaseMessage(Object... vals) {
        int row = tblHoldMessages.getSelectedRow();

        if (row != -1) {
            try {
                if (member.releaseMessages(holdback.get(row))) {
                    dtm = (DefaultTableModel) tblMessages.getModel();
                    dtm.addRow(vals);
                    fillHoldingQueue(holdback);
                    releaseHoldback();
                }
            } catch (RemoteException ex) {
                Logger.getLogger(DebugWindow.class.getName()).log(Level.SEVERE, "Remote Exception.", "");
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMembers = new javax.swing.JTable();
        btnElect = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        chkHold = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHoldMessages = new javax.swing.JTable();
        btnShuffle = new javax.swing.JButton();
        btnRelease = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblMessages = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblVectorClocks = new javax.swing.JTable();

        setTitle("Debug");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtLog.setEditable(false);
        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        tblMembers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Role", "Joined", "Identifier"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMembers.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblMembers);
        if (tblMembers.getColumnModel().getColumnCount() > 0) {
            tblMembers.getColumnModel().getColumn(1).setPreferredWidth(10);
            tblMembers.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblMembers.getColumnModel().getColumn(3).setPreferredWidth(5);
        }

        btnElect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/election.png"))); // NOI18N
        btnElect.setText("Start Election");
        btnElect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnElect, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnElect)
                        .addGap(0, 160, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(91, 91, 91))
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/pics/members.png")), jPanel2, "Members"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Message Queue"));

        chkHold.setSelected(true);
        chkHold.setText("Hold Messages");

        tblHoldMessages.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sender", "Receiver", "Message", "Timestamp"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblHoldMessages);
        if (tblHoldMessages.getColumnModel().getColumnCount() > 0) {
            tblHoldMessages.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblHoldMessages.getColumnModel().getColumn(3).setPreferredWidth(5);
        }

        btnShuffle.setText("Shuffle");
        btnShuffle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShuffleActionPerformed(evt);
            }
        });

        btnRelease.setText("Release");
        btnRelease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReleaseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRelease, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnShuffle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(chkHold)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkHold)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnShuffle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRelease)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblMessages.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sender", "Receiver", "Message", "Timestamp"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMessages.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tblMessages);
        if (tblMessages.getColumnModel().getColumnCount() > 0) {
            tblMessages.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblMessages.getColumnModel().getColumn(3).setPreferredWidth(5);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                .addGap(115, 115, 115))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane4.setViewportView(jPanel4);

        jTabbedPane2.addTab("Messages", jScrollPane4);

        tblVectorClocks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Current Value", "Received Value", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblVectorClocks);
        if (tblVectorClocks.getColumnModel().getColumnCount() > 0) {
            tblVectorClocks.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblVectorClocks.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblVectorClocks.getColumnModel().getColumn(2).setPreferredWidth(5);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(211, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Vector Clocks", jPanel5);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 329, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/pics/messages.png")), jPanel1, "Messages"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        memWindow.debugWindowClosed();
    }//GEN-LAST:event_formWindowClosing

    private void btnElectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElectActionPerformed
        new Thread() {

            public void run() {
                startElection();
            }
        }.start();

    }//GEN-LAST:event_btnElectActionPerformed

    private void btnReleaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReleaseActionPerformed
        new Thread() {

            @Override
            public void run() {
                int row = tblHoldMessages.getSelectedRow();

                if (row != -1) {
                    Object[] r = new Object[]{tblHoldMessages.getValueAt(row, 0), tblHoldMessages.getValueAt(row, 1), tblHoldMessages.getValueAt(row, 2), tblHoldMessages.getValueAt(row, 3)};
                    releaseMessage(r);
                }
            }
        }.start();

    }//GEN-LAST:event_btnReleaseActionPerformed

    private void btnShuffleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShuffleActionPerformed
        new Thread() {

            @Override
            public void run() {
                Collections.shuffle(holdback);
                try {
                    fillHoldingQueue(holdback);
                } catch (RemoteException ex) {
                    Logger.getLogger(DebugWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();

    }//GEN-LAST:event_btnShuffleActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnElect;
    private javax.swing.JButton btnRelease;
    private javax.swing.JButton btnShuffle;
    private javax.swing.JCheckBox chkHold;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable tblHoldMessages;
    private javax.swing.JTable tblMembers;
    private javax.swing.JTable tblMessages;
    private javax.swing.JTable tblVectorClocks;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables

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
     * @return the autoRelease
     */
    public boolean isIsMessageHoldEnabled() {
        return autoRelease;
    }

    /**
     * @param isMessageHoldEnabled the autoRelease to set
     */
    public void setIsMessageHoldEnabled(boolean isMessageHoldEnabled) {
        this.autoRelease = isMessageHoldEnabled;
    }

    public void vectorReceived(Object changed, Object values) {
        boolean isChanged = Boolean.valueOf(changed.toString());
        Object[] vecs = (Object[]) values;
        String msg = "Vector Clock Received : \n";
        if (isChanged) {
            msg += "  Current vector clock; " + vecs[0] + " is changed to ";
        }
        msg += "  " + vecs[1];
        if (!isChanged) {
            msg += "\n  Current vector clock ; " + vecs[1] + " is not changed.";
        }
        updateStatus(msg);
        dtm = (DefaultTableModel) tblVectorClocks.getModel();
        dtm.addRow(new Object[]{vecs[0].toString(), vecs[1].toString(), isChanged ? "Changed" : "Not Changed"});
    }
}
