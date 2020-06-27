package com.xiao.mapper;

import com.xiao.entity.SysUser;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper {
    @Select("select * from sys_user where username = #{username}")
    @Results({
            @Result(id=true, property = "id", column = "id"),
            @Result(property = "roles", column = "id", javaType = List.class,
            many = @Many(select = "com.xiao.mapper.SysRoleMapper.findRolesByUid"))
    })
    SysUser findUserByName(String username);
}
