package org.LinkedOn.server.models;

public class Connect {
    private String userId1;
    private String userId2;
    private ConnectionStatus status;

    public enum ConnectionStatus {
        CONNECTED, PENDING
    }

    {
        // default
        status = ConnectionStatus.PENDING;
    }

    public Connect(String userId1, String userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }

    // default constructor
    public Connect(){}

    public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }
}
