package net.mausberg.authentication_framework_backend.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import net.mausberg.authentication_framework_backend.model.AppUser;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	//@Value("${frontend.url}")
	//private String frontendUrl; 
	
	public void sendRegistrationEmail(AppUser appUser, String frontendUrl) throws MessagingException{
		String mail = appUser.getMail();
		String subject = "Herzlich Wilkommen";
		
		String body = "<html>"
				+ "<body>"
				+ "<p>Herzlich Willkommen " + appUser.getFirstName() + ",</p>"
				+ "<p>Hier ist dein Verifizierungslink:</p>"
				+ "<p><a href=\"" + frontendUrl + "/auth/verifymail?token=" + appUser.getVerificationToken() + "\">"
				+ "Verifiziere jetzt dein Konto</a></p>"
				+ "<p>Mit freundlichen Grüßen,<br/>Felix</p>"
				+ "</body>"
				+ "</html>";

		sendMail(subject, body, mail);
	}
	
	public void sendPasswordResetTokenEmail(AppUser appUser, String frontendUrl) throws MessagingException {
		String mail = appUser.getMail();
		String subject = "Password Reset";
		
		String body = "<html>"
				+ "<body>"
				+ "<p>Hallo " + appUser.getFirstName() + ",</p>"
				+ "<p>Klicke hier, um dein Passwort zurückzusetzen:</p>"
				+ "<p><a href=\"" + frontendUrl + "/passwordreset?token=" + appUser.getPasswordResetToken() + "\">"
				+ "Password zurücksetzen</a></p>"
				+ "<p>Mit freundlichen Grüßen,<br/>Felix</p>"
				+ "</body>"
				+ "</html>";

		sendMail(subject, body, mail);
	}
	
	public void sendMail(String subject, String body, String recipient) throws MessagingException {
			MimeMessage message = mailSender.createMimeMessage();
			message.setFrom(fromEmail);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); // Corrected this line
			message.setSubject(subject);
			message.setContent(body, "text/html; charset=utf-8");

			mailSender.send(message);

	}
}
