package potato.dumplings.sms.config.condition;

import com.google.gson.Gson;
import potato.dumplings.sms.config.ConfigTree;
import potato.dumplings.sms.config.properties.SmsProperties;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

import static potato.dumplings.sms.config.properties.SmsProperties.SMS_PREFIX;

/**
 * 短信模版Template基类
 * @author potato-dumplings
 */
public abstract class SmsCondition extends SpringBootCondition {

    protected SmsProperties smsProperties = null;

    protected final void setSmsType(SmsProperties.SmsTypeEnum type) {
        if (this.smsProperties == null) {
            this.smsProperties = new SmsProperties();
        }
        this.smsProperties.setType( type );
    }

    /**
     * 获取 {@link SmsProperties} 参数
     * @param context ConditionContext
     * @return {@link SmsProperties}
     */
    protected final SmsProperties getSmsProPerties(ConditionContext context) {
        if (smsProperties != null) {
            return smsProperties;
        }
        ConfigTree configTree = new ConfigTree((ConfigurableEnvironment) context.getEnvironment(), SMS_PREFIX + ".");
        Map<String, Object> properties = configTree.getProperties();
        if (properties.isEmpty()) {
            this.smsProperties = new SmsProperties();
        }
        Gson gson = new Gson();
        smsProperties = gson.fromJson( gson.toJson( properties ), SmsProperties.class );
        return smsProperties;
    }
}
