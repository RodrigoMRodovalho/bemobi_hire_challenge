package bemobi.hire.me.domain;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by rrodovalho on 07/02/17.
 */

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Url {
    String url;
    String alias;
    @Expose
    int access;
}
