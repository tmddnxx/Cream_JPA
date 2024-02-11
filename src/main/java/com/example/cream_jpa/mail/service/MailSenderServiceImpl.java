package com.example.cream_jpa.mail.service;

import com.example.cream_jpa.mail.service.MailSenderService;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {
    private final JavaMailSender mailSender; // 메일 보내는 역할
    private final TemplateEngine templateEngine;
    private String confirmKey;
    
    @Value("${myapp.custom.mail.sender.mailFrom}")
    private String mailFrom; // 보내는 메일

    @Value("${myapp.custom.mail.sender.mailFromName}")
    private String mailFromName; // 보내는 사람
    
    @Override
    public boolean sendMail(String mailTo) throws Exception { // 이메일 수정시 인증 메일 발송
        this.confirmKey = createConfirmKey();
        MimeMessage message = createMessageByAddMember(mailTo);
        try { // 예외 처리
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getConfirmKey() {
        return this.confirmKey;
    }
    
    private MimeMessage createMessageByAddMember(String mailTo) throws Exception {
        // 회원 가입시 인증 메일 관련 내용 작성
        log.info("보내는 대상 : " + mailTo);
        log.info("인증 번호 : " + confirmKey);

        // 메일 템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("confirmKey", confirmKey);
        // Thymeleaf 템플릿 엔진을 사용하여 매일 본문 생성
        String messageText = templateEngine.process("mail/confirmMail", context);

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, mailTo); // 보내는 대상
        message.setSubject(mailFromName + "이메일 인증"); // 제목
        message.setText(messageText, "utf-8", "html"); // 내용
        message.setFrom(new InternetAddress(mailFrom, mailFromName)); // 보내는 사람

        return message;
    }

    private static String createConfirmKey() {
        // 인증키 작성
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 12; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0 ~ 2까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    // a ~ z (ex. 1 + 97 = 98 => (char)98 = 'b'
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    // A ~ Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }
}
