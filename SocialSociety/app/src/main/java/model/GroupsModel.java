package model;

public class GroupsModel {

    String groupId,groupSocietyId,groupName,groupImage,groupStatus,groupCreatedBy,groupCreatedOn,groupImageUrl,groupUserName;
    int idd;
    public boolean isImageChanged;

    public int getIdd() {
        return idd;
    }

    public void setIdd(int idd) {
        this.idd = idd;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupSocietyId() {
        return groupSocietyId;
    }

    public void setGroupSocietyId(String groupSocietyId) {
        this.groupSocietyId = groupSocietyId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getGroupCreatedBy() {
        return groupCreatedBy;
    }

    public void setGroupCreatedBy(String groupCreatedBy) {
        this.groupCreatedBy = groupCreatedBy;
    }

    public String getGroupCreatedOn() {
        return groupCreatedOn;
    }

    public void setGroupCreatedOn(String groupCreatedOn) {
        this.groupCreatedOn = groupCreatedOn;
    }

    public String getGroupImageUrl() {
        return groupImageUrl;
    }

    public void setGroupImageUrl(String groupImageUrl) {
        this.groupImageUrl = groupImageUrl;
    }

    public String getGroupUserName() {
        return groupUserName;
    }

    public void setGroupUserName(String groupUserName) {
        this.groupUserName = groupUserName;
    }

    public boolean isImageChanged() {
        return isImageChanged;
    }

    public void setIsImageChanged(boolean isImageChanged) {
        this.isImageChanged = isImageChanged;
    }
}
