package bemobi.hire.me.controller;

import bemobi.hire.me.domain.Constants;
import bemobi.hire.me.domain.Statistics;
import bemobi.hire.me.domain.Url;
import bemobi.hire.me.exception.AliasAlreadyExistsException;
import bemobi.hire.me.exception.ShortenedUrlNotFoundException;
import bemobi.hire.me.response.ExpandResponse;
import bemobi.hire.me.response.MostAccessedResponse;
import bemobi.hire.me.response.ReduceResponse;
import bemobi.hire.me.service.UrlService;
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
    public ResponseEntity reduceUrl(@RequestBody Url url) throws AliasAlreadyExistsException {

        Instant before = Instant.now();
        Url reducedUrl = urlService.reduceUrl(url);
        Instant after = Instant.now();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    gson.toJson(
                        ReduceResponse.builder()
                            .alias(reducedUrl.getAlias())
                            .url(reducedUrl.getContent())
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
                                .url(expandedUrl.getContent()))
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
