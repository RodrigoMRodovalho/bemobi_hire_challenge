package bemobi.hire.me.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by rrodovalho on 07/02/17.
 */

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Url {
    @SerializedName("url")
    String content;
    @SerializedName("CUSTOM_ALIAS")
    String alias;
    @Expose
    int access;
}
