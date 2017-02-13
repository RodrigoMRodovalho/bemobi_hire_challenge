package bemobi.hire.me.api.controller;

import bemobi.hire.me.api.UrlShortenerApplication;
import bemobi.hire.me.api.domain.Constants;
import bemobi.hire.me.api.domain.Url;
import bemobi.hire.me.api.exception.AliasAlreadyExistsException;
import bemobi.hire.me.api.exception.ShortenedUrlNotFoundException;
import bemobi.hire.me.api.response.ErrorResponse;
import bemobi.hire.me.api.response.MostAccessedResponse;
import bemobi.hire.me.api.service.UrlService;
import bemobi.hire.me.api.response.ExpandResponse;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by rrodovalho on 08/02/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrlShortenerApplication.class)
@WebAppConfiguration
public class UrlControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    Gson gson;

    @MockBean
    UrlService urlServiceMock;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testReduceUrl_withAlias_Successfuly() throws Exception {

        Url url = Url.builder()
                .alias("alias")
                .url("url")
                .build();

        when(urlServiceMock.reduceUrl(url)).thenReturn(url);

        mockMvc.perform(put(Constants.URL_MAPPING.REDUCE)
                .contentType(MediaType.APPLICATION_JSON)
                .param("url",url.getUrl())
                .param("alias",url.getAlias()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias", is("alias")))
                .andExpect(jsonPath("$.statistics", notNullValue()))
                .andExpect(jsonPath("$.url", is("url")));
    }

    @Test
    public void testReduceUrl_withoutAlias_Successfuly() throws Exception {

        Url url = Url.builder()
                .url("url")
                .build();

        String generatedAlias = "ABCDEF";

        when(urlServiceMock.reduceUrl(url)).thenReturn(new Url(url.getUrl(),generatedAlias,0));

        mockMvc.perform(put(Constants.URL_MAPPING.REDUCE)
                .contentType(MediaType.APPLICATION_JSON)
                .param("url",url.getUrl()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias", is(generatedAlias)))
                .andExpect(jsonPath("$.statistics", notNullValue()))
                .andExpect(jsonPath("$.url", is("url")));


    }

    @Test
    public void testReduceUrl_withAlias_Failure() throws Exception {

        Url url = Url.builder()
                .url("url")
                .alias("alias")
                .build();

        when(urlServiceMock.reduceUrl(url)).thenThrow(new AliasAlreadyExistsException(url.getAlias()));

        mockMvc.perform(put(Constants.URL_MAPPING.REDUCE)
                .contentType(MediaType.APPLICATION_JSON)
                .param("url",url.getUrl())
                .param("alias",url.getAlias()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(gson.toJson(
                        ErrorResponse.builder()
                                .alias(url.getAlias())
                                .errorCode(Constants.ERROR_INFO.CODE_ALIAS_ALREADY_USED)
                                .description(Constants.ERROR_INFO.DESCRIPTION_ALIAS_ALREADY_USED)
                                .build())));

    }

    @Test
    public void testExpandUrl_Successfuly() throws Exception {

        Url url = Url.builder()
                .alias("alias")
                .url("url")
                .build();

        when(urlServiceMock.getExpandedUrl(url.getAlias())).thenReturn(url);

        mockMvc.perform(get(Constants.URL_MAPPING.EXPAND)
                .contentType(MediaType.APPLICATION_JSON)
                .param("alias",url.getAlias()))
                .andExpect(status().isSeeOther())
                .andExpect(header().string("Location",url.getUrl()))
                .andExpect(content().json(gson.toJson(
                        ExpandResponse.builder()
                                .alias(url.getAlias())
                                .url(url.getUrl())
                                .build())));
    }

    @Test
    public void testExpandUrl_Failure() throws Exception {

        String alias = "alias";

        when(urlServiceMock.getExpandedUrl(alias)).thenThrow(new ShortenedUrlNotFoundException(alias));

        mockMvc.perform(get(Constants.URL_MAPPING.EXPAND)
                .contentType(MediaType.APPLICATION_JSON)
                .param("alias",alias))
                .andExpect(status().isNotFound())
                .andExpect(content().json(gson.toJson(
                        ErrorResponse.builder()
                                .alias(alias)
                                .errorCode(Constants.ERROR_INFO.CODE_URL_NOT_FOUND)
                                .description(Constants.ERROR_INFO.DESCRIPTION_URL_NOT_FOUND)
                                .build())));

    }

    @Test
    public void testTenMostAccessed() throws Exception {

        List<Url> topFiveUrlsList = new ArrayList<>();
        topFiveUrlsList.add(new Url("www.bemobi.com.br","bmob",5));
        topFiveUrlsList.add(new Url("www.facebook.com/bemobi","fbmob",4));
        topFiveUrlsList.add(new Url("www.twitter.com/bemobi","tbmob",3));
        topFiveUrlsList.add(new Url("www.instagram.com/bemobi","ibmob",2));
        topFiveUrlsList.add(new Url("www.snapchat.com/bemobi","sbmob",1));

        when(urlServiceMock.getMostAccessedUrls()).thenReturn(topFiveUrlsList);

        mockMvc.perform(get(Constants.URL_MAPPING.TEN_MOST_ACCESSED))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(
                        new MostAccessedResponse(topFiveUrlsList))));

    }
}