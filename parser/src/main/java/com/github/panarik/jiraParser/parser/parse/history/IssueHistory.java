package com.github.panarik.jiraParser.parser.parse.history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueHistory {

    private String self;
    private int maxResults;
    private int startAt;
    private int total;
    private boolean isLast;
    private List<IssueHistoryValues> values;


    public List<IssueHistoryValues> getValues() {
        return values;
    }

    public void setValues(List<IssueHistoryValues> values) {
        this.values = values;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }


    @Override
    public String toString() {
        return "IssueHistory{" +
                "self='" + self + '\'' +
                ", maxResults=" + maxResults +
                ", startAt=" + startAt +
                ", total=" + total +
                ", isLast=" + isLast +
                ", values=" + values +
                '}';
    }
}
