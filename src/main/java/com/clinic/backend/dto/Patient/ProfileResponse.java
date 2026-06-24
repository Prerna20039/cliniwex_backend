package com.clinic.backend.dto.Patient;

public class ProfileResponse {
    private Long patientId;
    private String name;
    private String email;
    private String phone;
    private Integer age;

    public ProfileResponse() {}

    public ProfileResponse(Long patientId, String name, String email, String phone, Integer age) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
    }

    // Getters and Setters
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}