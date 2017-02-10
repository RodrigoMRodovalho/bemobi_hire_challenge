package bemobi.hire.me.api.response;

import bemobi.hire.me.api.domain.Url;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by rrodovalho on 08/02/17.
 */
@Data
@AllArgsConstructor
public class MostAccessedResponse {
    List<Url> urls;
}
