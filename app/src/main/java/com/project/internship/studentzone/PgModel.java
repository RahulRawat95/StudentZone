package com.project.internship.studentzone;

/**
 * Created by Rahul on 17-Sep-17.
 */

import java.io.Serializable;
import java.util.List;

public class PgModel implements Serializable{

    private String address;
    private String contact;
    private String key;
    private String name;
    private List<String> rental = null;
    private String sex;
    private String rentalSharingCount;
    private String depositSharingCount;
    private List<String> deposit = null;
    private String imageUrl;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRental() {
        return rental;
    }

    public void setRental(List<String> rental) {
        this.rental = rental;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRentalSharingCount() {
        return rentalSharingCount;
    }

    public void setRentalSharingCount(String rentalSharingCount) {
        this.rentalSharingCount = rentalSharingCount;
    }

    public String getDepositSharingCount() {
        return depositSharingCount;
    }

    public void setDepositSharingCount(String depositSharingCount) {
        this.depositSharingCount = depositSharingCount;
    }

    public List<String> getDeposit() {
        return deposit;
    }

    public void setDeposit(List<String> deposit) {
        this.deposit = deposit;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
