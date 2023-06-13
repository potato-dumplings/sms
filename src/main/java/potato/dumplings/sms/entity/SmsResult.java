package potato.dumplings.sms.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 上传返回体
 * @author potato-dumplings
 */
@Data
@Builder
public class SmsResult {

    /**
     * 是否成功
     */
    private boolean isSuccess;

    /**
     * code
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 实际响应体
     */
    private String response;
}
