package com.mycompany.mavenproject1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 *
 * @author Ethan
 * @date 09/2023
 */
public class Contact {

    private String name;
    private String phoneNumber;

    public Contact(String name,
            String phoneNumber) {
        this.name
                = name;
        this.phoneNumber
                = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name
                = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber
                = phoneNumber;
    }

    
    public static LinkedList<Contact> getContacts(Database database) throws SQLException {
        LinkedList<Contact> contacts
                = new LinkedList<Contact>();

        String sql
                = "SELECT * FROM contacts";

        Connection conn
                = database.connect();
        Statement stmt
                = conn.createStatement();
        ResultSet rs
                = stmt.executeQuery(sql);
     
        while (rs.next()) {
            String name = rs.getString("name");
            String phoneNumber = rs.getString("phone_number");
            Contact contact = new Contact(name, phoneNumber);
            contacts.add(contact);
        }
        
        return contacts;
    }
}