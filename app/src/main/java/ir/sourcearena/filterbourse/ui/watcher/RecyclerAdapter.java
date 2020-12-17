package ir.sourcearena.filterbourse.ui.watcher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.Settings;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Utils> utils,old;
    Settings setting;
    OnItemClickListener listener;
    OnItemLongClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(Utils item);
    }
    public interface OnItemLongClickListener {



        void onItemLongClick(Utils item);
    }
    public RecyclerAdapter(Context context,  List old,List utils, OnItemClickListener listener, OnItemLongClickListener mlistener) {
        this.context = context;
        this.old = old;

        this.utils = utils;
        this.mlistener = mlistener;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_favorite_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    Utils pu,ol;
    int a,b = 0;
    protected AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
    protected AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        setting = new Settings();
        holder.itemView.setTag(utils.get(position));

         pu = utils.get(position);
        holder.name.setText(pu.getName());
        holder.full_name.setText(pu.getFName());
        holder.state.setText(pu.getState());






        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);

        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);
        if(old != null) {
            try {
                ol = old.get(position);

                if (!ol.getPrice().equals(pu.getPrice())) {
                    b = 0 ;
                    if(Integer.parseInt(ol.getPrice().replace(",","")) > Integer.parseInt(pu.getPrice().replace(",",""))){

                        int colorFrom = Color.parseColor("#0000ff00");
                        int colorTo =  Color.parseColor("#50ff0000");
                        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000);
                        colorAnimation.setRepeatCount(0);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                    holder.price.setBackgroundColor((int) animator.getAnimatedValue());



                            }


                        });

                        colorAnimation.addListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                holder.price.setBackgroundColor(Color.parseColor("#00000000"));
                            }
                        });
                        colorAnimation.start();
                    } else if(Integer.parseInt(ol.getPrice().replace(",","")) < Integer.parseInt(pu.getPrice().replace(",",""))){
                        int colorFrom = Color.parseColor("#0000ff00");
                        int colorTo =  Color.parseColor("#5000ff00");
                        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000);
                        colorAnimation.setRepeatCount(0);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {

                                    holder.price.setBackgroundColor((int) animator.getAnimatedValue());


                            }


                        });

                        colorAnimation.addListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {

                                holder.price.setBackgroundColor(Color.parseColor("#00000000"));
                            }
                        });
                        colorAnimation.start();



                    }

                }

                if (!ol.getPercent().equals(pu.getPercent())) {
                    if(Float.parseFloat(ol.getPercent().replace("%","")) > Float.parseFloat(pu.getPercent().replace("%",""))){

                        int colorFrom = Color.parseColor("#0000ff00");
                        int colorTo =  Color.parseColor("#50ff0000");
                        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000);
                        colorAnimation.setRepeatCount(0);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                holder.percent.setBackgroundColor((int) animator.getAnimatedValue());

                            }


                        });

                        colorAnimation.addListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                holder.percent.setBackgroundColor(Color.parseColor("#00000000"));
                            }
                        });
                        colorAnimation.start();
                    } else if(Float.parseFloat(ol.getPercent().replace("%","")) < Float.parseFloat(pu.getPercent().replace("%",""))){
                        int colorFrom = Color.parseColor("#0000ff00");
                        int colorTo =  Color.parseColor("#5000ff00");
                        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000);
                        colorAnimation.setRepeatCount(0);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                holder.percent.setBackgroundColor((int) animator.getAnimatedValue());

                            }


                        });

                        colorAnimation.addListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                holder.percent.setBackgroundColor(Color.parseColor("#00000000"));
                            }
                        });
                        colorAnimation.start();


                        //  holder.price.setBackgroundColor(Color.parseColor("#5000ff00"));
                    }

                }
            }catch (IndexOutOfBoundsException e){

            }
        }
        holder.price.setText(pu.getPrice());

        holder.percent.setText(pu.getPercent());

        checkPercent(holder.percent);
        checkColor(holder.state);


        holder.bind(pu, listener);

    }

    private void checkColor(TextView tv) {
        if((tv.getText().toString()).contains("مجاز")){
            tv.setTextColor(context.getResources().getColor(R.color.green));
        }else if((tv.getText().toString()).contains("ممنوع")){
            tv.setTextColor(context.getResources().getColor(R.color.red));
        }else{
            tv.setTextColor(context.getResources().getColor(R.color.text2));
        }
    }

    private void checkPercent(TextView tv)  {
        String text = tv.getText().toString();
        text = PersianToEnglish(text);

        if(!text.contains("-")){

                tv.setTextColor(context.getResources().getColor(R.color.green));
            }else{
                tv.setTextColor(context.getResources().getColor(R.color.red));

        }

    }
    public static String PersianToEnglish(String persianStr)  {
        String answer = persianStr;
        answer = answer.replace("۱","1");
        answer = answer.replace("۲","2");
        answer = answer.replace("۳","3");
        answer = answer.replace("۴","4");
        answer = answer.replace("۵","5");
        answer = answer.replace("۶","6");
        answer = answer.replace("۷","7");
        answer = answer.replace("۸","8");
        answer = answer.replace("۹","9");
        answer = answer.replace("۰","0");
        return answer;
    }
    @Override
    public int getItemCount() {
        return utils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView full_name;
        public TextView name;
        public TextView state;
        public TextView price;

        public TextView percent;


        public ViewHolder(View itemView) {
            super(itemView);

            full_name = (TextView) itemView.findViewById(R.id.favorite_fullname);
            name = (TextView) itemView.findViewById(R.id.favorite_name);
            state = (TextView) itemView.findViewById(R.id.favorite_state);
            price = (TextView) itemView.findViewById(R.id.favorite_price);

            percent = (TextView) itemView.findViewById(R.id.favorite_percent);








        }

        public void bind(final Utils item, final OnItemClickListener listener) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mlistener.onItemLongClick(item);
                    return true;
                }


            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


}