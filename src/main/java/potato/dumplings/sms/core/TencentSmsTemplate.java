package potato.dumplings.sms.core;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import potato.dumplings.sms.config.properties.SmsProperties;
import potato.dumplings.sms.entity.SmsResult;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;
import potato.dumplings.sms.exception.SmsException;
import potato.dumplings.sms.utils.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Tencent 短信模板
 * @author potato-dumplings
 */
@Slf4j
public class TencentSmsTemplate implements SmsTemplate {

    private final SmsProperties properties;

    private final SmsClient client;

    public TencentSmsTemplate(SmsProperties smsProperties) {
        this.properties = smsProperties;
        Credential credential = new Credential(smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(smsProperties.getEndpoint());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        this.client = new SmsClient(credential, "", clientProfile);
    }

    @Override
    public boolean configuredCorrectly() {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeSmsTemplateListRequest req = new DescribeSmsTemplateListRequest();

        // 返回的resp是一个DescribeSmsTemplateListResponse的实例，与请求对象对应
        try {
            client.DescribeSmsTemplateList(req);
            // 调用没异常代表true
            return true;
        } catch (TencentCloudSDKException e) {
            log.error( "【SMS】腾讯云配置不正确; {}", e.getMessage() );
            return false;
        }
    }

    @Override
    public SmsResult send(String phones, String templateId) {
        return send( phones, templateId, null );
    }

    @Override
    public SmsResult send(String phones, String templateId, LinkedHashMap<String, String> param) {
        if (StringUtils.isBlank(phones)) {
            throw new SmsException("手机号不能为空");
        }
        if (StringUtils.isBlank(templateId)) {
            throw new SmsException("模板ID不能为空");
        }
        SendSmsRequest req = new SendSmsRequest();
        Set<String> set = Arrays.stream(phones.split("-")).map(p -> "+86" + p).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty( set )) {
            throw new SmsException( "手机号不能为空" );
        }
        req.setPhoneNumberSet(set.toArray( new String[0] ));
        if (!CollectionUtils.isEmpty( param )) {
            req.setTemplateParamSet(param.values().toArray(new String[0]));
        }
        req.setTemplateID(templateId);
        req.setSign(properties.getSignName());
        req.setSmsSdkAppid(properties.getSdkAppId());
        try {
            SendSmsResponse resp = client.SendSms(req);
            SmsResult.SmsResultBuilder builder = SmsResult.builder()
                .isSuccess(true)
                .message("send success")
                .response( new Gson().toJson(resp) );
            for (SendStatus sendStatus : resp.getSendStatusSet()) {
                if (!"Ok".equals(sendStatus.getCode())) {
                    builder.isSuccess(false).message(sendStatus.getMessage());
                    break;
                }
            }
            return builder.build();
        } catch (Exception e) {
            throw new SmsException(e.getMessage());
        }
    }

}
