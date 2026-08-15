package com.example.fitnexa2;




public class UserModel {

    private String name;
    private String age;
    private String gender;
    private String email;
    private String phone;

    // Required by Firebase
    public UserModel() {
    }

    public UserModel(
            String name,
            String age,
            String gender,
            String email,
            String phone) {

        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}