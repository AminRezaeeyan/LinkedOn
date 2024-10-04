package org.LinkedOn.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.sql.Timestamp;

import static org.LinkedOn.server.utils.Json.Views.Brief;
import static org.LinkedOn.server.utils.Json.Views.Full;

public class User {
    @JsonView(Brief.class)
    private String id;

    @JsonView(Brief.class)
    private String firstName;

    @JsonView(Brief.class)
    private String lastName;

    @JsonView(Brief.class)
    private String headline;

    @JsonView(Full.class)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Password shouldn't be sent to client for security
    private String password;

    @JsonView(Full.class)
    private String additionalName;

    @JsonView(Full.class)
    private String bio;

    @JsonView(Full.class)
    private String location;

    @JsonView(Full.class)
    private String industry;

    @JsonView(Full.class)
    private UserState userState;

    @JsonView(Full.class)
    private Timestamp createdAt;

    // Default values
    {
        userState = UserState.NONE;
    }

    public User(String id, String firstName, String lastName, String headline, String email, String password, String additionalName, String bio, String location, String industry, UserState userState, Timestamp createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.headline = headline;
        this.email = email;
        this.password = password;
        this.additionalName = additionalName;
        this.bio = bio;
        this.location = location;
        this.industry = industry;
        this.userState = userState;
        this.createdAt = createdAt;
    }

    // Default Constructor
    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public enum UserState {
        OPEN_TO_WORK, OPEN_TO_HIRING, OPEN_TO_PROVIDING_SERVICES, NONE
    }
}