package ch.epfl.sweng.melody.user;

public class UserContactInfo {

    private String userId;
    private String displayName;
    private String profilePhotoUrl;
    private String email;

    //temporarily set to public to create mock data, put it back to protected when add users works
    public UserContactInfo(String userId, String displayName, String profilePhotoUrl, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.profilePhotoUrl = profilePhotoUrl;
        this.email = email;
    }

    public UserContactInfo() {}

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(UserContactInfo.class) && this.userId.equals(((UserContactInfo) obj).getUserId());
    }
}
