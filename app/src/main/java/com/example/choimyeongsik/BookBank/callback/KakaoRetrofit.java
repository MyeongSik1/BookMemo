package com.example.choimyeongsik.BookBank.callback;

import com.example.choimyeongsik.BookBank.model.DocumentList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface KakaoRetrofit {
    // GET/POST/DELETE/PUT 메소드들을 인터페이스에 구현하여 사용할 수 있다.
    @Headers("Authorization: KakaoAK 3e82e4d9bea33046d8289b5e3eb53add")
    @GET("v3/search/book")
    // JSON Array를 리턴하므로 List<>가 되었다
    Call <DocumentList> getDocument(
            @Query("query") String title





    );


}




