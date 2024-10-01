package uz.pdp.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class Comment {
    private Integer postId;
    private Integer id;
    private String name;
    private String email;
    private String body;
}
