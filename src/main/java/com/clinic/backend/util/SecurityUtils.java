package com.clinic.backend.util;

import com.clinic.backend.entity.Patient;
import com.clinic.backend.service.PatientDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Patient getCurrentPatient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof PatientDetails) {
            return ((PatientDetails) principal).getPatient();
        }

        return null;
    }

    public static Long getCurrentPatientId() {
        Patient patient = getCurrentPatient();
        return patient != null ? patient.getId() : null;
    }

    public static String getCurrentPatientEmail() {
        Patient patient = getCurrentPatient();
        return patient != null ? patient.getEmail() : null;
    }
}