package com.github.panarik.jiraParser.parser.parse.search;

public class IssuePreview {

    private String expand;
    private String id;
    private String self;
    private String key;

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "IssuePreview{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
