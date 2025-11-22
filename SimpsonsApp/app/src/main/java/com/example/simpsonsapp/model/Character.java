package com.example.simpsonsapp.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Character implements Serializable {
    @SerializedName("id")
    private int id;
    
    @SerializedName("age")
    private Integer age;
    
    @SerializedName("birthdate")
    private String birthdate;
    
    @SerializedName("gender")
    private String gender;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("occupation")
    private String occupation;
    
    @SerializedName("portrait_path")
    private String portraitPath;
    
    @SerializedName("phrases")
    private List<String> phrases;
    
    @SerializedName("status")
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public List<String> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getFullImageUrl() {
        if (portraitPath != null && !portraitPath.isEmpty()) {
            return "https://thesimpsonsapi.com" + portraitPath;
        }
        return null;
    }
}

