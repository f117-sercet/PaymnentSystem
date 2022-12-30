package com.pay.pay.core.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Description： JWT工具包
 *
 * @author: 段世超
 * @aate: Created in 2022/12/30 9:35
 */
public class JWTUtils {

    public  static final String generateToken(JWTPayload jwtPayload,String jwtSecret){

        return Jwts.builder()
                .setClaims(jwtPayload.toMap())
                //过期时间 = 当前时间 + （设置过期时间[单位 ：s ] ）  token放置redis 过期时间无意义
                //.setExpiration(new Date(System.currentTimeMillis() + (jwtExpiration * 1000) ))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }

    /** 根据token与秘钥 解析token并转换为 JWTPayload **/
    public static  JWTPayload parseToken(String token,String secret){

        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

            JWTPayload result = new JWTPayload();
            result.setSysUserId(claims.get("sysUserId", Long.class));
            result.setCreated(claims.get("created", Long.class));
            result.setCacheKey(claims.get("cacheKey", String.class));
            return result;


        } catch (Exception e) {
            return null; //解析失败
        }

    }
}
