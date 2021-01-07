package kawa.ttehaute;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by NextU on 03/02/2018.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
