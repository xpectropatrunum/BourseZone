package ir.sourcearena.boursezone.ui.news;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import ir.sourcearena.boursezone.R;

public class ListAdapter extends ArrayAdapter<Utils> {
    private final Context context;
    private final  List<Utils> values;
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    public ListAdapter(Context context, List<Utils> values) {
        super(context,R.layout.news_single,values);
        this.context = context;
        this.values = values;
        sp = context.getSharedPreferences("news", Context.MODE_PRIVATE);
        ed = sp.edit();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.news_single, parent, false);

        Utils pu = values.get(position);




        TextView title = (TextView) rowView.findViewById(R.id.news_title_);
        TextView date = (TextView) rowView.findViewById(R.id.news_date);
        ImageView thm = rowView.findViewById(R.id.news_thumbmail);

        title.setText(pu.getItem(0));
        date.setText(pu.getItem(3));

        if(position == 0) {
            ed.putString("last", pu.getItem(0));
            ed.apply();
        }



        if(pu.getBmap() == null){
            newThread(thm,pu.getItem(2),pu);
        }else {
            Bitmap final_ = Bitmap.createScaledBitmap(pu.getBmap(), 120, 120, false);
            Drawable d = new BitmapDrawable(context.getResources(), final_);
            thm.setImageDrawable(d);
        }



        return rowView;
    }
    void newThread(final ImageView im, final String link,final Utils p) {


        HandlerThread handlerThread = new HandlerThread("TesHandlerThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {


            @Override
            public void run() {

                Bitmap image = null;
                try {
                    URL url = new URL(link);

                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                } catch (IOException e) {
                    System.out.println(e);
                }

                final Bitmap finalImage = image;
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if(finalImage != null){
                            Bitmap final_ = Bitmap.createScaledBitmap(finalImage, 120, 120, false);

                            Drawable d = new BitmapDrawable(context.getResources(), final_);
                            im.setImageDrawable(d);
                            p.setBmap(final_);
                        }

                    }
                });







            }
        });


    }
}