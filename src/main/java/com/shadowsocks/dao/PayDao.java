package com.shadowsocks.dao;

import com.shadowsocks.dto.PaymentDto;
import com.shadowsocks.dto.entity.PayOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PayDao {

    String BASE_RESULT = "payResult";
    String TABLE_NAME = "payment_order";

    @Insert("insert into " + TABLE_NAME + "(user_id, transaction_id, amount, channel, status, remark, create_time) " +
            "values(#{userId}, #{transactionId}, #{amount}, #{channel}, #{status}, #{remark}, #{createTime})")
    int createOrder(PaymentDto paymentDto);

    @Update("update " + TABLE_NAME + " set status='FINISHED', update_time=#{updateTime} where transaction_id=#{transactionId}")
    int updateStatus(@Param("transactionId") String transactionId, @Param("updateTime") String updateTime);

    @Select("select * from " + TABLE_NAME + " where transaction_id=#{transactionId}")
    @Results(
            id = BASE_RESULT,
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "transactionId", column = "transaction_id"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "amount", column = "amount"),
                    @Result(property = "channel", column = "channel"),
                    @Result(property = "status", column = "status"),
                    @Result(property = "remark", column = "remark"),
                    @Result(property = "createTime", column = "create_time"),
                    @Result(property = "updateTime", column = "update_time")
            }
    )
    PayOrder findOrderByTransactionId(@Param("transactionId") String transactionId);

    @Select("select * from " + TABLE_NAME + " where user_id=#{userId} order by id desc")
    @ResultMap(BASE_RESULT)
    List<PayOrder> findOrdersByUserId(@Param("userId") int userId);

    @Select("select * from " + TABLE_NAME + " order by id desc limit #{start}, #{pageSize}")
    @ResultMap(BASE_RESULT)
    List<PayOrder> findOrders(@Param("start") int start, @Param("pageSize") int pageSize);

    @Select("select count(*) from " + TABLE_NAME)
    int getTotal();


    @Update("update " + TABLE_NAME + " set status='FINISHED', update_time=#{updateTime} where transaction_id=#{transactionId} and status='PENDING'")
    int finishOrder(@Param("updateTime") String updateTime, @Param("transactionId") String transactionId);

    @Update("update " + TABLE_NAME + " set status='CANCELLED', update_time=#{updateTime} where transaction_id=#{transactionId} and status='PENDING'")
    int cancelOrder(@Param("updateTime") String updateTime, @Param("transactionId") String transactionId);
}
