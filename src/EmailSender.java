import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.AddressException;

public class EmailSender {

    private static String USER_NAME = "SpeedrunScrum";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "nztv mizc vvnm nkmi"; // GMail password

    public EmailSender(String recipient, String code) {
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = { recipient }; // list of recipient email addresses
        String subject = "Speedrun password change!";
        String body = "It seems you have requested a password change, here is your temporary code: \n" + code;
        String html = "<h1 style='color:DARKCYAN;'>Speedrun</h1> <p> - It seems you have requested a password change, here is your temporary code: <b> \n" + code + "</b> </p>";

        sendFromGMail(from, pass, to, subject, body, html);
    }

    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body, String html) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
            
            message.setSubject(subject);
            message.setContent(html, "text/html");
            //message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}