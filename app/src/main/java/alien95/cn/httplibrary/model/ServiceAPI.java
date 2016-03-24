package alien95.cn.httplibrary.model;

import alien95.cn.http.request.rest.method.GET;
import alien95.cn.http.request.rest.method.POST;
import alien95.cn.http.request.rest.param.Field;
import alien95.cn.http.request.rest.param.Query;
import alien95.cn.httplibrary.model.bean.UserInfo;

/**
 * Created by linlongxin on 2016/3/23.
 */
public interface ServiceAPI {

    @GET("http://219.153.62.77/oracle_ykt0529.php")
    String getBaidu(@Query("UsrID") String userId,@Query("page") int page);

    @POST("/v1/users/login.php")
    UserInfo login(@Field("name")
               String name,
                   @Field("password")
               String password);
}
