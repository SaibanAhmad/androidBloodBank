package com.saiban.bahawalpurblooddonors.Models;

/**
 * Created by maliksaif232 on 7/6/2017.
 */

public class Reg_Donor {
    String DonorName;
    String DonorEmail;
    String DonorContact;
    String DonorCity;
    String DonorAge;
    String DonorBloodGroup;




    public Reg_Donor() {
    }

    public Reg_Donor(String donorName, String donorEmail, String donorContact, String donorCity, String age, String donorBloodGroup) {
        this.DonorName = donorName;
        this.DonorEmail = donorEmail;
        this.DonorContact = donorContact;
        this.DonorCity = donorCity;
        this.DonorAge = age;
        this.DonorBloodGroup = donorBloodGroup;
    }


    public String getDonorName() {
        return DonorName;
    }

    public void setDonorName(String donorName) {
        DonorName = donorName;
    }

    public String getDonorEmail() {
        return DonorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        DonorEmail = donorEmail;
    }

    public String getDonorContact() {
        return DonorContact;
    }

    public void setDonorContact(String donorContact) {
        DonorContact = donorContact;
    }

    public String getDonorCity() {
        return DonorCity;
    }

    public void setDonorCity(String donorCity) {
        DonorCity = donorCity;
    }

    public String getDonorAge() {
        return DonorAge;
    }

    public void setDonorAge(String donorAge) {
        DonorAge = donorAge;
    }

    public String getDonorBloodGroup() {
        return DonorBloodGroup;
    }

    public void setDonorBloodGroup(String donorBloodGroup) {
        DonorBloodGroup = donorBloodGroup;
    }
}
