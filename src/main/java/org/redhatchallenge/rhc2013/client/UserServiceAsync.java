package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.redhatchallenge.rhc2013.shared.Student;

import java.io.IOException;
import java.util.List;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public interface UserServiceAsync {
    void getListOfStudents(AsyncCallback<List<Student>> async);

    void updateStudentData(Student student, AsyncCallback<Boolean> async);

    void deleteStudents(List<Student> students, AsyncCallback<Boolean> async);

    void registerStudent(String email, String password, String firstName, String lastName, String contact,
                         String country, String countryCode, String school, String lecturerFirstName, String lecturerLastName,
                         String lecturerEmail, String language, Boolean verified, AsyncCallback<Boolean> async);

    void exportCsv(List<Student> students, AsyncCallback<String> async);

    void assignTimeslotAndQuestions(String email, AsyncCallback<Void> async);
}
