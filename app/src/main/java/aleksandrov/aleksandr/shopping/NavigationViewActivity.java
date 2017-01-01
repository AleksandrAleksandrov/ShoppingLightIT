package aleksandrov.aleksandr.shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import aleksandrov.aleksandr.shopping.authorization.AuthorizationActivity;
import aleksandrov.aleksandr.shopping.my_account.MyAccountActivity;

/**
 * Created by aleksandr on 12/20/16.
 */

public class NavigationViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private TextView textViewNavigationName;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {





        DrawerLayout drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer_layout, null);
        FrameLayout activityContainer = (FrameLayout) drawer.findViewById(R.id.activity_content);

        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(NavigationViewActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        sharedPreferences = getSharedPreferences(Res.SHARED_PREFERENCES, MODE_PRIVATE);
        textViewNavigationName = (TextView) findViewById(R.id.nav_name_textview);
        textViewNavigationName.setText(sharedPreferences.getString(Res.PREF_USERNAME, ""));

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_auth:
                if (!sharedPreferences.getString(Res.PREF_USERNAME, "").equals("")) {
                    startActivity(new Intent(this, MyAccountActivity.class));
                } else {
                    startActivity(new Intent(this, AuthorizationActivity.class));
                }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textViewNavigationName != null) {
            textViewNavigationName.setText(sharedPreferences.getString(Res.PREF_USERNAME, ""));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
