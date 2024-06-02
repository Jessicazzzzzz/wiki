package com.example.springwiki.service;

import com.example.springwiki.domain.Ebook;
import com.example.springwiki.domain.EbookExample;
import com.example.springwiki.mapper.EbookMapper;
import com.example.springwiki.req.EbookReq;
import com.example.springwiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jessica~
 * @version 1.0
 */
@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;
    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResp> reqList = new ArrayList<>();
        for (Ebook ebook : ebookList) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            reqList.add(ebookResp);
        }
      return  reqList;
    }
}
