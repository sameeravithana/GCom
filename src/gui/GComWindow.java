/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GComWindow.java
 *
 * Created on Dec 16, 2013, 1:26:05 PM
 */
package gui;

import gcom.RMIServer;
import gcom.modules.group.Group;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author ens13pps
 */
public class GComWindow extends javax.swing.JFrame {
    
    private RMIServer server;
    private DefaultTreeModel tm;
    private DefaultMutableTreeNode root;

    /** Creates new form GComWindow */
    public GComWindow() {
        initComponents();
        setLocationRelativeTo(null);
        tm = (DefaultTreeModel) trGComStructure.getModel();
        root = (DefaultMutableTreeNode) tm.getRoot();
        
    }
    
    private void updateStatus(String newStatus) {
        txtLog.setText(txtLog.getText() + newStatus + "\n");
    }
    
    private void addNodeToTree(String child, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode ch = new DefaultMutableTreeNode(child, true);
        tm.insertNodeInto(ch, parent, parent.getChildCount());
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        trGComStructure = new javax.swing.JTree();
        mainMenu = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuStartServer = new javax.swing.JCheckBoxMenuItem();
        mnuNewGroup = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();
        mnuAbout = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtLog.setColumns(20);
        txtLog.setEditable(false);
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("GCom");
        trGComStructure.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        trGComStructure.setScrollsOnExpand(true);
        jScrollPane2.setViewportView(trGComStructure);

        mnuFile.setText("File");

        mnuStartServer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        mnuStartServer.setText("RMI Server");
        mnuStartServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuStartServerActionPerformed(evt);
            }
        });
        mnuFile.add(mnuStartServer);

        mnuNewGroup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        mnuNewGroup.setText("New Group");
        mnuNewGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewGroupActionPerformed(evt);
            }
        });
        mnuFile.add(mnuNewGroup);

        mainMenu.add(mnuFile);

        mnuEdit.setText("Edit");
        mainMenu.add(mnuEdit);

        mnuAbout.setText("About");
        mainMenu.add(mnuAbout);

        setJMenuBar(mainMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(126, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void mnuStartServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuStartServerActionPerformed
    if (mnuStartServer.getState()) {
        String input = JOptionPane.showInputDialog(this, "Enter port number :", "Port", JOptionPane.QUESTION_MESSAGE, null, null, "1099") + "";
        if (input != null || !input.trim().isEmpty()) {
            try {
                int port = Integer.parseInt(input);
                server = new RMIServer(port);
                server.start();
                String msg = "RMI Registry Server started on port " + port;
                txtLog.setText(txtLog.getText() + msg + "\n");
                
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Cannot connect to the RMIRegistry on given port : " + input, "Invalid Port", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid port number : " + input, "Invalid Port", JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        try {
            server.stop();
            String msg = "RMI Registry Server stopped.";
            txtLog.setText(txtLog.getText() + msg + "\n");
        } catch (NoSuchObjectException ex) {
            Logger.getLogger(GComWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}//GEN-LAST:event_mnuStartServerActionPerformed
    
private void mnuNewGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewGroupActionPerformed
    NewGroup ng = new NewGroup(GComWindow.this, true);
    ng.setVisible(true);
    Group createdGroup = ng.getCreatedGroup();
    updateStatus("New Group Created : " + createdGroup.getGroupName());
    addNodeToTree(createdGroup.getGroupName(), root);
}//GEN-LAST:event_mnuNewGroupActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GComWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GComWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GComWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GComWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                new GComWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JMenu mnuAbout;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenuItem mnuNewGroup;
    private javax.swing.JCheckBoxMenuItem mnuStartServer;
    private javax.swing.JTree trGComStructure;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
