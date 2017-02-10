package bemobi.hire.me.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by rrodovalho on 08/02/17.
 */
@Builder
@Data
@AllArgsConstructor
public class ExpandResponse {
    private String alias;
    private String url;
}
