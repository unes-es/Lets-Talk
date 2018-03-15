package com.example.essabri.myapplication;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


public class SettingsActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActionBar() != null) {
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        addPreferencesFromResource(R.xml.preferences);

        final int favCount = Data.Manager.getFavorites().size();
        if(favCount>0) {
            findPreference("pref_delete_favorites").setSummary("You have " + favCount + " favorite phrases");
        }
        else{
            findPreference("pref_delete_favorites").setSummary("You have no favorite phrases");
        }
        findPreference("pref_delete_favorites").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (favCount>0) {
                    Data.Manager.clearFavorites();
                    findPreference("pref_delete_favorites").setSummary("You have no favorite phrases");
                    Toast.makeText(SettingsActivity.this, "Favorite cleared", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SettingsActivity.this, "You have no favorite phrases", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                /*NavUtils.navigateUpFromSameTask(this);*/
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
