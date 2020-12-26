package api.dataclass;

// 这里才是 UserInfoRoot
public class Data implements java.io.Serializable {

    private long uid;
    private String username;
    private long fuid;
    private String fusername;
    private int isfriend;
    private long dateline;
    private String entityType;
    private long entityId;
    private String userAvatar;
    private UserInfo userInfo;
    private String fUserAvatar;
    private FUserInfo fUserInfo;

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setFuid(long fuid) {
        this.fuid = fuid;
    }

    public long getFuid() {
        return fuid;
    }

    public void setFusername(String fusername) {
        this.fusername = fusername;
    }

    public String getFusername() {
        return fusername;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public long getDateline() {
        return dateline;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setFUserAvatar(String fUserAvatar) {
        this.fUserAvatar = fUserAvatar;
    }

    public String getFUserAvatar() {
        return fUserAvatar;
    }

    public void setFUserInfo(FUserInfo fUserInfo) {
        this.fUserInfo = fUserInfo;
    }

    public FUserInfo getFUserInfo() {
        return fUserInfo;
    }

}
