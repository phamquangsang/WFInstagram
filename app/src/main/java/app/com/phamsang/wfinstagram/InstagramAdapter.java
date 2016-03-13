package app.com.phamsang.wfinstagram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Quang Quang on 3/11/2016.
 */
public class InstagramAdapter extends RecyclerView.Adapter<InstagramAdapter.ViewHolder> {
    private static final String LOG_TAG = InstagramAdapter.class.getSimpleName();
    private List<InstagItem> mDataSet = new ArrayList<InstagItem>();
    private Context mContext;

    public InstagramAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instag_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final InstagItem data = mDataSet.get(position);
        holder.mUserName.setText(data.getUserName());
        holder.mCaption.setText(data.getCaption());
        holder.mComment.setText(data.getComments());
        holder.mLike.setText(Integer.toString(data.getLikes()));

        long timeStamp = data.getTime()*1000;
        String relativeTime = "";
        relativeTime = DateUtils.getRelativeTimeSpanString(timeStamp, System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        holder.mTime.setText(relativeTime);
        Glide.with(mContext).load(data.getImageUrl()).placeholder(R.drawable.loading).into(holder.mImage);
        Glide.with(mContext).load(data.getProfileUrl()).into(holder.mProfile);
        if(data.isVideo()){
            holder.mVideoPlay.setVisibility(View.VISIBLE);
            //Toast.makeText(mContext,"video",Toast.LENGTH_SHORT).show();
            holder.mVideoPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoViewActivity.showRemoteVideo(mContext, data.getVideoUrl());
                }
            });
        }else{
            holder.mVideoPlay.setVisibility(View.GONE);
        }
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(data.getUrl()));
                mContext.startActivity(intent);
            }
        });
        holder.mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllCommentActivity.showAllComment(mContext, data.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView mImage;
        private TextView mLike;
        private TextView mTime;
        private RoundedImageView mProfile;
        private TextView mUserName;
        private TextView mCaption;
        private TextView mComment;
        private ImageView mVideoPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImage = (ImageView) itemView.findViewById(R.id.imageView);
            mLike = (TextView) itemView.findViewById(R.id.likeTextView);
            mTime = (TextView) itemView.findViewById(R.id.timeTextView);
            mProfile = (RoundedImageView)itemView.findViewById(R.id.imageViewUserProfile);
            mUserName = (TextView)itemView.findViewById(R.id.textView_user_name);
            mCaption = (TextView)itemView.findViewById(R.id.textView_caption);
            mComment = (TextView)itemView.findViewById(R.id.textViewComment);
            mVideoPlay = (ImageView)itemView.findViewById(R.id.imageView_video_play);
        }
    }

    public void swapDataSet(List<InstagItem> dataSet){
        mDataSet = dataSet;
        notifyDataSetChanged();
    }

    public List<InstagItem> getDataSet() {
        return mDataSet;
    }


}
