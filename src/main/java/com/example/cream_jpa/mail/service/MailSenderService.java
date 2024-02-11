package com.example.cream_jpa.mail.service;

public interface MailSenderService {
    boolean sendMail(String mailTo) throws Exception;
    String getConfirmKey();
}
