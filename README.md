##基于HttpURLConnection网络框架

### 本仓库不再更新，项目迁移至[RestHttp](https://github.com/llxdaxia/RestHttp)，欢迎一起学习。

- 初始化：设置是否开启调试模式（默认关闭）,设置网络请求Log输出Tag
    
        Http.initialize(this);
        if(BuildConfig.DEBUG){
                    Http.setDebug(true,"NetWork");
                }

- GET,POST请求

一，面向接口

        在接口中写好API

        public interface ServiceAPI {
        
            @POST("/v1/users/login.php")
            UserInfo login(@Field("name")
                       String name,
                           @Field("password")
                       String password);
        }
        
java代码

        RestHttpRequest restHttpRequest = new RestHttpRequest.Builder()
                        .baseUrl(BASE_URL)
                        .build();
        
                final ServiceAPI serviceAPI = (ServiceAPI) restHttpRequest.create(ServiceAPI.class);
        
                UserInfo userInfo = serviceAPI.login("alien95", "123456");
                serviceAPI.login("alien", "123456");
                serviceAPI.login("Lemon", "123456");
                serviceAPI.login("Lemon95", "123456");
        
                post.setText(userInfo.getName() + " --- " + userInfo.getFace());

其他请求方式：

(1)get请求：

        HttpRequest.getInstance().get(GET_URL, new HttpCallBack() {
                    @Override
                    public void success(String info) {
                        get.setText("GET:\n" + info);
                    }
                });

(2)post请求：

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

(3)添加header

        public void setHttpHeader(Map<String, String> header);

- 图片加载（包括了内存缓存和硬盘缓存）

(1)加载小图：

         <alien95.cn.http.view.HttpImageView
                    android:id="@+id/small_image"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:adjustViewBounds="true"
                    app:failedImage="@mipmap/ic_launcher"
                    app:loadImage="@mipmap/ic_launcher" />
                    
然后在java代码中：

        smallImage.setImageUrl(IMAGE_SMALL_URL);    设置一个图片地址就好了。                                       

(2)加载大图时可以进行图片压缩处理：

         <alien95.cn.http.view.HttpImageView
                    android:id="@+id/big_image"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:adjustViewBounds="true"
                    app:compressSize="2"
                    app:failedImage="@mipmap/ic_launcher"
                    app:loadImage="@mipmap/ic_launcher" />
                    
也可以通过：

        public void setInSimpleSize(int inSimpleSize);  设置压缩参数。
        
还可以通过：

        public void setImageUrlWithCompress(String url, int inSimpleSize);  设置压缩参数。

- 注意事项：
        
还依赖了其他库(避免重复依赖)：

        compile 'com.jakewharton:disklrucache:2.0.2'       
        
###Demo  

[美图APP](https://github.com/llxdaxia/Mito)

GET，POST请求：

<img src="/app/img_get.png" width="320" height="569" alt="GET "/>
<img src="/app/img_post.png" width="320" height="569" alt="POST"/>

图片加载（第一张小图，第二张大图）：

<img src="/app/img_image.png" width="320" height="569"/>

日志打印輸出：

<img src="/app/log.png" width="800" height="300"/>
