package com.jhl.admin.controller;

import com.jhl.admin.constant.ProxyConstant;
import com.jhl.admin.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class SubscriptionController {
    @Autowired
    private ProxyConstant proxyConstant;

    @Autowired
    SubscriptionService subscriptionService;
    /**
     * 防暴力，防中间人篡改
     * @param code code
     * @param type 订阅类型0通用,1....预留
     * @param  token  md5(code+timestamp+api.auth)
     * @return
     */
    @ResponseBody
    @RequestMapping("/subscribe/{code}")
    public String subscribe(@PathVariable String code,Integer type,  Long timestamp,String token) {
         if (code==null||type==null ||  timestamp==null || token ==null)  throw  new NullPointerException("参数错误");

         StringBuilder stringBuilder= new StringBuilder();
        StringBuilder tokenSrc = stringBuilder.append(code).append(timestamp).append(proxyConstant.getAuthPassword());
        if (!DigestUtils.md5Hex(tokenSrc.toString()).equals(token)) throw  new RuntimeException("认证失败");

        String result = subscriptionService.subscribe(code);

        return result;
    }
}
