package app.mail;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import app.services.MailService;

public class JakartaMailService implements MailService {
    private final Session session;
    private final String from;

    public JakartaMailService(String host, int port, String username, String password) {
        var properties = new Properties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        this.session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        this.from = username;
    }

    public void send(String to, String subject, String text) {
        var mimeMessage = new MimeMessage(session);

        try {
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(text);

            Transport.send(mimeMessage);
        } catch (MessagingException ex) {
            // TODO: handle exception
        }
    }
}
