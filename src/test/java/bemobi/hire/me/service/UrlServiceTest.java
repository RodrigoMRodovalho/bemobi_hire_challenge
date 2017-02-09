package bemobi.hire.me.service;

import bemobi.hire.me.data.UrlRepository;
import bemobi.hire.me.domain.Url;
import bemobi.hire.me.exception.ShortenedUrlNotFoundException;
import bemobi.hire.me.hash.HashGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rrodovalho on 08/02/17.
 */
public class UrlServiceTest {

    @Mock
    UrlRepository urlRepository;

    @Mock
    HashGenerator hashGenerator;

    UrlService urlService;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        urlService = new UrlService(urlRepository,hashGenerator);
    }

    @Test
    public void testReduceUrlWithAlias_Successfuly() throws Exception {

        Url url = Url.builder()
                .alias("bemobi")
                .url("http://www.bemobi.com.br")
                .build();

        when(urlRepository.saveUrl(url)).thenReturn(1);

        Url reducedUrl = urlService.reduceUrl(url);

        verify(urlRepository).saveUrl(url);
        assertEquals(url,reducedUrl);
        assertEquals(reducedUrl.getAccess(),0);

    }

    @Test
    public void testReduceUrlWithoutAlias_Successfuly() throws Exception {

        Url url = Url.builder()
                .url("http://www.bemobi.com.br")
                .build();

        String generatedAlias = "!@ABCDE";

        when(hashGenerator.generateAlias(url.getUrl())).thenReturn(generatedAlias);
        when(urlRepository.saveUrl(url)).thenReturn(1);

        Url reducedUrl = urlService.reduceUrl(url);

        verify(hashGenerator).generateAlias(url.getUrl());

        url.setAlias(generatedAlias);
        url.setAccess(0);

        verify(urlRepository).saveUrl(url);
        assertEquals(url,reducedUrl);
        assertEquals(reducedUrl.getAccess(),0);

    }

    @Test(expected = DuplicateKeyException.class)
    public void testReduceUrlWithAlias_Failure() throws Exception {

        Url url = Url.builder()
                .alias("bemobi")
                .url("http://www.bemobi.com.br")
                .build();

        when(urlRepository.saveUrl(any())).thenThrow(new DuplicateKeyException("alias"));
        urlService.reduceUrl(url);
    }

    @Test
    public void testGetExpandedUrl_Successfully() throws Exception {

        Url url = Url.builder()
                .alias("bemobi")
                .url("http://www.bemobi.com.br")
                .build();

        when(urlRepository.getUrlByAlias(url.getAlias())).thenReturn(url);
        Url expandedUrl = urlService.getExpandedUrl(url.getAlias());
        verify(urlRepository).getUrlByAlias(url.getAlias());
        assertEquals(expandedUrl,url);

    }

    @Test(expected = ShortenedUrlNotFoundException.class)
    public void testGetExpandedUrl_Failure() throws Exception {

        when(urlRepository.getUrlByAlias(any())).thenReturn(null);
        urlService.getExpandedUrl(any());

    }

    @Test
    public void testGetMostAccessedUrls() throws Exception {

        List<Url> topFiveUrlsList = new ArrayList<>();
        topFiveUrlsList.add(new Url("www.bemobi.com.br","bmob",5));
        topFiveUrlsList.add(new Url("www.facebook.com/bemobi","fbmob",4));
        topFiveUrlsList.add(new Url("www.twitter.com/bemobi","tbmob",3));
        topFiveUrlsList.add(new Url("www.instagram.com/bemobi","ibmob",2));
        topFiveUrlsList.add(new Url("www.snapchat.com/bemobi","sbmob",1));

        when(urlRepository.getMostTenAccessedUrl()).thenReturn(topFiveUrlsList);

        List<Url> mostAccesed = urlService.getMostAccessedUrls();
        verify(urlRepository).getMostTenAccessedUrl();
        assertEquals(topFiveUrlsList,mostAccesed);

    }
}