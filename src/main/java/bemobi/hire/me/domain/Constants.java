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

    interface DATABASE_CONFIG{
        String DRIVER = "com.mysql.jdbc.Driver";
        String URL_PRODUCTION = "jdbc:mysql://localhost:3306/shortenDB?useSSL=false";
        String URL_DEVELOPMENT = "jdbc:mysql://localhost:3306/shortenDBDev?useSSL=false";
        String USERNAME = "root";
        String PASSWORD = "gcm123";
    }

}
