package org.redhatchallenge.rhc2013.client;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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

    @UiField ListBox countryField;
    @UiField ListBox regionField;
    //    @UiField ListBox timeslotField;
    @UiField Label noTimeslot;
    @UiField CellTable<Student> timeslotCellTable;
    @UiField MySimplePager pager;
    @UiField Button noTimeSearchButton;


    private UserServiceAsync userService = UserService.Util.getInstance();
    private List<Student> studentList;
    private ListDataProvider<Student> provider;
    private List<Student> selectedStudentList = new ArrayList<Student>();
    List<Student> list = new ArrayList<Student>();

    private static final ProvidesKey<Student> KEY_PROVIDER = new ProvidesKey<Student>() {
        @Override
        public Object getKey(Student item) {
            return item.getEmail();
        }
    };

    public TimeslotScreen(){
        initWidget(UiBinder.createAndBindUi(this));

        userService.getListOfStudents(new AsyncCallback<List<Student>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<Student> students) {
                studentList = students;
                for (Student s : studentList){
                    list.add(s);
                }

                provider = new ListDataProvider<Student>(studentList);
                provider.addDataDisplay(timeslotCellTable);

                initTimeslotCellTable();
            }
        });

        pager.setDisplay(timeslotCellTable);
        pager.setPageSize(8);
    }

    private void initTimeslotCellTable(){
        List list = provider.getList();

        final MultiSelectionModel<Student> selectionModel = new MultiSelectionModel<Student>(KEY_PROVIDER);

        ColumnSortEvent.ListHandler<Student> sortHandler = new ColumnSortEvent.ListHandler<Student>(list);
        timeslotCellTable.addColumnSortHandler(sortHandler);

        Column<Student,Boolean> selectColumn = new Column<Student, Boolean>(new CheckboxCell(true, false)){
            @Override
            public Boolean getValue(Student student) {
                return selectionModel.isSelected(student);
            }
        };


        selectColumn.setFieldUpdater(new FieldUpdater<Student, Boolean>() {
            @Override
            public void update(int i, Student student, Boolean aBoolean) {
                if (aBoolean) {
                    selectedStudentList.add(student);
                }
                else {
                    selectedStudentList.remove(student);
                }
            }
        });

        Header<Boolean> selectAllHeader = new Header<Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue() {
                for (Student student : timeslotCellTable.getVisibleItems()){
                    if (!selectionModel.isSelected(student)){
                        return false;
                    }
                }
                return timeslotCellTable.getVisibleItems().size() >0;
            }
        };

        selectAllHeader.setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean aBoolean) {
                for(Student student : timeslotCellTable.getVisibleItems()){
                    selectionModel.setSelected(student, aBoolean);
                }
                if (aBoolean == true){
                    for (int i=0;i<timeslotCellTable.getVisibleItemCount(); i++){
                        if (!selectedStudentList.contains(timeslotCellTable.getVisibleItem(i)))
                            selectedStudentList.add(timeslotCellTable.getVisibleItem(i));
                    }
                }
                else if (aBoolean == false){
                    for (int i=0;i<timeslotCellTable.getVisibleItemCount(); i++){
                        selectedStudentList.remove(timeslotCellTable.getVisibleItem(i));
                    }
                }
            }
        });//End of checkbox

        Column<Student, String> emailColumn = new Column<Student, String>(new TextCell()) {
            @Override
            public String getValue(Student student) {
                return student.getEmail();
            }
        };

        emailColumn.setSortable(true);
        sortHandler.setComparator(emailColumn, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1 != null) {
                    return (o2 != null) ? o1.getEmail().compareTo(o2.getEmail()) : 1;
                }
                return -1;
            }
        });

        ArrayList<String> countryList = new ArrayList<String>();
        countryList.add("Singapore");
        countryList.add("Malaysia");
        countryList.add("Thailand");
        countryList.add("China/Beijing");
        countryList.add("China/Shanghai");
        countryList.add("China/Wuhan");
        countryList.add("China/Dalian");
        countryList.add("China/Jinan");
        countryList.add("China/Others");
        countryList.add("Hong Kong");
        countryList.add("Taiwan");

        Column<Student, String> countryColumn = new Column<Student, String>(new SelectionCell(countryList)) {
            @Override
            public String getValue(Student student) {
                return student.getCountry();
            }
        };

        countryColumn.setSortable(true);
        sortHandler.setComparator(countryColumn, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1 != null) {
                    return (o2 != null) ? o1.getCountry().compareTo(o2.getCountry()) : 1;
                }
                return -1;
            }
        });

        countryColumn.setFieldUpdater(new FieldUpdater<Student, String>() {
            @Override
            public void update(int index, Student object, String value) {
                object.setCountry(value);
                userService.updateStudentData(object, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if(!result) {
                            displayErrorBox("Failed", "Update has failed");
                        }

                        else {
                            timeslotCellTable.redraw();
                        }
                    }
                });
            }
        });

        Column<Student, String> timeSlotColumn = new Column<Student, String>(new TextCell()) {
            @Override
            public String getValue(Student student) {
                if(student.getTimeslot() == 0){
                    return "Time Slot is not Assigned";
                }
                else{
                    Date date = new Date(student.getTimeslot());
                    return DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(date);
                }
            }
        };

        timeSlotColumn.setSortable(true);
        sortHandler.setComparator(timeSlotColumn, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1 != null) {
                    return (o2 != null) ? String.valueOf(o1.getTimeslot()).compareTo(String.valueOf(o2.getTimeslot())) : 1;
                }
                return -1;
            }
        });

        timeslotCellTable.addColumn(selectColumn, selectAllHeader);
        timeslotCellTable.addColumn(emailColumn, "Email");
        timeslotCellTable.addColumn(countryColumn, "Region");
        timeslotCellTable.addColumn(timeSlotColumn, "Time Slot");

        timeslotCellTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<Student> createCheckboxManager(timeslotCellTable.getColumnIndex(selectColumn)));
    }

    @UiHandler("countryField")
    public void handleCountryChange(ChangeEvent event) {
        String contains;
        switch (countryField.getSelectedIndex()) {
            //Do nothing
            case 0:
                list.clear();
                for (Student s : studentList){
                        list.add(s);
                }
                break;

            // Singapore
            case 1:
                regionField.setVisible(false);
                 contains = countryField.getItemText(countryField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().equals(contains)){
                        list.add(s);
                    }
                }
                regionField.setSelectedIndex(0);
                break;
            // Malaysia
            case 2:
                regionField.setVisible(false);
                 contains = countryField.getItemText(countryField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().equals(contains)){
                        list.add(s);
                    }
                }
                regionField.setSelectedIndex(0);
                break;
            // Thailand
            case 3:
                regionField.setVisible(false);
                 contains = countryField.getItemText(countryField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().equals(contains)){
                        list.add(s);
                    }
                }
                regionField.setSelectedIndex(0);
                break;
            // China
            case 4:
                regionField.setVisible(true);
                contains = countryField.getItemText(countryField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().contains(contains)){
                        list.add(s);
                    }
                }
                regionField.setSelectedIndex(0);
                break;
            // Hong Kong
            case 5:
                regionField.setVisible(false);
                 contains = countryField.getItemText(countryField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().equals(contains)){
                        list.add(s);
                    }
                }
                regionField.setSelectedIndex(0);
                break;
            // Taiwan
            case 6:
                regionField.setVisible(false);
                 contains = countryField.getItemText(countryField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().equals(contains)){
                        list.add(s);
                    }
                }
                regionField.setSelectedIndex(0);
                break;
        }
        provider.setList(list);
    }


    @UiHandler("regionField")
    public void handleRegionChange(ChangeEvent event) {
        String contains;
        String region = null;
        switch (regionField.getSelectedIndex()) {
            case 0:
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().contains("China")){
                        list.add(s);
                    }
                }
                break;
            // Beijing
            case 1:
                region = "Beijing";
                contains = regionField.getItemText(regionField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().contains(contains)){
                        list.add(s);
                    }
                }
                countContestant(region);
                break;
            //Shanghai
            case 2:
                region = "Shanghai";
                contains = regionField.getItemText(regionField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().contains(contains)){
                        list.add(s);
                    }
                }
                countContestant(region);
                break;
            // Wuhan
            case 3:
                region = "Wuhan";
                contains = regionField.getItemText(regionField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().contains(contains)){
                        list.add(s);
                    }
                }
                countContestant(region);
                break;
            // Dalian
            case 4:
                region = "Dalian";
                contains = regionField.getItemText(regionField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().contains(contains)){
                        list.add(s);
                    }
                }
                countContestant(region);
                break;
            // Jinan
            case 5:
                region = "Jinan";
                contains = regionField.getItemText(regionField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().contains(contains)){
                        list.add(s);
                    }
                }
                countContestant(region);
                break;
            // Others
            case 6:
                region = "Others";
                contains = regionField.getItemText(regionField.getSelectedIndex());
                list.clear();
                for (Student s : studentList){
                    if (s.getCountry().contains(contains)){
                        list.add(s);
                    }
                }
                countContestant(region);
                break;
        }
        provider.setList(list);
    }


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

    private void displayErrorBox(String errorHeader, String message) {
        final DialogBox errorBox = new DialogBox();
        errorBox.setText(errorHeader);
        final HTML errorLabel = new HTML();
        errorLabel.setHTML(message);
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        final Button closeButton = new Button("Close");
        closeButton.setEnabled(true);
        closeButton.getElement().setId("close");
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                errorBox.hide();
            }
        });
        verticalPanel.add(errorLabel);
        verticalPanel.add(closeButton);
        errorBox.setWidget(verticalPanel);
        errorBox.center();
    }

    @UiHandler("noTimeSearchButton")
    public void onTimeSearchClick(ClickEvent event){
        List<Student> noTimeSlotList = new ArrayList<Student>();
        for (Student student : list)
        {
            noTimeSlotList.add(student);
        }
        list.clear();
        for (Student s : noTimeSlotList){
            if (s.getTimeslot() == 0){
                list.add(s);
            }
        }
        provider.setList(list);
    }
}
