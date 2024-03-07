package com.solaramps.api.commons.module.notification.email.suprSend.service;

import com.solaramps.api.commons.module.notification.email.suprSend.mapper.AndroidPushDTO;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.SlackIdentDTO;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.WebPushDTO;
import com.solaramps.api.commons.module.queue.alert.mapper.FileAttachment;
import org.springframework.web.multipart.MultipartFile;
import suprsend.Subscriber;

import java.util.List;

public interface SuprSendService {

    Subscriber getSubscriber(String distinctId);

    String addSubscriberAndEmail(String distinctId,
                           List<String> emails);

    String addChannels(String distinctId,
                           List<String> emails,
                           List<String> sms,
                           List<String> whatsApp,
                           List<AndroidPushDTO> androidpushes,
                           String androidpush,
                           String iospush,
                           List<SlackIdentDTO> slackIdents,
                           List<String> slackIncomingWebhooks,
                           WebPushDTO webPush
    );

    String removeChannels(String distinctId,
                           List<String> emails,
                           List<String> sms,
                           List<String> whatsApp,
                           List<AndroidPushDTO> androidpushes,
                           String androidpush,
                           String iospush,
                           List<SlackIdentDTO> slackIdents,
                           List<String> slackIncomingWebhooks,
                           WebPushDTO webPush
    );

    String removeChannelsBulk(String distinctId, List<String> channels);

    String trackEvent(String distinctId, String eventName, String propertiesJsonString, List<MultipartFile> files,
                      List<String> fileNames, String attachmentDirRef, String brandId);

    String trackEvent(String distinctId, String eventName, String propertiesJsonString,
                      List<FileAttachment> fileAttachments, String brandId);
}
