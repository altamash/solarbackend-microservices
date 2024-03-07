package com.solaramps.api.commons.module.notification.email.suprSend.controller;

import com.solaramps.api.commons.module.notification.email.suprSend.mapper.AndroidPushDTO;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.EmailCallBackObject;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.SlackIdentDTO;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.WebPushDTO;
import com.solaramps.api.commons.module.notification.email.suprSend.service.SuprSendService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suprsend.Subscriber;
import suprsend.SuprsendException;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/commons/notifications")
public class SuprSendController {

    private final SuprSendService suprSendService;

    public SuprSendController(SuprSendService suprSendService) {
        this.suprSendService = suprSendService;
    }

    @GetMapping("/subscriber/distinctId/{distinctId}")
    public Subscriber getSubscriber(@PathVariable String distinctId) {
        return suprSendService.getSubscriber(distinctId);
    }

    // @RequestParam(value = "Brand") @ApiParam("'GOODWE', 'SOLAX', 'SOLIS', 'SOLRENVIEW', 'EGAUGE', 'SOLAREDGE' etc")
    //                                   List<String> monitorPlatforms,
    @PostMapping(value = "/channels/distinctId/{distinctId}", produces = "application/json")
    public String addChannels(@PathVariable String distinctId,
                                  @RequestParam(required = false, defaultValue = "") List<String> emails,
                                  @RequestParam(required = false, defaultValue = "") List<String> sms,
                                  @RequestParam(required = false, defaultValue = "") List<String> whatsApp,
                                  @RequestParam(required = false, defaultValue = "") List<AndroidPushDTO> androidpushes,
                                  @RequestParam(required = false) String androidpush,
                                  @RequestParam(required = false) String iospush,
                                  @RequestParam(required = false, defaultValue = "") List<SlackIdentDTO> slackIdents,
                                  @RequestParam(required = false, defaultValue = "") List<String> slackIncomingWebhooks,
                                  @RequestParam(required = false) WebPushDTO webPush
                            ) {
        return suprSendService.addChannels(distinctId,
                emails,
                sms,
                whatsApp,
                androidpushes,
                androidpush,
                iospush,
                slackIdents,
                slackIncomingWebhooks,
                webPush);
    }

    @DeleteMapping(value = "/channels/distinctId/{distinctId}", produces = "application/json")
    public String removeChannels(@PathVariable String distinctId,
                                  @RequestParam(required = false, defaultValue = "") List<String> emails,
                                  @RequestParam(required = false, defaultValue = "") List<String> sms,
                                  @RequestParam(required = false, defaultValue = "") List<String> whatsApp,
                                  @RequestParam(required = false, defaultValue = "") List<AndroidPushDTO> androidpushes,
                                  @RequestParam(required = false) String androidpush,
                                  @RequestParam(required = false) String iospush,
                                  @RequestParam(required = false, defaultValue = "") List<SlackIdentDTO> slackIdents,
                                  @RequestParam(required = false, defaultValue = "") List<String> slackIncomingWebhooks,
                                  @RequestParam(required = false) WebPushDTO webPush
                            ) {
        return suprSendService.removeChannels(distinctId,
                emails,
                sms,
                whatsApp,
                androidpushes,
                androidpush,
                iospush,
                slackIdents,
                slackIncomingWebhooks,
                webPush);
    }

    @DeleteMapping(value = "/channels/bulk/distinctId/{distinctId}", produces = "application/json")
    public String removeChannelsBulk(@PathVariable String distinctId,
                                  @RequestParam(required = false, defaultValue = "") List<String> channels) {
        return suprSendService.removeChannelsBulk(distinctId, channels);
    }

    @PostMapping(value = "/channels/email",
            produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String trackEvent(@RequestParam String distinctId, @RequestParam String eventName,
                      @RequestParam String propertiesJsonString,
                      @RequestPart(value = "file", required = false) List<MultipartFile> files,
                      @RequestParam(required = false) List<String> fileNames,
                      @RequestParam String attachmentDirRef,
                      @RequestParam(required = false) String brandId)
            throws UnsupportedEncodingException, SuprsendException {
        return suprSendService.trackEvent(distinctId, eventName, propertiesJsonString, files, fileNames,
                attachmentDirRef, brandId);
    }

    @PostMapping(value = "/channels/email/callBack", consumes = MediaType.APPLICATION_JSON_VALUE)
    void emailCallBack(@RequestBody List<EmailCallBackObject> emailCallBackObject) {
        System.out.println(emailCallBackObject);
    }

    /*@PostMapping(value = "/storeInContainer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String storeInContainer(@RequestPart(value = "file") MultipartFile file,
                                   @RequestParam(value = "blobService") String blobService,
                                   @RequestParam(value = "sasToken") String sasToken,
                                   @RequestParam(value = "container") String container,
                                   @RequestParam(value = "directoryReference") String directoryReference,
                                   @RequestParam(value = "fileName") String fileName,
                                   @RequestParam(value = "relativeUrl", defaultValue = "false") Boolean relativeUrl)
            throws URISyntaxException, StorageException, IOException {
        return storageService.storeInContainer(blobService, sasToken, file, container, directoryReference, fileName, relativeUrl);
    }*/
}
