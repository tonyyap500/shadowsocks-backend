package com.shadowsocks.dao;

import com.shadowsocks.dto.entity.Admin;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AdminDao {

    String BASE_RESULT = "adminResult";
    String TABLE_NAME = "admin";

    @Select("select * from " + TABLE_NAME + " where username=#{username} and password=#{password}")
    @Results(
            id = BASE_RESULT,
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "username", column = "username"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "email", column = "email"),
                    @Result(property = "lastLoginTime", column = "last_login_time"),
                    @Result(property = "lastLoginIp", column = "last_login_ip")
            }
    )
    Admin login(@Param("username") String username, @Param("password") String password);

    @Update("update " + TABLE_NAME + " set last_login_time=#{lastLoginTime}, last_login_ip=#{lastLoginIp} where id=#{id}")
    boolean updateAdminInfo(@Param("lastLoginTime") String lastLoginTime, @Param("lastLoginIp") String lastLoginIp, @Param("id") int id);
}
