/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonebackupv4;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Ethan
 */
public class MainScreen extends javax.swing.JFrame {

    /**
     * Creates new form MainScreen
     */
    public MainScreen() {
        initComponents();
        //outputTextArea.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backupCallsButton = new javax.swing.JButton();
        backupTextsButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        outputTextArea = new java.awt.TextArea();
        filePathTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        backupCallsButton.setText("Backup Calls");
        backupCallsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backupCallsButtonActionPerformed(evt);
            }
        });

        backupTextsButton.setText("Backup Texts");

        jLabel1.setText("Enter the File Path and Click \"Backup Calls\" or \"Backup Texts\"");

        filePathTextField.setText("File Path (examples of valid file paths: \"C:/Users/username/downloads/calls.log\" or \"C:\\Users\\username\\downloads\\texts.txt\")");
        filePathTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePathTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(outputTextArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(backupCallsButton)
                        .addGap(18, 18, 18)
                        .addComponent(backupTextsButton))
                    .addComponent(filePathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
                .addContainerGap(127, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(filePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backupCallsButton)
                    .addComponent(backupTextsButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(270, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backupCallsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backupCallsButtonActionPerformed
        String path
                = fixFilePath(filePathTextField.getText());
        File file
                = new File(path);
        if (!file.exists()) {
            outputTextArea.setText("File does not exist");
            outputTextArea.setVisible(true);
        } else {
            Scanner myReader
                    = null;
            try {
                myReader
                        = new Scanner(file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainScreen.class.getName()).
                        log(Level.SEVERE,
                                null,
                                ex);
            }
            LinkedList<PhoneCall> phoneCalls
                    = new LinkedList<PhoneCall>();
            LinkedList<Contact> contacts
                    = new LinkedList<Contact>();

            try {
                database
                        = new Database();
                contacts
                        = Contact.getContacts(database);

                while (myReader.hasNextLine()) {
                    String data
                            = myReader.nextLine();
                    //LinkedList phoneCalls = getPhoneCalls(data);

                    JSONObject obj
                            = new JSONObject(data);
                    JSONArray arr
                            = obj.getJSONArray("listCallLogs");
                    int n
                            = arr.length();
                    String textForOutput
                            = "";
                    for (int i
                            = 0;
                            i < n;
                            ++i) {
                        final JSONObject d
                                = arr.getJSONObject(i);
                        String name
                                = "";
                        if (d.has("name")) { // some phone calls don't have a name
                            name
                                    = d.getString("name");
                        }

                        Contact contact
                                = new Contact(name,
                                        d.getString("number"));
                        PhoneCall phoneCall
                                = new PhoneCall(name,
                                        d.getString("number"),
                                        d.getLong("date"),
                                        d.getInt("duration"));

                        if (!contactExists(contacts,
                                contact) && !contact.getName().isEmpty()) {
                            contacts.add(contact);
                        }
                        phoneCalls.add(phoneCall);

                        textForOutput
                                += "Name: " + name + ", phone number: " + d.
                                getString(
                                        "number") + ", date: " + d.getLong(
                                        "date") + ", duration: " + d.
                                getInt("duration") + "\n\n";
                    }

                    outputTextArea.append(textForOutput);
                }

                myReader.close();

                // now backup the calls
                if (database.backupContacts(contacts)) {
                    outputTextArea.append("Contacts backed up successfully.");
                } else {
                    outputTextArea.append("Failed to backup contacts.");
                }
            } catch (SQLException sqle) {
                outputTextArea.append("Failed to backup contacts. Error: " + sqle.getMessage());
            }
        }
    }//GEN-LAST:event_backupCallsButtonActionPerformed

    private String backupContacts(LinkedList<Contact> contacts) {
        if (database.backupContacts(contacts)) {
            return "Contacts successfully backed up!";
        } else {
            return "There was an error backing up the contacts.";
        }
    }

    private void filePathTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePathTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filePathTextFieldActionPerformed

    public boolean contactExists(LinkedList<Contact> contacts,
            Contact contact) {
        for (int i
                = 0;
                i < contacts.size();
                i++) {
            if (contacts.get(i).
                    getName().
                    equals(contact.getName()) && contacts.get(i).
                    getPhoneNumber().
                    equals(contact.getPhoneNumber())) {
                return true;
            }
        }
        return false;
    }

    public static String fixFilePath(String filePath) {
        filePath
                = filePath.replaceAll("\"",
                        "");
        return filePath;
    }

    /**
     * @param args the command line arguments
     */
    public Database database;

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).
                    log(java.util.logging.Level.SEVERE,
                            null,
                            ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).
                    log(java.util.logging.Level.SEVERE,
                            null,
                            ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).
                    log(java.util.logging.Level.SEVERE,
                            null,
                            ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).
                    log(java.util.logging.Level.SEVERE,
                            null,
                            ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainScreen main
                        = new MainScreen();
                main.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backupCallsButton;
    private javax.swing.JButton backupTextsButton;
    private javax.swing.JTextField filePathTextField;
    private javax.swing.JLabel jLabel1;
    private java.awt.TextArea outputTextArea;
    // End of variables declaration//GEN-END:variables
}
