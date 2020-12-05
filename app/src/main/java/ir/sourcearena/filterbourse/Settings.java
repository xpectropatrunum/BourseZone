package ir.sourcearena.filterbourse;

public class Settings {
    public static final String JSON_INSPECT_TITLES = "https://sourcearena.ir/androidFilterApi/api.php?token=fpa&inspect=";
    public static final String JSON_SEARCH = "https://sourcearena.ir/androidFilterApi/search/call.php";
    public static final String JSON_NEWS = "https://sourcearena.ir/androidFilterApi/news/newsReader.php?p=";


    public static final String JSON_INDEX_BOURSE = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa&index=0";
    public static final String JSON_INDEX_H = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa&index=1";
    public static final String JSON_Q_NUMBER = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa&q_number";
    public static final String SERVER = "https://sourcearena.ir/";
    public static final String REPORT_BUG = "https://sourcearena.ir/androidFilterApi/comments/reportBug.php";
    public static final String LEAVE_COMMENT = "https://sourcearena.ir/androidFilterApi/comments/comment.php";
    public static final String JSON_INDEX_FARABOURSE = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa&index=2";
    public static final String JSON_STOCKHOLDER = "https://sourcearena.ir/androidFilterApi/api.php?token=fpa&stockholder=";
    public static final String JSON_TABLOU_KAHNI = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa&tablou=";
    public static final String JSON_FUNDAMENTAL = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa&funda=";
    public static final String JSON_ALL = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa&dideban=";
    public static final String JSON_TECHNICAL = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa&technical=";
    public static final String JSON_CANDLE = "https://sourcearena.ir/androidFilterApi/api.php?token=fpa&days=60&name=";
    public static final String JSON_CODAL_TITLES = "https://sourcearena.ir/androidFilterApi/api.php?token=fpa&codal=";
    public static final String ALL_FILTERS = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa&filter_method";
    public static final String JSON_INS_URL =  "https://sourcearena.ir/androidFilterApi/api.php?token=fpa&name=";
    public static final String ADVANCED_FIELD = "https://sourcearena.ir/androidFilterApi/template/field.php";
    public static float CARD_FONT_SIZE = 14;

    public static String JSON_API_URL = "https://sourcearena.ir/androidFilterApi/FilterApi.php?token=fpa";
    public static String[][] METHODS = new String[][]{
            new String[]{"&top_in", "بیشترین مقدار ورود پول"},
            new String[]{"&top_out", "بیشترین مقدار خروج پول"},
            new String[]{"&real_buy_per", "بیشترین سرانه خرید حقیقی"},
            new String[]{"&real_sell_per", "بیشترین سرانه فروش حقیقی"},
            new String[]{"&co_buy_per", "بیشترین سرانه خرید حقوقی"},
            new String[]{"&co_sell_per", "بیشترین سرانه فروش حقوقی"},
            new String[]{"&sell_q_val", "بیشترین ارزش صف فروش"},
            new String[]{"&sell_q_vol", "بیشترین حجم صف فروش"},
            new String[]{"&buy_q_val", "بیشترین ارزش صف خرید"},
            new String[]{"&buy_q_vol", "بیشترین حجم صف خرید"},
    };
    public static final String TITLE_EXTRA = "title";
    public static final String APPEND_URL = "appendix";
    public static final String FAIL_RELOAD = "خطا در بروزرسانی اطلاعات لطفا دوباره تلاش کنید";
}
