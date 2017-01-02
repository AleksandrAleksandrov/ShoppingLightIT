package aleksandrov.aleksandr.shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import aleksandrov.aleksandr.shopping.authorization.AuthorizationActivity;
import aleksandrov.aleksandr.shopping.list_of_goods.Good;
import aleksandrov.aleksandr.shopping.list_of_goods.ImageLoader;
import aleksandrov.aleksandr.shopping.list_of_reviews.ListOfReviewAdapter;
import aleksandrov.aleksandr.shopping.list_of_reviews.NonScrollableListView;
import aleksandrov.aleksandr.shopping.list_of_reviews.Review;
import aleksandrov.aleksandr.shopping.review.ReviewActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by aleksandr on 12/20/16.
 */

public class GoodDescriptionActivity extends NavigationViewActivity {
    private TextView titleView, textView;
    private ImageView imageView;
    private Button buttonWriteReview;
    private ImageLoader imageLoader;
    private Good good;
    private ListOfReviewAdapter listOfReviewAdapter;
    private NonScrollableListView listView;
    private SharedPreferences mSharedPreferences;
    private ArrayList<Review> mReviewArrayList = null;

    public static int RESULT_FROM_MY_REVIEW_ACTIVITY = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_description_activity);

        mSharedPreferences = getSharedPreferences(Res.SHARED_PREFERENCES, MODE_PRIVATE);

        ViewHolder viewHolder = new ViewHolder();
        imageLoader = new ImageLoader(GoodDescriptionActivity.this);
        listView = (NonScrollableListView) findViewById(R.id.review_list_view);

        titleView = (TextView) findViewById(R.id.good_title);
        textView = (TextView) findViewById(R.id.good_text);
        buttonWriteReview = (Button) findViewById(R.id.button_write_review);
        buttonWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSharedPreferences.getString(Res.PREF_USERNAME, "").equals("")) {
                    Intent intent = new Intent(GoodDescriptionActivity.this, ReviewActivity.class);
                    intent.putExtra(Res.ID, good.getId());
                    startActivityForResult(intent, RESULT_FROM_MY_REVIEW_ACTIVITY);
                } else {
                    startActivity(new Intent(GoodDescriptionActivity.this, AuthorizationActivity.class));
                    Toast.makeText(GoodDescriptionActivity.this, R.string.to_leave_review_need_authorization, Toast.LENGTH_LONG).show();
                }

            }
        });
        viewHolder.imageHolder = (ImageView) findViewById(R.id.good_image);


        imageView = viewHolder.imageHolder;

        good = getIntent().getParcelableExtra(Good.class.getCanonicalName());
        if (savedInstanceState == null) {
            getReviews(good.getId());
        }
        titleView.setText(good.getTitle());
        textView.setText(good.getText());
        imageLoader.DisplayImage(Res.PROTOCOL_SCHEME + Res.PICTURES_URL + good.getImageURL(), imageView);

    }

    private void fillData(final ArrayList<Review> reviewArrayList) {

        listOfReviewAdapter = new ListOfReviewAdapter(this, reviewArrayList);
        listView.setAdapter(listOfReviewAdapter);
    }

    private void getReviews(final int product_id) {
        Observable.create(new Observable.OnSubscribe<ArrayList<Review>>() {
            @Override
            public void call(final Subscriber<? super ArrayList<Review>> subscriber) {
                Communication communication = new Communication();
                ArrayList<Review> reviewsArrayList = communication.getReviews(product_id);
                subscriber.onNext(reviewsArrayList);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<Review>>() {
                    @Override
                    public void call(final ArrayList<Review> reviewArrayList) {
                        mReviewArrayList = reviewArrayList;
                        fillData(reviewArrayList);
                    }
                });
    }

    static class ViewHolder {
        public ImageView imageHolder;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        } else {
            if (data.getBooleanExtra(Res.SUCCESS, false)) {
                getReviews(good.getId());
            } else {
                return;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Review.class.getCanonicalName(), mReviewArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mReviewArrayList = savedInstanceState.getParcelableArrayList(Review.class.getCanonicalName());
        if (mReviewArrayList != null) {
            fillData(mReviewArrayList);
        }
    }
}
