package com.crs.web.servlet;

import com.crs.ejb.AdminDashboardEJB;
import com.crs.entity.Assessment;
import com.crs.entity.Course;
import com.crs.entity.Enrolment;
import com.crs.entity.Milestone;
import com.crs.entity.Notification;
import com.crs.entity.ProgressionRegistration;
import com.crs.entity.RecoveryPlan;
import com.crs.entity.Student;
import com.crs.entity.StudentResult;
import com.crs.entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @EJB
    private AdminDashboardEJB adminDashboardEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"COURSE_ADMIN".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        req.setAttribute("activePage", "admin_dashboard");

        try {
            List<User> users = adminDashboardEJB.listUsers();
            List<Student> students = adminDashboardEJB.listStudents();
            List<Course> courses = adminDashboardEJB.listCourses();
            List<Assessment> assessments = adminDashboardEJB.listAssessments();
            List<StudentResult> studentResults = adminDashboardEJB.listStudentResults();
            List<ProgressionRegistration> registrations = adminDashboardEJB.listProgressionRegistrations();
            List<Enrolment> enrolments = adminDashboardEJB.listEnrolments();
            List<RecoveryPlan> recoveryPlans = adminDashboardEJB.listRecoveryPlans();
            List<Milestone> milestones = adminDashboardEJB.listMilestones();
            List<Notification> notifications = adminDashboardEJB.listNotifications();

            req.setAttribute("users", users);
            req.setAttribute("students", students);
            req.setAttribute("courses", courses);
            req.setAttribute("assessments", assessments);
            req.setAttribute("studentResults", studentResults);
            req.setAttribute("registrations", registrations);
            req.setAttribute("enrolments", enrolments);
            req.setAttribute("recoveryPlans", recoveryPlans);
            req.setAttribute("milestones", milestones);
            req.setAttribute("notifications", notifications);

            req.setAttribute("userCount", users.size());
            req.setAttribute("studentCount", students.size());
            req.setAttribute("courseCount", courses.size());
            req.setAttribute("assessmentCount", assessments.size());
            req.setAttribute("studentResultCount", studentResults.size());
            req.setAttribute("registrationCount", registrations.size());
            req.setAttribute("enrolmentCount", enrolments.size());
            req.setAttribute("recoveryPlanCount", recoveryPlans.size());
            req.setAttribute("milestoneCount", milestones.size());
            req.setAttribute("notificationCount", notifications.size());

        } catch (Exception e) {
            req.setAttribute("error", "Failed to load admin dashboard: " + e.getMessage());
        }

        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }
}