package com.hospital.controller;

import com.hospital.dto.LoginRequest;
import com.hospital.dto.LoginResponse;
import com.hospital.dto.RegisterRequest;
import com.hospital.model.*;
import com.hospital.repository.*;
import com.hospital.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private StaffRepository staffRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login attempt: " + loginRequest.getUsername());
        var userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            User user = userOpt.get();
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt;
            try {
                jwt = jwtUtil.generateToken(userDetails);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("JWT generation failed");
            }
            return ResponseEntity.ok(new LoginResponse(jwt, user.getUsername(), user.getEmail(),
                    user.getFullName(), user.getRole()));
        } else {
            System.out.println("Invalid username or password");
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setAddress(request.getAddress());
        user.setActive(true);

        User savedUser = userRepository.save(user);

        switch (request.getRole()) {
            case DOCTOR -> {
                Doctor doctor = new Doctor();
                doctor.setUser(savedUser);
                doctor.setLicenseNumber("DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                doctor.setSpecialization("General Practitioner");
                doctor.setQualifications("MD");
                doctor.setExperienceYears(0);
                doctor.setConsultationFee("50.00");
                doctor.setSchedule("Mon-Fri: 9 AM - 5 PM");
                doctor.setAvailable(true);
                doctorRepository.save(doctor);
            }
            case PATIENT -> {
                Patient patient = new Patient();
                patient.setPatientNumber("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                patient.setFullName(savedUser.getFullName());
                patient.setDateOfBirth(LocalDate.now().minusYears(30));
                patient.setAge(30);
                patient.setGender("Not Specified");
                patient.setPhone(savedUser.getPhone());
                patient.setEmail(savedUser.getEmail());
                patient.setAddress(savedUser.getAddress());
                patient.setStatus("ACTIVE");
                patientRepository.save(patient);
            }
            case NURSE, RECEPTIONIST -> {
                Staff staff = new Staff();
                staff.setUser(savedUser);
                staff.setPosition(request.getRole().toString());
                staff.setShift("Morning");
                staff.setActive(true);
                staffRepository.save(staff);
            }
            default -> {
                // No additional entity to create
            }
        }
          // Generate JWT token for the newly registered user

        final UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(jwt, savedUser.getUsername(),
                savedUser.getEmail(), savedUser.getFullName(), savedUser.getRole()));
    }

    // New endpoint to get current user info
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }
            String jwt = token.substring(7);
            String username = jwtUtil.extractUsername(jwt);
            User user = userRepository.findByUsername(username).orElseThrow();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
}
