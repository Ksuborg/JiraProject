package test.jira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssueDTO {
    private String id;
    private String type;
    private String priority;

    public IssueDTO() {
    }
}
