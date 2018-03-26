package com.shadowsocks.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BalanceDao {

    String BASE_RESULT = "balanceResult";
    String TABLE_NAME = "balance";

    @Insert("insert into " + TABLE_NAME + "(user_id, balance, create_time) values(#{userId}, 0, #{createTime})")
    int createItem(@Param("userId") int userId, @Param("createTime") String createTime);

    @Update("update " + TABLE_NAME + " set balance=balance+#{amount}, update_time=#{updateTime}where user_id=#{userId}")
    int addBalanceByUserId(@Param("userId") int userId, @Param("amount") double amount, @Param("updateTime") String updateTime);
}
