package aleksandrov.aleksandr.shopping.list_of_goods;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aleksandr on 12/21/16.
 */

public class Good implements Parcelable {
    private int id;
    private String title;
    private String imageURL;
    private String text;
    private Image image;

    public Good(int id, String title, String imageURL, String text) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public Image getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Упаковываем объект в Parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(imageURL);
        parcel.writeString(text);

    }

    public static final Parcelable.Creator<Good> CREATOR = new Parcelable.Creator<Good>() {

        // Распаковываем объект из Parcel
        @Override
        public Good createFromParcel(Parcel parcel) {
            return new Good(parcel);
        }

        @Override
        public Good[] newArray(int i) {
            return new Good[0];
        }

    };
    // Конструктор, считывающий данные из Parcel
    private Good(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        imageURL = parcel.readString();
        text = parcel.readString();
    }
}
