package potato.dumplings.sms.core;

import potato.dumplings.sms.entity.SmsResult;

import java.util.LinkedHashMap;

/**
 * 短信模板
 * @author potato-dumplings
 */
public interface SmsTemplate {

    /**
     * 配置是否正确
     * @return 配置是否正确
     */
    boolean configuredCorrectly();

    /**
     * 发送短信
     *
     * @param phones     电话号(多个逗号分割)
     * @param templateId 模板id
     * @return 操作结果
     */
    SmsResult send(String phones, String templateId);

    /**
     * 发送短信
     *
     * @param phones     电话号(多个逗号分割)
     * @param templateId 模板id
     * @param param      模板对应参数
     *                   阿里 需使用 模板变量名称对应内容 例如: code=1234
     *                   腾讯 需使用 模板变量顺序对应内容 例如: 1=1234, 1为模板内第一个参数
     * @return 操作结果
     */
    SmsResult send(String phones, String templateId, LinkedHashMap<String, String> param);

}
