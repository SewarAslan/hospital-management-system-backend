package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DashboardStats {
    private Long totalPatients;
    private Long totalDoctors;
    private Long totalAppointments;
    private Long totalInvoices;
    private Long todayAppointments;
    private Long pendingAppointments;
    private List<Map<String, Object>> recentAppointments;
    private List<Map<String, Object>> appointmentsByDay;
    private Long scheduledAppointments;
    private Long completedAppointments;
    private Long cancelledAppointments;
}
