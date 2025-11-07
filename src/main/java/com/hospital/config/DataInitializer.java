package com.hospital.config;

import com.hospital.model.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@hospital.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Admin User");
            admin.setPhone("+1234567890");
            admin.setRole(Role.ADMIN);
            admin.setAddress("123 Hospital Street");
            admin.setActive(true);
            userRepository.save(admin);

            Department cardiology = new Department();
            cardiology.setName("Cardiology");
            cardiology.setDescription("Heart and cardiovascular system");
            cardiology.setHeadOfDepartment("Dr. Smith");
            cardiology.setLocation("Building A, Floor 2");
            cardiology.setPhone("+1234567891");
            cardiology.setActive(true);
            departmentRepository.save(cardiology);

            Department neurology = new Department();
            neurology.setName("Neurology");
            neurology.setDescription("Brain and nervous system");
            neurology.setHeadOfDepartment("Dr. Johnson");
            neurology.setLocation("Building B, Floor 3");
            neurology.setPhone("+1234567892");
            neurology.setActive(true);
            departmentRepository.save(neurology);

            User doctorUser = new User();
            doctorUser.setUsername("drjohn");
            doctorUser.setEmail("drjohn@hospital.com");
            doctorUser.setPassword(passwordEncoder.encode("doctor123"));
            doctorUser.setFullName("Dr. John Smith");
            doctorUser.setPhone("+1234567893");
            doctorUser.setRole(Role.DOCTOR);
            doctorUser.setAddress("456 Medical Plaza");
            doctorUser.setActive(true);
            userRepository.save(doctorUser);

            Doctor doctor = new Doctor();
            doctor.setUser(doctorUser);
            doctor.setLicenseNumber("DOC-12345");
            doctor.setSpecialization("Cardiologist");
            doctor.setQualifications("MD, PhD in Cardiology");
            doctor.setExperienceYears(10);
            doctor.setDepartment(cardiology);
            doctor.setConsultationFee("100.00");
            doctor.setSchedule("Mon-Fri: 9 AM - 5 PM");
            doctor.setAvailable(true);
            doctorRepository.save(doctor);

            User nurseUser = new User();
            nurseUser.setUsername("nurse1");
            nurseUser.setEmail("nurse1@hospital.com");
            nurseUser.setPassword(passwordEncoder.encode("nurse123"));
            nurseUser.setFullName("Nurse Mary Johnson");
            nurseUser.setPhone("+1234567894");
            nurseUser.setRole(Role.NURSE);
            nurseUser.setAddress("789 Care Street");
            nurseUser.setActive(true);
            userRepository.save(nurseUser);

            User receptionistUser = new User();
            receptionistUser.setUsername("receptionist1");
            receptionistUser.setEmail("receptionist@hospital.com");
            receptionistUser.setPassword(passwordEncoder.encode("receptionist123"));
            receptionistUser.setFullName("Sarah Williams");
            receptionistUser.setPhone("+1234567895");
            receptionistUser.setRole(Role.RECEPTIONIST);
            receptionistUser.setAddress("321 Front Desk Ave");
            receptionistUser.setActive(true);
            userRepository.save(receptionistUser);

            // Add more departments
            Department orthopedics = new Department();
            orthopedics.setName("Orthopedics");
            orthopedics.setDescription("Bone and joint treatment");
            orthopedics.setHeadOfDepartment("Dr. Ahmed");
            orthopedics.setLocation("Building C, Floor 1");
            orthopedics.setPhone("+1234567898");
            orthopedics.setActive(true);
            departmentRepository.save(orthopedics);

            // Add more doctors
            User doctor2User = new User();
            doctor2User.setUsername("drsarah");
            doctor2User.setEmail("drsarah@hospital.com");
            doctor2User.setPassword(passwordEncoder.encode("doctor123"));
            doctor2User.setFullName("Dr. Sarah Williams");
            doctor2User.setPhone("+1234567899");
            doctor2User.setRole(Role.DOCTOR);
            doctor2User.setAddress("789 Medical Center");
            doctor2User.setActive(true);
            userRepository.save(doctor2User);

            Doctor doctor2 = new Doctor();
            doctor2.setUser(doctor2User);
            doctor2.setLicenseNumber("DOC-54321");
            doctor2.setSpecialization("Neurologist");
            doctor2.setQualifications("MD, Neurology Specialist");
            doctor2.setExperienceYears(8);
            doctor2.setDepartment(neurology);
            doctor2.setConsultationFee("120.00");
            doctor2.setSchedule("Mon-Fri: 10 AM - 6 PM");
            doctor2.setAvailable(true);
            doctorRepository.save(doctor2);

            // Add patients
            Patient patient1 = new Patient();
            patient1.setPatientNumber("PAT-001");
            patient1.setFullName("Michael Brown");
            patient1.setDateOfBirth(LocalDate.of(1980, 5, 15));
            patient1.setAge(44);
            patient1.setGender("Male");
            patient1.setPhone("+1234567896");
            patient1.setEmail("michael.brown@email.com");
            patient1.setAddress("555 Patient Road");
            patient1.setBloodType("A+");
            patient1.setMedicalHistory("No major illnesses");
            patient1.setAllergies("None");
            patient1.setEmergencyContact("Jane Brown");
            patient1.setEmergencyPhone("+1234567897");
            patient1.setStatus("ACTIVE");
            patient1.setAssignedDoctor(doctor);
            patientRepository.save(patient1);

            Patient patient2 = new Patient();
            patient2.setPatientNumber("PAT-002");
            patient2.setFullName("Emily Davis");
            patient2.setDateOfBirth(LocalDate.of(1995, 8, 22));
            patient2.setAge(29);
            patient2.setGender("Female");
            patient2.setPhone("+1234567900");
            patient2.setEmail("emily.davis@email.com");
            patient2.setAddress("123 Oak Street");
            patient2.setBloodType("O+");
            patient2.setMedicalHistory("Asthma");
            patient2.setAllergies("Penicillin");
            patient2.setEmergencyContact("Tom Davis");
            patient2.setEmergencyPhone("+1234567901");
            patient2.setStatus("ACTIVE");
            patient2.setAssignedDoctor(doctor2);
            patientRepository.save(patient2);

            Patient patient3 = new Patient();
            patient3.setPatientNumber("PAT-003");
            patient3.setFullName("Ahmed Hassan");
            patient3.setDateOfBirth(LocalDate.of(1975, 3, 10));
            patient3.setAge(49);
            patient3.setGender("Male");
            patient3.setPhone("+1234567902");
            patient3.setEmail("ahmed.hassan@email.com");
            patient3.setAddress("456 Elm Avenue");
            patient3.setBloodType("B+");
            patient3.setMedicalHistory("Diabetes");
            patient3.setAllergies("None");
            patient3.setEmergencyContact("Fatima Hassan");
            patient3.setEmergencyPhone("+1234567903");
            patient3.setStatus("ACTIVE");
            patient3.setAssignedDoctor(doctor);
            patientRepository.save(patient3);

            // Add rooms
            Room room1 = new Room();
            room1.setRoomNumber("101");
            room1.setRoomType("GENERAL");
            room1.setFloor("1");
            room1.setDepartment(cardiology);
            room1.setCapacity(2);
            room1.setStatus("AVAILABLE");
            room1.setChargesPerDay(new BigDecimal("150.00"));
            roomRepository.save(room1);

            Room room2 = new Room();
            room2.setRoomNumber("202");
            room2.setRoomType("PRIVATE");
            room2.setFloor("2");
            room2.setDepartment(neurology);
            room2.setCapacity(1);
            room2.setStatus("OCCUPIED");
            room2.setChargesPerDay(new BigDecimal("300.00"));
            room2.setCurrentPatient(patient1);
            roomRepository.save(room2);

            Room room3 = new Room();
            room3.setRoomNumber("103");
            room3.setRoomType("ICU");
            room3.setFloor("1");
            room3.setDepartment(cardiology);
            room3.setCapacity(1);
            room3.setStatus("AVAILABLE");
            room3.setChargesPerDay(new BigDecimal("500.00"));
            roomRepository.save(room3);

            // Add staff
            Staff staff1 = new Staff();
            staff1.setUser(nurseUser);
            staff1.setPosition("NURSE");
            staff1.setDepartment(cardiology);
            staff1.setShift("Morning");
            staff1.setActive(true);
            staffRepository.save(staff1);

            Staff staff2 = new Staff();
            staff2.setUser(receptionistUser);
            staff2.setPosition("RECEPTIONIST");
            staff2.setShift("Evening");
            staff2.setActive(true);
            staffRepository.save(staff2);

            // Add appointments
            Appointment apt1 = new Appointment();
            apt1.setPatient(patient1);
            apt1.setDoctor(doctor);
            apt1.setAppointmentDate(LocalDateTime.now().plusDays(1));
            apt1.setReason("Regular checkup");
            apt1.setStatus("SCHEDULED");
            appointmentRepository.save(apt1);

            Appointment apt2 = new Appointment();
            apt2.setPatient(patient2);
            apt2.setDoctor(doctor2);
            apt2.setAppointmentDate(LocalDateTime.now().plusHours(2));
            apt2.setReason("Headache consultation");
            apt2.setStatus("SCHEDULED");
            appointmentRepository.save(apt2);

            Appointment apt3 = new Appointment();
            apt3.setPatient(patient3);
            apt3.setDoctor(doctor);
            apt3.setAppointmentDate(LocalDateTime.now().minusDays(1));
            apt3.setReason("Follow-up");
            apt3.setStatus("COMPLETED");
            apt3.setDiagnosis("Stable condition");
            apt3.setNotes("Continue medication");
            appointmentRepository.save(apt3);

            Appointment apt4 = new Appointment();
            apt4.setPatient(patient1);
            apt4.setDoctor(doctor2);
            apt4.setAppointmentDate(LocalDateTime.now().plusDays(3));
            apt4.setReason("Consultation");
            apt4.setStatus("CANCELLED");
            apt4.setNotes("Patient requested cancellation");
            appointmentRepository.save(apt4);

            // Add invoices
            Invoice invoice1 = new Invoice();
            invoice1.setInvoiceNumber("INV-001");
            invoice1.setPatient(patient1);
            invoice1.setConsultationFee(new BigDecimal("100.00"));
            invoice1.setTestsCost(new BigDecimal("250.00"));
            invoice1.setRoomCharges(new BigDecimal("100.00"));
            invoice1.setTotalAmount(new BigDecimal("450.00"));
            invoice1.setPaidAmount(new BigDecimal("450.00"));
            invoice1.setBalanceAmount(BigDecimal.ZERO);
            invoice1.setPaymentStatus("PAID");
            invoice1.setDescription("Consultation + Lab Tests");
            invoiceRepository.save(invoice1);

            Invoice invoice2 = new Invoice();
            invoice2.setInvoiceNumber("INV-002");
            invoice2.setPatient(patient2);
            invoice2.setConsultationFee(new BigDecimal("120.00"));
            invoice2.setMedicationCost(new BigDecimal("200.00"));
            invoice2.setTotalAmount(new BigDecimal("320.00"));
            invoice2.setPaidAmount(BigDecimal.ZERO);
            invoice2.setBalanceAmount(new BigDecimal("320.00"));
            invoice2.setPaymentStatus("PENDING");
            invoice2.setDescription("Consultation + Medication");
            invoiceRepository.save(invoice2);

            Invoice invoice3 = new Invoice();
            invoice3.setInvoiceNumber("INV-003");
            invoice3.setPatient(patient3);
            invoice3.setConsultationFee(new BigDecimal("100.00"));
            invoice3.setTestsCost(new BigDecimal("150.00"));
            invoice3.setRoomCharges(new BigDecimal("500.00"));
            invoice3.setTotalAmount(new BigDecimal("750.00"));
            invoice3.setPaidAmount(new BigDecimal("750.00"));
            invoice3.setBalanceAmount(BigDecimal.ZERO);
            invoice3.setPaymentStatus("PAID");
            invoice3.setDescription("Consultation + Surgery + Room");
            invoiceRepository.save(invoice3);


            System.out.println("Sample data initialized successfully!");
            System.out.println("Admin: username=admin, password=admin123");
            System.out.println("Doctor: username=drjohn, password=doctor123");
            System.out.println("Nurse: username=nurse1, password=nurse123");
            System.out.println("Receptionist: username=receptionist1, password=receptionist123");
        }
    }
}
