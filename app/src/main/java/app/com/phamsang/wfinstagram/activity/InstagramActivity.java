package app.com.phamsang.wfinstagram.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.com.phamsang.wfinstagram.R;
import app.com.phamsang.wfinstagram.Utilities;
import app.com.phamsang.wfinstagram.adapter.InstagramAdapter;
import app.com.phamsang.wfinstagram.object.InstagItem;
import cz.msebera.android.httpclient.Header;

public class InstagramActivity extends AppCompatActivity {
    private static final String LOG_TAG = InstagramActivity.class.getSimpleName();
    public final String PREFERENCE_NAME = "My_reference";
    public final String DATA_SET_JSON = "data_set_json";
    private RecyclerView mRecyclerView;
    private InstagramAdapter mAdapter = new InstagramAdapter(this);
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.swipeRefeshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        SharedPreferences setting = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        String dataSet = setting.getString(DATA_SET_JSON, "");
        if (dataSet.equalsIgnoreCase("")) {
            loadData();
        } else {
            Gson gson = new Gson();
            List<InstagItem> listInstag = new ArrayList<InstagItem>();
            Type type = new TypeToken<List<InstagItem>>() {
            }.getType();
            listInstag = gson.fromJson(dataSet, type);
            mAdapter.swapDataSet(listInstag);
        }

        //set up Adapter


    }

    public void setEmptyView() {
        if (mAdapter.getItemCount() == 0) {
            View view = findViewById(R.id.emptyView);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        } else {
            View view = findViewById(R.id.emptyView);
            if (view != null) {
                view.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instagram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        //saving current data.
        Gson gson = new Gson();
        String json = gson.toJson(mAdapter.getDataSet());
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        preferences.edit().putString(DATA_SET_JSON, json).commit();
        Log.i(LOG_TAG, json);
        super.onStop();

    }

    public void loadData() {
        if(!Utilities.isNetworkAvailable(this)){
            setEmptyView();
            Toast.makeText(this,"no internet connection", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://api.instagram.com/v1/media/popular?client_id=e05c462ebd86446ea48a5af73769b602", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<InstagItem> dataSet = new ArrayList<InstagItem>();
                try {
                    JSONArray data = response.getJSONArray("data");
                    Log.d(LOG_TAG, "download from server successfully, count: " + data.length());
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        InstagItem instagItem = new InstagItem();

                        instagItem.setTime(jsonObject.getLong("created_time"));

                        JSONObject userObject = jsonObject.getJSONObject("user");
                        instagItem.setProfileUrl(userObject.getString("profile_picture"));
                        instagItem.setUserName(userObject.getString("username"));

                        JSONObject captionObject = jsonObject.getJSONObject("caption");
                        instagItem.setCaption(captionObject.getString("text"));

                        JSONObject imageObject = jsonObject.getJSONObject("images");
                        JSONObject standardImageObject = imageObject.getJSONObject("standard_resolution");
                        instagItem.setImageUrl(standardImageObject.getString("url"));


                        JSONObject likeObject = jsonObject.getJSONObject("likes");
                        instagItem.setLikes(likeObject.getInt("count"));

                        JSONObject commentObject = jsonObject.getJSONObject("comments");
                        JSONArray commentData = commentObject.getJSONArray("data");
                        String insTagComment = "";
                        int j = commentData.length() - 3;
                        j = (j < 0) ? 0 : j;
                        for (; j < commentData.length(); ++j) {
                            JSONObject aComment = commentData.getJSONObject(j);
                            JSONObject commentFrom = aComment.getJSONObject("from");
                            insTagComment = insTagComment + commentFrom.getString("username") + ": " + aComment.getString("text") + "\n";
                        }
                        instagItem.setComments(insTagComment.trim() + "\nVIEW ALL COMMENTS");

                        String mediaType = jsonObject.getString("type");
                        if (mediaType.equalsIgnoreCase("image")) {
                            instagItem.setVideo(false);
                        } else {
                            instagItem.setVideo(true);
                            JSONObject videoObject = jsonObject.getJSONObject("videos");
                            JSONObject standardVideoObject = videoObject.getJSONObject("standard_resolution");
                            instagItem.setVideoUrl(standardVideoObject.getString("url"));
                        }

                        instagItem.setId(jsonObject.getString("id"));
                        instagItem.setUrl(jsonObject.getString("link"));


                        dataSet.add(instagItem);
                    }
                    Log.d(LOG_TAG, "parse to InstagItem successfully! count: " + dataSet.size() + " instagItems");
                    //todo setDatase, notify adapter
                    mAdapter.swapDataSet(dataSet);

                } catch (JSONException e) {
                    e.printStackTrace();

                    Log.d(LOG_TAG, "parse to InstagItem failed! count: " + dataSet.size() + " instagItems");
                }
                finally {
                    setEmptyView();//check if no data return show emptyView holder
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });


    }


}
