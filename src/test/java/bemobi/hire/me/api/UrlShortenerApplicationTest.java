package bemobi.hire.me.api;

import bemobi.hire.me.api.domain.Constants;
import bemobi.hire.me.api.response.ExpandResponse;
import bemobi.hire.me.api.response.MostAccessedResponse;
import bemobi.hire.me.api.response.ReduceResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by rrodovalho on 09/02/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class UrlShortenerApplicationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void reduceUrlWithAlias(){

        JdbcTestUtils.deleteFromTables(jdbcTemplate, Constants.DATABASE_CONFIG.TABLE_NAME);

        String url = "http://www.bemobi.com.br";
        String alias = "bemobi";

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("url", url);
        urlParams.put("alias", alias);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Constants.URL_MAPPING.REDUCE)
                .queryParam("url", url)
                .queryParam("alias", alias);

        ResponseEntity responseEntity =
                restTemplate.exchange(
                        builder.buildAndExpand(urlParams).toUri(),
                        HttpMethod.PUT,
                        entity,ReduceResponse.class);

        ReduceResponse response = (ReduceResponse) responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(Constants.SHORTEN_URL_PREFIX + alias, response.getUrl());
        assertEquals(alias, response.getAlias());
        assertNotNull(response.getStatistics());

    }

    @Test
    public void expandUrl() throws Exception{

        String alias ="bemobi";
        String url = "www.bemobi.com.br";

        JdbcTestUtils.deleteFromTables(jdbcTemplate,Constants.DATABASE_CONFIG.TABLE_NAME);
        jdbcTemplate.execute("insert into URL (url, alias,access) values('"+url+"', '"+alias +"',0)");


        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("alias", alias);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(Constants.URL_MAPPING.EXPAND)
                .queryParam("alias", alias);

        ResponseEntity responseEntity =
                restTemplate.getForEntity(
                        builder.buildAndExpand(urlParams).toUri(),
                        ExpandResponse.class);


        ExpandResponse response = (ExpandResponse) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(url,response.getUrl());
        assertEquals(alias, response.getAlias());

    }


    @Test
    public void mostAccessedUrls() throws Exception{

        JdbcTestUtils.deleteFromTables(jdbcTemplate,Constants.DATABASE_CONFIG.TABLE_NAME);

        for(int i=0;i<15;i++){
            jdbcTemplate.execute("insert into URL (url, alias,access) values('url"+i+"', 'alias"+i+"',"+i+")");
        }

        ResponseEntity responseEntity =
                restTemplate.getForEntity(
                        Constants.URL_MAPPING.TEN_MOST_ACCESSED,
                        MostAccessedResponse.class);

        MostAccessedResponse response = (MostAccessedResponse) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(10,response.getUrls().size());
        assertNotNull(response.getUrls());

    }



}