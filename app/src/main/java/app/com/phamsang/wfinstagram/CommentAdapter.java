package app.com.phamsang.wfinstagram;

import android.content.Context;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quang Quang on 3/12/2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentObject> mDataSet = new ArrayList<CommentObject>();
    private Context mContext;
    public CommentAdapter(Context c) {
        super();
        mContext = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentObject commentObject = mDataSet.get(position);
        holder.mUserName.setText(commentObject.getUserName());
        holder.mContent.setText(commentObject.getComment());
        Glide.with(mContext).load(commentObject.getProfileImageUrl()).into(holder.mProfile);
        long timeStamp = commentObject.getTime()*1000;
        String relativeTime =
                DateUtils.getRelativeTimeSpanString(timeStamp, System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        holder.mTime.setText(relativeTime);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView mProfile;
        private TextView mUserName;
        private TextView mContent;
        private TextView mTime;
        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mUserName = (TextView) itemView.findViewById(R.id.comment_user_name_textView);
            mProfile = (RoundedImageView) itemView.findViewById(R.id.commentProfileImageView);
            mContent = (TextView) itemView.findViewById(R.id.commentTextView);
            mTime = (TextView) itemView.findViewById(R.id.comment_time);
        }
    }

    void swapData(List<CommentObject> dataSet){
        mDataSet =dataSet;
        notifyDataSetChanged();
    }

}
