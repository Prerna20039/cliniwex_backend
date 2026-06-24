package com.clinic.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String specialty;

    private String phone;

    // ================= PROFILE FIELDS =================

    @Column(length = 1000)
    private String bio;

    private String qualification;

    private Integer experienceYears;

    private Double consultationFee;

    private String clinicName;

    private String clinicAddress;

    private String workingHours;

    // ================= SYSTEM FIELDS =================

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_live")
    private Boolean isLive = false;

    public Doctor() {
    }

    // ================= GETTERS =================

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getPhone() {
        return phone;
    }

    public String getBio() {
        return bio;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getIsLive() {
    return isLive;
}

public void setIsLive(Boolean isLive) {
    this.isLive = isLive;
}

    // ================= SETTERS =================

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setIsLive(boolean isLive) {
        this.isLive = isLive;
    }
}