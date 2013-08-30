package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.apache.commons.lang3.RandomStringUtils;
import org.redhatchallenge.rhc2013.shared.FieldVerifier;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class RegisterScreen extends Composite {
    interface RegisterScreenUiBinder extends UiBinder<Widget, RegisterScreen> {
    }

    private static RegisterScreenUiBinder UiBinder = GWT.create(RegisterScreenUiBinder.class);

    @UiField TextBox emailField;
    @UiField TextBox passwordField;
    @UiField TextBox confirmPasswordField;
    @UiField TextBox firstNameField;
    @UiField TextBox lastNameField;
    @UiField TextBox contactField;
    @UiField ListBox countryField;
    @UiField ListBox regionField;
    @UiField ListBox countryCodeField;
    @UiField TextBox schoolField;
    @UiField TextBox lecturerFirstNameField;
    @UiField TextBox lecturerLastNameField;
    @UiField TextBox lecturerEmailField;
    @UiField ListBox languageField;
    @UiField Button registerButton;
    @UiField CheckBox verifiedField;
    @UiField Button generatePasswordButton;
    @UiField Button backButton;
    @UiField Label messageLabel;

    //Validation Error Labels
    @UiField Label emailLabel;
    @UiField Label passwordLabel;
    @UiField Label confirmPasswordLabel;
    @UiField Label firstNameLabel;
    @UiField Label lastNameLabel;
    @UiField Label contactLabel;
    @UiField Label schoolLabel;

    private UserServiceAsync userService = null;

    public RegisterScreen() {
        initWidget(UiBinder.createAndBindUi(this));
    }

    @UiHandler("countryField")
    public void handleCountryChange(ChangeEvent event) {
        switch (countryField.getSelectedIndex()) {
            // Singapore
            case 0:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(0);
                regionField.setVisible(false);
                break;
            // Malaysia
            case 1:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(1);
                regionField.setVisible(false);
                break;
            // Thailand
            case 2:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(2);
                regionField.setVisible(false);
                break;
            // China
            case 3:
                languageField.setSelectedIndex(1);
                countryCodeField.setSelectedIndex(3);
                regionField.setVisible(true);
                break;
            // Hong Kong
            case 4:
                languageField.setSelectedIndex(0);
                countryCodeField.setSelectedIndex(4);
                regionField.setVisible(false);
                break;
            // Taiwan
            case 5:
                languageField.setSelectedIndex(2);
                countryCodeField.setSelectedIndex(5);
                regionField.setVisible(false);
                break;
        }
    }

    @UiHandler("generatePasswordButton")
    public void handleGeneratePasswordButtonClick(ClickEvent event) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder sb = new StringBuilder( 12 );
        for( int i = 0; i < 12; i++ )
            sb.append( AB.charAt( Random.nextInt(AB.length()) ) );

        passwordField.setText(sb.toString());
        confirmPasswordField.setText(sb.toString());
    }

    @UiHandler("registerButton")
    public void handleRegisterButtonClick(ClickEvent event) {
        int successCounter = 0;

        if(FieldVerifier.emailIsNull(emailField.getText())){
            emailLabel.setText("Email field cannot be empty.");
        }
        else if(!FieldVerifier.isValidEmail(emailField.getText())){
            emailLabel.setText("You have entered an invalid email format.");
        }
        else{
            emailLabel.setText("");
            successCounter++;
        }

        if(FieldVerifier.passwordIsNull(passwordField.getText())){
            passwordLabel.setText("Password field cannot be empty.");
        }

        else{
            passwordLabel.setText("");
            successCounter++;
        }

        if(FieldVerifier.passwordIsNull(confirmPasswordField.getText())){
            confirmPasswordLabel.setText("Confirm password field cannot be empty.");
        }
        else if(!confirmPasswordField.getText().equals(passwordField.getText())){
            confirmPasswordLabel.setText("Password does not match.");
        }
        else{
            confirmPasswordLabel.setText("");
            successCounter++;
        }

        if(FieldVerifier.fnIsNull(firstNameField.getText())){
            firstNameLabel.setText("First Name field cannot be empty.");
        }
        else{
            firstNameLabel.setText("");
            successCounter++;
        }

        if(FieldVerifier.lnIsNull(lastNameField.getText())){
            lastNameLabel.setText("Last name cannot be empty.");
        }
        else{
            lastNameLabel.setText("");
            successCounter++;
        }

        if(FieldVerifier.contactIsNull(contactField.getText())){
            contactLabel.setText("Contact field cannot be empty.");
        }
        else if(!FieldVerifier.isValidContact(contactField.getText())){
            contactLabel.setText("You have entered an invalid contact number.");
        }
        else{
            contactLabel.setText("");
            successCounter++;
        }

        if(FieldVerifier.schoolIsNull(schoolField.getText())){
            schoolLabel.setText("School field cannot be empty.");
        }
        else{
            schoolLabel.setText("");
            successCounter++;
        }

        if(successCounter == 7){
            registerStudent();
        }
    }

    @UiHandler("backButton")
    public void handleBackButtonClick(ClickEvent event) {
        ContentContainer.INSTANCE.setContent(new UserScreen());
    }

    private void registerStudent() {

        final String email = emailField.getText();
        final String password = passwordField.getText();
        final String firstName = firstNameField.getText();
        final String lastName = lastNameField.getText();
        final String contact = contactField.getText();
        final String countryCode = countryCodeField.getItemText(countryCodeField.getSelectedIndex());
        final String school = schoolField.getText();
        final String lecturerFirstName = lecturerFirstNameField.getText();
        final String lecturerLastName = lecturerLastNameField.getText();
        final String lecturerEmail = lecturerEmailField.getText();
        final String language = languageField.getItemText(languageField.getSelectedIndex());
        final Boolean verified = verifiedField.getValue();
        final String country;

        /**
         * If country is China, append the region.
         */
        if(countryField.getItemText(countryField.getSelectedIndex()).equalsIgnoreCase("china")) {
            country = countryField.getItemText(countryField.getSelectedIndex()) + "/" +
                    regionField.getItemText(regionField.getSelectedIndex());
        }

        else {
            country = countryField.getItemText(countryField.getSelectedIndex());
        }

        userService = UserService.Util.getInstance();

        userService.registerStudent(email, password, firstName, lastName, contact,
                country, countryCode, school, lecturerFirstName, lecturerLastName,
                lecturerEmail, language, verified, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                messageLabel.setText("An unexpected error has occurred, please try again later!");
            }

            @Override
            public void onSuccess(Boolean bool) {
                if(bool) {
                    messageLabel.setText("Successful");
                    emailField.setText("");
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                    firstNameField.setText("");
                    lastNameField.setText("");
                    contactField.setText("");
                    schoolField.setText("");
                    lecturerEmailField.setText("");
                    lecturerFirstNameField.setText("");
                    lecturerLastNameField.setText("");
                }

                else {
                    messageLabel.setText("Someone has already used this email/contact. Try another?");
                }
            }
        });
    }
}