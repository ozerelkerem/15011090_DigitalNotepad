package com.example.digitalnotepad;

import android.graphics.Color;

public enum NoteColor {
    RED(Color.RED),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE),
    WHITE(Color.WHITE);

    public int c;

    NoteColor(int c) {
        this.c = c;
    }


}
