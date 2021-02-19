package ir.sourcearena.boursezone;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughCard;

import java.util.ArrayList;
import java.util.List;

import ir.sourcearena.boursezone.Account.Login;

public class TourtoApp extends FancyWalkthroughActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;



        FancyWalkthroughCard fancywalkthroughCard1 = new FancyWalkthroughCard("به بورس زون خوش اومدی!", "بازارهای مالی بدون توجه به روند سودآوری یا زیاندهی که دارن، یه سری الگوهای ثابت داره که ...",R.drawable.undraw_all_the_data_re_hh4w);
        FancyWalkthroughCard fancywalkthroughCard2 = new FancyWalkthroughCard("", "اگه بتونی این الگوها رو پیدا کنی و سوارشون بشی حتی تو منفی های بازار هم میتونی سود کلان ببری... اما چجوری؟ تو این اپ علاوه بر اطلاعات بازار و نماد ها ...",R.drawable.undraw_wallet_aym5);
        FancyWalkthroughCard fancywalkthroughCard3 = new FancyWalkthroughCard("", "کلی فیلتر هست که باهاش میتونی بازار رو آنالیز کنی. تازه با فیلتریاب میتونی الگوریتم خودت رو تعریف کنی و همیشه به روز باشی..",R.drawable.undraw_predictive_analytics_kf9n);

        fancywalkthroughCard1.setBackgroundColor(R.color.primary);
        fancywalkthroughCard1.setIconLayoutParams(width*9 / 10,height/2,0,10,0,10);
        fancywalkthroughCard2.setBackgroundColor(R.color.primary);
        fancywalkthroughCard2.setIconLayoutParams(width*9 / 10,height/2,0,10,0,10);
        fancywalkthroughCard3.setBackgroundColor(R.color.primary);
        fancywalkthroughCard3.setIconLayoutParams(width*9 / 10,height/2,0,10,0,10);

        List<FancyWalkthroughCard> pages = new ArrayList<>();

        pages.add(fancywalkthroughCard1);
        pages.add(fancywalkthroughCard2);
        pages.add(fancywalkthroughCard3);


        for (FancyWalkthroughCard page : pages) {
            page.setTitleColor(R.color.pureWhite);


            page.setDescriptionColor(R.color.pureWhite);
        }
        setFinishButtonTitle("حالا وقت شروعه!");

        setFont(Typeface.createFromAsset(getAssets(),"iranyekanbold.ttf"));

        setColorBackground(R.color.pureWhite);
        //setImageBackground(R.drawable.restaurant);
        setInactiveIndicatorColor(R.color.white);
        setActiveIndicatorColor(R.color.grey_600);
        setOnboardPages(pages);

    }

    @Override
    public void onFinishButtonPressed() {
        startActivity(new Intent(this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
