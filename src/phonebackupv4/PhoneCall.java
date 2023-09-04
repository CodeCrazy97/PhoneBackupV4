/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonebackupv4;

/**
 *
 * @author Ethan
 */
public class PhoneCall {

    private String name; 
    private String phoneNumber;
    private Long date;
    private int duration;
    
    public PhoneCall(String name, String phoneNumber, Long date, int duration) {
        this.name = name;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.duration = duration;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber
                = phoneNumber;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date
                = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration
                = duration;
    }
    
    public String getName() {
        return this.name;
    }
    
    
    
}
