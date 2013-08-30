package org.redhatchallenge.rhc2013.shared;


public class FieldVerifier {

    public static boolean isValidEmail(String email) {
        return email.toUpperCase().matches("^[0-9A-Z._-]+@[0-9A-Z.]{2,120}$");
    }

    public static boolean emailIsNull(String email){

        return email.isEmpty();
    }

    public static boolean passwordIsNull(String password){

        return password.isEmpty();
    }

    public static boolean fnIsNull(String firstName){

        return firstName.isEmpty();
    }

    public static boolean lnIsNull(String lastName){

        return lastName.isEmpty();
    }

    public static boolean contactIsNull(String contact){

        return contact.isEmpty();
    }

    public static boolean isValidContact(String contact) {
        return contact.matches("^[0-9]+$");
    }

    public static boolean schoolIsNull(String school){

        return school.isEmpty();
    }
}
