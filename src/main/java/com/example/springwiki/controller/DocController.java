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

    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<DocQueryResp>> response = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
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

    /**
     * "/delete/{id}" 中的id 会自动映射到@PathVariable Long id中的id 中去
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp response = new CommonResp<>();
        docService.delete(id);
        return response;
    }


}
