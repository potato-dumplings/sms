package potato.dumplings.sms.config;

import potato.dumplings.sms.config.condition.AliYunSmsCondition;
import potato.dumplings.sms.config.condition.TencentSmsCondition;
import potato.dumplings.sms.config.properties.SmsProperties;
import potato.dumplings.sms.core.AliyunSmsTemplate;
import potato.dumplings.sms.core.SmsTemplate;
import potato.dumplings.sms.core.TencentSmsTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 短信配置类
 *
 * @author potato-dumplings
 */
@Configuration
@ConditionalOnProperty(name = "sms.enable", matchIfMissing = true)
@Import( {
        SmsProperties.class
} )
public class SmsConfig {

    @Bean
    @Conditional(value = AliYunSmsCondition.class)
    public SmsTemplate aliyunSmsTemplate(SmsProperties smsProperties) {
        return new AliyunSmsTemplate(smsProperties);
    }

    @Bean
    @Conditional(value = TencentSmsCondition.class)
    public SmsTemplate tencentSmsTemplate(SmsProperties smsProperties) {
        return new TencentSmsTemplate(smsProperties);
    }

}
