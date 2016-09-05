package com.roman.iweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Oleksii Shliama.
 */
public class BaseRefreshFragment extends Fragment {

    public static final int REFRESH_DELAY = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
