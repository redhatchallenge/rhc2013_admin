package org.redhatchallenge.rhc2013.server;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.type.StandardBasicTypes;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.redhatchallenge.rhc2013.client.UserService;
import org.redhatchallenge.rhc2013.shared.Student;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
    public Boolean assignTimeSlot(List<Student> students, String timeSlot) throws IllegalArgumentException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            for(Student s : students) {
                s.setTimeslot(convertTimeSlotOthers(timeSlot));
                session.update(s);
            }
            session.getTransaction().commit();
            return true;
        }

        catch (HibernateException e) {
            session.getTransaction().rollback();
            return false;
        }
    }

    private Long convertTimeSlotOthers(String timeSlot){
        DateTimeZone.setDefault(DateTimeZone.UTC);
        DateTime time;
        Long unixTime;

        if(timeSlot.equalsIgnoreCase("23 October 2013, 9am to 10am")){
            time = new DateTime(2013, 10, 23, 1, 0);
            unixTime = time.toInstant().getMillis();
            return unixTime;
        }

        else if(timeSlot.equalsIgnoreCase("23 October 2013, 10:15AM to 11:15AM")){
            time = new DateTime(2013, 10, 23, 2, 15);
            unixTime = time.toInstant().getMillis();
            return unixTime;
        }

        else if(timeSlot.equalsIgnoreCase("23 October 2013, 11:30AM to 12:30PM")){
            time = new DateTime(2013, 10, 23, 3, 30);
            unixTime = time.toInstant().getMillis();
            return unixTime;
        }

        else if(timeSlot.equalsIgnoreCase("23 October 2013, 12:45PM to 13:45pm")){
            time = new DateTime(2013, 10, 23, 4, 45);
            unixTime = time.toInstant().getMillis();
            return unixTime;
        }

        else if(timeSlot.equalsIgnoreCase("23 October 2013, 14:00PM to 15:00PM")){
            time = new DateTime(2013, 10, 23, 6, 0);
            unixTime = time.toInstant().getMillis();
            return unixTime;
        }

        else if(timeSlot.equalsIgnoreCase("23 October 2013, 15:15PM to 16:15PM")){
            time = new DateTime(2013, 10, 23, 7, 15);
            unixTime = time.toInstant().getMillis();
            return unixTime;
        }

        else if(timeSlot.equalsIgnoreCase("23 October 2013, 16:30PM to 17:30PM")){
            time = new DateTime(2013, 10, 23, 8, 30);
            unixTime = time.toInstant().getMillis();
            return unixTime;
        }

        else if(timeSlot.equalsIgnoreCase("23 October 2013, 17:45PM to 18:45PM")){
            time = new DateTime(2013, 10, 23, 9, 45);
            unixTime = time.toInstant().getMillis();
            return unixTime;
        }

        else if(timeSlot.equalsIgnoreCase("23 October 2013, 19:00PM to 20:00PM")){
            time = new DateTime(2013, 10, 23, 11, 0);
            unixTime = time.toInstant().getMillis();
            return unixTime;
        }

        else{
            time = new DateTime(2013, 10, 23, 12, 15);
            unixTime = time.toInstant().getMillis();
            return unixTime;
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

    @Override
    public void assignTimeslotAndQuestions(String email) throws IllegalArgumentException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Student.class);
            criteria.add(Restrictions.eq("email", email));
            Student student = (Student)criteria.uniqueResult();

            Set<Integer> questions = randomQuestions();
            Integer[] arr = questions.toArray(new Integer[questions.size()]);
            int[] questionsArray = ArrayUtils.toPrimitive(arr);

            String timeslot = assignTimeslot(student.getCountry());

            student.setTimeslot(convertTimeSlot(timeslot));
            student.setQuestions(questionsArray);

            session.update(student);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
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

    /**
     * Randomly generate a set of 150 questions based on the
     * given set of 500 questions.
     *
     * @return  Set of 150 random questions.
     */
    private Set<Integer> randomQuestions() {

        Random rand = new Random();
        int max;
        int min;

        Set<Integer> listOfQuestions = new HashSet<Integer>();
        int levelOne = 69;
        int levelTwo = 52;
        int levelThree = 29;

        while(listOfQuestions.size()<levelOne) {

            max = 230;
            min = 1;
            listOfQuestions.add(rand.nextInt(max - min + 1) + min);
        }

        while(listOfQuestions.size()<levelOne + levelTwo) {
            max = 406;
            min = 231;
            listOfQuestions.add(rand.nextInt(max - min + 1) + min);
        }

        while(listOfQuestions.size()<levelOne + levelTwo + levelThree) {
            max = 500;
            min = 407;
            listOfQuestions.add(rand.nextInt(max - min + 1) + min);
        }

        return listOfQuestions;
    }

    /**
     * Assigns time slot based on the country
     *
     * @param country  Input country
     * @return  Assigned time slot. Null if none available
     */
    private String assignTimeslot(String country) {

        if(country.substring(0,5).equalsIgnoreCase("china")) {
            return null;
        }

        else {
            Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();

            try {
                SQLQuery query = currentSession.createSQLQuery("select count(*) from contestant where country not like 'China%'");
                int result = (Integer) query.addScalar("count", StandardBasicTypes.INTEGER).uniqueResult();

                if(result <=300) {
                    return "A1";
                }

                else if(result <= 600) {
                    return "A2";
                }

                else {
                    return null;
                }
            } catch (HibernateException e) {
                return null;
            }
        }
    }

    /**
     * Converts assigned time slot into an actual time value.
     *
     * @param timeslot  Timeslot to be converted
     * @return  Actual time value in millisecond format
     */
    private long convertTimeSlot(String timeslot) {

        DateTimeZone.setDefault(DateTimeZone.UTC);

        if(timeslot == null) {
            return 0;
        }

        if(timeslot.equalsIgnoreCase("A1")) {
            DateTime a1Time = new DateTime(2013, 10, 24, 6, 0);
            return a1Time.toInstant().getMillis();
        }

        else {
            DateTime a2Time = new DateTime(2013, 10, 24, 8, 0);
            return a2Time.toInstant().getMillis();
        }
    }
}