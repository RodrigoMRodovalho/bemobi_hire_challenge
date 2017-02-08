package bemobi.hire.me.service;

import bemobi.hire.me.data.UrlRepository;
import bemobi.hire.me.domain.Url;
import bemobi.hire.me.exception.AliasAlreadyExistsException;
import bemobi.hire.me.exception.ShortenedUrlNotFoundException;
import bemobi.hire.me.hash.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (url.getAlias() == null)
            url.setAlias(hashGenerator.generateAlias(url.getContent()));

        url.setAccess(0);
        Url savedUrl = urlRepository.saveUrl(url);

        if (savedUrl == null)
            throw new AliasAlreadyExistsException(url.getAlias());

        return savedUrl;
    }

    public Url getExpandedUrl(String alias) throws ShortenedUrlNotFoundException {

        Url url = urlRepository.getUrlByAlias(alias);

        if (url == null)
            throw new ShortenedUrlNotFoundException(alias);

        urlRepository.updateUrlAccess(url.getAccess() + 1, url.getAlias());

        return url;
    }

    public List<Url> getMostAccessedUrls(){
        return urlRepository.getMostFiveAccessedUrl();
    }

}
