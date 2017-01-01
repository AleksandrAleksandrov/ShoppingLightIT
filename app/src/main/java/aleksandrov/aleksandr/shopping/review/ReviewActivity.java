package aleksandrov.aleksandr.shopping.review;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import aleksandrov.aleksandr.shopping.Communication;
import aleksandrov.aleksandr.shopping.GoodDescriptionActivity;
import aleksandrov.aleksandr.shopping.NavigationViewActivity;
import aleksandrov.aleksandr.shopping.R;
import aleksandrov.aleksandr.shopping.Res;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by aleksandr on 12/31/16.
 */

public class ReviewActivity extends NavigationViewActivity {

    private EditText editTextReview;
    private Button buttonWriteReview;
    private RatingBar ratingBar;
    private int rating = 0;
    private int productId = -1;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        mSharedPreferences = getSharedPreferences(Res.SHARED_PREFERENCES, MODE_PRIVATE);
        productId = getIntent().getIntExtra(Res.ID, -1);

        ratingBar = (RatingBar) findViewById(R.id.ratingBarReview);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = (int) v;
            }
        });
        editTextReview = (EditText) findViewById(R.id.edit_text_review_text);
        buttonWriteReview = (Button) findViewById(R.id.button_send_review);
        buttonWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(final Subscriber<? super Boolean> subscriber) {
                        Communication communication = new Communication();
                        boolean success = communication.sendReview(productId, mSharedPreferences.getString(Res.PREF_TOKEN, ""), rating, editTextReview.getText().toString());
                        subscriber.onNext(success);
                        subscriber.onCompleted();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(final Boolean success) {
                                if (success) {
                                    Intent intent = new Intent();
                                    intent.putExtra(Res.SUCCESS, success);
                                    setResult(GoodDescriptionActivity.RESULT_FROM_MY_REVIEW_ACTIVITY, intent);
                                    finish();
                                } else {
                                    Toast.makeText(ReviewActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG);
                                }
                            }
                        });
            }
        });
    }
}
