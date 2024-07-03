package com.example.springwiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springwiki.req.UserLoginReq;
import com.example.springwiki.req.UserQueryReq;
import com.example.springwiki.req.UserResetPasswordReq;
import com.example.springwiki.req.UserSaveReq;
import com.example.springwiki.resp.CommonResp;
import com.example.springwiki.resp.PageResp;
import com.example.springwiki.resp.UserLoginResp;
import com.example.springwiki.resp.UserQueryResp;
import com.example.springwiki.service.UserService;
import com.example.springwiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @author jessica~
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 添加 @Valid 就是表示里面的参数是需要添加校验规则的
     * 而PageReq 中已经添加@Null @Max 的规则就会生效
     *
     * @param req
     * @return
     */
    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> response = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        response.setContent(list);
        return response;
    }

    /**
     * 这里需要添加@RequestBody,因为post 请求的content-type 就是application/json 格式的
     *
     * @param req
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        // 密码进行加密
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp response = new CommonResp<>();
        userService.save(req);
        return response;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        // 密码进行加密
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp response = new CommonResp<>();
        userService.resetPassword(req);
        return response;
    }

    /**
     * 登录
     *
     * @param req
     * @return
     */
    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req) {
        // 密码进行加密
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> response = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        // 用雪花算法生成唯一标识的token
        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token{},并且放入redis", token);

        userLoginResp.setToken(token.toString());
        // 设置token 到redis 中去
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);

        response.setContent(userLoginResp);
        return response;
    }

    /**
     * "/delete/{id}" 中的id 会自动映射到@PathVariable Long id中的id 中去
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp response = new CommonResp<>();
        userService.delete(id);
        return response;
    }


}
