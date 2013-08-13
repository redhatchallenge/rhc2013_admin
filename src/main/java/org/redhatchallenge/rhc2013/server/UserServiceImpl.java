package org.redhatchallenge.rhc2013.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
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
}