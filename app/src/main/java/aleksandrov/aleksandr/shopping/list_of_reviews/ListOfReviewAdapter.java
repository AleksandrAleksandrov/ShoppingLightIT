package aleksandrov.aleksandr.shopping.list_of_reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import aleksandrov.aleksandr.shopping.R;

/**
 * Created by aleksandr on 12/25/16.
 */

public class ListOfReviewAdapter extends BaseAdapter {

    private static LayoutInflater layoutInflater;
    private ArrayList<Review> reviewArrayList;
    private TextView nameTextView, dateTextView, reviewBodyTextView;
    private RatingBar mRatingBar;

    public ListOfReviewAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return reviewArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        View view = contentView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_of_review, viewGroup, false);

            nameTextView = (TextView) view.findViewById(R.id.name_of_reviewer);
            dateTextView = (TextView) view.findViewById(R.id.date_of_review);
            mRatingBar = (RatingBar) view.findViewById(R.id.rate_of_good);
            reviewBodyTextView = (TextView) view.findViewById(R.id.body_of_review);




        }
        Review review = (Review) getItem(position);
        if (review.getFirst_name() != null && review.getLast_name() != null) {
            nameTextView.setText(review.getFirst_name() + review.getLast_name());
        } else {
            nameTextView.setText(review.getUser_name());
        }


            dateTextView.setText(review.getDate());
            mRatingBar.setRating(review.getRate());
            reviewBodyTextView.setText(review.getText());



        return view;
    }
}
