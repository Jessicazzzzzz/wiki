package com.example.springwiki.service;

import com.example.springwiki.domain.Demo;
import com.example.springwiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jessica~
 * @version 1.0
 */
@Service
public class DemoService {
    @Resource
    private DemoMapper DemoMapper;
    public List<Demo> list(){
        return  DemoMapper.selectByExample(null);

    }
}
