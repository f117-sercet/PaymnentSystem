package com.pay.pay.core.exeception;

import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.model.ApiRes;
import lombok.Data;

/**
 * Description： 自定义异常
 *
 * @author: 段世超
 * @aate: Created in 2022/8/1 10:46
 */
@Data
public class BizException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    private ApiRes apiRes;

    /** 业务自定义异常 **/
    public BizException(String msg) {
        super(msg);
        this.apiRes = ApiRes.customFail(msg);
    }

    public BizException(ApiCodeEnum apiCodeEnum, String... params) {
        super();
        apiRes = ApiRes.fail(apiCodeEnum, params);
    }

    public BizException(ApiRes apiRes) {
        super(apiRes.getMsg());
        this.apiRes = apiRes;
    }
}
