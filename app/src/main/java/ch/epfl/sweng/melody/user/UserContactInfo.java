package ch.epfl.sweng.melody.user;

class UserContactInfo {

    private String userId;
    private String displayName;
    private String profilePhotoUrl;
    private String email;

    protected UserContactInfo(String userId, String displayName, String profilePhotoUrl, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.profilePhotoUrl = profilePhotoUrl;
        this.email = email;
    }

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
}
