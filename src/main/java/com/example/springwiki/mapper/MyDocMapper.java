package com.example.springwiki.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author jessica~
 * @version 1.0
 */
public interface MyDocMapper {
    void increaseViewCount(@Param("id") Long id);

    void increaseVoteCount(@Param("id") Long id);
}
