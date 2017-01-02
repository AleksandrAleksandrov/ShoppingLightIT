package aleksandrov.aleksandr.shopping.my_account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import aleksandrov.aleksandr.shopping.NavigationViewActivity;
import aleksandrov.aleksandr.shopping.R;
import aleksandrov.aleksandr.shopping.Res;

/**
 * Created by aleksandr on 12/31/16.
 */

public class MyAccountActivity extends NavigationViewActivity {

    private SharedPreferences sharedPreferences;
    private TextView textViewName;
    private Button buttonLogOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_activity);

        getSupportActionBar().setTitle(R.string.personal_area);

        sharedPreferences = getSharedPreferences(Res.SHARED_PREFERENCES, MODE_PRIVATE);
        textViewName = (TextView) findViewById(R.id.text_view_my_account_activity);
        textViewName.setText(sharedPreferences.getString(Res.PREF_USERNAME, ""));
        buttonLogOut = (Button) findViewById(R.id.button_log_out_my_account_activity);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
            }
        });
    }
}
