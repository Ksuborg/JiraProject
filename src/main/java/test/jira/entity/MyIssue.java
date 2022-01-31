package test.jira.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyIssue {
    private String projectKey;
    private String id;
    private Long type;
    private String summary;
    private Long priority;
}