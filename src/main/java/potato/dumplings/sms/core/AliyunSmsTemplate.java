package potato.dumplings.sms.core;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.google.gson.Gson;
import potato.dumplings.sms.config.properties.SmsProperties;
import potato.dumplings.sms.entity.SmsResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import potato.dumplings.sms.exception.SmsException;
import potato.dumplings.sms.utils.StringUtils;

import java.util.LinkedHashMap;

/**
 * Aliyun 短信模板
 * @author potato-dumplings
 */
@Slf4j
public class AliyunSmsTemplate implements SmsTemplate {

    private final SmsProperties properties;

    private final Client client;

    @SneakyThrows(Exception.class)
    public AliyunSmsTemplate(SmsProperties smsProperties) {
        this.properties = smsProperties;
        Config config = new Config()
            // 您的AccessKey ID
            .setAccessKeyId(smsProperties.getAccessKeyId())
            // 您的AccessKey Secret
            .setAccessKeySecret(smsProperties.getAccessKeySecret())
            // 访问的域名
            .setEndpoint(smsProperties.getEndpoint());
        this.client = new Client(config);
    }

    @Override
    public boolean configuredCorrectly() {
        QuerySmsSignListRequest querySmsSignListRequest = new QuerySmsSignListRequest();
        querySmsSignListRequest.pageIndex = 1;
        querySmsSignListRequest.pageSize = 1;
        try {
            QuerySmsSignListResponse querySmsSignListResponse = client.querySmsSignList( querySmsSignListRequest );
            return "OK".equals( querySmsSignListResponse.getBody().getCode());
        } catch (Exception e) {
            log.error( "【SMS】阿里云配置不正确; {}", e.getMessage() );
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
        SendSmsRequest req = new SendSmsRequest()
            .setPhoneNumbers(phones)
            .setSignName(properties.getSignName())
            .setTemplateCode(templateId);
        if (param != null && !param.isEmpty()) {
            req.setTemplateParam( new Gson().toJson( param ) );
        }

        SendSmsResponse response;
        try {
            response = client.sendSms(req);
        } catch (Exception e) {
            log.error( "短信发送失败：phones:{}; templateId: {}; params: {}; errorMessage: {}", phones, templateId, param , e.getMessage() );
            throw new SmsException("发送短信异常");
        }

        if ("isv.BUSINESS_LIMIT_CONTROL".equals(response.getBody().getCode())) {
            log.error( "短信发送频繁：phones:{}; templateId: {}; params: {};", phones, templateId, param );
            throw new SmsException( "发送频繁请稍后再试" );
        }

        return SmsResult.builder()
                .isSuccess("OK".equals(response.getBody().getCode()))
                .code( response.getBody().getCode() )
                .message(response.getBody().getMessage())
                .response(new Gson().toJson( response ))
                .build();
    }

}
