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

    @Select("select distinct country, country_in_chinese from " + TABLE_NAME + " where status='AVALIABLE'")
    @Results(
            id = "findCountryList",
            value = {
                    @Result(property = "country", column = "country"),
                    @Result(property = "countryInChinese", column = "country_in_chinese"),
            }
    )
    List<CountryDto> findCountryList();

    @Select("select id, city, city_in_chinese from " + TABLE_NAME + " where country=#{country} and status='AVALIABLE' limit 5")
    @Results(
            id = "findCityList",
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "city", column = "city"),
                    @Result(property = "cityInChinese", column = "city_in_chinese"),
            }
    )
    List<CityDto> findCityList(@Param("country") String country);
}
