package org.redhatchallenge.rhc2013.server;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Created with IntelliJ IDEA.
 * User: Jun
 * Date: 30/8/13
 * Time: 10:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmailUtil {
    private static HtmlEmail mail;

    public static void sendEmail(String subject, String htmlMessage, String textMessage, String recipient) {

        mail = new HtmlEmail();

        try {
            mail.setHostName("smtp.mailgun.org");
            mail.setSmtpPort(465);
            mail.setSSLOnConnect(true);
            mail.setAuthentication("postmaster@redhatchallenge.mailgun.org", "");
            mail.setCharset(EmailConstants.UTF_8);
            mail.setFrom("contact@redhatchallenge.mailgun.org");
            mail.setSubject(subject);
            mail.setTextMsg(textMessage);
            mail.setHtmlMsg(htmlMessage);
            mail.addTo(recipient);

            mail.send();

        } catch (EmailException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public static String generateToken(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
