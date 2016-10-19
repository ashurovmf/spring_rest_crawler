package com.gft.backend.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by miav on 2016-10-18.
 */
@Service
public class MailService {
    private static final Logger logger = Logger.getLogger(MailService.class);

    @Autowired
    private MailSender sender;

    public void sendEmail(String toAddress, String fromAddress, String subject, String msgBody) {
        SimpleMailMessage crunchifyMsg = new SimpleMailMessage();
        crunchifyMsg.setFrom(fromAddress);
        crunchifyMsg.setTo(toAddress);
        crunchifyMsg.setSubject(subject);
        crunchifyMsg.setText(msgBody);
        sender.send(crunchifyMsg);
    }
}
