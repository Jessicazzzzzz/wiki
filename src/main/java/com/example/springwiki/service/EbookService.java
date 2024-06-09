package com.example.springwiki.service;

import com.example.springwiki.domain.Ebook;
import com.example.springwiki.domain.EbookExample;
import com.example.springwiki.mapper.EbookMapper;
import com.example.springwiki.req.EbookReq;
import com.example.springwiki.resp.EbookResp;
import com.example.springwiki.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jessica~
 * @version 1.0
 */
@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
//       动态添加sql
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
//        List<EbookResp> reqList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
////            EbookResp ebookResp = new EbookResp();
////            BeanUtils.copyProperties(ebook,ebookResp);
//            //单个对象复制
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            reqList.add(ebookResp);
//
//        }

        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);

        return respList;
    }
}
