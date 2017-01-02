package aleksandrov.aleksandr.shopping.list_of_reviews;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aleksandr on 12/25/16.
 */

public class Review implements Parcelable {
    private int comment_id;
    private int product_id;
    private int rate;
    private String text;

    private int id_of_user;
    private String user_name;
    private String first_name;
    private String last_name;
    private String email;

    private String mDate;
    private SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM yyyy");

    public Review(int comment_id, int product_id, int rate, String text, int id_of_user, String user_name, String email, String timestamp) {
        this.comment_id = comment_id;
        this.product_id = product_id;
        this.rate = rate;
        this.text = text;
        this.id_of_user = id_of_user;
        this.user_name = user_name;
        this.email = email;
        try {

            Date date = serverFormat.parse(timestamp.replaceAll("Z$", "+0000"));
            this.mDate = myFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public int getId_of_user() {
        return id_of_user;
    }

    public void setId_of_user(int id_of_user) {
        this.id_of_user = id_of_user;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Упаковываем объект в Parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {

//        private SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        private SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM yyyy");
        parcel.writeInt(comment_id);
        parcel.writeInt(product_id);
        parcel.writeInt(rate);
        parcel.writeString(text);

        parcel.writeInt(id_of_user);
        parcel.writeString(user_name);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(email);

        parcel.writeString(mDate);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {

        // Распаковываем объект из Parcel
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[0];
        }

    };
    // Конструктор, считывающий данные из Parcel
    private Review(Parcel parcel) {
        comment_id = parcel.readInt();
        product_id = parcel.readInt();
        rate = parcel.readInt();
        text = parcel.readString();
        id_of_user = parcel.readInt();

        user_name = parcel.readString();
        first_name = parcel.readString();
        last_name = parcel.readString();
        email = parcel.readString();
        mDate = parcel.readString();
    }
}
