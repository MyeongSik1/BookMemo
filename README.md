#  독서노트 어플 프로젝트


##  개발언어
   JAVA
##  API
 - Google Drive 
 - MPAndroid
 - PhotView
 - Glide
 - 카카오 도서 검색 
 - 도서관 정보나루

### 프로젝트 기능
 본 프로젝트의 초기에 구상한 기능은 책을 검색하는 API를 이용해서 
 책을 검색하고 저장할수있게 하려했지만 점점 만들어 갈수록
 욕심이생겨서 비슷한 어플들을 다운받아서 몇몇 기능들을 추가하게 되었습니다.

#### 초기 기능
 - 책을 검색하여 SQLite DataBase에 저장한다.
 - 저장된 데이터를 RecyclerView에 표시한다.
 - RecycleView를 클릭해 메모를 할수있게한다.
 
#### 추가 기능
 - 온라인베스트셀러(Parsing)와 도서관베스트셀러(API)를 RecyclerView에 표시한다
 - 책 RecyclerView를 클릭했을때 해당 ISBN(도서번호)를 활용해 Parsing 하고 TextView에 표시하게함
 - 카테고리 데이터도 저장해 서재에 저장한책들을 분류하고  MPAdnroid를 활용해 표시


# 1. Build Gradle에 추가한다
       //카카오API 관련
       implementation group: project.KAKAO_SDK_GROUP, name: 'usermgmt', version: project.KAKAO_SDK_VERSION
       //카드뷰 관련
       implementation "androidx.cardview:cardview:1.0.0"
       //리사이클러뷰 관련
       implementation "androidx.recyclerview:recyclerview:1.1.0"
       implementation "androidx.recyclerview:recyclerview-selection:1.1.0-rc01"
       implementation 'com.google.android.material:material:1.1.0'

       //깃허브 관련
       implementation 'com.github.bumptech.glide:glide:4.9.0'
       implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
       implementation 'com.github.chrisbanes:PhotoView:2.1.4'
       // Json 관련
       implementation 'com.fasterxml.jackson.core:jackson-core:2.9.7'
       implementation 'com.fasterxml.jackson.core:jackson-annotations:2.9.7'
       implementation 'com.fasterxml.jackson.core:jackson-databind:2.9.7'
       
       
       
# 2. Android에 권한을 추가한다.

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.CAMERA" />
    
    
# 3. manifests에 추가한다

        <meta-data   // 카카오 API
            android:name="com.kakao.sdk.AppKey"
            android:value="@string/kakao_app_Key" />

        <meta-data // Google Drive
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />
        <meta-data // 광고
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="@string/admob_app_id"/>
            
            
        
  카카오 API를 사용하기위해선 카카오개발자 사이트에서 키를 발급받아야합니다
   https://developers.kakao.com/
        
   구글드라이브 API를 사용하려면 프로젝트를 생성해야합니다
    https://console.developers.google.com/flows/enableapi?apiid=drive&pli=1
    
    
       <provider // 파일관련
            android:name="androidx.core.content.FileProvider"
            android:authorities="package name"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider" />
        </provider>
        
   Android 7.0 이상부터 파일공유 정책이 변경되어서 
   파일의 공유 uri는 file:// ~ 이 아닌 content://로 시작되어야하며, 접근권한을 줘야하며
   안드로이드에서는 FileProvider사용을 권장하고 있습니다.
   
 # 4. 데이터베이스
 
 데이터베이스는 SQLite를 사용합니다
 
        String bookSQL = "create table tb_book" +
                "(_id integer PRIMARY KEY autoincrement," +
                "title," +
                "pirce," +
                "authors," +
                "publisher," +
                "category," +
                "thumbnail," +
                "isbn," +
                "name," +
                "image," + "BLOB," +
                "contents)";
        
        
 title | pirce | authors | publisher | thumbnail | isbn | name | image | contents
 ----- | ----- |  ----- | ----- |  ----- | ----- |  ----- | ----- |  ----- |
 책제목 | 책가격 | 글쓴이 | 출판사 | 책이미지 | 국제표준도서번호 | 메모 | 메모이미지 | 메모내용
 
 
 # 5. 실행
 ![20210214_154720](https://user-images.githubusercontent.com/78843235/107870518-661d7880-6edc-11eb-808b-32878dd4335c.gif)
 
        
        
        
        
        
