import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import axios from '../api/axios';
import { BarChart, Bar, PieChart, Pie, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import './Dashboard.css';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user.role === 'ADMIN' || user.role === 'DOCTOR' || user.role === 'RECEPTIONIST') {
      loadStats();
    } else {
      setLoading(false);
    }
  }, [user.role]);

  const loadStats = async () => {
    try {
     const response = await axios.get('/dashboard/stats');
      setStats(response.data);
    } catch (error) {
      console.error('Error loading stats:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <nav className="dashboard-nav">
        <h1>Hospital Management System</h1>
        <div className="nav-right">
          <span>Welcome, {user.fullName} ({user.role})</span>
          <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
      </nav>

      <div className="dashboard-content">
        <div className="sidebar">
          <button onClick={() => navigate('/dashboard')}>Dashboard</button>
          {(user.role === 'ADMIN' || user.role === 'DOCTOR' || user.role === 'NURSE' || user.role === 'RECEPTIONIST') && (
            <button onClick={() => navigate('/patients')}>Patients</button>
          )}
          <button onClick={() => navigate('/doctors')}>Doctors</button>
          <button onClick={() => navigate('/appointments')}>Appointments</button>
          {(user.role === 'ADMIN' || user.role === 'RECEPTIONIST') && (
            <button onClick={() => navigate('/invoices')}>Invoices</button>
          )}
          {user.role === 'ADMIN' && (
            <>
              <button onClick={() => navigate('/departments')}>Departments</button>
              <button onClick={() => navigate('/rooms')}>Rooms</button>
              <button onClick={() => navigate('/staff')}>Staff</button>
              <button onClick={() => navigate('/users')}>Users</button>
            </>
          )}
        </div>

        <div className="main-content">
          <h2>Dashboard</h2>
          
          {loading && <div>Loading statistics...</div>}
          
          {!loading && stats && (
            <>
              <div className="stats-grid">
                <div className="stat-card">
                  <h3>Total Patients</h3>
                  <p className="stat-number">{stats.totalPatients}</p>
                </div>
                <div className="stat-card">
                  <h3>Total Doctors</h3>
                  <p className="stat-number">{stats.totalDoctors}</p>
                </div>
                <div className="stat-card">
                  <h3>Total Appointments</h3>
                  <p className="stat-number">{stats.totalAppointments}</p>
                </div>
                <div className="stat-card">
                  <h3>Today's Appointments</h3>
                  <p className="stat-number">{stats.todayAppointments}</p>
                </div>
              </div>

              <div className="charts-section">
                <div className="chart-container">
                  <h3>Overview Statistics</h3>
                  <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={[
  { name: 'Patients', value: stats.totalPatients },
  { name: 'Doctors', value: stats.totalDoctors },
  { name: 'Appointments', value: stats.totalAppointments },
  { name: 'Today', value: stats.todayAppointments }
]}>
  <CartesianGrid strokeDasharray="3 3" />
  <XAxis dataKey="name" />
  <YAxis />
  <Tooltip />
  <Legend />
  <Bar dataKey="value">
    <Cell fill="#4f46e5" />  {/* Patients */}
    <Cell fill="#10b981" />  {/* Doctors */}
    <Cell fill="#f59e0b" />  {/* Appointments */}
    <Cell fill="#ef4444" />  {/* Today */}
  </Bar>
</BarChart>

                  </ResponsiveContainer>
                </div>
                
                <div className="chart-container">
                  <h3>Appointment Status Distribution</h3>
                  <ResponsiveContainer width="100%" height={300}>
                    <PieChart>
                      <Pie
  data={[
    { name: 'Scheduled', value: stats.scheduledAppointments || 0 },
    { name: 'Completed', value: stats.completedAppointments || 0 },
    { name: 'Cancelled', value: stats.cancelledAppointments || 0 }
  ]}
  cx="50%"
  cy="50%"
  labelLine={false}
  label={({ name, percent }) => (
    <text fill="#333" textAnchor="middle" fontSize={12}>
      {`${name}: ${(percent * 100).toFixed(0)}%`}
    </text>
  )}
  outerRadius={80}
  dataKey="value"
>
  <Cell fill="#fbbf24" />  {/* Scheduled - yellow */}
  <Cell fill="#10b981" />  {/* Completed - green */}
  <Cell fill="#ef4444" />  {/* Cancelled - red */}
</Pie>
                      <Tooltip />
                      <Legend />
                    </PieChart>
                  </ResponsiveContainer>
                </div>
              </div>

              {stats.recentAppointments && stats.recentAppointments.length > 0 && (
                <div className="recent-appointments">
                  <h3>Recent Appointments</h3>
                  <table>
                    <thead>
                      <tr>
                        <th>Patient</th>
                        <th>Doctor</th>
                        <th>Date</th>
                        <th>Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {stats.recentAppointments.map((apt) => (
                        <tr key={apt.id}>
                          <td>{apt.patientName}</td>
                          <td>{apt.doctorName}</td>
                          <td>{new Date(apt.date).toLocaleString()}</td>
                          <td><span className={`status ${apt.status.toLowerCase()}`}>{apt.status}</span></td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </>
          )}

          {!loading && !stats && (
            <div className="welcome-message">
              <h3>Welcome to Hospital Management System</h3>
              <p>Use the sidebar to navigate through different sections.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;