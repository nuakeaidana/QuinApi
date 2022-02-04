package POJO;
import lombok.*;

@Getter@Setter
@ToString
@NoArgsConstructor@AllArgsConstructor
public class Metadata {
    private String id;
    private boolean _private;
    private String createdAt;
    private String name;

}
