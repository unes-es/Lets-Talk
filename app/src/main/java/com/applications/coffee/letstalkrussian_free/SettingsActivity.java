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
            findPreference("pref_delete_favorites").setSummary(getResources().getString(R.string.favorite_count,""+favCount));
        }
        else{
            findPreference("pref_delete_favorites").setSummary(getResources().getString(R.string.no_favorites));
        }
        findPreference("pref_delete_favorites").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (favCount>0) {
                    Data.Manager.clearFavorites();
                    findPreference("pref_delete_favorites").setSummary(getResources().getString(R.string.no_favorites));
                    Toast.makeText(SettingsActivity.this, getResources().getString(R.string.favorite_cleared), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SettingsActivity.this, getResources().getString(R.string.no_favorites), Toast.LENGTH_SHORT).show();
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
        findPreference("pref_recommend").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                final String pn = getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //TODO: translate hard coded strings
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I learned Russian with this app try it:http://play.google.com/store/apps/details?id=" + pn);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
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
