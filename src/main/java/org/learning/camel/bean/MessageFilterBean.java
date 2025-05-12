package org.learning.camel.bean;

import com.sun.mail.imap.IMAPMessage;
import org.apache.camel.Exchange;
import org.apache.camel.component.mail.MailMessage;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import java.util.HashSet;

public class MessageFilterBean {
    private static final String FILTRATING_EMAIL = "shmyrovyury@gmail.com";
    public static final HashSet<String> stringContainer = new HashSet<>();
    public boolean allowedOnlyUnique(Exchange exchange){
        String curString = exchange.getIn().getBody(String.class);
        return stringContainer.add(curString);
    }
    public boolean emailFilter(Exchange exchange){
        try {
            MailMessage mailMessage = exchange.getIn(MailMessage.class);
            if (mailMessage == null) {
                return false;
            }
            Message original = mailMessage.getOriginalMessage();
            if (!(original instanceof IMAPMessage)) {
                return false;
            }
            IMAPMessage imapMessage = (IMAPMessage) original;
            Address[] fromAddresses = imapMessage.getFrom();
            if (fromAddresses != null && fromAddresses.length > 0 && fromAddresses[0] instanceof InternetAddress) {
                String senderEmail = ((InternetAddress) fromAddresses[0]).getAddress();
                return FILTRATING_EMAIL.equalsIgnoreCase(senderEmail);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
