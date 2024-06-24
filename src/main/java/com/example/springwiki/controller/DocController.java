package com.example.springwiki.controller;

import com.example.springwiki.req.DocQueryReq;
import com.example.springwiki.req.DocSaveReq;
import com.example.springwiki.resp.DocQueryResp;
import com.example.springwiki.resp.CommonResp;
import com.example.springwiki.resp.PageResp;
import com.example.springwiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author jessica~
 * @version 1.0
 */
@RestController
@RequestMapping("/doc")
public class DocController {
    @Resource
    private DocService docService;

    /**
     * 添加 @Valid 就是表示里面的参数是需要添加校验规则的
     * 而PageReq 中已经添加@Null @Max 的规则就会生效
     *
     * @param req
     * @return
     */
    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> response = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        response.setContent(list);
        return response;
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        CommonResp<String> response = new CommonResp<>();
        String content = docService.findContent(id);
        response.setContent(content);
        return response;
    }

    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> response = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
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
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp response = new CommonResp<>();
        docService.save(req);
        return response;
    }

//    "/delete/{id}" 中的id 会自动映射到@PathVariable Long id中的id 中去


    /**
     * @param
     * @return
     */
    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
        CommonResp response = new CommonResp<>();
        List<String> lists = Arrays.asList(idsStr.split(","));
        docService.delete(lists);
        return response;
    }


}
