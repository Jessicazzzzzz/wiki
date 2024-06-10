package com.example.springwiki.controller;

import com.example.springwiki.req.EbookQueryReq;
import com.example.springwiki.req.EbookSaveReq;
import com.example.springwiki.resp.CommonResp;
import com.example.springwiki.resp.EbookQueryResp;
import com.example.springwiki.resp.PageResp;
import com.example.springwiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author jessica~
 * @version 1.0
 */
@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> response = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
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
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp response = new CommonResp<>();
        ebookService.save(req);
        return response;
    }


}
