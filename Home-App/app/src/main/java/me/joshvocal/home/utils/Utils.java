package me.joshvocal.home.utils;

import android.content.Context;
import android.support.v7.app.ActionBar;

/**
 * Created by josh on 1/2/18.
 */

public class Utils {

    public static void setBackButton(ActionBar actionBar) {

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
