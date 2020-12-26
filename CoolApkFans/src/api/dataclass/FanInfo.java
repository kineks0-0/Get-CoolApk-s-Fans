package api.dataclass;

public class FanInfo implements java.io.Serializable {
    private int uid;
    private String username;
    private int fuid;
    private String fusername;
    private int isfriend;
    private int dateline;
    private String entityType;
    private int entityId;
    private String userAvatar;
    private UserInfo userInfo;
    private String fUserAvatar;
    private FUserInfo fUserInfo;

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFuid() {
        return this.fuid;
    }

    public void setFuid(int fuid) {
        this.fuid = fuid;
    }

    public String getFusername() {
        return this.fusername;
    }

    public void setFusername(String fusername) {
        this.fusername = fusername;
    }

    public int getIsfriend() {
        return this.isfriend;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public int getDateline() {
        return this.dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getUserAvatar() {
        return this.userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getFUserAvatar() {
        return this.fUserAvatar;
    }

    public void setFUserAvatar(String fUserAvatar) {
        this.fUserAvatar = fUserAvatar;
    }

    public FUserInfo getFUserInfo() {
        return this.fUserInfo;
    }

    public void setFUserInfo(FUserInfo fUserInfo) {
        this.fUserInfo = fUserInfo;
    }
}
