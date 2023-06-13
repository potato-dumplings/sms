package potato.dumplings.sms.config.condition;

import potato.dumplings.sms.config.properties.SmsProperties;
import potato.dumplings.sms.core.AliyunSmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static potato.dumplings.sms.config.properties.SmsProperties.SmsTypeEnum.ALI;

/**
 * 判断阿里云Sms模版是否能生效
 * @author potato-dumplings
 */
@Slf4j
public class AliYunSmsCondition extends SmsCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        SmsProperties smsProperties = getSmsProPerties( context );
        if (smsProperties == null) {
            return ConditionOutcome.noMatch( "【SMS模版】配置不存在" );
        }
        if (ALI.equals( smsProperties.getType() )) {
            return ConditionOutcome.match("【短信模版】采用阿里云");
        }
        if (smsProperties.getType() != null) {
            return ConditionOutcome.noMatch( "【短信模版】未使用阿里云" );
        }
        boolean matches =  new AliyunSmsTemplate(smsProperties).configuredCorrectly();
        if (matches) {
            setSmsType(ALI);
            return ConditionOutcome.match("【短信模版】采用阿里云");
        }
        return ConditionOutcome.noMatch( "【短信模版】未使用阿里云" );
    }

}
