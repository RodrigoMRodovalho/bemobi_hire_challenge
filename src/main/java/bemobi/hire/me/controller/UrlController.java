package bemobi.hire.me.controller;

import bemobi.hire.me.domain.Constants;
import bemobi.hire.me.domain.Url;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rrodovalho on 07/02/17.
 */
@RestController
public class UrlController {

    @RequestMapping(value = Constants.URL_MAPPING.REDUCE, method = RequestMethod.PUT)
    public ResponseEntity reduceUrl(@RequestBody Url url){

        return null;
    }

    @RequestMapping(value = Constants.URL_MAPPING.EXPAND,method = RequestMethod.GET)
    public ResponseEntity expandUrl(@RequestParam String alias){

        return null;
    }

    @RequestMapping(value = Constants.URL_MAPPING.FIVE_MOST_ACCESSED, method = RequestMethod.GET)
    public ResponseEntity fiveMostAccessed(){

        return null;
    }

}
