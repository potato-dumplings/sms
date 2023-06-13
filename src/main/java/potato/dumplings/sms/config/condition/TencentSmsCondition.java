package potato.dumplings.sms.config.condition;

import potato.dumplings.sms.config.properties.SmsProperties;
import potato.dumplings.sms.core.TencentSmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static potato.dumplings.sms.config.properties.SmsProperties.SmsTypeEnum.TENCENT;

/**
 * 判断腾讯云Sms模版是否能生效
 * @author potato-dumplings
 */
@Slf4j
public class TencentSmsCondition extends SmsCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        SmsProperties smsProperties = getSmsProPerties( context );
        if (smsProperties == null) {
            return ConditionOutcome.noMatch( "【SMS模版】配置不存在" );
        }
        if (TENCENT.equals(smsProperties.getType())) {
            return ConditionOutcome.match("【短信模版】使用腾讯云");
        }
        if (smsProperties.getType() != null) {
            return ConditionOutcome.noMatch( "【短信模版】未使用腾讯云" );
        }
        boolean matches =  new TencentSmsTemplate( smsProperties ).configuredCorrectly();
        if (matches) {
            setSmsType(TENCENT);
            return ConditionOutcome.match("【短信模版】使用腾讯云");
        }
        return ConditionOutcome.noMatch( "【短信模版】未使用腾讯云" );
    }
}
