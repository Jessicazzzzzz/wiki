package com.example.springwiki.service;

import com.example.springwiki.domain.Doc;
import com.example.springwiki.domain.DocExample;
import com.example.springwiki.mapper.DocMapper;
import com.example.springwiki.req.DocQueryReq;
import com.example.springwiki.req.DocSaveReq;
import com.example.springwiki.resp.DocQueryResp;
import com.example.springwiki.resp.PageResp;
import com.example.springwiki.util.CopyUtil;
import com.example.springwiki.util.SnowFlake;
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
public class DocService {
    @Resource
    private DocMapper docMapper;
    @Resource
    private SnowFlake snowFlake;
    private static final Logger Log = LoggerFactory.getLogger(DocService.class);

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc"); //按照升序排序
        DocExample.Criteria criteria = docExample.createCriteria();
//       动态添加sql,动态的按照来名字来查询
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // 从第一页开始,每页3条数据
        PageHelper.startPage(req.getPage(), req.getSize());

        List<Doc> docList = docMapper.selectByExample(docExample);
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        // 日志输出,用占位符的写法
        Log.info("总行数:{}", pageInfo.getTotal());
        Log.info("总页数:{}", pageInfo.getPages());
//        List<DocQueryResp> reqList = new ArrayList<>();
//        for (Doc doc : docList) {
////            DocQueryResp docResp = new DocQueryResp();
////            BeanUtils.copyProperties(doc,docResp);
//            //单个对象复制
//            DocQueryResp docResp = CopyUtil.copy(doc, DocQueryResp.class);
//            reqList.add(docResp);
//
//        }
        // 尽量不要返回实体类

        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        // 需要将总页数和list 一起返回回去
        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    /**
     * 保存表单修改的数据
     *
     * @param req
     */
    public void save(DocSaveReq req) {
        // 将请求的参数转换为实体类
        Doc doc = CopyUtil.copy(req, Doc.class);
        if (ObjectUtils.isEmpty(doc.getId())) {
            // 新增,是根据是否存在id来决定的
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        } else {
//             修改
            docMapper.updateByPrimaryKey(doc);
        }

    }

    /**
     * 删除操作
     *
     * @param id
     */
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        // 根据条件来删除
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        // 如果id 在某个范围就删除
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc"); //按照升序排序
        // 查询条件
        DocExample.Criteria criteria = docExample.createCriteria();
//
        List<Doc> docList = docMapper.selectByExample(docExample);

        // 尽量不要返回实体类
        // 列表复制
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);

        return respList;
    }


}
