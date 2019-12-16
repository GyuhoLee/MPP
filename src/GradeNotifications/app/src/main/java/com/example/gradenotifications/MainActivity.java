package com.example.gradenotifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ArrayList<ItemObject> list = new ArrayList();
    private EditText userId;
    private EditText userPw;
    private String sUserId = "";
    private String sUserPw = "";
    private TextView testText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button button = (Button)findViewById(R.id.loginButton);
        userId = (EditText) findViewById(R.id.userId);
        userPw = (EditText) findViewById(R.id.userPw);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), SecondActivity.class);
                startActivity(intent);
                /*
                sUserId = "" + userId.getText();
                sUserPw = "" + userPw.getText();
                setContentView(R.layout.activity_main);
                recyclerView = (RecyclerView) findViewById(R.id.recycler);
                //AsyncTask 작동시킴(파싱)
                new Description().execute();
                */
            }
        });
    }



    private class Description extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                /*
                Connection.Response res = Jsoup.connect("https://klas.khu.ac.kr/index.jsp?sso=ok")
                        .data("USER_ID",sUserId,
                                "PASSWORD", sUserPw)
                        //.cookie("PHPSESSID", sessionID)
                        .timeout(15000)
                        .method(Connection.Method.POST)
                        .execute();

                Document docs = res.parse();
                Log.d("테스트용", "" + docs.body());

                Map<String, String> loginCookie = res.cookies();

                Document doc = Jsoup.connect("https://portal.khu.ac.kr/haksa/clss/scre/allScre/index.do")
                        .cookies(loginCookie)
                        .get();
                 */
                Connection.Response res = Jsoup.connect("http://35.201.234.66/view.html")
                        .execute();
                Document doc = res.parse();
                Log.d("이거이거이거이거이거", "" + doc.body());
                Elements mElementDataSize = doc.select("div#grade div.subject"); //필요한 녀석만 꼬집어서 지정
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.
                Log.d("몇개냐 ", Integer.toString(mElementSize));
                for (Element elem : mElementDataSize) {
                    Elements fElem = elem.select("div");
                    String my_title = fElem.first().text().split(" ")[0];
                    String my_release = fElem.last().text();
                    list.add(new ItemObject(my_title, my_release));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            try {
                Document doc = Jsoup.connect("https://movie.naver.com/movie/running/current.nhn").get();
                Elements mElementDataSize = doc.select("ul[class=lst_detail_t1]").select("li"); //필요한 녀석만 꼬집어서 지정
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.

                for (Element elem : mElementDataSize) { //이렇게 요긴한 기능이
                    //영화목록 <li> 에서 다시 원하는 데이터를 추출해 낸다.
                    String my_title = elem.select("li dt[class=tit] a").text();
                    Element rElem = elem.select("dl[class=info_txt1] dt").next().first();
                    String my_release = rElem.select("dd").text();
                    list.add(new ItemObject(my_title, my_release));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
             */
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            MyAdapter myAdapter = new MyAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);

            progressDialog.dismiss();
        }
    }
}

