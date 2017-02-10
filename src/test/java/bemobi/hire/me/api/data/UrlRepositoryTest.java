package bemobi.hire.me.api.data;

import bemobi.hire.me.api.config.DataConfig;
import bemobi.hire.me.api.domain.Constants;
import bemobi.hire.me.api.domain.Url;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rrodovalho on 09/02/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UrlRepositoryTest {

    UrlRepository urlRepository;
    SqlSession sqlSession;

    @Before
    public void setUp() throws Exception {

        DataConfig dataConfig = new DataConfig();
        SqlSessionFactory sqlSessionFactory = dataConfig.sqlSessionFactory(Constants.DATABASE_CONFIG.URL_DEVELOPMENT);
        sqlSessionFactory.getConfiguration().addMapper(UrlRepository.class);
        sqlSession = sqlSessionFactory.openSession();
        urlRepository = sqlSession.getMapper(UrlRepository.class);
    }

    @Test
    public void testSave_And_Retrieve_Url_Successfuly() throws Exception {

        urlRepository.deleteAllData();

        String alias = "alias";
        String url = "url";

        Url urToSave = new Url(url,alias,0);

        int ret = urlRepository.saveUrl(urToSave);

        assertEquals(1,ret);

        Url urlRetrieved = urlRepository.getUrlByAlias(alias);

        assertEquals(urToSave,urlRetrieved);

    }

    @Test(expected = PersistenceException.class)
    public void saveUrl_Failure() throws Exception{

        urlRepository.deleteAllData();

        Url urlToSave = new Url("url","alias",0);

        int ret = urlRepository.saveUrl(urlToSave);

        assertEquals(1,ret);

        urlRepository.saveUrl(urlToSave);

    }

    @Test
    public void updateTokenAccess() throws Exception {

        urlRepository.deleteAllData();

        String alias = "alias";
        String url = "url";
        Integer access = 5;

        Url urlToSave = new Url(url,alias,access);
        int ret = urlRepository.saveUrl(urlToSave);
        assertEquals(1,ret);

        ret = urlRepository.updateUrlAccess((access + 1),alias);
        assertEquals(1,ret);

        Url updatedUrl = urlRepository.getUrlByAlias(alias);
        assertEquals((access + 1),updatedUrl.getAccess());
    }

    @Test
    public void testGetMostTenAccessedUrl() throws Exception {

        urlRepository.deleteAllData();

        List<Url> mostAccessed = new ArrayList<>(); //10
        List<Url> lessAccessed = new ArrayList<>(); //5

        for (int i=0;i<10;i++){
            Url mostUrl = new Url("url "+i,"alias"+i, 100 + i);
            mostAccessed.add(mostUrl);
            urlRepository.saveUrl(mostUrl);

            if (i % 2 == 0 ){
                Url lessUrl = new Url("url "+i+100,"alias"+i+100,1 + i);
                lessAccessed.add(lessUrl);
                urlRepository.saveUrl(lessUrl);
            }
        }

        Collections.reverse(mostAccessed);
        List<Url> mAccessed = urlRepository.getMostTenAccessedUrl();

        assertEquals(mAccessed, mostAccessed);

    }

    @After
    public void tearDown() throws Exception{
        sqlSession.close();
    }


}