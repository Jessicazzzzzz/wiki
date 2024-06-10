package com.example.springwiki.service;

import com.example.springwiki.domain.Ebook;
import com.example.springwiki.domain.EbookExample;
import com.example.springwiki.mapper.EbookMapper;
import com.example.springwiki.req.EbookQueryReq;
import com.example.springwiki.req.EbookSaveReq;
import com.example.springwiki.resp.EbookQueryResp;
import com.example.springwiki.resp.PageResp;
import com.example.springwiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger Log = LoggerFactory.getLogger(EbookService.class);

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
//       动态添加sql
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // 从第一页开始,每页3条数据
        PageHelper.startPage(req.getPage(), req.getSize());

        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        // 日志输出,用占位符的写法
        Log.info("总行数:{}", pageInfo.getTotal());
        Log.info("总页数:{}", pageInfo.getPages());
//        List<EbookQueryResp> reqList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
////            EbookQueryResp ebookResp = new EbookQueryResp();
////            BeanUtils.copyProperties(ebook,ebookResp);
//            //单个对象复制
//            EbookQueryResp ebookResp = CopyUtil.copy(ebook, EbookQueryResp.class);
//            reqList.add(ebookResp);
//
//        }

        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    /**
     * 保存表单修改的数据
     *
     * @param req
     */
    public void save(EbookSaveReq req) {
        // 将请求的参数转换为实体类
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(ebook.getId())) {
            // 新增,是根据是否存在id来决定的
            ebookMapper.insert(ebook);
        } else {
//             修改
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }
}
