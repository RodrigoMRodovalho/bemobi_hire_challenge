package bemobi.hire.me.api.service;

import bemobi.hire.me.api.data.UrlRepository;
import bemobi.hire.me.api.domain.Url;
import bemobi.hire.me.api.exception.AliasAlreadyExistsException;
import bemobi.hire.me.api.exception.ShortenedUrlNotFoundException;
import bemobi.hire.me.api.hash.HashGenerator;
import bemobi.hire.me.api.domain.Constants;
import bemobi.hire.me.api.util.BaseEncodeUtil;
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

        if (url.getAlias() == null)
            url.setAlias(BaseEncodeUtil.encodeToBase62(hashGenerator.computeHash(url.getUrl())));

        try{
            urlRepository.saveUrl(url);
        }
        catch (DuplicateKeyException ex){
            throw new AliasAlreadyExistsException(url.getAlias());
        }

        url.setUrl(Constants.SHORTEN_URL_PREFIX + url.getAlias());
        return url;
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
