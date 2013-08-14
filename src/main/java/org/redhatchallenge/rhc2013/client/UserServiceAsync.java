package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.List;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public interface UserServiceAsync {
    void getListOfStudents(AsyncCallback<List<Student>> async);

    void updateStudentData(Student student, AsyncCallback<Boolean> async);

    void deleteStudents(List<Student> students, AsyncCallback<Boolean> async);
}
