package id.co.telkomsigma.githubsearch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int current_page = 1;
    private ListView mSearchNFilterLv;
    private EditText mSearchEdt;
    List<UserModel> listUser;
    private UserAdapter valueAdapter;
    private UserModel userModel;
    ProgressBar progressBar;
    String tampung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchNFilterLv= findViewById(R.id.list_view);
        mSearchEdt= findViewById(R.id.txt_search);
        progressBar= findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                getUser(s.toString());
                tampung = s.toString();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        mSearchNFilterLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    current_page++;
                    getUserNext(tampung);
                }
            }
        });
    }

    private void getUser(final String key){
        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.get("https://api.github.com/search/users?q="+key+"&page="+current_page+"&per_page=1000")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listUser = new ArrayList<UserModel>();
                        System.out.println(key+"RESPONYA "+response);
                        try {
                            Toast.makeText(MainActivity.this,"", Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = response.getJSONArray("items");
                            String total_item = response.getString("total_count");
                            if (jsonArray.length()==0) {
                                Toast.makeText(MainActivity.this, "no result for "+key, Toast.LENGTH_SHORT).show();
                            } else {
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String login = object.getString("login");
                                    String avatar_url = object.getString("avatar_url");
                                    userModel = new UserModel(login, avatar_url);
                                    listUser.add(userModel);
                                }
                                Toast.makeText(MainActivity.this, total_item, Toast.LENGTH_SHORT).show();
                                valueAdapter=new UserAdapter(listUser,MainActivity.this);
                                valueAdapter.notifyDataSetChanged();
                                mSearchNFilterLv.setAdapter(valueAdapter);
                            }
                            progressBar.setVisibility(View.GONE);
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "Catch : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println(e+"CODERESPONSENYA");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode()==403) {
                            Toast.makeText(MainActivity.this, "API access limit exceeded for your device", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void getUserNext(final String key){
        AndroidNetworking.get("https://api.github.com/search/users?q="+key+"&page="+current_page+"&per_page=100")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(key+"RESPONYA "+response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            String total_item = response.getString("total_count");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                String login = object.getString("login");
                                String avatar_url = object.getString("avatar_url");
                                userModel = new UserModel(login, avatar_url);
                                listUser.add(userModel);
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }
}
