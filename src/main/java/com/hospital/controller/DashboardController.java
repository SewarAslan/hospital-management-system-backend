package com.hospital.controller;

import com.hospital.dto.DashboardStats;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        Long totalPatients = patientRepository.count();
        Long totalDoctors = doctorRepository.count();
        Long totalAppointments = appointmentRepository.count();
        Long totalInvoices = invoiceRepository.count();

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        Long todayAppointments = (long) appointmentRepository.findByAppointmentDateBetween(startOfDay, endOfDay).size();
        
        Long pendingAppointments = (long) appointmentRepository.findByStatus("SCHEDULED").size();
                 Long scheduledAppointments = (long) appointmentRepository.findByStatus("SCHEDULED").size();
        Long completedAppointments = (long) appointmentRepository.findByStatus("COMPLETED").size();
        Long cancelledAppointments = (long) appointmentRepository.findByStatus("CANCELLED").size();
        List<Map<String, Object>> recentAppointments = appointmentRepository.findAll().stream()
                .sorted((a1, a2) -> a2.getCreatedAt().compareTo(a1.getCreatedAt()))
                .limit(5)
                .map(apt -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", apt.getId());
                    map.put("patientName", apt.getPatient().getFullName());
                    map.put("doctorName", apt.getDoctor().getUser().getFullName());
                    map.put("date", apt.getAppointmentDate());
                    map.put("status", apt.getStatus());
                    return map;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> appointmentsByDay = new ArrayList<>();

        DashboardStats stats = new DashboardStats(
                totalPatients,
                totalDoctors,
                totalAppointments,
                totalInvoices,
                todayAppointments,
                pendingAppointments,
                recentAppointments,
                    new ArrayList<>(),  // appointmentsByDay

                scheduledAppointments,
                completedAppointments,
                cancelledAppointments
        );

        return ResponseEntity.ok(stats);
    }
}
