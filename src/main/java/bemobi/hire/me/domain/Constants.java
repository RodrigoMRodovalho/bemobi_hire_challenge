package bemobi.hire.me.domain;

/**
 * Created by rrodovalho on 07/02/17.
 */
public interface Constants {

    interface URL_MAPPING{

        String REDUCE = "/reduce";
        String EXPAND = "/expand";
        String TEN_MOST_ACCESSED = "/topten";

    }

    interface ERROR_INFO{

        String DESCRIPTION_ALIAS_ALREADY_USED = "CUSTOM ALIAS ALREADY EXISTS";
        String DESCRIPTION_URL_NOT_FOUND = "SHORTENED URL NOT FOUND";
        String CODE_ALIAS_ALREADY_USED = "001";
        String CODE_URL_NOT_FOUND = "002";

    }

}
