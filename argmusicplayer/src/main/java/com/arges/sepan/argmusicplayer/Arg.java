package com.arges.sepan.argmusicplayer;

import android.view.View;
import android.view.ViewGroup;


class Arg {
    static void disableContent(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                Arg.disableContent((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }
    static void enableContent(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                Arg.disableContent((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }
}
