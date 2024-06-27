package com.example.springwiki.service;

import com.example.springwiki.domain.User;
import com.example.springwiki.domain.UserExample;
import com.example.springwiki.exception.BusinessException;
import com.example.springwiki.exception.BusinessExceptionCode;
import com.example.springwiki.mapper.UserMapper;
import com.example.springwiki.req.UserQueryReq;
import com.example.springwiki.req.UserSaveReq;
import com.example.springwiki.resp.UserQueryResp;
import com.example.springwiki.resp.PageResp;
import com.example.springwiki.util.CopyUtil;
import com.example.springwiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jessica~
 * @version 1.0
 */
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private SnowFlake snowFlake;
    private static final Logger Log = LoggerFactory.getLogger(UserService.class);

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
//       动态添加sql,动态的按照来名字来查询
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
//        if (!ObjectUtils.isEmpty(req.getCategoryId2())) {
//            criteria.andCategory2IdEqualTo(req.getCategoryId2());
//        }
        // 从第一页开始,每页3条数据
        PageHelper.startPage(req.getPage(), req.getSize());

        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // 日志输出,用占位符的写法
        Log.info("总行数:{}", pageInfo.getTotal());
        Log.info("总页数:{}", pageInfo.getPages());
//        List<UserQueryResp> reqList = new ArrayList<>();
//        for (User user : userList) {
////            UserQueryResp userResp = new UserQueryResp();
////            BeanUtils.copyProperties(user,userResp);
//            //单个对象复制
//            UserQueryResp userResp = CopyUtil.copy(user, UserQueryResp.class);
//            reqList.add(userResp);
//
//        }
        // 尽量不要返回实体类

        List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);
        // 需要将总页数和list 一起返回回去
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    /**
     * 保存表单修改的数据
     *
     * @param req
     */
    public void save(UserSaveReq req) {
        // 将请求的参数转换为实体类
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(user.getId())) {
            if (ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))) {
                // 新增,是根据是否存在id来决定的
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                // 用户已经存在
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);

            }

        } else {
//             修改
            // 首先先把LoginName 置空
            // 跟新,看到如果是Null, 就不会跟新LoginName ,那么前端即使是修改了用户名,也是无法修改的
            //updateByPrimaryKeySelective 表示的是user 中的值有变化才会去跟新user 中修改的值,不然就不跟新
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }

    }

    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
//       动态添加sql,动态的按照来名字来查询
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);// 它只会返回list
        if (CollectionUtils.isEmpty(userList)) {// 需要判断一下userList是否为空,为空,就是用户名不存在
            return null;
        } else return userList.get(0);// 不然就只会查到一条数据,只需要返回第一个就可以了

    }

    /**
     * 删除操作
     *
     * @param id
     */
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
