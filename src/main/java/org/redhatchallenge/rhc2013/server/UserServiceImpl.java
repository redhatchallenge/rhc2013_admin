package org.redhatchallenge.rhc2013.server;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.redhatchallenge.rhc2013.client.UserService;
import org.redhatchallenge.rhc2013.shared.Student;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class UserServiceImpl extends RemoteServiceServlet implements UserService {

    @Override
    public List<Student> getListOfStudents() throws IllegalArgumentException {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            //noinspection unchecked
            List<Student> studentList = session.createCriteria(Student.class).list();
            session.close();
            return studentList;
        } catch (HibernateException e) {
            session.close();
            return null;
        }
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

    @Override
    public String exportCsv(List<Student> students) throws IllegalArgumentException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CSVWriter writer;
        try {
            String fname = UUID.randomUUID().toString();
            writer = new CSVWriter(new FileWriter(System.getenv("OPENSHIFT_TMP_DIR") + fname + ".csv"));
            List<String[]> list = new ArrayList<String[]>();

            for(Student s : students) {
                list.add(studentToStringArray(s));
            }

            writer.writeAll(list);
            writer.close();

            return fname + ".csv";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a Student entity to a String[] representation.
     *
     * @param student  Student entity to be converted
     * @return  String[] representation of the Student entity
     */
    private String[] studentToStringArray(Student student) {

        String[] strings = new String[14];
        strings[0] = student.getEmail();
        strings[1] = student.getPassword();
        strings[2] = student.getFirstName();
        strings[3] = student.getLastName();
        strings[4] = student.getContact();
        strings[5] = student.getCountry();
        strings[6] = student.getCountryCode();
        strings[7] = student.getSchool();
        strings[8] = student.getLecturerFirstName();
        strings[9] = student.getLecturerLastName();
        strings[10] = student.getLecturerEmail();
        strings[11] = student.getLanguage();
        strings[12] = student.getVerified().toString();
        strings[13] = student.getStatus().toString();

        return strings;
    }
}