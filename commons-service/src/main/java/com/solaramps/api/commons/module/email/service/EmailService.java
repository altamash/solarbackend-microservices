package com.solaramps.api.commons.module.email.service;

import com.sendgrid.Response;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.solaramps.api.commons.module.email.dto.EmailDTO;
import com.solaramps.api.commons.module.email.dto.PersonalizationDTO;

import java.io.File;
import java.io.IOException;

public interface EmailService {

    String getSendGridApiKey();

    Response sendEmail(EmailDTO emailDTO) throws IOException;

    Response sendEmail(PersonalizationDTO personalizationDTO) throws IOException;

    /**
     * Send Email with Content
     *
     * @param toEmail
     * @param content
     * @param subject
     * @return
     * @throws IOException
     */
    Response sendEmail(Email toEmail, Content content, String subject) throws IOException;

    /**
     * Send Email with Dynamic SendGrid template
     * With File
     *
     * @param toEmail
     * @param subject
     * @param file
     * @param fileName
     * @param personalization
     * @return
     * @throws IOException
     */
    Response sendEmailWithDynamicTemplate(Email toEmail, String subject, File file, String fileName, String fileType,
                                          Personalization personalization) throws IOException;

    /**
     * Send Bulk Email with Dynamic SendGrid template
     * With File
     *
     * @param template
     * @param file
     * @param fileName
     * @param personalization
     * @return
     * @throws IOException
     */
    Response sendEmailsWithDynamicTemplate(String template, File file, String fileName, String fileType,
                                           Personalization personalization) throws IOException;

    Attachments addAttachment(File file, String fileName, String fileType) throws IOException;

    /**
     * @param template
     * @param personalization
     * @return
     * @throws IOException
     */
    Response emailDynamicTemplateWithNoFile(String template, Personalization personalization) throws IOException;

    /**
     * CSV
     *
     * @param csv
     * @param personalization
     * @return
     * @throws IOException
     */
    void addTo(String csv, Personalization personalization);

    /**
     * CSV
     *
     * @param csv
     * @param personalization
     * @return
     * @throws IOException
     */
    void addCC(String csv, Personalization personalization);

    /**
     * CSV
     *
     * @param csv
     * @param personalization
     * @return
     * @throws IOException
     */
    void addBCC(String csv, Personalization personalization);

}
