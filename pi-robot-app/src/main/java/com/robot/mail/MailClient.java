package com.robot.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Lechu on 14.09.2016.
 */
@Service
public class MailClient {
    private static Logger logger = LoggerFactory.getLogger(MailClient.class);
    @Value("${email.address}")
    private String addressFrom;
    @Value("${email.password}")
    private String password;

    public void sendEmail(String subject, String message, String fileName, String to) {
        try {
            logger.info("send email...");
            Session session = getSession();
            logger.info("Session created..");
            sendEmail(session, to, subject, message);
            logger.info("Email sent.");
        } catch (Exception e) {
            logger.info("Could not send email", e);
        }
    }

    private Session getSession() {
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(addressFrom, password);
            }
        });
    }

    public void sendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);

            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(addressFrom, "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse(addressFrom, false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);
        } catch (Exception e) {
            logger.error("Email not sent", e);
        }
    }
}
