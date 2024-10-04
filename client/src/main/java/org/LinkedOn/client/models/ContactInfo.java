package org.linkedon.client.models;

import java.sql.Date;

public class ContactInfo {
    private String userId, address, website, phoneNumber;
    private PhoneType phoneType;
    private Date birthday;
    private String instantMessaging, instantMessagingType; // Telegram,Skype,...

    // Default values
    {
        phoneType = PhoneType.NONE;
    }

    public ContactInfo(String userId, String address, String website, String phoneNumber, PhoneType phoneType, Date birthday, String instantMessaging, String instantMessagingType) {
        this.userId = userId;
        this.address = address;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.birthday = birthday;
        this.instantMessaging = instantMessaging;
        this.instantMessagingType = instantMessagingType;
    }

    // Default Constructor
    public ContactInfo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getInstantMessaging() {
        return instantMessaging;
    }

    public void setInstantMessaging(String instantMessaging) {
        this.instantMessaging = instantMessaging;
    }

    public String getInstantMessagingType() {
        return instantMessagingType;
    }

    public void setInstantMessagingType(String instantMessagingType) {
        this.instantMessagingType = instantMessagingType;
    }

    public enum PhoneType {
        HOME, WORK, MOBILE, NONE
    }
}
