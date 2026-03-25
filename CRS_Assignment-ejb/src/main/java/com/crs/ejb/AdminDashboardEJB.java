package com.crs.ejb;

import com.crs.dao.AssessmentDAO;
import com.crs.dao.CourseDAO;
import com.crs.dao.EnrolmentDAO;
import com.crs.dao.MilestoneDAO;
import com.crs.dao.NotificationDAO;
import com.crs.dao.ProgressionRegistrationDAO;
import com.crs.dao.RecoveryPlanDAO;
import com.crs.dao.StudentDAO;
import com.crs.dao.StudentResultDAO;
import com.crs.dao.UserDAO;
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
import jakarta.ejb.Stateless;

import java.sql.SQLException;
import java.util.List;

@Stateless
public class AdminDashboardEJB {

    public List<User> listUsers() throws SQLException {
        return new UserDAO().findAll();
    }

    public List<Student> listStudents() throws SQLException {
        return new StudentDAO().findAll();
    }

    public List<Course> listCourses() throws SQLException {
        return new CourseDAO().findAll();
    }

    public List<Assessment> listAssessments() throws SQLException {
        return new AssessmentDAO().findAll();
    }

    public List<StudentResult> listStudentResults() throws SQLException {
        return new StudentResultDAO().findAll();
    }

    public List<ProgressionRegistration> listProgressionRegistrations() throws SQLException {
        return new ProgressionRegistrationDAO().findAll();
    }

    public List<Enrolment> listEnrolments() throws SQLException {
        return new EnrolmentDAO().findAll();
    }

    public List<RecoveryPlan> listRecoveryPlans() throws SQLException {
        return new RecoveryPlanDAO().findAll();
    }

    public List<Milestone> listMilestones() throws SQLException {
        return new MilestoneDAO().findAll();
    }

    public List<Notification> listNotifications() throws SQLException {
        return new NotificationDAO().findAll();
    }
}