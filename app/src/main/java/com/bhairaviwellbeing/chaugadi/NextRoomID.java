package com.bhairaviwellbeing.chaugadi;

public class NextRoomID {

    private String nextroomid;
    private boolean isroomopen;

    public NextRoomID() {
    }

    public NextRoomID(String nexrroomid,boolean isroomopen) {
        this.nextroomid = nexrroomid;
        this.isroomopen = isroomopen;
    }

    public String getNexrroomid() {
        return nextroomid;
    }

    public void setNexrroomid(String nexrroomid) {
        this.nextroomid = nexrroomid;
    }

    public boolean isIsroomopen() {
        return isroomopen;
    }

    public void setIsroomopen(boolean isroomopen) {
        this.isroomopen = isroomopen;
    }
}
