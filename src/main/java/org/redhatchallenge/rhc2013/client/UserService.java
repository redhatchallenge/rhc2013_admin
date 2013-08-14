package org.redhatchallenge.rhc2013.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.List;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
@RemoteServiceRelativePath("UserService")
public interface UserService extends RemoteService {

    List<Student> getListOfStudents() throws IllegalArgumentException;
    Boolean updateStudentData(Student student) throws IllegalArgumentException;
    Boolean deleteStudents(List<Student> students) throws IllegalArgumentException;

    public static class Util {
        private static final UserServiceAsync Instance = (UserServiceAsync) GWT.create(UserService.class);

        public static UserServiceAsync getInstance() {
            return Instance;
        }
    }
}
