package com.clinic.backend.dto;

public class AnalyticsResponse {

    private long totalAppointments;
    private long pendingAppointments;
    private long acceptedAppointments;
    private long completedAppointments;
    private long cancelledAppointments;

    private long waitingPatients;
    private long inProgressPatients;
    private long completedConsultations;

    private long totalQueueEntries;

    public AnalyticsResponse() {
    }

    public AnalyticsResponse(
            long totalAppointments,
            long pendingAppointments,
            long acceptedAppointments,
            long completedAppointments,
            long cancelledAppointments,
            long waitingPatients,
            long inProgressPatients,
            long completedConsultations,
            long totalQueueEntries) {

        this.totalAppointments = totalAppointments;
        this.pendingAppointments = pendingAppointments;
        this.acceptedAppointments = acceptedAppointments;
        this.completedAppointments = completedAppointments;
        this.cancelledAppointments = cancelledAppointments;

        this.waitingPatients = waitingPatients;
        this.inProgressPatients = inProgressPatients;
        this.completedConsultations = completedConsultations;

        this.totalQueueEntries = totalQueueEntries;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public long getPendingAppointments() {
        return pendingAppointments;
    }

    public void setPendingAppointments(long pendingAppointments) {
        this.pendingAppointments = pendingAppointments;
    }

    public long getAcceptedAppointments() {
        return acceptedAppointments;
    }

    public void setAcceptedAppointments(long acceptedAppointments) {
        this.acceptedAppointments = acceptedAppointments;
    }

    public long getCompletedAppointments() {
        return completedAppointments;
    }

    public void setCompletedAppointments(long completedAppointments) {
        this.completedAppointments = completedAppointments;
    }

    public long getCancelledAppointments() {
        return cancelledAppointments;
    }

    public void setCancelledAppointments(long cancelledAppointments) {
        this.cancelledAppointments = cancelledAppointments;
    }

    public long getWaitingPatients() {
        return waitingPatients;
    }

    public void setWaitingPatients(long waitingPatients) {
        this.waitingPatients = waitingPatients;
    }

    public long getInProgressPatients() {
        return inProgressPatients;
    }

    public void setInProgressPatients(long inProgressPatients) {
        this.inProgressPatients = inProgressPatients;
    }

    public long getCompletedConsultations() {
        return completedConsultations;
    }

    public void setCompletedConsultations(long completedConsultations) {
        this.completedConsultations = completedConsultations;
    }

    public long getTotalQueueEntries() {
        return totalQueueEntries;
    }

    public void setTotalQueueEntries(long totalQueueEntries) {
        this.totalQueueEntries = totalQueueEntries;
    }
}