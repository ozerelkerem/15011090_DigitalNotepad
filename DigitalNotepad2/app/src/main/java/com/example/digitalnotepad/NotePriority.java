package com.example.digitalnotepad;

public enum NotePriority {
        HIGH(0),
        NORMAL(1),
        LOW(2);

    public int i;

    NotePriority(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        switch (i)
        {
            case 0:
                return App.getContext().getString(R.string.high);
            case 2:
                return App.getContext().getString(R.string.low);
            default:
                return App.getContext().getString(R.string.normal);
        }


    }
}
