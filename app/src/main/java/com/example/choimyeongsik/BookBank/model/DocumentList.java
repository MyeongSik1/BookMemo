package com.example.choimyeongsik.BookBank.model;
//
import com.example.choimyeongsik.BookBank.model.Document;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DocumentList {
    @SerializedName("documents")
    public ArrayList<Document> documents = new ArrayList <>();

}
