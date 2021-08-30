package com.kemp.commonlib.net;

import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by jiaojian on 2016/11/23.
 */
public class NetInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(request != null && request.url() != null) {
            Log.v("NetInterceptor", "url : " + request.url().toString());
        }
        Response response = chain.proceed(request);
        long tookSec = System.currentTimeMillis();
        handleException(request, response, tookSec);
//        if (LibBundle.Companion.isDebug()) {
//            logForRequest(request);
//            return logForResponse(response);
//        }
        return response;
    }

    private void handleException(Request request, Response response, long tookSec) {
        int code = response.code();
//        DebugLog.i("responseCode:" + code);
//        if (code >= AppConstants.NET_EXCEPITON_CODE) {
//            StatisticsException exception = new StatisticsException();
//            exception.ClientVisitTime = tookSec;
//            exception.ResponseCode = code + "";
//            exception.url = request.url().toString();
//            DebugLog.i("Exception:" + exception.toString());
//            LocalStatisticsExceptionDao.getInstance().insert(exception);
//        }
    }

    private Response logForResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
//        DebugLog.i("========response'log=======");
//        if (contentType != null) {
//            try {
//                charset = contentType.charset(UTF8);
//                if (isPlaintext(buffer) && contentLength != 0) {
//                    DebugLog.i(buffer.clone().readString(charset));
//                }
//                DebugLog.i("========response'log=======end");
//            } catch (UnsupportedCharsetException e) {
//                DebugLog.i("Couldn't decode the response body; charset is likely malformed.");
//                DebugLog.i("========response'log=======end");
//                return response;
//            }
//        }
        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                if (Character.isISOControl(prefix.readUtf8CodePoint())) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

//            DebugLog.i("========preRequest'log=======");
//            DebugLog.i("method : " + request.method());
//            DebugLog.i("url : " + url);
//            if (headers != null && headers.size() > 0) {
//                DebugLog.i("headers : " + headers.toString());
//            }
//            RequestBody requestBody = request.body();
//            if (requestBody != null) {
//                MediaType mediaType = requestBody.contentType();
//                if (mediaType != null) {
//                    DebugLog.i("requestBody's contentType : " + mediaType.toString());
//                    if (isText(mediaType)) {
//                        DebugLog.i("requestBody's content : " + bodyToString(request));
//                    } else {
//                        DebugLog.i("requestBody's content : " + " maybe [file part] , too large too print , ignored!");
//                    }
//                }
//            }
//            DebugLog.i("========preRequest'log=======end");
        } catch (Exception e) {
            //            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")) {
                return true;
            }
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}

