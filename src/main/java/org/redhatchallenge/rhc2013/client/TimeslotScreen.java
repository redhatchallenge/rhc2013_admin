package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: winnie
 * Date: 8/28/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeslotScreen extends Composite {
    interface TimeslotUiBinder extends com.google.gwt.uibinder.client.UiBinder<Widget, TimeslotScreen> {
    }

    private static TimeslotUiBinder UiBinder = GWT.create(TimeslotUiBinder.class);
    private UserServiceAsync userService = UserService.Util.getInstance();

    public TimeslotScreen(){

        initWidget(UiBinder.createAndBindUi(this));
    }

    @UiField
    ListBox countryField;
    @UiField ListBox regionField;
    //    @UiField ListBox timeslotField;
    @UiField
    Label noTimeslot;


    @UiHandler("countryField")
    public void handleCountryChange(ChangeEvent event) {
        switch (countryField.getSelectedIndex()) {
            // Singapore
            case 0:
                regionField.setVisible(false);
                break;
            // Malaysia
            case 1:
                regionField.setVisible(false);
                break;
            // Thailand
            case 2:
                regionField.setVisible(false);
                break;
            // China
            case 3:
                regionField.setVisible(true);
                break;
            // Hong Kong
            case 4:
                regionField.setVisible(false);
                break;
            // Taiwan
            case 5:
                regionField.setVisible(false);
                break;
        }
    }


    @UiHandler("regionField")
    public void handleRegionChange(ChangeEvent event) {

        String region = null;
        switch (regionField.getSelectedIndex()) {
            case 0:
                break;
            // Beijing
            case 1:
                region = "Beijing";
                countContestant(region);
                break;
            //Shanghai
            case 2:
                region = "Shanghai";
                countContestant(region);
                break;
            // Wuhan
            case 3:
                region = "Wuhan";
                countContestant(region);
                break;
            // Dalian
            case 4:
                region = "Dalian";
                countContestant(region);
                break;
            // Jinan
            case 5:
                region = "Jinan";
                countContestant(region);
                break;
            // Others
            case 6:
                region = "Others";
                countContestant(region);
                break;
        }
    }

//    @UiHandler("timeslotField")
//    public void handleTimeslotChange(ChangeEvent event){
//        Student student = new Student();
//        switch (timeslotField.getSelectedIndex()) {
//            case 0:
//                //Wed, 23 Oct 2013 09:00:00 GMT
//                student.setTimeslot(1382518800);
//                break;
//            case 1:
//                //Wed, 23 Oct 2013 10:15:00 GMT
//                student.setTimeslot(1382523300);
//                break;
//            case 2:
//                //Wed, 23 Oct 2013 11:30:00 GMT
//                student.setTimeslot(1382527800);
//                break;
//            case 3:
//                //Wed, 23 Oct 2013 12:45:00 GMT
//                student.setTimeslot(1382532300);
//                break;
//            case 4:
//                //Wed, 23 Oct 2013 14:00:00 GMT
//                student.setTimeslot(1382536800);
//                break;
//            case 5:
//                //Wed, 23 Oct 2013 15:15:00 GMT
//                student.setTimeslot(1382541300);
//                break;
//            case 6:
//                //Wed, 23 Oct 2013 16:30:00 GMT
//                student.setTimeslot(1382545800);
//                break;
//            case 7:
//                //Wed, 23 Oct 2013 17:45:00 GMT
//                student.setTimeslot(1382550300);
//                break;
//            case 8:
//                //Wed, 23 Oct 2013 19:00:00 GMT
//                student.setTimeslot(1382554800);
//                break;
//            case 9:
//                //Wed, 23 Oct 2013 20:15:00 GMT
//                student.setTimeslot(1382559300);
//                break;
//        }
//    }

    private void countContestant(String region){
        final String region1 = region;

        userService = UserService.Util.getInstance();

        userService.getListOfStudents(new AsyncCallback<List<Student>>(){

            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
                noTimeslot.setText("Error123");
            }

            @Override
            public void onSuccess(List<Student> studentList) {

                int counter = 0;
//            String region = regionField.getItemText(regionField.getSelectedIndex());
                for(Student s : studentList) {
                    if(s.getCountry().substring(6).equals(region1)){
                        if(s.getTimeslot() == 0){
                            counter++;
                        }
                    }
                }
                noTimeslot.setText("Number of contestant from "+ region1 + " without timeslot: " + counter);


            }
        });

    }

}
