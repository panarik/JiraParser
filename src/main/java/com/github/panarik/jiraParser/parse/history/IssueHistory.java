package com.github.panarik.jiraParser.parse.history;

public class IssueHistory {

    private String self;
    private int maxResults;
    private int startAt;
    private int total;
    private boolean isLast;
    private IssueHistoryValues values;

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

    public IssueHistoryValues getValues() {
        return values;
    }

    public void setValues(IssueHistoryValues values) {
        this.values = values;
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
