package com.pay.pay.core.jwt;

import com.alibaba.fastjson.JSONObject;
import com.pay.pay.core.model.security.JeeUserDetails;
import lombok.Data;

import java.util.Map;

/**
 * Description：JWT payload 载体
 * * 格式：
 *     {
 *         "sysUserId": "10001",
 *         "created": "1568250147846",
 *         "cacheKey": "KEYKEYKEYKEY",
 *     }
 *
 * @author: 段世超
 * @aate: Created in 2022/8/2 15:57
 */
@Data
public class JWTPayload {

    private Long sysUserId;       //登录用户ID
    private Long created;         //创建时间, 格式：13位时间戳
    private String cacheKey;      //redis保存的key

    protected JWTPayload(){}

    public JWTPayload(JeeUserDetails jeeUserDetails){

        this.setSysUserId(jeeUserDetails.getSysUser().getSysUserId());
        this.setCreated(System.currentTimeMillis());
        this.setCacheKey(jeeUserDetails.getCacheKey());
    }


    /** toMap **/
    public Map<String, Object> toMap(){
        JSONObject json = (JSONObject)JSONObject.toJSON(this);
        return json.toJavaObject(Map.class);
    }

}