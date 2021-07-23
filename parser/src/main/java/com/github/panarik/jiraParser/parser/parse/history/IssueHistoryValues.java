package com.github.panarik.jiraParser.parser.parse.history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueHistoryValues {

    private int id;
    private IssueHistoryValuesAuthor author;
    private String created;
    private List<IssueHistoryValuesItems> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IssueHistoryValuesAuthor getAuthor() {
        return author;
    }

    public void setAuthor(IssueHistoryValuesAuthor author) {
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

    @Override
    public String toString() {
        return "IssueHistoryValues{" +
                "id=" + id +
                ", author=" + author +
                ", created='" + created + '\'' +
                ", items=" + items +
                '}';
    }
}
