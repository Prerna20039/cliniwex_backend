package com.clinic.backend.dto;

public class DashboardResponse {

    private final long totalAppointments;
    private final long pendingAppointments;
    private final long acceptedAppointments;
    private final long completedAppointments;
    private final long cancelledAppointments;
    private final long patientsInQueue;

    public DashboardResponse(
            long totalAppointments,
            long pendingAppointments,
            long acceptedAppointments,
            long completedAppointments,
            long cancelledAppointments,
            long patientsInQueue) {

        this.totalAppointments = totalAppointments;
        this.pendingAppointments = pendingAppointments;
        this.acceptedAppointments = acceptedAppointments;
        this.completedAppointments = completedAppointments;
        this.cancelledAppointments = cancelledAppointments;
        this.patientsInQueue = patientsInQueue;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public long getPendingAppointments() {
        return pendingAppointments;
    }

    public long getAcceptedAppointments() {
        return acceptedAppointments;
    }

    public long getCompletedAppointments() {
        return completedAppointments;
    }

    public long getCancelledAppointments() {
        return cancelledAppointments;
    }

    public long getPatientsInQueue() {
        return patientsInQueue;
    }
}