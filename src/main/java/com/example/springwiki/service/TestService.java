package com.example.springwiki.service;

import com.example.springwiki.domain.Test;
import com.example.springwiki.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jessica~
 * @version 1.0
 */
@Service
public class TestService {
    @Resource
    private TestMapper testMapper;
    public List<Test> list(){
        return  testMapper.list();

    }
}
