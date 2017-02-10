package bemobi.hire.me.api.domain;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by rrodovalho on 08/02/17.
 */
@Data
@AllArgsConstructor
public class Statistics {
    @SerializedName("time_taken")
    private String timeTaken;
}
