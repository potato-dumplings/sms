package potato.dumplings.sms.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static potato.dumplings.sms.config.properties.SmsProperties.SMS_PREFIX;

/**
 * SMS短信 配置属性
 * @author potato-dumplings
 */
@Data
@Component
@ConfigurationProperties(prefix = SMS_PREFIX)
public class SmsProperties {

    public static final String SMS_PREFIX = "sms";

    public enum SmsTypeEnum {
        /**
         * 阿里
         */
        ALI,

        /**
         * 腾讯
         */
        TENCENT
    }

    /**
     * 类型
     */
    private SmsTypeEnum type;

    /**
     * 配置节点
     * 阿里云 dysmsapi.aliyuncs.com
     * 腾讯云 sms.tencentcloudapi.com
     */
    private String endpoint;

    /**
     * key
     */
    private String accessKeyId;

    /**
     * 密匙
     */
    private String accessKeySecret;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信应用ID (腾讯专属)
     */
    private String sdkAppId;

}
