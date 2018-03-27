package com.shadowsocks.dao;

import com.shadowsocks.dto.response.CountryDto;
import com.shadowsocks.dto.entity.Server;
import com.shadowsocks.dto.response.CityDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ServerDao {

    String BASE_RESULT = "serverResult";
    String TABLE_NAME = "server";

    @Select("select * from " + TABLE_NAME + " where id=#{id}")
    @Results(
            id = BASE_RESULT,
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "country", column = "country"),
                    @Result(property = "countryInChinese", column = "country_in_chinese"),
                    @Result(property = "city", column = "city"),
                    @Result(property = "cityInChinese", column = "city_in_chinese"),
                    @Result(property = "domain", column = "domain"),
                    @Result(property = "port", column = "port"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "currentOwner", column = "current_owner"),
                    @Result(property = "updateTime", column = "update_time"),
            }
    )
    Server findById(@Param("id") int id);

    @Select("select distinct country, country_in_chinese from " + TABLE_NAME + " where status='AVAILABLE' and current_owner=0")
    @Results(
            id = "findCountryList",
            value = {
                    @Result(property = "country", column = "country"),
                    @Result(property = "countryInChinese", column = "country_in_chinese"),
            }
    )
    List<CountryDto> findCountryList();

    @Select("select id, city, city_in_chinese from " + TABLE_NAME + " where country=#{country} and status='AVAILABLE' and current_owner=0 limit 5")
    @Results(
            id = "findCityList",
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "city", column = "city"),
                    @Result(property = "cityInChinese", column = "city_in_chinese"),
            }
    )
    List<CityDto> findCityList(@Param("country") String country);

    @Update("update " + TABLE_NAME + " set status='NONAVAILABLE', current_owner=#{userId}, update_time=#{updateTime} " +
            "where id=#{id} and status='AVAILABLE'")
    int applyServer(@Param("id") int id, @Param("userId") int userId, @Param("updateTime") String updateTime);

    @Insert("insert into " + TABLE_NAME + "(country, country_in_chinese, city, city_in_chinese, domain, port, password, status, current_owner) " +
            "values(#{country}, #{countryInChinese}, #{city}, #{cityInChinese}, #{domain}, #{port}, #{password}, 'AVAILABLE', #{currentOwner})")
    int addNewServer(Server server);
}
