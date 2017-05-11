package com.inkswipe.SocialSociety;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.CityModel;
import model.StateModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class EditProfile extends AppCompatActivity implements View.OnClickListener{

    Context context;

    AppPreferences appPreferences;
    int stateClickCheck=0;
    List<StateModel> statesarray;
    List <CityModel>cityarray;
    String android_id,state_id,country_id;
    String[] mStringArray,mstrinArraystid;
    String[] mcityStringArray,mcitystrinArraystid;
    String[] cityArray;
    String[] genderArray = new String[]{"Male","Female","Other"};
    String[] letters = new String[] { "Andhra Pradesh", "Arunachal Pradesh",
            "Assam", "Bihar", "Chhattisgarh", "Chandhigarh",
            "Dadra Nagar Haveli", "Delhi", "Goa", "Gujarat", "Haryana",
            "Himachal Pradesh", "Jammu And Kashmir", "Jharkhand", "Karnataka",
            "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
            "Mizoram", "Nagaland", "Orissa", "Punjab", "Pondicherry",
            "Rajasthan", "Sikkim", "Tamil Nadu", "Tripura", "Uttar Pradesh",
            "Uttarakhand", "West Bengal" };
    HashMap<String, String[]> hashMap = new HashMap<String, String[]>();
    String[] AndhraPradesh = new String[] { "Adilabad", "Adoni", "Amalapuram",
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
            "Vishakapatnam", "Vizianagaram", "Wanaparthy", "Warangal", "Yanam" };
    String[] ArunachalPradesh = new String[] { "Along", "Basar", "Bomdila",
            "Changlang", "Daporijo", "Itanagar", "Jairampur", "Khonsa",
            "Namsai", "Pasighat", "Pasighat ", "Roing", "Seppa", "Tezu",
            "Yingkiong", "Yingkiong ", "Ziro" };
    String[] Assam = new String[] { "Bongaigaon", "Dhubri", "Dibrugarh",
            "Dispur", "Goalpara", "Guwahati", "Hojai", "Jorhat", "Mangaldoi",
            "Nagaon", "Nalbari", "North Lakhimpur", "Sibsagar", "Silchar",
            "Tezpur", "Tinsukia" };
    String[] Bihar = new String[] { "Begusarai", "Bhagalpur", "Chapra",
            "Darbhanga", "Gaya", "Motihari", "Muzaffarpur", "Patna", "Purnea" };
    String[] Chandhigarh = new String[] { "Chandhigarh", "Mohali" };
    String[] Chhattisgarh = new String[] { "Ambikapur", "Baloda Bazar",
            "Bhatapara", "Bhilai", "Bilaspur", "Champa", "Dhamtari",
            "Jagdalpur", "Kawardha", "Kharsia", "Korba", "Mahasamund",
            "Manendragarh", "Raigarh", "Raipur", "Rajim", "Rajnandgaon",
            "Supela", "Tilda" };
    String[] DadraNagarHaveli = new String[] { "Daman", "Silvassa" };
    String[] Delhi = new String[] { "Delhi", "New Delhi" };
    String[] Goa = new String[] { "Bardez", "Bicholim", "Chikalim",
            "Curchorem", "Dona Paula", "Mapusa", "Margao", "Panjim", "Ponda",
            "Salcette", "Vasco" };

    String[] Gujarat = new String[] { "Adipur", "Ahmedabad", "Ambaji",
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
            "Valsad", "Vapi", "Veraval", "Vijapur", "Visnagar", "Wankaner" };

    String[] Haryana = new String[] { "Ambala", "Bahadurgarh", "Barwala",
            "Bhiwani", "Ellenabad", "Faridabad", "Fatehabad", "Gurgaon",
            "Hansi", "Hissar", "Jagadhri", "Jind", "Kaithal", "Kalawali",
            "Karnal", "Kurukshetra", "Mandi Dabwali", "Palwal", "Panchkula",
            "Panipat", "Pehowa", "Pinjore", "Rania", "Rewari", "Rohtak",
            "Samalkha", "Shahabad", "Sirsa", "Sonepat", "Tohana",
            "Uklana Mandi", "Yamuna Nagar" };

    String[] HimachalPradesh = new String[] { "Baddi", "Dalhousie",
            "Dharamshala", "Hamirpur", "Kangra", "Kullu", "Manali", "Mandi",
            "Nalagarh", "Palampur", "Paonta Sahib", "Rohru", "Shimla", "Solan",
            "Sundernagar", "Una" };

    String[] JammuAndKashmir = new String[] { "Ganderbal", "Jammu", "Leh",
            "Pulwama", "Srinagar", "Sunderbani", "Udhampur" };

    String[] Jharkhand = new String[] { "Bokaro", "Daltonganj", "Deoghar",
            "Dhanbad", "Dumka", "Giridih", "Hazaribag", "Jamshedpur",
            "Jhumri Telaiya", "Ramgarh Cantt", "Ranchi" };

    String[] Karnataka = new String[] { "Athani", "Bagalkot", "Bangalore",
            "Bantwal", "Belgaum", "Bellary", "Belthangady", "Bhadravati",
            "Bhatkal", "Bidadi", "Bidar", "Bijapur", "Challakere",
            "Chickamagalur", "Chikodi", "Chitradurga", "Davangere", "Dharwad",
            "Gadag", "Gangavati", "Gulbarga", "Hassan", "Hospet", "Hubli",
            "Kanakapura", "Karkala", "Karwar", "Kengari", "Kolar", "Koteshwar",
            "Kumta", "Kundapura", "Madkeri", "Mandya", "Mangalore", "Manipal",
            "Mudhol", "Mulki", "Mysore", "Nanjangud", "Puttur", "Raichur",
            "Ranebennur", "Sandur", "Shimoga", "Sindhanur", "Sirsi", "Sullia",
            "Tiptur", "Tumkur", "Udupi", "Ujire" };

    String[] Kerala = new String[] { "Adimali", "Alappuzha", "Alathur",
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
            "Vaikom", "Varkala", "Vatakara" };

    String[] MadhyaPradesh = new String[] { "Ashoknagar", "Ashta", "Bairagarh",
            "Balaghat", "Betul", "Bhind", "Bhopal", "Biaora", "Bina",
            "Burhanpur", "Burhar", "Chhatarpur", "Chhindwara", "Dabra",
            "Damoh", "Datia", "Dewas", "Dhamond", "Dhar", "Ganj Basoda",
            "Guna", "Gwalior", "Harda", "Hoshangabad", "Indore", "Itarsi",
            "Jabalpur", "Jaora", "Katni", "Khandwa", "Khargone", "Khurai",
            "Kotma", "Kukshi", "Mandsaur", "Mhow", "Morena", "Narsinghpur",
            "Neemuch", "Pipariya", "Raisen", "Ratlam", "Rau", "Rewa", "Sagar",
            "Satna", "Sehore", "Seoni", "Shahdol", "Shivpuri", "Shujalpur",
            "Sidhi", "Sihora", "Sironj", "Tikamgarh", "Ujjain", "Vidisha",
            "Waidhan" };

    String[] Maharashtra = new String[] { "Ahmednagar", "Ahmedpur", "Akluj",
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
            "Warora", "Warud", "Washim", "Yavatmal", "Yevola" };

    String[] Manipur = new String[] { "Imphal" };

    String[] Meghalaya = new String[] { "Jowai", "Shillong", "Tura" };

    String[] Mizoram = new String[] { "Aizawl" };

    String[] Nagaland = new String[] { "Dimapur", "Kohima" };

    String[] Orissa = new String[] { "Angul", "Aska", "Balasore", "Barbil",
            "Bargarh", "Baripada", "Behrampur", "Bhadrak", "Bhubaneshwar",
            "Bolangir", "Cuttack", "Dhenkanal", "Jagatsinghpur", "Jajpur Road",
            "Jajpur Town", "Jharsuguda", "Joda", "Kendrapara", "Keonjhar",
            "Khurda", "Paradip Port", "Pattamundai", "Puri", "Rayagada",
            "Rourkela", "Sambalpur", "Sunabeda", "Talcher" };

    String[] Pondicherry = new String[] { "Pondicherry" };

    String[] Punjab = new String[] { "Abohar", "Ahmedgarh", "Ajnala",
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
            "Talwara Township", "Tarn Taran", "Urmar Tanda", "Zira" };

    String[] Rajasthan = new String[] { "Ajmer", "Alwar", "Anupgarh", "Bagru",
            "Balotra", "Banswara", "Baran", "Barmer", "Beawar", "Behror",
            "Bharatpur", "Bhilwara", "Bhiwadi", "Bijainagar", "Bikaner",
            "Chaksu", "Chittorgarh", "Chomu", "Churu", "Dausa", "Dholpur",
            "Dungarpur", "Hanumangarh Town", "Jaipur", "Jalore", "Jhalawar",
            "Jhalrapatan", "Jhunjhunu", "Jodhpur", "Kankroli", "Kekri",
            "Kishingarh", "Kota", "Kotputli", "Makrana", "Nagaur", "Nasirabad",
            "Nathdwara", "Nokha", "Pali", "Phulera", "Pilibanga", "Pratapgarh",
            "Raisinghnagar", "Rawat Bhata", "Sanganer", "Sawai Madhopur",
            "Shahpura", "Sikar", "Sirohi", "Sri Ganganagar", "Sumerpur",
            "Suratgarh", "Tonk", "Udaipur" };

    String[] Sikkim = new String[] { "Gangtok" };

    String[] TamilNadu = new String[] { "Arcot", "Avadi", "Chengalpattu",
            "Chennai", "Chidambaram", "Coimbatore", "Cuddalore", "Dharmapuri",
            "Dindigul", "Erode", "Hosur", "Kanchipuram", "Karaikudi", "Karur",
            "Krishnagiri", "Kumbakonam", "Madurai", "Mayiladuthurai",
            "Mettupalayam", "Nagercoil", "Namakkal", "Neyveli", "Nilgiris",
            "Ooty", "Pattukottai", "Pollachi", "Pudukottai", "Rajapalayam",
            "Ramanathapuram", "Salem", "Sathyamangalam", "Sivaganga",
            "Sivakasi", "Thanjavur", "Theni", "Tirunelveli", "Tirupattur",
            "Tirupur", "Tiruvannamalai", "Tiruvarur", "Trichy", "Tuticorin",
            "Udumalpet", "Vellore", "Villupuram", "Virudunagar" };

    String[] Tripura = new String[] { "Agartala" };

    String[] UttarPradesh = new String[] { "Agra", "Akbarpur", "Aligarh",
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
            "Sultanpur", "Varanasi", "Vrindavan" };

    String[] Uttaranchal = new String[] { "Almora", "Dehradun", "Haldwani",
            "Kashipur", "Kotdwara", "Nainital", "Rishikesh", "Rudrapur" };

    String[] WestBengal = new String[] { "Arambagh", "Asansol", "Bankura",
            "Barasat", "Barrackpore", "Berhampore", "Bhatpara", "Bolpur",
            "Burdwan", "Chinsurah R S", "Contai", "Cooch Behar", "Darjeeling",
            "Durgapur", "Haldia", "Hooghly", "Howrah", "Jalpaiguri", "Kalyani",
            "Kharagpur", "Kolkata", "Krishnagar", "Malda", "Midnapore",
            "Paschimmidnapur", "Purulia", "Serampore", "Siliguri" };

    TextView gender,state,city,verifydialog;
    ScrollView scrollView;

    TextView emailId,phNo,add,land,pin,stateHead,cityHead,gen;

    EditText email,phoneNo,address,landmark,pincode;

    LinearLayout edsubmit;
    String device_id;
    String savestateid;

    String uid,uname,uemail,umobile ,ugender,uaddress,ulandmark,ustate,ucity,upincode,uprofile_image_url,ucover_image_url;
    String tid,tname,temail,tmobile ,tgender,taddress,tlandmark,tstate,tcity,tpincode,tprofile_image_url,tcover_image_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_edit_profile);
        context = EditProfile.this;
        appPreferences =AppPreferences.getAppPreferences(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        TextView title = ((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Edit Profile");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //    toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent intent = new Intent(EditProfile.this, Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

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

        gender = (TextView) findViewById(R.id.gender);

        state = (TextView) findViewById(R.id.state);

        city = (TextView) findViewById(R.id.city);

        emailId = (TextView) findViewById(R.id.emailHead);
        phNo = (TextView) findViewById(R.id.phoneHeaad);
        gen = (TextView) findViewById(R.id.genderHead);
        add = (TextView) findViewById(R.id.addHead);
        land = (TextView) findViewById(R.id.landmarkHead);
        pin = (TextView) findViewById(R.id.pinHead);
        stateHead = (TextView) findViewById(R.id.stateHead);
        cityHead = (TextView) findViewById(R.id.cityHead);

        emailId.setTypeface(Common.font(context, "arial.ttf"));
        phNo.setTypeface(Common.font(context, "arial.ttf"));
        gen.setTypeface(Common.font(context, "arial.ttf"));
        add.setTypeface(Common.font(context, "arial.ttf"));
        land.setTypeface(Common.font(context, "arial.ttf"));
        pin.setTypeface(Common.font(context, "arial.ttf"));
        stateHead.setTypeface(Common.font(context, "arial.ttf"));
        cityHead.setTypeface(Common.font(context, "arial.ttf"));

        email = (EditText) findViewById(R.id.email);

        phoneNo = (EditText) findViewById(R.id.mobile);

        address = (EditText) findViewById(R.id.address);

        landmark = (EditText) findViewById(R.id.landmark);

        pincode = (EditText) findViewById(R.id.pincode);

        email.setTypeface(Common.font(context, "arial.ttf"));
        phoneNo.setTypeface(Common.font(context, "arial.ttf"));
        address.setTypeface(Common.font(context, "arial.ttf"));
        landmark.setTypeface(Common.font(context, "arial.ttf"));
        pincode.setTypeface(Common.font(context, "arial.ttf"));


        uid = getIntent().getStringExtra("id");
        uname = getIntent().getStringExtra("name");
        uemail = getIntent().getStringExtra("email");
        umobile = getIntent().getStringExtra("mobile");
        ugender = getIntent().getStringExtra("gender");
        uaddress = getIntent().getStringExtra("address");
        ulandmark = getIntent().getStringExtra("landmark");
        ucity = getIntent().getStringExtra("city");
        ustate = getIntent().getStringExtra("state");
        upincode = getIntent().getStringExtra("pincode");


        email.setText(uemail);
        phoneNo.setText(umobile);
        if (uaddress != null) {
            address.setText(uaddress);
        }

        if (umobile != null) {
            phoneNo.setText(umobile);
        }
        if (ugender != null) {
            gender.setText(ugender);
        }

        if (ulandmark != null) {
            landmark.setText(ulandmark);
        }

        if (ustate != null) {
            state.setText(ustate);
        }
        if (ucity != null) {
            city.setText(ucity);
        }

        if (upincode != null) {
            pincode.setText(upincode);
        }

        scrollView = (ScrollView) findViewById(R.id.editScrollview);

        state.setOnClickListener(this);
        city.setOnClickListener(this);

        gender.setOnClickListener(this);

        edsubmit = (LinearLayout) findViewById(R.id.edsubmit);
        edsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profile = new Intent(EditProfile.this, Profile.class);
                profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


                  temail = email.getText().toString().trim();
                  tmobile = phoneNo.getText().toString().trim();
                  tgender = gender.getText().toString().trim();
                  taddress = address.getText().toString().trim();
                  tlandmark = landmark.getText().toString().trim();
                  tstate = state.getText().toString().trim();
                  tcity = city.getText().toString().trim();
                  tpincode = pincode.getText().toString().trim();



                    if (temail != null && temail.length() > 0) {

                        if (Common.emailValidator(temail)) {


                            if (tmobile != null && tmobile.length() > 0) {
                                if (tmobile.length() == 10) {


                                    if (!(city.getText().toString().equalsIgnoreCase("Select City")) && city.length()>0) {
                                  /*      startActivity(profile);
                                        finish();
*/


                                        if(tpincode!=null && tpincode.length()>0 && !tpincode.matches("[0]+")) {
                                            if(tpincode.length()==6){
                                                if (Common.isOnline(context)) {
                                                    new editProfile().execute();
                                                } else {
                                                    Common.showToast(context, "No internet connection");
                                                }
                                            }
                                            else {
                                                Common.showToast(context, "Enter a valid pincode");
                                            }
                                        }
                                        else {
                                            Common.showToast(context, "Enter a valid pincode");
                                        }
                                       /* }
                                        else{
                                            Common.showToast(context, "Enter pincode");
                                        }*/
                                    } else {
                                        Common.showToast(context, "Select City");
                                    }


                                } else {

                                    Common.showToast(context, "Enter a valid mobile number");
                                }

                            } else {
                                Common.showToast(context, "Enter a  mobile number");
                            }


                        } else {

                            Common.showToast(context, "Enter a valid Email id");
                        }


                    } else {
                        Common.showToast(context, "Enter emailid");
                    }



            }
        });
    }



    class  editProfile extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;

        String im;

        String c_image,p_image;
        String ch_image;

        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.EditProfile;

            JSONObject object = new JSONObject();
            try {

                object.put("name",uname);
                object.put("email",temail);
                object.put("mobile",tmobile);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("device_id",device_id);
                object.put("device_type","1");
                object.put("device_token",appPreferences.getString("Token",""));


                object.put("cover_extension","jpeg");
                object.put("cover_image","");

                object.put("profile_extension","jpeg");
                object.put("profile_image","");

                if(gender !=null){
                    object.put("gender",tgender);
                }
                if(address!=null){
                    object.put("address",taddress);
                }

                if(landmark!=null){
                    object.put("landmark",tlandmark);

                }
                if(pincode!=null){
                    object.put("pincode",tpincode);
                }
                if(state!=null){
                    object.put("state",tstate);
                }

                if(city!=null){
                    object.put("city",tcity);
                }










            } catch (Exception ex) {

            }

            String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            return jsonObject;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
      /*    transparent.setVisibility(View.VISIBLE);
            rotateLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();
*/
            mDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Processing...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresultsstate" + results);
            mDialog.dismiss();

            if(results!=null){


                try {
                    JSONObject jsonObject =results.getJSONObject("meta");


                    int  status_code  = jsonObject.getInt("status_code");

                    String message = jsonObject.getString("message");
                    int account_status = 1;
                    if(jsonObject.has("account_status")){
                        if(!jsonObject.isNull("account_status")){
                            account_status = jsonObject.getInt("account_status");
                        }
                    }



                    if(account_status==1) {
                        if (status_code == 0) {
                            Common.showToast(context, "User profile updated successfully");




                            JSONObject jsonObject1 = results.getJSONObject("data");

                            JSONObject data = jsonObject1.getJSONObject("data");

                            String name = data.getString("name");

                            String profile_image_urll = data.getString("profile_image_url");

                            String cover_image_urll   = data.getString("cover_image_url");

                            appPreferences.putString("profile_image_url", profile_image_urll);
                            appPreferences.putString("cover_image_url",cover_image_urll);
                            Intent profile = new Intent(EditProfile.this, Profile.class);
                            profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Common.showToast(context,"Profile successfully updated");

                            startActivity(profile);
                            finish();
                        }
                    }
                    if(account_status==0){
                        Intent intent = new Intent(EditProfile.this,Login.class);
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
    public void onClick(View v) {

            switch (v.getId()) {
                case R.id.state:
                    //  onClickList();

                    statesarray = new ArrayList<StateModel>();

                    if(state_id!=null) {
                        state_id = null;
                    }
                    if(Common.isOnline(context)) {
                        new getState().execute();

                    }
                    else {
                        Common.showToast(context,"No internet connection !");
                    }

                    break;
                case R.id.city:
                    ///  onClickCityList();
                    if(stateClickCheck==1) {
                        cityarray = new ArrayList<CityModel>();

                        if(state_id==null){
                            state_id=savestateid;
                        }

                        if (Common.isOnline(context)) {
                            new getCity().execute();
                        } else {
                            Common.showToast(context, "No internet connection !");
                        }
                    }
                    else {
                        Common.showToast(context, "First select state");
                    }

                    break;
                case R.id.gender:
                    onClickgender();

                    break;
            }
    }

    public void onClickList() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(letters, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                state.setText(letters[which]);
                cityArray = hashMap.get(state.getText().toString());
            //    city.setText(cityArray[0]);
                city.setText("Select City");
                // The 'which' argument contains the index position
                // of the selected item
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
    public void onClickCityList() {
        if (!state.getText().toString().equalsIgnoreCase("")&&!state.getText().toString().equalsIgnoreCase("State")) {
            cityArray = hashMap.get(state.getText().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setItems(cityArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    city.setText(cityArray[which]);
                    // The 'which' argument contains the index position
                    // of the selected item
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else {

            Common.showToast(context, "Select state first");
        }
    }


    public void onClickgender(){
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditProfile.this, Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



    class  getState extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.Statelist;

            JSONObject object = new JSONObject();
            try {

                object.put("country_id","1");
                object.put("skip","0");
                object.put("take","1000");
                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

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

            mDialog = new ProgressDialog(EditProfile.this,ProgressDialog.THEME_HOLO_DARK);
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

            if(results!=null){

                try {
                    JSONArray jsonArray = results.getJSONArray("data");


                    LoggerGeneral.info("arraystate-----"+jsonArray);


                    JSONObject jsonObject=null;

                    for(int i = 0;i<=jsonArray.length()-1;i++){

                        jsonObject =jsonArray.getJSONObject(i);

                        String stateid          = jsonObject.getString("state_id");

                        String CountryId        = jsonObject.getString("country_id");

                        String statename        = jsonObject.getString("state_name");

                        StateModel stateModel   = new StateModel();

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

    public void onStateClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ArrayList<String>arr= new ArrayList<String>();

        ArrayList<String>arrst = new ArrayList<String>();
        LoggerGeneral.info("showstarr"+statesarray.size()+"---"+statesarray.toString());
        for(int i=0;i<=statesarray.size()-1;i++) {


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

                stateClickCheck=1;
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

    class  getCity extends  AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.CityList;

            JSONObject object = new JSONObject();
            try {

                object.put("country_id","1");
                object.put("state_id",state_id);
                object.put("skip","0");
                object.put("take","1000");
                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

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

            mDialog = new ProgressDialog(EditProfile.this,ProgressDialog.THEME_HOLO_DARK);
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


            if(results!=null){

                try {
                    JSONArray jsonArray = results.getJSONArray("data");


                    LoggerGeneral.info("arraycity-----"+jsonArray);


                    JSONObject jsonObject=null;

                    for(int i = 0;i<=jsonArray.length()-1;i++){

                        jsonObject =jsonArray.getJSONObject(i);

                        String CityId = jsonObject.getString("city_id");

                        String CStateId  = jsonObject.getString("state_id");

                        String CountryId  = jsonObject.getString("country_id");

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

    public void onCityClick(){

        if (!state.getText().toString().equalsIgnoreCase("")&&!state.getText().toString().equalsIgnoreCase("State")) {

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

            if(mcityStringArray!=null ){
                mcityStringArray=null;
            }
            if(mcitystrinArraystid!=null){
                mcitystrinArraystid=null;
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

        }
        else {
            Common.showToast(context,"Select State first");
        }

    }




}
