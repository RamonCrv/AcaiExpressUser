package com.example.gs.myapplication;

/**
 * Created by User on 3/14/2017.
 */

public class Person {
     String Name;
     String Birthday;
     String Sex;
     String Naosei0;



    public Person(String name, String birthday, String sex,String naosei0) {
        this.Birthday = birthday;
        this.Name = name;
        this.Sex = sex;
        this.Naosei0 = naosei0;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        this.Birthday = birthday;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        this.Sex = sex;
    }
    public String getNaosei0() {
        return Naosei0;
    }

    public void setNaosei0(String naosei0) {
        Naosei0 = naosei0;
    }
}
