package com.shadowsocks.dao;

import com.shadowsocks.dto.entity.Balance;
import org.apache.ibatis.annotations.*;


@Mapper
public interface BalanceDao {

    String BASE_RESULT = "balanceResult";
    String TABLE_NAME = "balance";

    @Insert("insert into " + TABLE_NAME + "(user_id, balance, create_time) values(#{userId}, 0, #{createTime})")
    int createItem(@Param("userId") int userId, @Param("createTime") String createTime);

    @Update("update " + TABLE_NAME + " set balance=balance+#{amount}, update_time=#{updateTime}where user_id=#{userId}")
    int addBalanceByUserId(@Param("userId") int userId, @Param("amount") double amount, @Param("updateTime") String updateTime);

    @Select("select * from " + TABLE_NAME + " where user_id=#{userId}")
    @Results(
            id = BASE_RESULT,
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "currentBalance", column = "balance"),
                    @Result(property = "createTime", column = "create_time"),
                    @Result(property = "updateTime", column = "update_time")
            }
    )
    Balance findBalanceByUserId(@Param("userId") int userId);

    @Update("update " + TABLE_NAME + " set balance=(case when balance-#{transactionAmount}>0 then balance-#{transactionAmount} else 0 end), update_time=#{updateTime} where user_id=#{userId}")
    int minusBalanceByUserId(@Param("userId") int userId, @Param("transactionAmount") double transactionAmount, @Param("updateTime") String updateTime);
}
