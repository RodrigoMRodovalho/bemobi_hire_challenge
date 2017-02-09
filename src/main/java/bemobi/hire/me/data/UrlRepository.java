package bemobi.hire.me.data;

import bemobi.hire.me.domain.Url;

import java.util.List;

/**
 * Created by rrodovalho on 07/02/17.
 */
public interface UrlRepository {

    Url saveUrl(Url url);

    Url getUrlByAlias(String alias);

    int updateUrlAccess(int currentAccess, String alias);

    List<Url> getMostTenAccessedUrl();
}
