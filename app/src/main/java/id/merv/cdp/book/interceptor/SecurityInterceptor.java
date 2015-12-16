package id.merv.cdp.book.interceptor;

import id.merv.cdp.book.entity.Authentication;
import id.merv.cdp.book.util.AuthenticationUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by akm on 06/11/15.
 */
public class SecurityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Authentication auth = AuthenticationUtils.getCurrentAuthentication();

        if (auth != null) {
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer" + auth.getAccesToken())
                    .build();
        }

        return chain.proceed(request);
    }
}
