package com.github.panarik.jiraParser.parse.history;

import java.util.List;

public class IssueHistoryValuesAuthor {

    private String self;
    private String accountId;
    private String emailAddress;
    private List<IssueHistoryValuesAuthorAvatar> avatarUrls;
    private String displayName;
    private boolean active;
    private String timeZone;
    private String accountType;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<IssueHistoryValuesAuthorAvatar> getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(List<IssueHistoryValuesAuthorAvatar> avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
