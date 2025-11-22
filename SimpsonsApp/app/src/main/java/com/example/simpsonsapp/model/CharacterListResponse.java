package com.example.simpsonsapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CharacterListResponse {
    @SerializedName("results")
    private List<Character> results;
    
    @SerializedName("count")
    private Integer count;
    
    @SerializedName("next")
    private String next;
    
    @SerializedName("prev")
    private String prev;
    
    @SerializedName("pages")
    private Integer pages;

    public List<Character> getResults() {
        return results;
    }

    public void setResults(List<Character> results) {
        this.results = results;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
    
    // Método de compatibilidad para obtener los datos
    public List<Character> getData() {
        return results;
    }
}

