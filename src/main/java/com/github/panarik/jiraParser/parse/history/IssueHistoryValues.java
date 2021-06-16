package com.github.panarik.jiraParser.parse.history;

import java.util.List;

public class IssueHistoryValues {

    private int id;
    private List<IssueHistoryValuesAuthor> author;
    private String created;
    private List<IssueHistoryValuesItems> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<IssueHistoryValuesAuthor> getAuthor() {
        return author;
    }

    public void setAuthor(List<IssueHistoryValuesAuthor> author) {
        this.author = author;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<IssueHistoryValuesItems> getItems() {
        return items;
    }

    public void setItems(List<IssueHistoryValuesItems> items) {
        this.items = items;
    }
}
