package id.meteor.alfamind.helper;

/**
 * Created by bodacious on 7/12/17.
 */

public class Constant {


    public static final String PREFENCE_NAME = "MyPreferences";

    // status
    public static String FIRST_TIME = "FIRST_TIME";
    public static String IS_LOGGED = "IS_LOGGED";
    public static String REMBER_PASSWORD = "REMBER_PASSWORD";
    public static String REMEMBER_EMAIL = "REMBMBER_EMAIL";
    public static String USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN";
    public static String INTIP_MARGIN = "INTIP_MARGIN";
    public static String SIGNUP_DATA = "SIGNUP_DATA";


    //  user detailsKey
    public static String imageKey = "image";
    public static String imagePath = "imagePath";
    public static String email_key = "eamil";
    public static String password_key = "password";
    public static String firstNameKey = "fistNameKey";
    public static String lastNameKey = "lastNameKey";
    public static String genderKey = "genderKey";
    public static String birthDateKey = "birthDateKey";
    public static String birthMonthKey = "birthMonthKey";
    public static String birthYearKey = "birthYearKey";
    public static String NOMOR_KEY = "nomorKey";
    public static String ADDRESS_KEY = "addressKey";
    public static String KTP_IMAGE_KEY = "ktpImageKey";
    public static String NPWP_IMAGE_KEY = "npwpImageKey";
    public static String NomorHP = "NomorHP";
    public static String CART_KEY = "cartKey";
    public static String COUSTMER_ID = "CoustmerId";
    public static String STORE_NAME = "storeName";
    public static String COUSTMER_ID_ORA = "coustmerIdOra";
    public static String WALLET_NO = "walletNo";
    public static String POINT = "point";
    public static String DOB = "dob";

    //  address detailsKey
    public static String cityKey = "cityKey";
    public static String stateKey = "stateKey";
    public static String countryKey = "countryKey";
    public static String countryCodeKey = "countryCodeKey";
    public static String districtKey = "districtKey";
    public static String zipCodeKey = "zipCodeKey";
    public static String phoneNumberKey = "phoneNumberKey";
    public static String kecamatanKey = "kecamatanKey";

    //  bank detailsKey
    public static String bankNameKey = "bankNameKey";
    public static String accountNumberKey = "accountNumberKey";
    public static String BANK_OWNER_NAME_KEY = "bankOwnerKey";
    public static String BANK_CODE = "BANK_CODE";

    // LOCAL URL
    public static String LOCAL_API = "http://www.wir-fit.com/app_alfamind/";
    public static String API = LOCAL_API;


    // FOR User API..
    public static String USER = API+"user/";
    public static String GET_BANK = USER + "get_bank";
    public static String GET_CAPTCHA = USER + "gen_captcha";
    public static String USER_REGISTER = USER + "register";
    public static String USER_AUTHENTICATE = USER + "authenticate";
    public static String GET_USER_INFO = USER + "get_user_info/";
    public static String UPDATE_USER_INFO = USER + "update_user_info/";
    public static String GET_CK = USER + "get_wv_key/";
    public static String GET_USER_BALANCE = USER + "get_user_balance/";
    public static String GET_REWARD_HISTORY = USER + "get_point_reward_transaction_history/";
    public static String GET_USER_DOWNLINKS = USER + "get_user_downlinks/";
    public static String GET_USER_NETWORK_PERFORMANCE = USER + "get_user_network_performance/";
    public static String GET_EWALLET_DETAIL = "http://www.wir-fit.com/app_alfamind/doku_rq/inquiry_dw_customer/";

    // Notification.................
    public static String GET_NOTIFICATION = USER + "get_mailbox/";
    public static String GET_READ_NOTIFICATION = USER + "read_mailbox/";

    //  GetService...
    public static String SHOP_OWNER_GUIDE = "http://www.wir-fit.com/app_alfamind/guide/shop_owner_guide";
    public static String GET_BRAND_ID = "http://www.wir-fit.com/app_alfamind/product/get_brand";
    public static String GET_PRODUCT = "http://www.wir-fit.com/app_alfamind/product/catalogue_delta/";

    // Post service
    public static String GET_AREA = "http://www.wir-fit.com/app_alfamind/pws/get_area/1";
    public static String GET_KABUPATEN = "http://www.wir-fit.com/app_alfamind/pws/get_area/2/";
    public static String GET_KECAMATAN = "http://www.wir-fit.com/app_alfamind/pws/get_area/3/";
    public static String GET_EKSPEDIS = "http://www.wir-fit.com/app_alfamind/retur_rq/get_ekspedisi";

    // forgot Password
    public static String BASE_URL_PASSWORD = "http://www.wir-fit.com/app_alfamind/pws/";
    public static String RESET_PASSWORD = BASE_URL_PASSWORD + "reset_something/password";
    public static String RESET_PASSWORD_TOKEN_RESET = BASE_URL_PASSWORD + "validate_reset_token/your_token_reset_password";
    public static String RESET_PASSWORD_DIRECT_CHAGED = BASE_URL_PASSWORD + "password_dirct_change";

    //  For Product......
    public static String BASE_URL_PRODUCT = "http://www.wir-fit.com/app_alfamind/product/";
    public static String HIGHLIGHT_URL = BASE_URL_PRODUCT + "highlight/";
    public static String GET_DEP_ID = BASE_URL_PRODUCT + "catalogue_delta";
    public static String GET_TABS_DETAIL = GET_DEP_ID + "/";
    public static String SEARCH_PRODUCT = BASE_URL_PRODUCT + "search";
    public static String FILTER_PRODUCT = BASE_URL_PRODUCT + "filter_produk";
    public static String PRODUCT_RETURN = "http://www.wir-fit.com/app_alfamind/retur_rq/addRetur/";

    // For Cart..............
    public static String BASE_URL_CART = "http://www.wir-fit.com/app_alfamind/cart_v2/";
    public static String ADD_TO_CART = BASE_URL_CART + "add_to_cart/";
    public static String DELETE_TO_CART = BASE_URL_CART + "delete_from_cart/";
    public static String GET_CART_LIST = BASE_URL_CART + "cart_list/";
    public static String GET_CART_COUNT = BASE_URL_CART + "cart_count/";


    // extra
    public static final String pID1 = "pId1";
    public static final String pID2 = "pId2";
    public static final String pID3 = "pId3";
    public static final String pName1 = "name1";
    public static final String pName2 = "name2";
    public static final String pName3 = "name3";
    public static final String FILTER_ID = "88";

    /////////////////// WEB VIEW................
    public static String ACTIVITY_WEBVIEW_URL = "http://mind.alfaonline.com/auth/index/cid/0/cst/";
    public static String SALDO_WALLET_MUTASI_URL = "http://www.wir-fit.com/app_alfamind/user/get_user_estimated_margin_v2/?access_token=";
    public static String SALDO_WALLET_TOPUP_URL = "http://www.wir-fit.com/app_alfamind/doku_rq/topup?access_token=";
    public static String HISTORY_TRANSAKSI_URL = "http://www.wir-fit.com/app_alfamind/user/get_user_estimated_margin_v2/?access_token=";
    public static String TERMS_AND_CONDITION_URL = "http://www.wir-fit.com/app_alfamind/pws/term_condition";
    public static String CHECK_OUT = "http://mind.alfaonline.com/auth/index/cid/";
    public static String SALDO_WALLET_GENERATE_TOKEN = "http://www.wir-fit.com/app_alfamind/doku_rq/cashout?access_token=";
    public static String SALDO_WALLET_AKTIVASI_EWALLET = "http://www.wir-fit.com/app_alfamind/doku_rq/request_kyc?access_token=";
    public static String FAQ = "https://mndsvr.net/faq_guide/";
    public static String TOP_UP_CASHLESS = "http://www.wir-fit.com/app_alfamind/doku_rq/topup?access_token=";
    public static String BANTUAN = "https://mndsvr.net/faq_guide/";

    ///////////////////////Rewards
    public static String REWARDS_LIST = "http://www.wir-fit.com/app_alfamind/rewards/reward_list/";
    public static String REWARDS_DETAIL = "http://www.wir-fit.com/app_alfamind/rewards/reward_detail/";
    public static String REWARDS_REDEEM = "http://www.wir-fit.com/app_alfamind/rewards/redeem_reward/";


    ///////////////////////Shopping
    public static String PULSA_LIST = "http://www.wir-fit.com/app_alfamind/ppob/pulsa_list/";
    public static String PULSA_BUY = "http://www.wir-fit.com/app_alfamind/ppob/pulsa_buy/";
    public static String PULSA_FILTER = "http://www.wir-fit.com/app_alfamind/ppob/pulsa_filter_number/";

    ///////////////////////App
    public static String APP_VERSION = "http://www.wir-fit.com/app_alfamind/welcome/version";

    //////////////////////Share
    public static String PRODUCT_SHARE = "http://www.wir-fit.com/app_alfamind/user/get_share_key";
    public static String PRODUCT_SHARE_URL = "https://alfamind.id/share/?ck=";

    //////////////////////Feedback
    public static String CONTACT_US = "http://www.wir-fit.com/app_alfamind/user/contact_us";

    /////////////////////GET BANK STATUS
    public static String BANK_STATUS = "http://www.wir-fit.com/app_alfamind/user/cek_status_bank";

    /*Sales Alienbrainz (sales@bodaciousithub.com, sales@alienbrainz.com)
    username : frengky90@gmail.com
    pass : asdfgh

    akbar.minus@gmail.com
    zalkxw

    username : akbar.satria@meteor.id
    pass : c26pbk*/

    /*username : frengky90@gmail.com
    pass : asdfgh

    username : akbar.satria@meteor.id
    pass : 123456789*/

    //String thisYear =


    ////////////////////////      https://marvelapp.com/3ccgjca/screen/35337210

    //please replace all http://www.wir-fit.com/app_alfamind/

    //with http://www.wir-fit.com/app_alfamind/
    // git remote remove origin

}
