package com.example.springwiki.service;

import com.example.springwiki.domain.Category;
import com.example.springwiki.domain.CategoryExample;
import com.example.springwiki.mapper.CategoryMapper;
import com.example.springwiki.req.CategoryQueryReq;
import com.example.springwiki.req.CategorySaveReq;
import com.example.springwiki.resp.CategoryQueryResp;
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
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private SnowFlake snowFlake;
    private static final Logger Log = LoggerFactory.getLogger(CategoryService.class);

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc"); //按照升序排序
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
//       动态添加sql,动态的按照来名字来查询
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // 从第一页开始,每页3条数据
        PageHelper.startPage(req.getPage(), req.getSize());

        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        // 日志输出,用占位符的写法
        Log.info("总行数:{}", pageInfo.getTotal());
        Log.info("总页数:{}", pageInfo.getPages());
//        List<CategoryQueryResp> reqList = new ArrayList<>();
//        for (Category category : categoryList) {
////            CategoryQueryResp categoryResp = new CategoryQueryResp();
////            BeanUtils.copyProperties(category,categoryResp);
//            //单个对象复制
//            CategoryQueryResp categoryResp = CopyUtil.copy(category, CategoryQueryResp.class);
//            reqList.add(categoryResp);
//
//        }
        // 尽量不要返回实体类

        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        // 需要将总页数和list 一起返回回去
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    /**
     * 保存表单修改的数据
     *
     * @param req
     */
    public void save(CategorySaveReq req) {
        // 将请求的参数转换为实体类
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(category.getId())) {
            // 新增,是根据是否存在id来决定的
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
//             修改
            categoryMapper.updateByPrimaryKey(category);
        }

    }

    /**
     * 删除操作
     *
     * @param id
     */
    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<CategoryQueryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc"); //按照升序排序
        // 查询条件
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
//
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        // 尽量不要返回实体类
        // 列表复制
        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return respList;
    }
}
