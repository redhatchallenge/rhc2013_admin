package org.redhatchallenge.rhc2013.server;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.redhatchallenge.rhc2013.shared.ConfirmationTokens;
import org.redhatchallenge.rhc2013.shared.Student;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 */
public class SendConfirmationEmail implements Runnable {

        private final ServletContext servletContext;
        private final String email;

        SendConfirmationEmail(String email, ServletContext servletContext) {
            this.email = email;
            this.servletContext = servletContext;
        }

        @Override
        public void run() {

            ConfirmationTokens token = new ConfirmationTokens();
            token.setToken(EmailUtil.generateToken(32));
            token.setEmail(email);

            String html = null;


            Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
            currentSession.beginTransaction();

            Criteria criteria = currentSession.createCriteria(Student.class);
            criteria.add(Restrictions.eq("email", email));
            Student student = (Student) criteria.uniqueResult();

            try {
                if (student.getLanguage().equalsIgnoreCase("English")) {
                    String path = servletContext.getRealPath("emails/confirm_en.html");
                    html = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
                    html = html.replaceAll("REPLACEME", "https://redhatchallenge2013-rhc2013.rhcloud.com/?locale=en#confirmToken/" + token.getToken());
                } else if (student.getLanguage().equalsIgnoreCase("Chinese (Simplified)")) {
                    String path = servletContext.getRealPath("emails/confirm_ch.html");
                    html = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
                    html = html.replaceAll("REPLACEME", "https://redhatchallenge2013-rhc2013.rhcloud.com/?locale=ch#confirmToken/" + token.getToken());
                } else if (student.getLanguage().equals("Chinese (Traditional)")) {
                    String path = servletContext.getRealPath("emails/confirm_zh.html");
                    html = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
                    html = html.replaceAll("REPLACEME", "https://redhatchallenge2013-rhc2013.rhcloud.com/?locale=zh#confirmToken/" + token.getToken());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            EmailUtil.sendEmail("Confirmation of account", html, "Your client does not support HTML messages, your token is " + token.getToken(),
                    email);

            currentSession.save(token);
            currentSession.getTransaction().commit();
        }
    }
