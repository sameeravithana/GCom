/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewGroup.java
 *
 * Created on Dec 16, 2013, 3:25:53 PM
 */
package gui;

import gcom.modules.group.Group;
import gcom.modules.group.GroupDef;
import gcom.modules.group.GroupManagement;
import gcom.modules.group.GroupManagementException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author ens13pps
 */
public class NewGroup extends javax.swing.JDialog {

    private JComboBox[] cmb;
    private Group group;

    /** Creates new form NewGroup */
    public NewGroup(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setLocationRelativeTo(parent);
        initComponents();
        cmb = new JComboBox[]{cmbGroupType, cmbComType, cmbMsgOrd};
        try {
            Properties p = new Properties();
            p.load(new FileInputStream(NewGroup.class.getResource("../server.properties").getPath()));

            HashMap<String, String[]> hm = new HashMap<String, String[]>();
            int i = 0;
            for (String k : p.stringPropertyNames()) {
                String property = p.getProperty(k);
                String[] split = property.split(",");
                hm.put(k, split);
                cmb[i].removeAllItems();
                for (int j = 0; j < split.length; j++) {
                    cmb[i].addItem(split[j]);
                }
                i++;

            }
        } catch (IOException ex) {
            Logger.getLogger(NewGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Group getCreatedGroup() {
        return group;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtGroupName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cmbGroupType = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmbComType = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbMsgOrd = new javax.swing.JComboBox();
        btnOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Group");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setText("Group Name");

        jLabel2.setText("Group Type");

        cmbGroupType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Static", "Dynamic" }));

        jLabel3.setText("Com. Type");

        cmbComType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Basic", "Reliable" }));

        jLabel4.setText("Msg. Ordering");

        cmbMsgOrd.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Unordered", "Causal" }));

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(14, 14, 14)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGroupName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                            .addComponent(cmbMsgOrd, javax.swing.GroupLayout.Alignment.TRAILING, 0, 178, Short.MAX_VALUE)
                            .addComponent(cmbGroupType, javax.swing.GroupLayout.Alignment.TRAILING, 0, 178, Short.MAX_VALUE)
                            .addComponent(cmbComType, javax.swing.GroupLayout.Alignment.TRAILING, 0, 178, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbGroupType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbComType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMsgOrd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(btnOK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
    if (!txtGroupName.getText().trim().isEmpty()) {
        String groupType = cmbGroupType.getSelectedItem().toString();
        GroupDef gd = new GroupDef(txtGroupName.getText(), cmbGroupType.getSelectedItem().toString(), cmbComType.getSelectedItem().toString(), cmbMsgOrd.getSelectedItem().toString());
        try {
            group = GroupManagement.createGroup(gd);
            setVisible(false);
        } catch (GroupManagementException ex) {
            Logger.getLogger(NewGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
    } else {
        JOptionPane.showMessageDialog(NewGroup.this, "Please enter a group name.", "Empty Group Name", JOptionPane.INFORMATION_MESSAGE);
    }
}//GEN-LAST:event_btnOKActionPerformed

private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
// TODO add your handling code here:
}//GEN-LAST:event_formWindowOpened

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox cmbComType;
    private javax.swing.JComboBox cmbGroupType;
    private javax.swing.JComboBox cmbMsgOrd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txtGroupName;
    // End of variables declaration//GEN-END:variables
}
