package com.clinic.backend.dto;

public class DoctorUpdateRequest {

    private String name;
    private String phoneNumber;
    private String specialization;

    private String qualification;
    private Integer experienceYears;
    private Double consultationFee;

    private String clinicName;
    private String clinicAddress;
    private String workingHours;
    private String bio;

    // getters & setters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }
    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Double getConsultationFee() {
        return consultationFee;
    }
    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getClinicName() {
        return clinicName;
    }
    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }
    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getWorkingHours() {
        return workingHours;
    }
    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
}