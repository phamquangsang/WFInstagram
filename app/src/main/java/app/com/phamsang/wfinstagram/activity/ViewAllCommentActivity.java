package app.com.phamsang.wfinstagram.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.com.phamsang.wfinstagram.R;
import app.com.phamsang.wfinstagram.adapter.CommentAdapter;
import app.com.phamsang.wfinstagram.object.CommentObject;
import cz.msebera.android.httpclient.Header;

public class ViewAllCommentActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "instagram_id";
    private static final String LOG_TAG = ViewAllCommentActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private CommentAdapter mAdapter = new CommentAdapter(this);

    public static void showAllComment(Context c, String id) {
        Intent intent = new Intent(c, ViewAllCommentActivity.class);
        Bundle extra = new Bundle();
        extra.putString(ViewAllCommentActivity.EXTRA_ID, id);
        intent.putExtras(extra);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_all_comment);
        getSupportActionBar().setTitle("comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            //todo get comment url then load data
            String id = extra.getString(EXTRA_ID, "");
            loadData(id);
        } else {
            //error
            finish();
        }
    }

    public void loadData(String instagId) {
        AsyncHttpClient client = new AsyncHttpClient();
        String cmtUrl = "https://api.instagram.com/v1/media/instagram_id/comments?client_id=e05c462ebd86446ea48a5af73769b602";
        String url = cmtUrl.replaceAll(new String("instagram_id"), instagId);
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode != 200) {
                    Log.e(LOG_TAG, "http error status code: " + statusCode);
                    return;
                }
                List<CommentObject> dataSet = new ArrayList<CommentObject>();
                try {
                    JSONArray data = response.getJSONArray("data");
                    Log.d(LOG_TAG, "download comment from server successfully, count: " + data.length());
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        CommentObject comment = new CommentObject();
                        comment.setComment(jsonObject.getString("text"));
                        comment.setTime(jsonObject.getLong("created_time"));

                        JSONObject userObject = jsonObject.getJSONObject("from");
                        comment.setUserName(userObject.getString("username"));
                        comment.setProfileImageUrl(userObject.getString("profile_picture"));


                        dataSet.add(comment);
                    }
                    Log.d(LOG_TAG, "parse to CommentObject successfully! count: " + dataSet.size() + " CommentObjects");
                    //todo setDatase, notify adapter
                    mAdapter.swapData(dataSet);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "parse to CommentObject failed! count: " + dataSet.size() + " CommentObjects");
                }

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });


    }

}
