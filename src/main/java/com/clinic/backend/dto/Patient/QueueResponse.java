package com.clinic.backend.dto.Patient;

public class QueueResponse {

    


    private Integer tokenNumber;
    private Integer currentToken;
    private Integer position;
    private String status;
    private String estimatedWaitingTime;

    // Generate getters and setters
    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public Integer getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(Integer currentToken) {
        this.currentToken = currentToken;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEstimatedWaitingTime() {
        return estimatedWaitingTime;
    }

    public void setEstimatedWaitingTime(String estimatedWaitingTime) {
        this.estimatedWaitingTime = estimatedWaitingTime;
    }

    
    
}
