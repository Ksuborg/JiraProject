package test.jira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DescriptionDTO {
    private String id;
    private String summary;

    public DescriptionDTO() {
    }
}
