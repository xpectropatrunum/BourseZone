package ir.sourcearena.filterbourse;

public class FieldsUtil {
    static String[][] interpreter = new String[][]{
            new String[]{"close_price_change","تغییر آخرین قیمت"},
            new String[]{"final_price_change","تغییر قیمت پایانی"},
            new String[]{"top_in_res","ورود پول"},
            new String[]{"top_out_res","خروج پول"},
            new String[]{"trade_volume","حجم معامله شده"},
            new String[]{"buy_per_value","ارزش خرید هر کد"},
            new String[]{"buy_per_volume", "حجم خرید هر کد"},
            new String[]{"sell_per_value","ارزش فروش هر کد"},
            new String[]{"sell_per_volume", "حجم فروش هر کد"},
            new String[]{"final_price", "قیمت پایانی"},
            new String[]{"sell_q_val", "ارزش صف فروش"},
            new String[]{"sell_q_vol","حجم صف فروش"},
            new String[]{"buy_q_val", "ارزش صف خرید"},
            new String[]{"buy_q_vol", "حجم صف خرید"},
            new String[]{"close_price_change_percent", " آخرین قیمت"},
            new String[]{"final_price_change_percent", "قیمت پایانی"},
            new String[]{"state_", "وضعیت نماد"},
            new String[]{"market_", "بازار"},
            new String[]{"eps_", "eps"},
            new String[]{"P_E", "P/E"},
            new String[]{"month_volume", "میانگین حجم ماه"},
            new String[]{"volume_to_month", "حجم به میانگین ماه"},
            new String[]{"10_volume", "میانگین 10 روزه"},
            new String[]{"volume_to_10", "حجم به میانگین 10 روزه"},
            new String[]{"power_", "بازدهی 10 روزه"},
            new String[]{"co_change", "تغییر مالکیت حقوقی"},
            new String[]{"index_influence", "تاثیر در شاخص"},
            new String[]{"trade_value", "ارزش معامله"},
            new String[]{"trade_volume", "حجم معامله"},
            new String[]{"buy_power", "قدرت خریدار"},
            new String[]{"free_float", "شناوری"},
            new String[]{"sell_power", "قدرت فروشنده"},

    };
    public String getLocale(String tag){
        for(int i =0; i<interpreter.length; i++){
            if(interpreter[i][0].equals(tag)){
                return interpreter[i][1];
            }
        }
        return null;

    }
}
