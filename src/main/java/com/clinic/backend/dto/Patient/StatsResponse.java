package com.clinic.backend.dto.Patient;

public class StatsResponse {
    private int yourToken;
    private int currentToken;
    private int patientsAhead;
    private int estimatedWaitMinutes;

    // Getters and Setters
    public int getYourToken() { return yourToken; }
    public void setYourToken(int yourToken) { this.yourToken = yourToken; }

    public int getCurrentToken() { return currentToken; }
    public void setCurrentToken(int currentToken) { this.currentToken = currentToken; }

    public int getPatientsAhead() { return patientsAhead; }
    public void setPatientsAhead(int patientsAhead) { this.patientsAhead = patientsAhead; }

    public int getEstimatedWaitMinutes() { return estimatedWaitMinutes; }
    public void setEstimatedWaitMinutes(int estimatedWaitMinutes) { this.estimatedWaitMinutes = estimatedWaitMinutes; }
}