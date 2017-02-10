package bemobi.hire.me.api.controller;

import bemobi.hire.me.api.domain.Statistics;
import bemobi.hire.me.api.domain.Url;
import bemobi.hire.me.api.exception.AliasAlreadyExistsException;
import bemobi.hire.me.api.exception.ShortenedUrlNotFoundException;
import bemobi.hire.me.api.response.MostAccessedResponse;
import bemobi.hire.me.api.service.UrlService;
import bemobi.hire.me.api.domain.Constants;
import bemobi.hire.me.api.response.ExpandResponse;
import bemobi.hire.me.api.response.ReduceResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Created by rrodovalho on 07/02/17.
 */
@RestController
public class UrlController {

    @Autowired
    UrlService urlService;

    @Autowired
    Gson gson;

    @RequestMapping(value = Constants.URL_MAPPING.REDUCE, method = RequestMethod.PUT)
    public ResponseEntity reduceUrl(@RequestParam String url, @RequestParam(required = false) String alias) throws AliasAlreadyExistsException {

        Instant before = Instant.now();
        Url reducedUrl = urlService.reduceUrl(new Url(url,alias,0));
        Instant after = Instant.now();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    gson.toJson(
                        ReduceResponse.builder()
                            .alias(reducedUrl.getAlias())
                            .url(reducedUrl.getUrl())
                            .statistics(
                                    new Statistics(Duration.between(before, after)
                                    .toMillis() + "ms")
                            )
                    )
                );

    }

    @RequestMapping(value = Constants.URL_MAPPING.EXPAND,method = RequestMethod.GET)
    public ResponseEntity expandUrl(@RequestParam String alias) throws ShortenedUrlNotFoundException {

        Url expandedUrl = urlService.getExpandedUrl(alias);

        return ResponseEntity
                .ok()
                .body(gson.toJson(
                        ExpandResponse.builder()
                                .alias(expandedUrl.getAlias())
                                .url(expandedUrl.getUrl()))
                );
    }

    @RequestMapping(value = Constants.URL_MAPPING.TEN_MOST_ACCESSED, method = RequestMethod.GET)
    public ResponseEntity tenMostAccessed(){

        List<Url> top10urls = urlService.getMostAccessedUrls();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gson.toJson(new MostAccessedResponse(top10urls)));

    }

}
