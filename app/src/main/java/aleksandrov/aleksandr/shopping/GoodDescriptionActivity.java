package aleksandrov.aleksandr.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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

    public static int RESULT_FROM_MY_REVIEW_ACTIVITY = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_description_activity);

        ViewHolder viewHolder = new ViewHolder();
        imageLoader = new ImageLoader(GoodDescriptionActivity.this);
        listView = (NonScrollableListView) findViewById(R.id.review_list_view);

        titleView = (TextView) findViewById(R.id.good_title);
        textView = (TextView) findViewById(R.id.good_text);
        buttonWriteReview = (Button) findViewById(R.id.button_write_review);
        buttonWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodDescriptionActivity.this, ReviewActivity.class);
                intent.putExtra(Res.ID, good.getId());
                startActivityForResult(intent, RESULT_FROM_MY_REVIEW_ACTIVITY);
            }
        });
        viewHolder.imageHolder = (ImageView) findViewById(R.id.good_image);


        imageView = viewHolder.imageHolder;

        good = getIntent().getParcelableExtra(Good.class.getCanonicalName());
        getReviews(good.getId());
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
}
