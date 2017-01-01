package aleksandrov.aleksandr.shopping.list_of_reviews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aleksandr on 12/25/16.
 */

public class Review {
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
}
