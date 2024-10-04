package org.linkedon.client.models;

import java.util.ArrayList;

public class Profile {
    private User user;
    private ContactInfo contactInfo;
    private ArrayList<User> connections;
    private ArrayList<User> followers;
    private ArrayList<User> followings;
    private ArrayList<String> skills;
    private ArrayList<Education> educations;

    public Profile(User user,ContactInfo contactInfo, ArrayList<User> connections, ArrayList<User> followers,ArrayList<User> followings, ArrayList<String> skills, ArrayList<Education> educations) {
        this.user = user;
        this.contactInfo = contactInfo;
        this.connections = connections;
        this.followers = followers;
        this.followings = followings;
        this.skills = skills;
        this.educations = educations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public ArrayList<User> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<User> connections) {
        this.connections = connections;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<User> followers) {
        this.followers = followers;
    }

    public ArrayList<User> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<User> followings) {
        this.followings = followings;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public ArrayList<Education> getEducations() {
        return educations;
    }

    public void setEducations(ArrayList<Education> educations) {
        this.educations = educations;
    }
}
