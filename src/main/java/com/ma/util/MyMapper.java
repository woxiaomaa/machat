package com.ma.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by mh on 2019/1/5.
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
