package bemobi.hire.me.response;

import bemobi.hire.me.domain.Url;
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
