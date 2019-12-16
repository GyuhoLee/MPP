package com.example.gradenotifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.util.Log;

public class SecondActivity extends PreferenceActivity {
    String beforePreference = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        ListPreference LastGrade = (ListPreference) findPreference("lastGrade");
        beforePreference = "" + LastGrade.getValue();

        setOnPreferenceChange(findPreference("lastGrade"));
        //setOnPreferenceChange(findPreference("useUpdateNofiti"));
        //setOnPreferenceChange(findPreference("updateTime"));
        //setOnPreferenceChange(findPreference("changeGrade"));
    }
    private void setOnPreferenceChange(Preference mPreference) {
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);

        onPreferenceChangeListener.onPreferenceChange(
                mPreference,
                PreferenceManager.getDefaultSharedPreferences(
                        mPreference.getContext()).getString(
                        mPreference.getKey(), ""));
    }

    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof SwitchPreference) {
                preference.setDefaultValue(stringValue);

            } else if (preference instanceof ListPreference) {
                /**
                 * ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를
                 * 적용하지 못한다 따라서 설정한 entries에서 String을 로딩하여 적용한다
                 */
                if (preference.getKey().equals("lastGrade")) {
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);
                    Log.d(stringValue, Integer.toString(index));
                    if (!beforePreference.equals(stringValue)) {
                        Intent intent = new Intent(getApplicationContext(), LastGradeActivity.class);
                        startActivity(intent);
                    }
                    beforePreference = stringValue;
                }
            }
            return true;
        }

    };
}

