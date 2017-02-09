package bemobi.hire.me.service;

import bemobi.hire.me.data.UrlRepository;
import bemobi.hire.me.domain.Constants;
import bemobi.hire.me.domain.Url;
import bemobi.hire.me.exception.AliasAlreadyExistsException;
import bemobi.hire.me.exception.ShortenedUrlNotFoundException;
import bemobi.hire.me.hash.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rrodovalho on 07/02/17.
 */
@Service
public class UrlService {

    UrlRepository urlRepository;
    HashGenerator hashGenerator;

    @Autowired
    public UrlService(UrlRepository urlRepository, HashGenerator hashGenerator) {
        this.urlRepository = urlRepository;
        this.hashGenerator = hashGenerator;
    }

    public Url reduceUrl(Url url) throws AliasAlreadyExistsException {

        Url reducedUrl = new Url();

        if (url.getAlias() == null)
            reducedUrl.setAlias(hashGenerator.generateAlias(url.getUrl()));
        else
            reducedUrl.setAlias(url.getAlias());

        reducedUrl.setAccess(0);
        try{
            urlRepository.saveUrl(reducedUrl);
        }
        catch (DuplicateKeyException ex){
            throw new AliasAlreadyExistsException(url.getAlias());
        }
        reducedUrl.setUrl(Constants.SHORTEN_URL_PREFIX + reducedUrl.getAlias());
        return reducedUrl;
    }

    public Url getExpandedUrl(String alias) throws ShortenedUrlNotFoundException {

        Url url = urlRepository.getUrlByAlias(alias);

        if (url == null)
            throw new ShortenedUrlNotFoundException(alias);

        urlRepository.updateUrlAccess(url.getAccess() + 1, url.getAlias());

        return url;
    }

    public List<Url> getMostAccessedUrls(){
        return urlRepository.getMostTenAccessedUrl();
    }

}
