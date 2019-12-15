package com.example.gradenotifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ItemObject> list = new ArrayList();
    private TextView userId;
    private TextView userPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button button = (Button)findViewById(R.id.loginButton);
        userId = (TextView)findViewById(R.id.userId);
        userPw = (TextView)findViewById(R.id.userPw);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                recyclerView = (RecyclerView) findViewById(R.id.recycler);
                //AsyncTask 작동시킴(파싱)
                new Description().execute();
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
                Connection.Response loginPageResponse = Jsoup.connect("https://info21.khu.ac.kr/com/LoginCtr/login.do?sso=ok")
                        .timeout(3000)
                        .header("Origin", "https://portal.khu.ac.kr")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .method(Connection.Method.GET)
                        .execute();

                // 로그인 페이지에서 얻은 쿠키
                Map<String, String> loginTryCookie = loginPageResponse.cookies();

                // 로그인 페이지에서 로그인에 함께 전송하는 토큰 얻어내기
                Document loginPageDocument = loginPageResponse.parse();

                String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";
                Map<String, String> data = new HashMap<>();
                data.put("userId", "" + userId.getText());
                data.put("userPw", "" + userPw.getText());
               // data.put("idSaveCheck", "1");

                Connection.Response response = Jsoup.connect("https://info21.khu.ac.kr/com/LoginCtr/login.do?sso=ok")
                        .userAgent(userAgent)
                        .timeout(3000)
                        .header("Origin", "https://portal.khu.ac.kr")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .cookies(loginTryCookie)
                        .data(data)
                        .method(Connection.Method.POST)
                        .execute();

                Map<String, String> loginCookie = response.cookies();

                Document doc = Jsoup.connect("https://portal.khu.ac.kr/haksa/clss/scre/allScre/index.do")
                        .cookies(loginCookie)
                        .get();
                

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

