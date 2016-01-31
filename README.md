##基于HttpUrlConnection网络框架

- 依赖

        compile 'cn.alien95:http:1.1.4'

- 初始化：设置是否开启调试模式（默认关闭）,设置网络请求Log输出Tag
    
        Http.initialize(this);
        if(BuildConfig.DEBUG){
                    Http.setDebug(true,"NetWork");
                }

- GET,POST请求

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
[App-Demo](https://github.com/llxdaxia/Http/tree/develop/app)

[美图APP](https://github.com/llxdaxia/Mito)

Demo:

GET，POST请求：

<img src="/app/img_get.png" width="320" height="569" alt="GET "/>
<img src="/app/img_post.png" width="320" height="569" alt="POST"/>

图片加载（第一张小图，第二张大图）：

<img src="/app/img_image.png" width="320" height="569"/>