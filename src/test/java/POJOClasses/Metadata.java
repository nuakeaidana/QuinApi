package POJOClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//I needed this annotation to ignore reserved word "private"
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
    private String id;
    private String createdAt;

    @JsonProperty("private")
    private boolean _private;
}


