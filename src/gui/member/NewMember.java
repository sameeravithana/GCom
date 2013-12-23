/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewMember.java
 *
 * Created on Dec 17, 2013, 11:07:21 AM
 */
package gui.member;

import gcom.RMIServer;
import gcom.interfaces.IGroupManagement;
import gcom.interfaces.IMember;
import gcom.interfaces.MESSAGE_TYPE;
import gcom.modules.group.GroupManagementException;
import gcom.modules.group.Member;
import gcom.modules.group.Message;
import gui.GComWindow;
import java.awt.Color;
import java.awt.HeadlessException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author ens13pps
 */
public class NewMember extends javax.swing.JFrame {

    private Registry registry;
    private HashMap<String, Integer> gs;
    private IGroupManagement igm;
    private IMember imem;
    private Member member;
    private RMIServer srv;
    private String groupName;

    private MemberContainer memContainer;

    private String statusLog = "";

    /**
     * Creates new form NewMember
     *
     * @param parent
     * @param modal
     */
    public NewMember() {
        initComponents();
        setLocationRelativeTo(null);
        panelMem.setVisible(false);
        setIconImage(new ImageIcon(GComWindow.class.getResource("/pics/logo.png")).getImage());
        memContainer = new MemberContainer();

    }

    private void checkConnection(String host, int port) {

        try {
            srv = new RMIServer(host, port);
            registry = srv.start();
            igm = srv.regLookUp("IGroupManagement");
            gs = igm.getGroupDetails();

            statusLog += "Connection to " + host + " from " + port + " successful.\n";

            lblMsg.setText("Connection to " + host + " from " + port + " successful.");
            lblMsg.setForeground(Color.black);
        } catch (AccessException ex) {
            Logger.getLogger(NewMember.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(NewMember.class.getName()).log(Level.SEVERE, null, ex);
            lblMsg.setText("Cannot Connect to " + host + " from " + port + ".");
            lblMsg.setForeground(Color.red);
        } catch (NotBoundException ex) {
            Logger.getLogger(NewMember.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void connect() throws HeadlessException {
        String host = cmbHost.getSelectedItem().toString().trim();
        int port = -1;
        if (host.isEmpty()) {
            int res = JOptionPane.showConfirmDialog(NewMember.this, "Invalid host specified : " + host + "\nDo you want to use localhost?", "Invalid Host", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                host = "localhost";
            }
        }
        try {
            port = Integer.parseInt(cmbPort.getSelectedItem().toString().trim());
        } catch (Exception e) {
            int res = JOptionPane.showConfirmDialog(NewMember.this, "Invalid port number specified : " + port + "\nDo you want to use the default port (1099)?", "Invalid Port", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                port = 1099;
            }
        }
        checkConnection(host, port);
        if (gs != null) {
            panelMem.setVisible(true);
            cmbGroup.removeAllItems();
            for (String key : gs.keySet()) {
                cmbGroup.addItem(key);
            }
        } else {
            panelMem.setVisible(false);
        }
    }

    private void createMember() throws HeadlessException, GroupManagementException {
        groupName = cmbGroup.getSelectedItem().toString().trim();
        String memName = txtMember.getText();
        if (memName.isEmpty()) {
            JOptionPane.showMessageDialog(NewMember.this, "Member name can not be empty", "Invalid Member", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                ArrayList<String> params = new ArrayList<String>();
                params.add(groupName);
                params.add(memName);
                member = new Member(memName, null);
                member.setSrv(srv);
                memContainer.setMember(member);

                IMember stub = (IMember) member;
                Message msg = new Message(groupName, member, params, MESSAGE_TYPE.JOIN_REQUEST);

                MemberWindow memWindow = new MemberWindow(member, stub);
                member.addPropertyChangeListener(new SignalListener(memWindow));

                memContainer.setStub(stub);

                statusLog += "Member," + memContainer.getMember().getName() + " (" + memContainer.getMember().getIdentifier() + ") added to Group " + groupName;

                if (gs.get(groupName) <= 0) {
                    memContainer.setMember(igm.sendRequest(msg));
                    srv.rebind(groupName, stub);
                    statusLog += " as the Group Leader";
                } else {
                    imem = srv.regMemLookUp(groupName);
                    memContainer.setMember(imem.sendRequest(msg));
                }
                statusLog += ".";
                igm.addMember(member.getParentGroup(), member);
                setVisible(false);
                memWindow.setMember(member);
                memWindow.setMemContainer(memContainer);
                memWindow.initialize(member, statusLog);
                memWindow.setServer(srv);
                memWindow.setVisible(true);

            } catch (RemoteException ex) {
                Logger.getLogger(NewMember.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(NewMember.class.getName()).log(Level.SEVERE, null, ex);
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

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbHost = new javax.swing.JComboBox();
        cmbPort = new javax.swing.JComboBox();
        panelMem = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbGroup = new javax.swing.JComboBox();
        txtMember = new javax.swing.JTextField();
        btnCreateMember = new javax.swing.JButton();
        btnConnect = new javax.swing.JButton();
        lblMsg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Join to a Group");
        setResizable(false);

        jLabel4.setText("Host");

        jLabel5.setText("Port");

        cmbHost.setEditable(true);
        cmbHost.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "localhost" }));

        cmbPort.setEditable(true);
        cmbPort.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1099" }));

        panelMem.setBorder(javax.swing.BorderFactory.createTitledBorder("Member Info"));

        jLabel2.setText("Member Name");

        jLabel1.setText("Group");

        btnCreateMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/proceed.png"))); // NOI18N
        btnCreateMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateMemberActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMemLayout = new javax.swing.GroupLayout(panelMem);
        panelMem.setLayout(panelMemLayout);
        panelMemLayout.setHorizontalGroup(
            panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelMemLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCreateMember))
                    .addGroup(panelMemLayout.createSequentialGroup()
                        .addGroup(panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(31, 31, 31)
                        .addGroup(panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbGroup, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMember))))
                .addContainerGap())
        );
        panelMemLayout.setVerticalGroup(
            panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCreateMember, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnConnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/connect.png"))); // NOI18N
        btnConnect.setToolTipText("Connect to RMI Server");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        lblMsg.setText("specify the host+port and click \"Connect\"");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblMsg, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                        .addGap(56, 56, 56))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbHost, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbPort, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelMem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cmbPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnConnect, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMsg)
                .addGap(12, 12, 12)
                .addComponent(panelMem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
    connect();
}//GEN-LAST:event_btnConnectActionPerformed

private void btnCreateMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateMemberActionPerformed
        try {
            createMember();
        } catch (HeadlessException ex) {
            Logger.getLogger(NewMember.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GroupManagementException ex) {
            Logger.getLogger(NewMember.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_btnCreateMemberActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnCreateMember;
    private javax.swing.JComboBox cmbGroup;
    private javax.swing.JComboBox cmbHost;
    private javax.swing.JComboBox cmbPort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblMsg;
    private javax.swing.JPanel panelMem;
    private javax.swing.JTextField txtMember;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) throws RemoteException {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            new NewMember().setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(NewMember.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @return the registry
     */
    public Registry getRegistry() {
        return registry;
    }

    /**
     * @param registry the registry to set
     */
    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
