package bemobi.hire.me.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by rrodovalho on 08/02/17.
 */
@Builder
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String alias;
    @SerializedName("err_code")
    private String errorCode;
    private String description;
}
