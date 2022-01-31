package test.jira.utils;

import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Priority;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import test.jira.dto.DescriptionDTO;
import test.jira.dto.IssueDTO;
import test.jira.entity.MyIssue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class IssueParse {
    private String mProjectKey;
    Iterable<Priority> mPriorities;
    Iterable<IssueType> mIssueTypes;

    public IssueParse(Iterable<Priority> priorities, Iterable<IssueType> issueTypes) {
        mPriorities = priorities;
        mIssueTypes = issueTypes;
    }

    public List<MyIssue> parse(InputStream file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return mapMyIssue(parseIssues(document), parseDescriptions(document));
    }

    private List<MyIssue> mapMyIssue(List<IssueDTO> issueDTOList, List<DescriptionDTO> descriptionDTOList) {
        List<MyIssue> myIssueList = new ArrayList<>();
        for (IssueDTO issueDTO : issueDTOList) {
            for (DescriptionDTO descriptionDTO : descriptionDTOList) {
                if (issueDTO.getId().equals(descriptionDTO.getId())) {
                    String id = issueDTO.getId();
                    Long type = getIdType(issueDTO.getType());
                    Long priority = getIdPriority(issueDTO.getPriority());
                    String summary = descriptionDTO.getSummary();
                    myIssueList.add(new MyIssue(mProjectKey, id, type, summary, priority));
                }
            }
        }
        return myIssueList;
    }

    private List<IssueDTO> parseIssues(Document document) {
        List<IssueDTO> issueDTOList = new ArrayList<>();
        mProjectKey = document.getElementsByTagName("projectKey").item(0).getTextContent();
        NodeList nodeIssues = document.getDocumentElement().getElementsByTagName("issue");
        for (int i = 0; i < nodeIssues.getLength(); i++) {
            IssueDTO issueDTO = new IssueDTO();
            Node issue = nodeIssues.item(i);
            Element element = (Element) issue;
            issueDTO.setId(element.getElementsByTagName("id").item(0).getTextContent());
            issueDTO.setType(element.getElementsByTagName("type").item(0).getTextContent());
            issueDTO.setPriority(element.getElementsByTagName("priority").item(0).getTextContent());
            issueDTOList.add(issueDTO);
        }
        return issueDTOList;
    }

    private List<DescriptionDTO> parseDescriptions(Document document) {
        List<DescriptionDTO> descriptionDTOList = new ArrayList<>();
        NodeList nodeDescriptions = document.getDocumentElement().getElementsByTagName("description");
        for (int i = 0; i < nodeDescriptions.getLength(); i++) {
            DescriptionDTO descriptionDTO = new DescriptionDTO();
            Node description = nodeDescriptions.item(i);
            Element element = (Element) description;
            descriptionDTO.setId(element.getElementsByTagName("id").item(0).getTextContent());
            descriptionDTO.setSummary(element.getElementsByTagName("summary").item(0).getTextContent());
            descriptionDTOList.add(descriptionDTO);
        }
        return descriptionDTOList;
    }

    private Long getIdPriority(String priorityName) {
        Long priorityId = null;
        for (Priority priority : mPriorities) {
            if (priority.getName().equalsIgnoreCase(priorityName)) {
                priorityId = priority.getId();
            }
        }
        if (priorityId == null) {
            return 1L;
        }
        return priorityId;
    }
    private Long getIdType(String typeName) {
        Long typeId = null;
        for (IssueType issueType : mIssueTypes) {
            if (issueType.getName().equalsIgnoreCase(typeName)) {
                typeId = issueType.getId();
            }
        }
        if (typeId == null) {
            return 10001L;
        }
        return typeId;
    }
}
