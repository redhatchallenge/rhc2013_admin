package org.redhatchallenge.rhc2013.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.redhatchallenge.rhc2013.client.UserService;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.List;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class UserServiceImpl extends RemoteServiceServlet implements UserService {

    @Override
    public List<Student> getListOfStudents() throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //noinspection unchecked
        List<Student> studentList = session.createCriteria(Student.class).list();
        session.close();
        return studentList;
    }

    @Override
    public Boolean updateStudentData(Student student) throws IllegalArgumentException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            session.beginTransaction();
            session.update(student);
            session.getTransaction().commit();
            return true;
        }

        catch (HibernateException e) {
            session.close();
            return false;

        }
    }

    @Override
    public Boolean deleteStudents(List<Student> students) throws IllegalArgumentException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            for(Student s : students) {
                session.delete(s);
            }

            session.getTransaction().commit();
            return true;
        }

        catch (HibernateException e) {
            session.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Boolean registerStudent(String email, String password, String firstName, String lastName,
                                   String contact, String country, String countryCode, String school,
                                   String lecturerFirstName, String lecturerLastName, String lecturerEmail,
                                   String language, Boolean verified) throws IllegalArgumentException {

        /**
         * Escape the all inputs received except for the password.
         * This is because the password will be hashed anyway so
         * it won't be affected by XSS is any way.
         */

        email = SecurityUtil.escapeInput(email);
        firstName = SecurityUtil.escapeInput(firstName);
        lastName = SecurityUtil.escapeInput(lastName);
        contact = SecurityUtil.escapeInput(contact);
        country = SecurityUtil.escapeInput(country);
        countryCode = SecurityUtil.escapeInput(countryCode);
        school = SecurityUtil.escapeInput(school);
        lecturerFirstName = SecurityUtil.escapeInput(lecturerFirstName);
        lecturerLastName = SecurityUtil.escapeInput(lecturerLastName);
        lecturerEmail = SecurityUtil.escapeInput(lecturerEmail);
        language = SecurityUtil.escapeInput(language);

        password = SecurityUtil.hashPassword(password);

        Student student = new Student();
        student.setEmail(email);
        student.setPassword(password);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setContact(contact);
        student.setCountry(country);
        student.setCountryCode(countryCode);
        student.setSchool(school);
        student.setLecturerFirstName(lecturerFirstName);
        student.setLecturerLastName(lecturerLastName);
        student.setLecturerEmail(lecturerEmail);
        student.setLanguage(language);
        student.setVerified(verified);

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(student);
            session.getTransaction().commit();

            return true;
        } catch (ConstraintViolationException e) {
            session.getTransaction().rollback();
            return false;
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            return false;
        }
    }
}