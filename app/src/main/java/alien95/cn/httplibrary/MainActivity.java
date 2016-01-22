package alien95.cn.httplibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import alien95.cn.http.request.HttpCallBack;
import alien95.cn.http.request.HttpRequest;
import alien95.cn.http.view.HttpImageView;

public class MainActivity extends AppCompatActivity {

    private TextView get, post;
    private HttpImageView smallImage,bigImage;
    private static final String GET_URL = "http://219.153.62.77/oracle_ykt0529.php?UsrID=1635159&page=1";
    private static final String POST_URL = "http://alien95.cn/lazyman/login.php";
    private static final String IMAGE_SMALL_URL = "http://i02.pictn.sogoucdn.com/5602ce182cd6899e";
    private static final String IMAGE_BIG_URL = "http://img03.sogoucdn.com/app/a/100520093/84bbacd9cddc14de-71e1f69c051f39b5-9b2699bc39567827fca983cfb05efe0a.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        get = (TextView) findViewById(R.id.get);
        post = (TextView) findViewById(R.id.post);
        smallImage = (HttpImageView) findViewById(R.id.small_image);
        bigImage = (HttpImageView) findViewById(R.id.big_image);

        httpGetRequest();
        httpPostRequest();
        smallImage.setImageUrl(IMAGE_SMALL_URL);
        bigImage.setImageUrl(IMAGE_BIG_URL);
    }


    public void httpGetRequest() {
        HttpRequest.getInstance().get(GET_URL, new HttpCallBack() {
            @Override
            public void success(String info) {
                get.setText("GET:\n" + info);
            }
        });
    }

    public void httpPostRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "alien95");
        params.put("password", "123456");
        HttpRequest.getInstance().post(POST_URL, params, new HttpCallBack() {
            @Override
            public void success(String info) {
                post.setText("POST:\n" + info);
            }
        });
    }
}
