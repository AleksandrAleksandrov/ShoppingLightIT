package aleksandrov.aleksandr.shopping.list_of_goods;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

//    private ArrayList<Good> goodStringArray = new ArrayList<>();
    private ListOfGoodsAdapter listOfGoodsAdapter;
    private SharedPreferences sharedPreferences;
    private TextView textViewNavigationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getProducts();
        if (savedInstanceState == null) {
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
}
