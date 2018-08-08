package com.applications.coffee.letstalkrussian_free;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.view.MenuItem;
import android.widget.Toast;


public class SettingsActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String pn = getPackageName();
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


        findPreference("pref_rate").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + pn)));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + pn)));
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
