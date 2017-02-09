package bemobi.hire.me.data;

import bemobi.hire.me.domain.Url;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by rrodovalho on 07/02/17.
 */
public interface UrlRepository {

    @Insert("insert into URL (url, alias,access) values(#{url}, #{alias},#{access})")
    int saveUrl(Url url);

    @Select("SELECT * FROM URL WHERE alias like #{alias}")
    Url getUrlByAlias(String alias);

    //This query in real should be using transaction to get concurrency safety
    @Update("UPDATE URL SET access = #{currentAccess} WHERE alias like #{alias}")
    int updateUrlAccess(@Param("currentAccess") Integer currentAccess, @Param("alias") String alias);

    @Select("SELECT * FROM URL ORDER BY access DESC LIMIT 10")
    List<Url> getMostTenAccessedUrl();

    //For tests
    @Delete("DELETE FROM URL")
    int deleteAllData();
}

