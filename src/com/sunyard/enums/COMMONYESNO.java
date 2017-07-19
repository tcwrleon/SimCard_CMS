package com.sunyard.enums;
public enum COMMONYESNO {

    YES(0), NO(1);

    private int code;

    private COMMONYESNO(int i) {
        code = i;
    }

    public int getCode() {
        return code;
    }


}