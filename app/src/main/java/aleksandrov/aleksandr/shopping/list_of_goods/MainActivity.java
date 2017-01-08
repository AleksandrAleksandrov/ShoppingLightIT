package aleksandrov.aleksandr.shopping.list_of_goods;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import aleksandrov.aleksandr.shopping.Communication;
import aleksandrov.aleksandr.shopping.GoodDescriptionActivity;
import aleksandrov.aleksandr.shopping.NavigationViewActivity;
import aleksandrov.aleksandr.shopping.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends NavigationViewActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListOfGoodsAdapter listOfGoodsAdapter;
    private SharedPreferences sharedPreferences;
    private TextView textViewNavigationName;
    private ArrayList<Good> mGoodArrayList = null;
    private static final String MY_GOOD_ARRAY_LIST_TAG = "myGoodArrayList";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (savedInstanceState == null) {
            getProducts();
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    private void fillData(final ArrayList<Good> goodArrayList) {

        listOfGoodsAdapter = new ListOfGoodsAdapter(this, goodArrayList);

        ListView listView = (ListView) findViewById(R.id.goods_list_view);
        listView.setAdapter(listOfGoodsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, GoodDescriptionActivity.class);
                intent.putExtra(Good.class.getCanonicalName(), goodArrayList.get(i));
                startActivity(intent);
            }
        });
    }

    private void getProducts() {
        Observable.create(new Observable.OnSubscribe<ArrayList<Good>>() {
            @Override
            public void call(final Subscriber<? super ArrayList<Good>> subscriber) {
                Communication communication = new Communication();
                ArrayList<Good> goodArrayList = communication.getProducts();
                subscriber.onNext(goodArrayList); // Emit the contents of the URL
                subscriber.onCompleted(); // Nothing more to emit
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<Good>>() {
                    @Override
                    public void call(final ArrayList<Good> goodArrayList) {
                        mGoodArrayList = goodArrayList;
                        fillData(goodArrayList);
                    }
                });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return super.onNavigationItemSelected(item);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
            return rootView;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MY_GOOD_ARRAY_LIST_TAG, mGoodArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGoodArrayList = savedInstanceState.getParcelableArrayList(MY_GOOD_ARRAY_LIST_TAG);
        if (mGoodArrayList != null) {
            fillData(mGoodArrayList);
        }
    }
}
