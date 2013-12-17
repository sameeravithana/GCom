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
import java.awt.Color;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ens13pps
 */
public class NewMember extends javax.swing.JDialog {

    private Registry registry;

    /** Creates new form NewMember */
    public NewMember(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        panelMem.setVisible(false);

    }

    private HashMap<String, Integer> checkConnection(String host, int port) {
        HashMap<String, Integer> gs=null;
        try {
            RMIServer srv = new RMIServer(host, port);
            registry = srv.start();
            IGroupManagement igm=srv.regLookUp("IGroupManagement");
            gs=igm.getGroupDetails();
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
        return gs;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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
        btnConnect = new javax.swing.JButton();
        lblMsg = new javax.swing.JLabel();
        btnCreateMember = new javax.swing.JButton();

        jLabel4.setText("Host");

        jLabel5.setText("Port");

        cmbHost.setEditable(true);
        cmbHost.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "localhost" }));

        cmbPort.setEditable(true);
        cmbPort.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1099" }));

        panelMem.setBorder(javax.swing.BorderFactory.createTitledBorder("Member Info"));

        jLabel2.setText("Member Name");

        jLabel1.setText("Group");

        javax.swing.GroupLayout panelMemLayout = new javax.swing.GroupLayout(panelMem);
        panelMem.setLayout(panelMemLayout);
        panelMemLayout.setHorizontalGroup(
            panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(31, 31, 31)
                .addGroup(panelMemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMember)
                    .addComponent(cmbGroup, 0, 186, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        lblMsg.setText("specify the host+port you need to connect and click \"Connect\"");

        btnCreateMember.setText("Create");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelMem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbHost, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbPort, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCreateMember, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMsg)
                .addGap(12, 12, 12)
                .addComponent(panelMem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCreateMember)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
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
    HashMap<String, Integer> gs=checkConnection(host, port);
    if(gs!=null){
        panelMem.setVisible(true);
        cmbGroup.removeAllItems();
        for(String key:gs.keySet()) cmbGroup.addItem(key);     
    }else{
        panelMem.setVisible(false);
    }

}//GEN-LAST:event_btnConnectActionPerformed
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

    public static void main(String[] args) {
        
        new NewMember(null, true).setVisible(true);
    }
}
