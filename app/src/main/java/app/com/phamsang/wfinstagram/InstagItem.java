package app.com.phamsang.wfinstagram;

/**
 * Created by Quang Quang on 3/11/2016.
 */
public class InstagItem {
    private String mUserName;
    private String mCaption;
    private long mTime;
    private int mLikes;
    private String mImageUrl;
    private String mProfileUrl;
    private String mComments;
    private boolean mIsVideo;
    private String mId;
    private String mUrl;
    private String mVideoUrl;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public InstagItem(String userName, String caption, long time, int likes, String imageUrl,
                      String profileUrl, String comments, boolean isVideo, String id, String url, String videoUrl) {
        mUserName = userName;
        mCaption = caption;
        mTime = time;
        mLikes = likes;
        mImageUrl = imageUrl;
        mProfileUrl = profileUrl;

        mComments = comments;
        mIsVideo = isVideo;
        mId = id;
        mUrl = url;
        mVideoUrl = videoUrl;
    }

    public InstagItem() {

    }

    public String getUserName() {

        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public boolean isVideo() {
        return mIsVideo;
    }

    public void setVideo(boolean video) {
        mIsVideo = video;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int likes) {
        mLikes = likes;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getProfileUrl() {
        return mProfileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        mProfileUrl = profileUrl;
    }

    public String getComments() {
        return mComments;
    }

    public void setComments(String comments) {
        mComments = comments;
    }
}
