package com.felixwhitesean.classcommapp;

public class TimetableModelClass {
    private String d_o_w, lesson_1, lesson_2, lesson_3, lesson_4, lesson_5, session1_room, session2_room, session3_room, session4_room, session5_room;

    public TimetableModelClass(String d_o_w, String lesson_1,String session1_room, String lesson_2, String session2_room,String lesson_3, String session3_room,String lesson_4, String session4_room, String lesson_5, String session5_room) {
        this.d_o_w = d_o_w;
        this.lesson_1 = lesson_1;
        this.lesson_2 = lesson_2;
        this.lesson_3 = lesson_3;
        this.lesson_4 = lesson_4;
        this.lesson_5 = lesson_5;
        this.session1_room = session1_room;
        this.session2_room = session2_room;
        this.session3_room = session3_room;
        this.session4_room = session4_room;
        this.session5_room = session5_room;
    }

    public String getD_o_w() {
        return d_o_w;
    }

    public void setD_o_w(String d_o_w) {
        this.d_o_w = d_o_w;
    }

    public String getLesson_1() {
        return lesson_1;
    }

    public void setLesson_1(String lesson_1) {
        this.lesson_1 = lesson_1;
    }

    public String getLesson_2() {
        return lesson_2;
    }

    public void setLesson_2(String lesson_2) {
        this.lesson_2 = lesson_2;
    }

    public String getLesson_3() {
        return lesson_3;
    }

    public void setLesson_3(String lesson_3) {
        this.lesson_3 = lesson_3;
    }

    public String getLesson_4() {
        return lesson_4;
    }

    public void setLesson_4(String lesson_4) {
        this.lesson_4 = lesson_4;
    }

    public String getLesson_5() {
        return lesson_5;
    }

    public void setLesson_5(String lesson_5) {
        this.lesson_5 = lesson_5;
    }

    public String getSession1_room() {
        return session1_room;
    }

    public void setSession1_room(String session1_room) {
        this.session1_room = session1_room;
    }

    public String getSession2_room() {
        return session2_room;
    }

    public void setSession2_room(String session2_room) {
        this.session2_room = session2_room;
    }

    public String getSession3_room() {
        return session3_room;
    }

    public void setSession3_room(String session3_room) {
        this.session3_room = session3_room;
    }

    public String getSession4_room() {
        return session4_room;
    }

    public void setSession4_room(String session4_room) {
        this.session4_room = session4_room;
    }

    public String getSession5_room() {
        return session5_room;
    }

    public void setSession5_room(String session5_room) {
        this.session5_room = session5_room;
    }

    @Override
    public String toString() {
        return "TimetableModelClass{" +
                "d_o_w='" + d_o_w + '\'' +
                ", lesson_1='" + lesson_1 + '\'' +
                ", lesson_2='" + lesson_2 + '\'' +
                ", lesson_3='" + lesson_3 + '\'' +
                ", lesson_4='" + lesson_4 + '\'' +
                ", lesson_5='" + lesson_5 + '\'' +
                ", session1_room='" + session1_room + '\'' +
                ", session2_room='" + session2_room + '\'' +
                ", session3_room='" + session3_room + '\'' +
                ", session4_room='" + session4_room + '\'' +
                ", session5_room='" + session5_room + '\'' +
                '}';
    }
}
