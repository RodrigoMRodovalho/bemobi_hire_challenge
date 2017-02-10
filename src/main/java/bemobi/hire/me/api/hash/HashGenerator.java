package bemobi.hire.me.api.hash;

import com.sangupta.murmur.Murmur2;
import com.sangupta.murmur.Murmur3;

/**
 * Created by rrodovalho on 08/02/17.
 */
public class HashGenerator {

    private static final long SEED = 12345678;

    public HashGenerator(){

    }

    public long computeHash(String urlContent){

        byte[] urlContentByteArray = urlContent.getBytes();

        long hash = Murmur3.hash_x86_32(
                urlContentByteArray,
                urlContentByteArray.length,
                SEED
        );

        return hash;

    }

}
