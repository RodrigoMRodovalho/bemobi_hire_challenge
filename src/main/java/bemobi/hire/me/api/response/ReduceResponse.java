package bemobi.hire.me.api.response;

import bemobi.hire.me.api.domain.Statistics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by rrodovalho on 08/02/17.
 */

@Builder
@Data
@AllArgsConstructor
public class ReduceResponse {
    private String alias;
    private String url;
    private Statistics statistics;
}
