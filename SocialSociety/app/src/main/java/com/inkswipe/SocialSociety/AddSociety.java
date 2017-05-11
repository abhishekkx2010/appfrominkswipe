package com.inkswipe.SocialSociety;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.CropOptionAdapter;
import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.CityModel;
import model.CropOption;
import model.DrawerList;
import model.StateModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class AddSociety extends AppCompatActivity  implements View.OnClickListener {


    LinearLayout home;
    Fragment fragment = null;
    Context context;

    List<StateModel> statesarray;
    List<CityModel> cityarray;
    String android_id, state_id, country_id;
    String[] mStringArray, mstrinArraystid;
    String[] mcityStringArray, mcitystrinArraystid;
    TextView gender;
    public static int permissionChecker=0;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Uri imageUri;
    static final int requestCodeForSdCard = 1, requestCodeForCamera = 2, requestCodeForCorp = 3;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    String fromsearch;
    String socName, socAddress, socLandmark, socArea, socState, socCity, socPincode, socPost, socPostName, socImage="", userId, socImageExt;

    String[] cityArray;
    String[] genderArray = new String[]{"Male", "Female","Other"};
    String[] letters = new String[]{"Andhra Pradesh", "Arunachal Pradesh",
            "Assam", "Bihar", "Chhattisgarh", "Chandhigarh",
            "Dadra Nagar Haveli", "Delhi", "Goa", "Gujarat", "Haryana",
            "Himachal Pradesh", "Jammu And Kashmir", "Jharkhand", "Karnataka",
            "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
            "Mizoram", "Nagaland", "Orissa", "Punjab", "Pondicherry",
            "Rajasthan", "Sikkim", "Tamil Nadu", "Tripura", "Uttar Pradesh",
            "Uttarakhand", "West Bengal"};
    HashMap<String, String[]> hashMap = new HashMap<String, String[]>();
    String[] AndhraPradesh = new String[]{"Adilabad", "Adoni", "Amalapuram",
            "Anakapalle", "Anantapur", "Armoor", "Bapatla", "Bhadrachalam",
            "Bhimavaram", "Bhongir", "Chilakaluripet", "Chirala", "Chittoor",
            "Eluru", "Gadwal", "Gudivada", "Gudur", "Guntakal", "Guntur",
            "Hindupur", "Hyderabad", "Jaggayyapet", "Janjaon", "Kadapa",
            "Kakinada", "Karimnagar", "Kasibugga", "Kavali", "Khammam",
            "Kodada", "Kothagudem", "Kurnool", "Machalipatnam", "Madanapalli",
            "Mahabubnagar", "Mancherial", "Mandapeta", "Medchal", "Miryalguda",
            "Nalgonda", "Nandyal", "Narasaraopet", "Narsampet", "Narsapur",
            "Nellore", "Nirmal", "Nizamabad", "Ongole", "Palakol",
            "Parvathipuram", "Patancheru", "Peddapuram", "Piduguralla",
            "Proddatur", "Puttaparthy", "Rajahmundry", "Rajam", "Rapalle",
            "Ravulapalem", "Salur", "Sattenapalli", "Secunderabad",
            "Shamshabad", "Siddipet", "Srikakulam", "Suryapet",
            "Tadepalligudem", "Tadpatri", "Tandur", "Tanuku", "Tenali",
            "Tirupati", "Tiruvuru", "Venkatagiri", "Vijaywada",
            "Vishakapatnam", "Vizianagaram", "Wanaparthy", "Warangal", "Yanam"};
    String[] ArunachalPradesh = new String[]{"Along", "Basar", "Bomdila",
            "Changlang", "Daporijo", "Itanagar", "Jairampur", "Khonsa",
            "Namsai", "Pasighat", "Pasighat ", "Roing", "Seppa", "Tezu",
            "Yingkiong", "Yingkiong ", "Ziro"};
    String[] Assam = new String[]{"Bongaigaon", "Dhubri", "Dibrugarh",
            "Dispur", "Goalpara", "Guwahati", "Hojai", "Jorhat", "Mangaldoi",
            "Nagaon", "Nalbari", "North Lakhimpur", "Sibsagar", "Silchar",
            "Tezpur", "Tinsukia"};
    String[] Bihar = new String[]{"Begusarai", "Bhagalpur", "Chapra",
            "Darbhanga", "Gaya", "Motihari", "Muzaffarpur", "Patna", "Purnea"};
    String[] Chandhigarh = new String[]{"Chandhigarh", "Mohali"};
    String[] Chhattisgarh = new String[]{"Ambikapur", "Baloda Bazar",
            "Bhatapara", "Bhilai", "Bilaspur", "Champa", "Dhamtari",
            "Jagdalpur", "Kawardha", "Kharsia", "Korba", "Mahasamund",
            "Manendragarh", "Raigarh", "Raipur", "Rajim", "Rajnandgaon",
            "Supela", "Tilda"};
    String[] DadraNagarHaveli = new String[]{"Daman", "Silvassa"};
    String[] Delhi = new String[]{"Delhi", "New Delhi"};
    String[] Goa = new String[]{"Bardez", "Bicholim", "Chikalim",
            "Curchorem", "Dona Paula", "Mapusa", "Margao", "Panjim", "Ponda",
            "Salcette", "Vasco"};

    String[] Gujarat = new String[]{"Adipur", "Ahmedabad", "Ambaji",
            "Amreli", "Anand", "Anjar", "Ankleshwar", "Balasinor", "Bardoli",
            "Bavla", "Bharuch", "Bhavnagar", "Bhuj", "Bilimora", "Bodeli",
            "Botad", "Chikhli", "Dabhoi", "Dahod", "Dakor", "Deesa", "Dehgam",
            "Dhanera", "Dhari", "Dhrangadhra", "Dwarka", "Gandhidham",
            "Gandhinagar", "Godhra", "Gondal", "Halol", "Halvad",
            "Himmatnagar", "Idar", "Jambusar", "Jamkhambhalia", "Jamnagar",
            "Jasdan", "Jetpur", "Junagadh", "Kadi", "Kalol", "Karjan",
            "Keshod", "Khambhat", "Kheda", "Mahuva", "Mandvi", "Mansa",
            "Mehsana", "Mithapur", "Modasa", "Morbi", "Mundra", "Nadiad",
            "Navsari", "Padra", "Palanpur", "Patan", "Petlad", "Porbander",
            "Rajkot", "Rajpipla", "Rajula", "Savarkundla", "Sihor ", "Surat",
            "Surendranagar", "Talaja", "Thangadh", "Unjha", "Vadodara",
            "Valsad", "Vapi", "Veraval", "Vijapur", "Visnagar", "Wankaner"};

    String[] Haryana = new String[]{"Ambala", "Bahadurgarh", "Barwala",
            "Bhiwani", "Ellenabad", "Faridabad", "Fatehabad", "Gurgaon",
            "Hansi", "Hissar", "Jagadhri", "Jind", "Kaithal", "Kalawali",
            "Karnal", "Kurukshetra", "Mandi Dabwali", "Palwal", "Panchkula",
            "Panipat", "Pehowa", "Pinjore", "Rania", "Rewari", "Rohtak",
            "Samalkha", "Shahabad", "Sirsa", "Sonepat", "Tohana",
            "Uklana Mandi", "Yamuna Nagar"};

    String[] HimachalPradesh = new String[]{"Baddi", "Dalhousie",
            "Dharamshala", "Hamirpur", "Kangra", "Kullu", "Manali", "Mandi",
            "Nalagarh", "Palampur", "Paonta Sahib", "Rohru", "Shimla", "Solan",
            "Sundernagar", "Una"};

    String[] JammuAndKashmir = new String[]{"Ganderbal", "Jammu", "Leh",
            "Pulwama", "Srinagar", "Sunderbani", "Udhampur"};

    String[] Jharkhand = new String[]{"Bokaro", "Daltonganj", "Deoghar",
            "Dhanbad", "Dumka", "Giridih", "Hazaribag", "Jamshedpur",
            "Jhumri Telaiya", "Ramgarh Cantt", "Ranchi"};

    String[] Karnataka = new String[]{"Athani", "Bagalkot", "Bangalore",
            "Bantwal", "Belgaum", "Bellary", "Belthangady", "Bhadravati",
            "Bhatkal", "Bidadi", "Bidar", "Bijapur", "Challakere",
            "Chickamagalur", "Chikodi", "Chitradurga", "Davangere", "Dharwad",
            "Gadag", "Gangavati", "Gulbarga", "Hassan", "Hospet", "Hubli",
            "Kanakapura", "Karkala", "Karwar", "Kengari", "Kolar", "Koteshwar",
            "Kumta", "Kundapura", "Madkeri", "Mandya", "Mangalore", "Manipal",
            "Mudhol", "Mulki", "Mysore", "Nanjangud", "Puttur", "Raichur",
            "Ranebennur", "Sandur", "Shimoga", "Sindhanur", "Sirsi", "Sullia",
            "Tiptur", "Tumkur", "Udupi", "Ujire"};

    String[] Kerala = new String[]{"Adimali", "Alappuzha", "Alathur",
            "Aluva", "Angamaly", "Athani", "Attungal", "Cannanore",
            "Chalakudy", "Chalikkavattom", "Changanacherry", "Chengannur",
            "Edappal", "Ernakulam", "Ettumanoor", "Guruvayur", "Kakkanad",
            "Kaloor", "Kalpetta", "Kanhangad", "Kanjirapally", "Karunagapally",
            "Kasaragod", "Kattappana", "Kayamkulam", "Kochi", "Kodungallur",
            "Kollam", "Koothattukulam", "Kottarakara", "Kottayam",
            "Kozhencherry", "Kozhikode", "Kunnamkulam", "Malappuram",
            "Mananthavady", "Manjeri", "Mattanur", "Mavelikara",
            "Muvattupuzha", "N.Paravur", "Nadavaramba P.O", "Nedumangad",
            "Neyyattinkara", "Nilambur", "Ottapalam", "Pala", "Palakkad",
            "Pathanamthitta", "Payyanur", "Perinthalmanna", "Perumbavoor",
            "Piravom", "Ponnani", "Punalur", "Quilandi", "Ranny", "Shertally",
            "Sultan Battery", "Taliparamba", "Thalassery", "Thodupuzha",
            "Tirur", "Tiruvalla", "Trichur", "Tripunithura", "Trivandrum",
            "Vaikom", "Varkala", "Vatakara"};

    String[] MadhyaPradesh = new String[]{"Ashoknagar", "Ashta", "Bairagarh",
            "Balaghat", "Betul", "Bhind", "Bhopal", "Biaora", "Bina",
            "Burhanpur", "Burhar", "Chhatarpur", "Chhindwara", "Dabra",
            "Damoh", "Datia", "Dewas", "Dhamond", "Dhar", "Ganj Basoda",
            "Guna", "Gwalior", "Harda", "Hoshangabad", "Indore", "Itarsi",
            "Jabalpur", "Jaora", "Katni", "Khandwa", "Khargone", "Khurai",
            "Kotma", "Kukshi", "Mandsaur", "Mhow", "Morena", "Narsinghpur",
            "Neemuch", "Pipariya", "Raisen", "Ratlam", "Rau", "Rewa", "Sagar",
            "Satna", "Sehore", "Seoni", "Shahdol", "Shivpuri", "Shujalpur",
            "Sidhi", "Sihora", "Sironj", "Tikamgarh", "Ujjain", "Vidisha",
            "Waidhan"};

    String[] Maharashtra = new String[]{"Ahmednagar", "Ahmedpur", "Akluj",
            "Akola", "Alibag", "Amalner", "Ambejogai", "Ambernath", "Amravati",
            "Arvi", "Aurangabad", "Ballarpur", "Baramati", "Barshi", "Beed",
            "Bhandara", "Bhayander", "Bhusawal", "Boisar", "Buldana",
            "Chalisgaon", "Chandrapur", "Chiplun", "Dahanu Road", "Dhule",
            "Dombivali", "Gadhinglaj", "Gondia", "Hinganghat", "Hingoli",
            "Ichalkaranji", "Jalgaon", "Jalna", "Jaysingpur", "Kalmeshwar",
            "Kalyan", "Kamptee", "Kankavli", "Karad", "Katol", "Khamgaon",
            "Khopoli", "Kolhapur", "Kudal", "Latur", "Lonavala", "Mahad",
            "Malegaon", "Malvan", "Manmad", "Mumbai", "Nagpur", "Nanded",
            "Narayangaon", "Nasik", "Navi Mumbai", "Nira", "Osmanabad",
            "Pachora", "Pandharkawada", "Panvel", "Parbhani", "Phaltan",
            "Pune", "Rahata", "Rahuri", "Ramtek", "Ratnagiri", "Sangamner",
            "Sangli", "Saoner", "Satana", "Satara", "Savda", "Sawantwadi",
            "Shirur", "Shirval", "Shrirampur", "Sillod", "Solapur", "Talegaon",
            "Thane", "Tumsar", "Umred", "Vasai", "Vita", "Wai", "Wardha",
            "Warora", "Warud", "Washim", "Yavatmal", "Yevola"};

    String[] Manipur = new String[]{"Imphal"};

    String[] Meghalaya = new String[]{"Jowai", "Shillong", "Tura"};

    String[] Mizoram = new String[]{"Aizawl"};

    String[] Nagaland = new String[]{"Dimapur", "Kohima"};

    String[] Orissa = new String[]{"Angul", "Aska", "Balasore", "Barbil",
            "Bargarh", "Baripada", "Behrampur", "Bhadrak", "Bhubaneshwar",
            "Bolangir", "Cuttack", "Dhenkanal", "Jagatsinghpur", "Jajpur Road",
            "Jajpur Town", "Jharsuguda", "Joda", "Kendrapara", "Keonjhar",
            "Khurda", "Paradip Port", "Pattamundai", "Puri", "Rayagada",
            "Rourkela", "Sambalpur", "Sunabeda", "Talcher"};

    String[] Pondicherry = new String[]{"Pondicherry"};

    String[] Punjab = new String[]{"Abohar", "Ahmedgarh", "Ajnala",
            "Amritsar", "Badhni Kalan", "Bagha Purana", "Balachaur", "Banga",
            "Banur", "Barnala", "Batala", "Begowal", "Bhatinda", "Bhawanigarh",
            "Bhikhiwind", "Bhogpur", "Bholath", "Budhlada", "Dasuya", "Dhuri",
            "Dinanagar", "Faridkot", "Fateghgarh Churian", "Fatehabad",
            "Fazilka", "Ferozpur", "Garhshankar", "Giddarbaha", "Gill Road",
            "Goniana Mandi", "Goraya", "Gurdaspur", "Guruharsahai",
            "Haibowal Kalan", "Hoshiarpur", "Jagraon", "Jaitu", "Jalalabad",
            "Jalandhar", "Jandiala Guru", "Jodhan", "Kapurthala", "Kartarpur",
            "Khamano", "Khanna", "Kharar", "Kot Ise Khan", "Kotkapura",
            "Longowal", "Ludhiana", "Machhiwara", "Majitha", "Malerkotla",
            "Malout", "Mandi Gobindgarh", "Mansa", "Maur Mandi", "Moga",
            "Morinda", "Mukerian", "Muktsar", "Mullanpur", "Nabha", "Nakodar",
            "Nawanshahr", "Naya Nangal", "Nihal Singh Wala", "Nurmahal",
            "Pathankot", "Patiala", "Patran", "Patti", "Phagwara", "Phillaur",
            "Qadian", "Raekot", "Rajpura", "Raman", "Rampura Phul", "Ropar",
            "Samana", "Sanehwal", "Sangrur", "Shahid Udham Singh Nagar",
            "Shahkot", "Sirhind", "Sultanpur Lodhi", "Sunam", "Talwandi Sabo",
            "Talwara Township", "Tarn Taran", "Urmar Tanda", "Zira"};

    String[] Rajasthan = new String[]{"Ajmer", "Alwar", "Anupgarh", "Bagru",
            "Balotra", "Banswara", "Baran", "Barmer", "Beawar", "Behror",
            "Bharatpur", "Bhilwara", "Bhiwadi", "Bijainagar", "Bikaner",
            "Chaksu", "Chittorgarh", "Chomu", "Churu", "Dausa", "Dholpur",
            "Dungarpur", "Hanumangarh Town", "Jaipur", "Jalore", "Jhalawar",
            "Jhalrapatan", "Jhunjhunu", "Jodhpur", "Kankroli", "Kekri",
            "Kishingarh", "Kota", "Kotputli", "Makrana", "Nagaur", "Nasirabad",
            "Nathdwara", "Nokha", "Pali", "Phulera", "Pilibanga", "Pratapgarh",
            "Raisinghnagar", "Rawat Bhata", "Sanganer", "Sawai Madhopur",
            "Shahpura", "Sikar", "Sirohi", "Sri Ganganagar", "Sumerpur",
            "Suratgarh", "Tonk", "Udaipur"};

    String[] Sikkim = new String[]{"Gangtok"};

    String[] TamilNadu = new String[]{"Arcot", "Avadi", "Chengalpattu",
            "Chennai", "Chidambaram", "Coimbatore", "Cuddalore", "Dharmapuri",
            "Dindigul", "Erode", "Hosur", "Kanchipuram", "Karaikudi", "Karur",
            "Krishnagiri", "Kumbakonam", "Madurai", "Mayiladuthurai",
            "Mettupalayam", "Nagercoil", "Namakkal", "Neyveli", "Nilgiris",
            "Ooty", "Pattukottai", "Pollachi", "Pudukottai", "Rajapalayam",
            "Ramanathapuram", "Salem", "Sathyamangalam", "Sivaganga",
            "Sivakasi", "Thanjavur", "Theni", "Tirunelveli", "Tirupattur",
            "Tirupur", "Tiruvannamalai", "Tiruvarur", "Trichy", "Tuticorin",
            "Udumalpet", "Vellore", "Villupuram", "Virudunagar"};

    String[] Tripura = new String[]{"Agartala"};

    String[] UttarPradesh = new String[]{"Agra", "Akbarpur", "Aligarh",
            "Allahabad", "Amroha", "Azamgarh", "Baghpat", "Bahraich", "Ballia",
            "Banda", "Barabanki", "Bareilly", "Basti", "Bhadohi", "Bijnor",
            "Bulandshahr", "Chandauli", "Deoria", "Dhampur", "Etah", "Etawah",
            "Faizabad", "Farruckhabad", "Fathepur", "Firozabad", "Ghaziabad",
            "Ghazipur", "Gonda", "Gorakhpur", "Hapur", "Hardoi", "Haridwar",
            "Hathras", "Jaunpur", "Jhansi", "Kanpur", "Kasganj", "Khurja",
            "Kunda", "Lakhimpur", "Lalitpur", "Lucknow", "Mainpuri", "Mathura",
            "Mauranipur", "Meerut", "Mirzapur", "Moradabad", "Muzaffar Nagar",
            "Noida", "Orai", "Padrauna", "Pilibhit", "Pratapgarh",
            "Rae Bareli", "Rampur", "Renukoot", "Robertsganj", "Roorkee",
            "Saharanpur", "Shahjahanpur", "Shamli", "Shikohabad", "Sitapur",
            "Sultanpur", "Varanasi", "Vrindavan"};

    String[] Uttaranchal = new String[]{"Almora", "Dehradun", "Haldwani",
            "Kashipur", "Kotdwara", "Nainital", "Rishikesh", "Rudrapur"};

    String[] WestBengal = new String[]{"Arambagh", "Asansol", "Bankura",
            "Barasat", "Barrackpore", "Berhampore", "Bhatpara", "Bolpur",
            "Burdwan", "Chinsurah R S", "Contai", "Cooch Behar", "Darjeeling",
            "Durgapur", "Haldia", "Hooghly", "Howrah", "Jalpaiguri", "Kalyani",
            "Kharagpur", "Kolkata", "Krishnagar", "Malda", "Midnapore",
            "Paschimmidnapur", "Purulia", "Serampore", "Siliguri"};


    TextView state, city;

    RadioButton yes, no;

    EditText name, add, landmark, pin, post;
    LinearLayout addsoc, uploadPhoto;

    RelativeLayout notification;

    AppPreferences appPreferences;
    String savestateid;
    EditText area;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_add_society);
        context = AddSociety.this;
        appPreferences  = AppPreferences.getAppPreferences(context);



        fromsearch = getIntent().getStringExtra("fromsearch");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  TextView title=((TextView) toolbar.findViewById(toolbar.setTitleTextColor(android.graphics.Color.WHITE));

        appPreferences=AppPreferences.getAppPreferences(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //    toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked

                if (fromsearch != null) {
                    Intent intent = new Intent(AddSociety.this, SearchSociety.class);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(AddSociety.this, Profile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        notification = (RelativeLayout) findViewById(R.id.notification);

        RelativeLayout notificationcount= (RelativeLayout) findViewById(R.id.notificationcount);

        TextView notificationtext= (TextView) findViewById(R.id.notificationtext);

        if(Constants.notififcationcount>0) {
            notificationcount.setVisibility(View.VISIBLE);
            notificationtext.setText(String.valueOf(Constants.notififcationcount));
        }
        else {
            notificationcount.setVisibility(View.GONE);
        }

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(AddSociety.this,NotificationApp.class);
                //    notification.putExtra("SocietyId",Soci)
                int test1=appPreferences.getInt("NotificationOld",0);
                test1=test1+Constants.notififcationcount;
                appPreferences.putInt("NotificationOld",test1);
                Constants.notififcationcount=0;
                startActivity(notification);
                finish();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list, null, false);

        LinearLayout navHome = (LinearLayout) listHeaderView.findViewById(R.id.Navhome);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appPreferences.getInt("property_count",0)>0) {
                Intent myProperty = new Intent(AddSociety.this, MyProperty.class);
                    PropertyAdapter.intentCheck=0;
                startActivity(myProperty);
                finish();
                }
                else
                {
                    Common.showToast(context, "Please add minimum 1 property");
                    mDrawerLayout.closeDrawers();
                }

            }
        });






        home = (LinearLayout) findViewById(R.id.home);
        home.setOnClickListener(homeOnclickListener);

        mDrawerList.addHeaderView(listHeaderView);

        DrawerList list = new DrawerList();

        List<ItemObject> listViewItems = list.drawer();


        mDrawerList.setAdapter(new CustomAdapter(this, listViewItems));


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make ToastCustom when click
                /*ToastCustom.makeText(getApplicationContext(), "Position " + position, ToastCustom.LENGTH_LONG).show();
                */

                switch (position) {

                    case 1:
                        LoggerGeneral.info("1");
                        Intent profile = new Intent(AddSociety.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        /*Intent addSociety=new Intent(AddSociety.this,AddSociety.class);
                        startActivity(addSociety);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent myProperty = new Intent(AddSociety.this, MyProperty.class);
                            PropertyAdapter.intentCheck = 0;
                            startActivity(myProperty);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent events = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 4;
                            events.putExtra("event", 3);
                            startActivity(events);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent createEvents = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 5;
                            startActivity(createEvents);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent archivedEvents = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 6;
                            startActivity(archivedEvents);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent announcement = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 7;
                            announcement.putExtra("announcement", 4);
                            startActivity(announcement);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent createAnnouncement = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 8;
                            startActivity(createAnnouncement);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent polls = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 9;
                            polls.putExtra("polls", 2);
                            startActivity(polls);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent createEventPoll = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 10;
                            startActivity(createEventPoll);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent archivedEventPoll = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 11;
                            startActivity(archivedEventPoll);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent members = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 12;
                            startActivity(members);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent offers = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 13;
                            startActivity(offers);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent complaint = new Intent(AddSociety.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 14;
                            startActivity(complaint);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(AddSociety.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld",test1);
                        Constants.notififcationcount=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:


                      /*  Intent intent = new Intent(AddSociety.this, Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("property_count");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        startActivity(intent);
                        finish();*/

                        if(Common.isOnline(context)) {

                            new getLogout().execute();

                        }
                        else
                        {
                            Common.showToast(context,"No internet connection");
                        }

                        break;
                }
            }
        });


        hashMap.put("Andhra Pradesh", AndhraPradesh);
        hashMap.put("Arunachal Pradesh", ArunachalPradesh);
        hashMap.put("Assam", Assam);
        hashMap.put("Bihar", Bihar);
        hashMap.put("Chandhigarh", Chandhigarh);
        hashMap.put("Chhattisgarh", Chhattisgarh);
        hashMap.put("Dadra Nagar Haveli", DadraNagarHaveli);
        hashMap.put("Delhi", Delhi);
        hashMap.put("Goa", Goa);
        hashMap.put("Maharashtra", Maharashtra);
        hashMap.put("Madhya Pradesh", MadhyaPradesh);
        hashMap.put("Kerala", Kerala);
        hashMap.put("Karnataka", Karnataka);
        hashMap.put("Jharkhand", Jharkhand);
        hashMap.put("Jammu And Kashmir", JammuAndKashmir);
        hashMap.put("Himachal Pradesh", HimachalPradesh);
        hashMap.put("Haryana", Haryana);
        hashMap.put("Gujarat", Gujarat);
        hashMap.put("Manipur", Manipur);
        hashMap.put("Meghalaya", Meghalaya);
        hashMap.put("Mizoram", Mizoram);
        hashMap.put("Nagaland", Nagaland);
        hashMap.put("Orissa", Orissa);
        hashMap.put("Pondicherry", Pondicherry);
        hashMap.put("Punjab", Punjab);
        hashMap.put("Rajasthan", Rajasthan);
        hashMap.put("Sikkim", Sikkim);
        hashMap.put("Tamil Nadu", TamilNadu);
        hashMap.put("Tripura", Tripura);
        hashMap.put("Uttar Pradesh", UttarPradesh);
        hashMap.put("Uttaranchal", Uttaranchal);
        hashMap.put("West Bengal", WestBengal);

        state = (TextView) findViewById(R.id.state);

        Spanned spstate = Html.fromHtml("<font color='#707070'>" + "State" + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        state.setHint(spstate);

        city = (TextView) findViewById(R.id.city);

        Spanned spcity = Html.fromHtml("<font color='#707070'>" + "City" + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        city.setHint(spcity);

        name = (EditText) findViewById(R.id.socname);

        Spanned spname = Html.fromHtml("<font color='#707070'>" + "Society Name" + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        name.setHint(spname);

        add = (EditText) findViewById(R.id.address);

        Spanned spadd = Html.fromHtml("<font color='#707070'>" + "Address" + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        add.setHint(spadd);

        landmark = (EditText) findViewById(R.id.landmark);

        Spanned spland = Html.fromHtml("<font color='#707070'>" + "Landmark" + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        landmark.setHint(spland);

        area = (EditText) findViewById(R.id.area);

        Spanned sarea = Html.fromHtml("<font color='#707070'>" + "Area" + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        area.setHint(sarea);

        pin = (EditText) findViewById(R.id.pincode);

        Spanned sppin = Html.fromHtml("<font color='#707070'>" + "Pincode" + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        pin.setHint(sppin);

        post = (EditText) findViewById(R.id.yourpost);

        yes = (RadioButton) findViewById(R.id.yes);

        no = (RadioButton) findViewById(R.id.no);

        no.setTypeface(Common.font(context, "arial.ttf"));
        yes.setTypeface(Common.font(context, "arial.ttf"));

        state.setTypeface(Common.font(context, "arial.ttf"));
        city.setTypeface(Common.font(context, "arial.ttf"));

        name.setTypeface(Common.font(context, "arial.ttf"));
        landmark.setTypeface(Common.font(context, "arial.ttf"));
        area.setTypeface(Common.font(context, "arial.ttf"));
        add.setTypeface(Common.font(context, "arial.ttf"));
        pin.setTypeface(Common.font(context, "arial.ttf"));
        post.setTypeface(Common.font(context, "arial.ttf"));

       /* name.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub

                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            LoggerGeneral.info("555enter55555====" + name.length());
                            if (name.length() < 50) {
                                return cs;

                            } else {
                                LoggerGeneral.info("555enter");
                                Toast.makeText(context, "Name should not be more than 50 characters.", Toast.LENGTH_LONG).show();
                                return "";
                            }
                        }
                        return "";
                    }
                }
        });*/


        state.setOnClickListener(this);
        city.setOnClickListener(this);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.setVisibility(View.VISIBLE);
                post.setEnabled(true);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.setVisibility(View.GONE);
                post.setEnabled(false);
            }
        });

        uploadPhoto = (LinearLayout) findViewById(R.id.uploadphoto);

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff


                    if (!checkPermission2()) {

                        requestPermission();

                    } else {
                        selectImage();
                    }
                } else {
                    selectImage();
                }
            }
        });

        addsoc = (LinearLayout) findViewById(R.id.addsoc);
        addsoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                socName = name.getText().toString().trim();

                socAddress = add.getText().toString().trim();

                socLandmark = landmark.getText().toString().trim();

                socArea = area.getText().toString().trim();

                socPincode = pin.getText().toString().trim();

                socState=state.getText().toString();

                socCity=city.getText().toString();

                socPostName = post.getText().toString().trim();

                if (yes.isChecked()) {
                    socPost = yes.getText().toString();
                    LoggerGeneral.info("socPost===" + socPost);
                }

                if (no.isChecked()) {
                    socPost = no.getText().toString();
                    LoggerGeneral.info("socPost===" + socPost);
                }

                if(socName!=null && socName.length()>0) {
                    if(socAddress!=null && socAddress.length()>0)
                    {
                        if(socLandmark!=null && socLandmark.length()>0) {
                            if (socArea != null && socArea.length() > 0) {
                                if (!state.getText().toString().equalsIgnoreCase("State") && state.length() > 0) {
                                    if (!(city.getText().toString().equalsIgnoreCase("Select City")) && city.length() > 0) {
                                        if (socPincode != null && socPincode.length() > 0) {

                                            if(socPincode.length()==6 && !socPincode.matches("[0]+")) {
                                                if (socPost != null && socPost.equals("Yes")) {
                                                    if (socPostName != null && socPostName.length() > 0) {
                                                        socPostName = post.getText().toString().trim();

                                                        new addSociety().execute();

                                                    } else {
                                                        Common.showToast(context, "Enter your post");
                                                    }

                                                } else {
                                                    socPostName = "Member";

                                                    new addSociety().execute();

                                                }
                                            }
                                            else {
                                                Common.showToast(context, "Invalid society pincode");
                                            }
                                        } else {
                                            Common.showToast(context, "Enter society pincode");
                                        }
                                    } else {
                                        Common.showToast(context, "Select city");
                                    }
                                } else {
                                    Common.showToast(context, "Select state");
                                }
                            } else {
                                Common.showToast(context, "Enter society area");
                            }
                        }else{
                            Common.showToast(context, "Enter society landmark");
                        }
                    }
                    else
                    {
                        Common.showToast(context, "Enter society address");
                    }

                }
                else
                {
                    Common.showToast(context, "Enter society name");
                }




                /*final Dialog dialog = new Dialog(AddSociety.this);
                dialog.setContentView(R.layout.addsocpopup);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                LinearLayout ok = (LinearLayout)dialog.findViewById(R.id.verifyb);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent ii = new Intent(AddSociety.this, Profile.class);

                        if(!(city.getText().toString().equalsIgnoreCase("Select City"))) {
                            startActivity(ii);
                            finish();

                        }
                        else {
                            Common.showToast(context,"Select City");
                        }
                    }
                });




                dialog.show();*/
            }
        });

    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.state:
                onClickList();

                break;
            case R.id.city:
                onClickCityList();

                break;
        }
    }*/

    public  class  getLogout extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;


        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub
            //   String url = "http://fortune4tech.in/fast5/frontend/www/api/login";
            //   String url = "http://120.88.39.187/peep/code/frontend/public/v1/login";


            String url = Constants.Base+Constants.Logout;

            LoggerGeneral.info("Url data Cpture"+url);

            JSONObject object = new JSONObject();
            try {

                //   object.put("user_id",appPreferences.getString("user_id",""));
                object.put("user_id",appPreferences.getString("user_id",""));
                LoggerGeneral.info("JsonObjectPrint" + object.toString());


                LoggerGeneral.info("JsonObjectPrintLogout" + object.toString());

            } catch (Exception ex) {

            }

            //   String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            return jsonObject;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(AddSociety.this,ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Processing...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresults" + results);
            mDialog.dismiss();



            if(results!=null){

                try {
                    JSONObject jsonObject =results.getJSONObject("meta");


                    int  status_code  = jsonObject.getInt("status_code");


                    if(status_code==0){


                        //  JSONObject data =results.getJSONObject("data");
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("property_count");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");

                        Common.showToast(context,"You have successfully logged out!");
                        Intent register   = new Intent(AddSociety.this,Login.class);
                        register.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(register);

                        finish();

                    }

                    if(status_code==1){


                        Common.showToast(context,"Logout failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.state:
                //  onClickList();

                statesarray = new ArrayList<StateModel>();

                if (state_id != null) {
                    state_id = null;
                }
                if (Common.isOnline(context)) {
                    new getState().execute();

                } else {
                    Common.showToast(context, "No internet connection !");
                }

                break;
            case R.id.city:
                ///  onClickCityList();
                cityarray = new ArrayList<CityModel>();
                if(state_id==null){
                    state_id=savestateid;
                }
                if (Common.isOnline(context)) {
                    new getCity().execute();
                } else {
                    Common.showToast(context, "No internet connection !");
                }

                break;
            case R.id.gender:
                onClickgender();

                break;
        }
    }

    public void onClickgender() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(genderArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                gender.setText(genderArray[which]);
                // The 'which' argument contains the index position
                // of the selected item
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    class getCity extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;

        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url = Constants.Base + Constants.CityList;

            JSONObject object = new JSONObject();
            try {

                object.put("country_id", "1");
                object.put("state_id", state_id);
                object.put("skip", "0");
                object.put("take", "1000");
                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            //String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            if (jsonObject != null) {
                if (jsonObject.has("Data")) {
                    try {
                        Log.d("hi", "getresponse11" + jsonObject);

                        String response = jsonObject.toString();
                        Log.d("hi", "getresponse22" + response);

                    } catch (Exception e) {
                    }
                }
            }
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(AddSociety.this, ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresultscity" + results);


            if (results != null) {

                try {
                    JSONArray jsonArray = results.getJSONArray("data");


                    LoggerGeneral.info("arraycity-----" + jsonArray);


                    JSONObject jsonObject = null;

                    for (int i = 0; i <= jsonArray.length() - 1; i++) {

                        jsonObject = jsonArray.getJSONObject(i);

                        String CityId = jsonObject.getString("city_id");

                        String CStateId = jsonObject.getString("state_id");

                        String CountryId = jsonObject.getString("country_id");

                        String CityName = jsonObject.getString("city_name");


                        CityModel cityModel = new CityModel();

                        cityModel.setCityId(CityId);
                        cityModel.setCityName(CityName);
                        cityModel.setCountryId(CountryId);
                        cityModel.setStateId(CStateId);

                        cityarray.add(cityModel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();
                onCityClick();
            }
        }

    }

    public void onCityClick() {

        if (!state.getText().toString().equalsIgnoreCase("") && !state.getText().toString().equalsIgnoreCase("State")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            ArrayList<String> arrc = new ArrayList<String>();

            ArrayList<String> arrstc = new ArrayList<String>();
            LoggerGeneral.info("showstarr" + cityarray.size() + "---" + cityarray.toString());
            for (int i = 0; i <= cityarray.size() - 1; i++) {


                String array = cityarray.get(i).getCityName();

                String arraystid = cityarray.get(i).getCityId();

                arrc.add(array);

                arrstc.add(arraystid);
            }

            if (mcityStringArray != null) {
                mcityStringArray = null;
            }
            if (mcitystrinArraystid != null) {
                mcitystrinArraystid = null;
            }


            mcityStringArray = new String[arrc.size()];
            mcityStringArray = arrc.toArray(mcityStringArray);

            mcitystrinArraystid = new String[arrstc.size()];
            mcitystrinArraystid = arrstc.toArray(mcitystrinArraystid);


            LoggerGeneral.info("arrayyyc---" + mcityStringArray + "---" + arrc.size());
            builder.setItems(mcityStringArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    city.setText(mcityStringArray[which]);


                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Common.showToast(context, "Select State first");
        }

    }

    public void onStateClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ArrayList<String> arr = new ArrayList<String>();

        ArrayList<String> arrst = new ArrayList<String>();
        LoggerGeneral.info("showstarr" + statesarray.size() + "---" + statesarray.toString());
        for (int i = 0; i <= statesarray.size() - 1; i++) {


            String array = statesarray.get(i).getStatename();

            String arraystid = statesarray.get(i).getStateid();

            arr.add(array);

            arrst.add(arraystid);
        }

        mStringArray = new String[arr.size()];
        mStringArray = arr.toArray(mStringArray);

        mstrinArraystid = new String[arrst.size()];
        mstrinArraystid = arrst.toArray(mstrinArraystid);

        if(savestateid!=null){
            state_id=savestateid;
        }
        LoggerGeneral.info("arrayyy---" + mStringArray + "---" + arr.size());
        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                state.setText(mStringArray[which]);
                //  cityArray = hashMap.get(state.getText().toString());

                city.setText("");

                if (state_id != null) {
                    state_id = null;
                }
                state_id = mstrinArraystid[which];

                savestateid = state_id;

                LoggerGeneral.info("showstateid" + state_id);
                //  city.setText(cityArray[0]);
                // The 'which' argument contains the index position
                // of the selected item
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    class getState extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;

        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url = Constants.Base + Constants.Statelist;

            JSONObject object = new JSONObject();
            try {

                object.put("country_id", "1");
                object.put("skip", "0");
                object.put("take", "1000");
                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            //String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            if (jsonObject != null) {
                if (jsonObject.has("Data")) {
                    try {
                        Log.d("hi", "getresponse11" + jsonObject);

                        String response = jsonObject.toString();
                        Log.d("hi", "getresponse22" + response);

                    } catch (Exception e) {
                    }
                }
            }
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(AddSociety.this, ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresultsstate" + results);
            //mDialog.dismiss();

            if (results != null) {

                try {
                    JSONArray jsonArray = results.getJSONArray("data");


                    LoggerGeneral.info("arraystate-----" + jsonArray);


                    JSONObject jsonObject = null;

                    for (int i = 0; i <= jsonArray.length() - 1; i++) {

                        jsonObject = jsonArray.getJSONObject(i);

                        String stateid = jsonObject.getString("state_id");

                        String CountryId = jsonObject.getString("country_id");

                        String statename = jsonObject.getString("state_name");


                        StateModel stateModel = new StateModel();

                        stateModel.setStateid(stateid);
                        stateModel.setCountryid(CountryId);
                        stateModel.setStatename(statename);


                        statesarray.add(stateModel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();
                onStateClick();
            }
        }

    }

    public void onClickList() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(letters, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                state.setText(letters[which]);
                cityArray = hashMap.get(state.getText().toString());
                city.setText(cityArray[0]);
                // The 'which' argument contains the index position
                // of the selected item
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void onClickCityList() {
        if (!state.getText().toString().equalsIgnoreCase("") && !state.getText().toString().equalsIgnoreCase("State")) {
            cityArray = hashMap.get(state.getText().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setItems(cityArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    city.setHint("Select City");
                    //    city.setText(cityArray[which]);
                    // The 'which' argument contains the index position
                    // of the selected item
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {

            Common.showToast(context, "Select state first");
        }
    }

    View.OnClickListener homeOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawers();
            } else {
                InputMethodManager inputMethodManager3 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager3.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawers();
            return;
        } else {

            super.onBackPressed();
          /*  Intent back = new Intent(AddSociety.this, Profile.class);
            startActivity(back);
            finish();*/
            if (fromsearch != null) {
                Intent intent = new Intent(AddSociety.this, SearchSociety.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(AddSociety.this, Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
    }


    private void requestPermission() {

        if (permissionChecker == 1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionChecker = 0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

            } else {
                permissionChecker = 0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        }
    }


    private boolean checkPermission2() {
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    private void selectImage(){


        View view = getLayoutInflater ().inflate (R.layout.alertimage1, null);
        LinearLayout camera = (LinearLayout)view.findViewById( R.id.camera);
        LinearLayout gallery = (LinearLayout)view.findViewById( R.id.gallery);
        LinearLayout cancel = (LinearLayout)view.findViewById( R.id.cancel);

        final Dialog mBottomSheetDialog = new Dialog (AddSociety.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff



                    if (!checkPermission3()) {
                        permissionChecker = 1;
                        requestPermission();

                    } else {

                        File f = new File(Common.getChacheDir(context), "abc.jpg");
                        if (f.exists()) {
                            f.delete();
                        }

                        f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageUri = Uri.fromFile(f);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(i, requestCodeForCamera);
                    }
                } else {


                    File f = new File(Common.getChacheDir(context), "abc.jpg");
                    if (f.exists()) {
                        f.delete();
                    }

                    f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri = Uri.fromFile(f);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(i, requestCodeForCamera);
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff



                    if (!checkPermission3()) {
                        permissionChecker = 1;
                        requestPermission();

                    } else {
                        Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                        startActivityForResult(gallery_Intent, requestCodeForSdCard);
                    }
                }
                else {
                    Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                    startActivityForResult(gallery_Intent, requestCodeForSdCard);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
            }
        });

        mBottomSheetDialog.show();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == requestCodeForSdCard && resultCode == RESULT_OK && data != null) {


            String picturePath = data.getStringExtra("picturePath");
            //perform Crop on the Image Selected from Gallery
            doCrop1(picturePath);


        } else if (requestCode == requestCodeForCamera && resultCode == RESULT_OK) {

            LoggerGeneral.info("comek here 1");
            /*File f = new File(Common.getChacheDir(context), "abc.jpg");
            imageUri = Uri.fromFile(f);
            Bitmap newBMP = null;
            newBMP = Common.decodeFile(f);
            Common.saveBitmapToFile(newBMP, f);*/
            doCrop(imageUri);


        } else if (requestCode == requestCodeForCorp && resultCode == RESULT_OK) {

            LoggerGeneral.info("requestcode for corp ");
            try {
                if (data != null) {
                    LoggerGeneral.info("data != null");
                    Bitmap newBMP = null;
                    Bitmap rotBMP = null;

                    Uri imageUri = data.getData();


                    if(imageUri!=null) {
                        newBMP = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    }
                    else {
                        newBMP = data.getExtras().getParcelable("data");
                    }
                    //newBMP = data.getExtras().getParcelable("data");

                    // File uri=new File(imageUri.getPath());
                    //imgPreview.setImageBitmap(newBMP);


                    File f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                    Common.saveBitmapToFile(newBMP, f);
                    startUploadActivity(newBMP);


                } else {
                    LoggerGeneral.info("data == null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            LoggerGeneral.info("failed");
        }

    }

    public void startUploadActivity(Bitmap newBMP) {

        Common.showToast(context, "Image selected");
        socImage=getStringImage(newBMP);

        Bitmap resizedBMP = getResizedBitmap(newBMP, 500,500);


        ByteArrayOutputStream bs = new ByteArrayOutputStream();

		/*FileOutputStream bs;
		try {
			bs =  new FileOutputStream(f);
			resizedBMP.compress(Bitmap.CompressFormat.JPEG,100, bs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/

       /* resizedBMP.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        mBitmap = BitmapFactory.decodeByteArray(
                bs.toByteArray(), 0,
                bs.toByteArray().length);*/
        // FileOutputStream out = new FileOutputStream(new File();)

        /*if(Common.isOnline(context)) {
            new GetImageUpload().execute();
        }
        else {
            Common.showToast(context,"No internet connection");
        }*/
        //   editimage.setImageBitmap(mBitmap);

        //rphoto.setImageBitmap(newBMP);
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    private void doCrop1(final String mImageCaptureUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            LoggerGeneral.info("Hiiiiiiii" + mImageCaptureUri);
            File f = new File(mImageCaptureUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");


            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 9);
            cropIntent.putExtra("aspectY", 5);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 400);
            cropIntent.putExtra("outputY", 200);

            // retrieve data on return


            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, requestCodeForCorp);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void doCrop(final Uri mImageCaptureUri) {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {


            intent.putExtra("outputX", 400);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 9);
            intent.putExtra("aspectY", 5);
            intent.putExtra("scale", false);
            //intent.putExtra("extra_size_limit", 512);
            intent.putExtra("return-data", true);
            //intent.putExtra("crop", true);
            intent.setData(mImageCaptureUri);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, requestCodeForCorp);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    LoggerGeneral.info(res.activityInfo.packageName + " " + res.activityInfo.name);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, requestCodeForCorp);
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return false;
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialog) {
                        if (mImageCaptureUri != null) {

                            getContentResolver().delete(mImageCaptureUri, null, null);

                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        // String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.e("string", "in Byte" + imageBytes);
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

       /* final int lnth=bmp.getByteCount();
        ByteBuffer dst= ByteBuffer.allocate(lnth);
        bmp.copyPixelsToBuffer(dst);
        byte[] barray=dst.array();
        String encodedImage = Base64.encodeToString(barray, Base64.DEFAULT);*/
        return encodedImage;
    }

    private boolean checkPermission3(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    class  addSociety extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;




        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub
            //   String url = "http://fortune4tech.in/fast5/frontend/www/api/login";
            //   String url = "http://120.88.39.187/peep/code/frontend/public/v1/login";


            String url = Constants.Base+Constants.AddSociety;


            JSONObject object = new JSONObject();
            try {

                object.put("name",socName);
                object.put("address",socAddress);
                object.put("landmark",socLandmark);
                object.put("state",socState);
                object.put("area",socArea);
                object.put("city",socCity);
                object.put("pincode",socPincode);
                object.put("post",socPost);
                object.put("post_name",socPostName);
                object.put("society_image",socImage);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("image_extension","jpg");


                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            //String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            return jsonObject;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(AddSociety.this, ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Processing...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresults" + results);
            mDialog.dismiss();



            if(results!=null){

                try {
                    JSONObject jsonObject = results.getJSONObject("meta");


                    int status_code = jsonObject.getInt("status_code");

                    int account_status = jsonObject.getInt("account_status");

                    String message = jsonObject.getString("message");


                    if (account_status == 1){


                        if (status_code == 0) {


                            JSONObject data = results.getJSONObject("data");

                            final Dialog dialog = new Dialog(AddSociety.this);
                            dialog.setContentView(R.layout.addsocpopup);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setCancelable(false);

                            LinearLayout ok = (LinearLayout) dialog.findViewById(R.id.verifyb);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent ii = new Intent(AddSociety.this, Profile.class);
                                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(ii);
                                        finish();

                                }
                            });

                            dialog.show();





                        /*Intent register   = new Intent(AddSociety.this,Profile.class);
                        startActivity(register);
                        finish();*/
                        }

                    if (status_code == 1) {


                        Common.showToast(context, "Invalid  Credentials");
                    }

                }


                    if(account_status==0){
                        Intent intent = new Intent(AddSociety.this,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "QTPRGMHYD693T2MFCTN2");
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogEvents(true);
        FlurryAgent.setLogLevel(Log.VERBOSE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
}













//{"name":"kedar", "email":"kedar.g@fortune4.in1", "password":"admin1234", "device_id": "0987876867", "device_type":"1","device_token": "4f63855269530b08deda6221ea8a039b6715fe38561da9714c72ec6f3c66bda8","mobile":"8080414220","byte_array":"IMAGE_IN_BYTE_ARRAY_FORMAT","extension":"jpeg"}
       /*final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Register.this);
        View dialog_layout = getLayoutInflater().inflate(R.layout.alertimage1, null);

        ad = alertDialog.create();
        ad.setView(dialog_layout);
        TextView capture = (TextView) dialog_layout.findViewById(R.id.btn1);
        TextView gallery = (TextView) dialog_layout.findViewById(R.id.btn2);
        TextView cancel = (TextView) dialog_layout.findViewById(R.id.btn3);

        capture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

             *//*   ad.cancel();

                File f = new File(Common.getChacheDir(context), "abc.jpg");
                if (f.exists()) {
                    f.delete();
                }

                f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = Uri.fromFile(f);
                i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(i, requestCodeForCamera);
*//*

                if(Build.VERSION.SDK_INT >= 23 ){
                    // Do some stuff
                    ad.cancel();

                    if (!checkPermission3()) {
                        permissionChecker=1;
                        requestPermission();

                    }
                    else {

                        File f = new File(Common.getChacheDir(context), "abc.jpg");
                        if (f.exists()) {
                            f.delete();
                        }

                        f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageUri = Uri.fromFile(f);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(i, requestCodeForCamera);
                    }
                }
                else {


                    ad.cancel();


                    File f = new File(Common.getChacheDir(context), "abc.jpg");
                    if (f.exists()) {
                        f.delete();
                    }

                    f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri = Uri.fromFile(f);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(i, requestCodeForCamera);
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
                File f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");

                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image*//*");

                imageUri = Uri.fromFile(f);
                i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(i, requestCodeForSdCard);


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
            }
        });

        ad.show();*/