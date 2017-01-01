package aleksandrov.aleksandr.shopping.list_of_goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import aleksandrov.aleksandr.shopping.R;
import aleksandrov.aleksandr.shopping.Res;

/**
 * Created by aleksandr on 12/21/16.
 */

public class ListOfGoodsAdapter extends BaseAdapter {

    private Context context;
    private Good good;
    private ImageView image;
    private static LayoutInflater layoutInflater;
    private ArrayList<Good> goodArrayList;
    private ImageLoader imageLoader;

    ListOfGoodsAdapter(Context context, ArrayList<Good> goodArrayList) {
        this.context = context;
        this.goodArrayList = goodArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return goodArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup viewGroup) {
        View view = contentView;
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_of_goods, viewGroup, false);
            holder = new ViewHolder();

            holder.title = (TextView) view.findViewById(R.id.item_of_goods_title);
            holder.imageView = (ImageView) view.findViewById(R.id.item_of_goods_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        good = getGood(position);
        holder.title.setText(good.getTitle());

        image = holder.imageView;

        //DisplayImage function from ImageLoader Class
        imageLoader.DisplayImage(Res.PROTOCOL_SCHEME + Res.PICTURES_URL + good.getImageURL(), image);



        return view;
    }

    static class ViewHolder {
        public TextView title;
        public ImageView imageView;
    }

    private Good getGood(int position){
        return ((Good) getItem(position));
    }
}
