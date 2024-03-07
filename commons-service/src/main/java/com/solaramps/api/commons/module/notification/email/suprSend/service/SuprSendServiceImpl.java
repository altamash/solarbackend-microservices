package com.solaramps.api.commons.module.notification.email.suprSend.service;

import com.solaramps.api.commons.Utility;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.AndroidPushDTO;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.SlackIdentDTO;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.WebPushDTO;
import com.solaramps.api.commons.module.queue.alert.AlertUtility;
import com.solaramps.api.commons.module.queue.alert.mapper.FileAttachment;
import com.solaramps.api.commons.module.storage.service.StorageService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import suprsend.Event;
import suprsend.Subscriber;
import suprsend.Suprsend;
import suprsend.SuprsendException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SuprSendServiceImpl implements SuprSendService {

    private final Logger LOGGER = LoggerFactory.getLogger(SuprSendServiceImpl.class);
    private final Suprsend suprsend;
    private final StorageService storageService;
    private final String sasToken;
    private final String blobService;
    private final String container;
//    private final String attachmentDirRef;
    private SimpleDateFormat dateFormat;
    private final AlertUtility alertUtility;

    public SuprSendServiceImpl(
//            Suprsend suprsend,
                               StorageService storageService,
                               @Value("${app.storage.azureBlobSasToken}") String sasToken,
                               @Value("${app.storage.blobService}") String blobService,
                               @Value("${app.storage.container}") String container,
                               @Value("${notification.suprSend.workSpaceKey}") String workSpaceKey,
                               @Value("${notification.suprSend.workSpaceSecret}") String workSpaceSecret,
                               @Value("${notification.attachment.directoryReference}") String attachmentDirRef, AlertUtility alertUtility) throws SuprsendException {
        this.suprsend = new Suprsend(workSpaceKey, workSpaceSecret, true);
//        this.suprsend = suprsend;
        this.storageService = storageService;
        this.sasToken = sasToken;
        this.blobService = blobService;
        this.container = container;
//        this.attachmentDirRef = attachmentDirRef;
        this.alertUtility = alertUtility;
        dateFormat = new SimpleDateFormat(Utility.SYSTEM_DATE_FORMAT);
    }

    @Override
    public Subscriber getSubscriber(String distinctId) {
        try {
            Subscriber user = suprsend.user.getInstance(distinctId);
//            user.addAndroidpush("");
            return user;
        } catch (SuprsendException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String addSubscriberAndEmail(String distinctId, List<String> emails) {
        if (distinctId == null) {
            return null;
        }
        Subscriber user;
        try {
            user = suprsend.user.getInstance(distinctId);
        } catch (SuprsendException e) {
            throw new RuntimeException(e);
        }

        // Add Email
        emails.forEach(email -> user.addEmail(email));

        // Save
        JSONObject response = user.save();
        LOGGER.info(String.valueOf(response));
        return String.valueOf(response);
    }

    @Override
    public String addChannels(String distinctId,
                            List<String> emails,
                            List<String> sms,
                            List<String> whatsApp,
                            List<AndroidPushDTO> androidpushes,
                            String androidpush,
                            String iospush,
                            List<SlackIdentDTO> slackIdents,
                            List<String> slackIncomingWebhooks,
                            WebPushDTO webPush) {
        if (distinctId == null) {
            return null;
        }
        Subscriber user;
        try {
            user = suprsend.user.getInstance(distinctId);
        } catch (SuprsendException e) {
            throw new RuntimeException(e);
        }

        // Add Email
        emails.forEach(email -> user.addEmail(email));

        // Add SMS
        sms.forEach(s -> user.addSms(s));

        // Add Whatsapp
        whatsApp.forEach(wa -> user.addWhatsapp(wa));

        // Add Androidpush token. Pass the vendor as 2nd param.
        // user.addAndroidpush("androidpush_fcm_token__","fcm");
        // user.addAndroidpush("androidpush_xiaomi_token__", "xiaomi");
        androidpushes.forEach(ap -> user.addAndroidpush(ap.getToken(), ap.getVendor()));

        // Add iospush token
        if (androidpush != null) {
            user.addAndroidpush(androidpush);
        }

        // Add iospush token
        if (iospush != null) {
            user.addIospush(iospush);
        }

        // Add Slack using user email id
        /*JSONObject slackIdent = new JSONObject()
                .put("access_token", "xoxb-XXXXXXXX")
                .put("email", "user@example.com");
        user.addSlack(slackIdent);
        JSONObject slackIdent = new JSONObject()
                .put("access_token", "xoxb-XXXXXXXX")
                .put("email", "user@example.com");
        user.addSlack(slackIdent);
        // Add Slack using member_id of the user if known
        JSONObject slackIdent = new JSONObject()
                .put("access_token", "xoxb-XXXXXXXX")
                .put("user_id", "U03XXXXXXXX");
        user.addSlack(slackIdent);
        // Add Slack channel_id
        JSONObject slackIdent = new JSONObject()
                .put("access_token", "xoxb-XXXXXXXX")
                .put("channel_id", "C04XXXXXXXX");*/
        slackIdents.forEach(si ->
                {
                    JSONObject o = new JSONObject();
                    si.getKeyValues().entrySet().forEach(set -> {
                        o.put(set.getKey(), set.getValue());
                    });
                    user.addSlack(o);
                });

        // Add Slack incoming webhook
        slackIncomingWebhooks.forEach(url -> {
            user.addSlack(new JSONObject()
                    .put("incoming_webhook", new JSONObject().put("url", url)));
        });

        // Add Webpush token json (VAPID)
        // JSONObject webpush = new JSONObject()
        //        .put("endpoint", "__end_point__")
        //        .put("expirationTime", "")
        //        .put("keys", new JSONObject()
        //                .put("p256dh", "__p256dh__")
        //                .put("auth", "__auth_key__"));
        // user.addWebpush(webpush, "vapid");
        if (webPush != null) {
            JSONObject webpush = new JSONObject()
                    .put("endpoint", webPush.getEndpoint())
                    .put("expirationTime", webPush.getExpirationTime());
            JSONObject keys = new JSONObject();
            webPush.getKeys().entrySet().forEach(set -> {
                keys.put(set.getKey(), set.getValue());
            });
            webpush.put("keys", keys);
            user.addWebpush(webpush, "vapid");
        }

        // Save
        JSONObject response = user.save();
        LOGGER.info(String.valueOf(response));
        return String.valueOf(response);
    }

    @Override
    public String removeChannels(String distinctId, List<String> emails, List<String> sms, List<String> whatsApp, List<AndroidPushDTO> androidpushes, String androidpush, String iospush, List<SlackIdentDTO> slackIdents, List<String> slackIncomingWebhooks, WebPushDTO webPush) {
        if (distinctId == null) {
            return null;
        }
        Subscriber user;
        try {
            user = suprsend.user.getInstance(distinctId);
        } catch (SuprsendException e) {
            throw new RuntimeException(e);
        }

        // Remove Email
        emails.forEach(email -> user.removeEmail(email));

        // Remove SMS
        sms.forEach(s -> user.removeSms(s));

        // Remove Whatsapp
        whatsApp.forEach(wa -> user.removeWhatsapp(wa));

        // Remove Androidpush token. Pass the vendor as 2nd param.
        androidpushes.forEach(ap -> user.removeAndroidpush(ap.getToken(), ap.getVendor()));

        // Remove iospush token
        if (androidpush != null) {
            user.removeAndroidpush(androidpush);
        }

        // Remove iospush token
        if (iospush != null) {
            user.removeIospush(iospush);
        }

        // Remove Slack using user email id
        slackIdents.forEach(si ->
        {
            JSONObject o = new JSONObject();
            si.getKeyValues().entrySet().forEach(set -> {
                o.put(set.getKey(), set.getValue());
            });
            user.removeSlack(o);
        });

        // Remove Slack incoming webhook
        slackIncomingWebhooks.forEach(url -> {
            user.removeSlack(new JSONObject()
                    .put("incoming_webhook", new JSONObject().put("url", url)));
        });

        // Remove Webpush token json (VAPID)
        if (webPush != null) {
            JSONObject webpush = new JSONObject()
                    .put("endpoint", webPush.getEndpoint())
                    .put("expirationTime", webPush.getExpirationTime());
            JSONObject keys = new JSONObject();
            webPush.getKeys().entrySet().forEach(set -> {
                keys.put(set.getKey(), set.getValue());
            });
            webpush.put("keys", keys);
            user.removeWebpush(webpush, "vapid");
        }

        // Save
        JSONObject response = user.save();
        LOGGER.info(String.valueOf(response));
        return String.valueOf(response);
    }

    @Override
    public String removeChannelsBulk(String distinctId, List<String> channels) {
        if (distinctId == null) {
            return null;
        }
        Subscriber user;
        try {
            user = suprsend.user.getInstance(distinctId);
        } catch (SuprsendException e) {
            throw new RuntimeException(e);
        }
        // --- To unset one channel, for example to delete all emails associated with user
        // user.unset("$email");

        //what value to pass to unset channels
        // for email:                $email
        // for whatsapp:             $whatsapp
        // for SMS:                  $sms
        // for androidpush tokens:   $androidpush
        // for iospush tokens:       $iospush
        // for webpush tokens:       $webpush
        // for slack:                $slack

        // --- multiple channels can also be deleted in one call by passing argument as a list
        // ArrayList<String> channels =  new ArrayList<>(Arrays.asList("$email","$slack","$androidpush","$iospush","$webpush","$whatsapp"));
        for (int i = 0; i < channels.size(); i++) {
            if (!channels.get(i).startsWith("$")) {
                channels.set(i, "$" + channels.get(i));
            }
        }
        user.unset(channels);

        // Save
        JSONObject response = user.save();
        LOGGER.info(String.valueOf(response));
        return String.valueOf(response);
    }

    @Override
    public String trackEvent(String distinctId, String eventName, String propertiesJsonString,
                             List<MultipartFile> files, List<String> fileNames, String attachmentDirRef,
                             String brandId) {
//        String filePath = null;
        List<FileAttachment> fileAttachments = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (int i = 0, filesSize = files.size(); i < filesSize; i++) {
                MultipartFile file = files.get(i);
                String fileName = fileNames.size() == files.size() ? fileNames.get(i) : null;
                fileName = alertUtility.updateFileName(file, fileName);
                String path = alertUtility.getFilePath(file, fileName, attachmentDirRef);
                fileAttachments.add(FileAttachment.builder()
                        .name(fileName)
                        .path(path)
                        .build());
            }
        }

        /*if (file != null) {
            try {
                filePath = storageService.storeInContainer(blobService, sasToken, file, container,
                        attachmentDirRef + "/" + dateFormat.format(new Date()) , fileName, false);
            } catch (IOException | URISyntaxException | StorageException ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }*/
        return trackEvent(distinctId, eventName, propertiesJsonString, fileAttachments, brandId);
    }

    @Override
    public String trackEvent(String distinctId, String eventName, String propertiesJsonString,
                             List<FileAttachment> fileAttachments, String brandId) {
        // Properties:  Optional, default=None, a dict representing event-attributes
//        JSONObject eventProps = new JSONObject();
//        properties.entrySet().forEach(set -> eventProps.put(set.getKey(), set.getValue()));

        Event e;
        try {
//            e = new Event(distinctId, eventName, eventProps);
            e = new Event(distinctId, eventName, new JSONObject(propertiesJsonString
//                    .replaceAll("\\\\\"", "\"")
            ), null, brandId);
        } catch (SuprsendException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        if (fileAttachments != null && !fileAttachments.isEmpty()) {
            fileAttachments.forEach(attachment -> {
                try {
                    if (attachment.getName() != null) {
                        e.addAttachment(attachment.getPath(), attachment.getName());
                    } else {
                        e.addAttachment(attachment.getPath());
                    }
                } catch (IOException | SuprsendException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                    throw new RuntimeException(ex);
                }
            });
        }

        // Track event
        JSONObject response;
        try {
            response = suprsend.trackEvent(e);
        } catch (SuprsendException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
        LOGGER.info(String.valueOf(response));
        return String.valueOf(response);
    }

}
