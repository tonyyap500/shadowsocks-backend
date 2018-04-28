package com.shadowsocks.dao;


import com.shadowsocks.dto.entity.Withdraw;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WithdrawDao {

    String BASE_RESULT = "withdrawResult";
    String TABLE_NAME = "withdraw_order";

    @Select("select * from " + TABLE_NAME + " limit #{start}, #{pageSize}")
    @Results(
            id = BASE_RESULT,
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "transactionId", column = "transaction_id"),
                    @Result(property = "amount", column = "amount"),
                    @Result(property = "channel", column = "channel"),
                    @Result(property = "status", column = "status"),
                    @Result(property = "remark", column = "remark"),
                    @Result(property = "operator", column = "operator"),
                    @Result(property = "createTime", column = "create_time"),
                    @Result(property = "updateTime", column = "update_time"),
            }
    )
    List<Withdraw> findWithdrawOrders(@Param("start") int start, @Param("pageSize") int pageSize);

    @Insert("insert into " + TABLE_NAME + "(user_id, transaction_id, amount, channel, status, remark, operator," +
            "create_time) values(#{userId}, #{transactionId}, #{amount}, #{channel}, #{status}, #{remark}, " +
            "#{operator}, #{createTime})")
    int createOrder(Withdraw withdraw);


    @Select("select * from " + TABLE_NAME + " where user_id=#{userId} order by id desc")
    @ResultMap(BASE_RESULT)
    List<Withdraw> findWithdrawHistory(@Param("userId") int userId);
}
