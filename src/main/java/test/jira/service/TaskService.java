package test.jira.service;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Priority;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import test.jira.entity.MyIssue;
import test.jira.entity.MyJiraClient;
import test.jira.utils.IssueParse;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class TaskService {

    public void createIssue(MyIssue myIssue) {
        IssueRestClient issueClient = MyJiraClient.getInstance().getRestClient().getIssueClient();
        IssueInput newIssue = new IssueInputBuilder()
                .setProjectKey(myIssue.getProjectKey())
                .setIssueTypeId(myIssue.getType())
                .setSummary(myIssue.getSummary())
                .setPriorityId(myIssue.getPriority())
                .build();
        issueClient.createIssue(newIssue);
    }

    public ResponseEntity<?> createIssues(InputStream file) {
        try {
            for (MyIssue issue : getIssues(file)) {
                createIssue(issue);
            }
            return ResponseEntity.ok().build();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public List<MyIssue> getIssues(InputStream file) throws ParserConfigurationException, IOException, SAXException {
        Iterable<Priority> priorities = MyJiraClient.getInstance().getRestClient().getMetadataClient().getPriorities().claim();
        Iterable<IssueType> issueTypes = MyJiraClient.getInstance().getRestClient().getMetadataClient().getIssueTypes().claim();
        IssueParse issueParse = new IssueParse(priorities, issueTypes);
        return issueParse.parse(file);
    }
}