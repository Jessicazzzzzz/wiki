package com.example.springwiki.controller;

import com.example.springwiki.req.UserQueryReq;
import com.example.springwiki.req.UserSaveReq;
import com.example.springwiki.resp.CommonResp;
import com.example.springwiki.resp.UserQueryResp;
import com.example.springwiki.resp.PageResp;
import com.example.springwiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author jessica~
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

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
