package com.clinic.backend.dto;

public class DoctorProfileResponse {

    private Long doctorId;

    private String name;
    private String email;
    private String phoneNumber;
    private String specialization;

    private String qualification;
    private Integer experienceYears;
    private Double consultationFee;
    private String clinicName;
    private String clinicAddress;
    private String workingHours;
    private String bio;

    public DoctorProfileResponse() {
    }

    // ================= GETTERS =================

    public Long getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public Double getConsultationFee() {
        return consultationFee;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public String getBio() {
        return bio;
    }

    // ================= SETTERS =================

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}