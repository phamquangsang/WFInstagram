package app.com.phamsang.wfinstagram.object;

/**
 * Created by Quang Quang on 3/12/2016.
 */
public class CommentObject {
    private String mUserName;
    private String mProfileImageUrl;
    private String mComment;
    private long mTime;

    public CommentObject(String userName, String profileImageUrl, String comment, long time) {
        mUserName = userName;
        mProfileImageUrl = profileImageUrl;
        mComment = comment;
        mTime = time;
    }

    public CommentObject() {

    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        mProfileImageUrl = profileImageUrl;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }
}
