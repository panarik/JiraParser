package com.github.panarik.jiraParser.parse.history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueHistoryValuesAuthorAvatar {

    private String avatarX48;
    private String avatarX24;
    private String avatarX16;
    private String avatarX32;

    public String getAvatarX48() {
        return avatarX48;
    }

    public void setAvatarX48(String avatarX48) {
        this.avatarX48 = avatarX48;
    }

    public String getAvatarX24() {
        return avatarX24;
    }

    public void setAvatarX24(String avatarX24) {
        this.avatarX24 = avatarX24;
    }

    public String getAvatarX16() {
        return avatarX16;
    }

    public void setAvatarX16(String avatarX16) {
        this.avatarX16 = avatarX16;
    }

    public String getAvatarX32() {
        return avatarX32;
    }

    public void setAvatarX32(String avatarX32) {
        this.avatarX32 = avatarX32;
    }

}
