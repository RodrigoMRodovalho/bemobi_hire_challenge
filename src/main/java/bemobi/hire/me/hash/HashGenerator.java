package bemobi.hire.me.hash;

import com.sangupta.murmur.Murmur2;

/**
 * Created by rrodovalho on 08/02/17.
 */
public class HashGenerator {

    private static final long SEED = 12345678;

    public HashGenerator(){

    }

    public String generateAlias(String urlContent){

        byte[] urlContentByteArray = urlContent.getBytes();

        long hash = Murmur2.hash64(
                urlContentByteArray,
                urlContentByteArray.length,
                SEED
        );

        return String.valueOf(hash);

    }

}
