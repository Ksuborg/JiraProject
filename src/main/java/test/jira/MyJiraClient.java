package test.jira;


import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import lombok.Data;

import java.net.URI;

@Data
public class MyJiraClient {
    private static MyJiraClient myJiraClient;

    private String username;
    private String token;
    private String jiraUrl;
    private JiraRestClient restClient;

    private MyJiraClient(String username, String password, String jiraURL) {
        this.username = username;
        this.token = password;
        this.jiraUrl = jiraURL;
        this.restClient = generateJiraRestClient();
    }

    public static synchronized MyJiraClient getInstance() {
        if (myJiraClient == null) {
            myJiraClient = new MyJiraClient(
                    Constants.USER_NAME,
                    Constants.TOKEN,
                    Constants.JIRA_URL
            );
        }
        return myJiraClient;
    }

    private JiraRestClient generateJiraRestClient() {
        AuthenticationHandler authenticationHandler = new BasicHttpAuthenticationHandler(this.username, this.token);

        return new AsynchronousJiraRestClientFactory()
                .createWithAuthenticationHandler(getJiraUri(), authenticationHandler);
    }

    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }
}