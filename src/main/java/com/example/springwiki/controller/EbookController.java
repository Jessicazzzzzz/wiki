package com.example.springwiki.controller;

import com.example.springwiki.req.EbookReq;
import com.example.springwiki.resp.CommonResp;
import com.example.springwiki.resp.EbookResp;
import com.example.springwiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    public CommonResp list(EbookReq req){
        CommonResp<List<EbookResp>> response = new CommonResp<>();
        List<EbookResp> list =   ebookService.list(req);
        response.setContent(list);
        return response;
    }


}
