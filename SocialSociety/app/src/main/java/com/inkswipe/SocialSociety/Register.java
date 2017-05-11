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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.inkswipe.SocialSociety.pulltozoomview.PullToZoomScrollViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.CropOptionAdapter;
import model.CityModel;
import model.CropOption;
import model.StateModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class Register extends AppCompatActivity implements View.OnClickListener {
    static AlertDialog ad;
    Context context;
    Uri imageUri;
    Bitmap mBitmap;
    static final int requestCodeForSdCard = 1, requestCodeForCamera = 2, requestCodeForCorp = 3;
    ImageView transparent;

    AppPreferences appPreferences;
    ImageView rphoto;
    String[] cityArray;
    String[] genderArray = new String[]{"Male","Female","Other"};
    String[] letters = new String[] {"Andhra Pradesh", "Arunachal Pradesh",
            "Assam", "Bihar", "Chhattisgarh", "Chandhigarh",
            "Dadra Nagar Haveli", "Delhi", "Goa", "Gujarat", "Haryana",
            "Himachal Pradesh", "Jammu And Kashmir", "Jharkhand", "Karnataka",
            "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
            "Mizoram", "Nagaland", "Orissa", "Punjab", "Pondicherry",
            "Rajasthan", "Sikkim", "Tamil Nadu", "Tripura", "Uttar Pradesh",
            "Uttarakhand", "West Bengal" };
    HashMap<String, String[]> hashMap = new HashMap<String, String[]>();
    String[] AndhraPradesh = new String[] {"Adilabad", "Adoni", "Amalapuram",
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
    String[] ArunachalPradesh = new String[] {"Along", "Basar", "Bomdila",
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
    String[] DadraNagarHaveli = new String[] {"Daman", "Silvassa" };
    String[] Delhi = new String[] {"Delhi", "New Delhi" };
    String[] Goa = new String[] {"Select City", "Bardez", "Bicholim", "Chikalim",
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

    String[] Haryana = new String[] {"Ambala", "Bahadurgarh", "Barwala",
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

    String[] Jharkhand = new String[] {"Bokaro", "Daltonganj", "Deoghar",
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

    String[] MadhyaPradesh = new String[] {"Ashoknagar", "Ashta", "Bairagarh",
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

    String[] Manipur = new String[] {"Imphal" };

    String[] Meghalaya = new String[] {"Jowai", "Shillong", "Tura" };

    String[] Mizoram = new String[] { "Aizawl" };

    String[] Nagaland = new String[] {"Dimapur", "Kohima" };

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

    String[] Rajasthan = new String[] {"Ajmer", "Alwar", "Anupgarh", "Bagru",
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

    String[] UttarPradesh = new String[] {"Agra", "Akbarpur", "Aligarh",
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

    String[] WestBengal = new String[] {"Arambagh", "Asansol", "Bankura",
            "Barasat", "Barrackpore", "Berhampore", "Bhatpara", "Bolpur",
            "Burdwan", "Chinsurah R S", "Contai", "Cooch Behar", "Darjeeling",
            "Durgapur", "Haldia", "Hooghly", "Howrah", "Jalpaiguri", "Kalyani",
            "Kharagpur", "Kolkata", "Krishnagar", "Malda", "Midnapore",
            "Paschimmidnapur", "Purulia", "Serampore", "Siliguri" };



    List <StateModel>statesarray;
    List <CityModel>cityarray;
    List <String>nstatesarray;
    TextView gender,state,city,verifydialog,verifymailid;
    PullToZoomScrollViewEx scrollView;
    LinearLayout loginRegister;
    String register;

    RelativeLayout camera;
    public static int permissionChecker=0;
    private static final int PERMISSION_REQUEST_CODE = 1;
    TextView stimer,resend;
    EditText verifymobedtxt;
    int secs;
    Bitmap bitmap;
    public  Bitmap strbitmap;
    String mImage;
    Bitmap rbitmap;
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimerforotp;
    Dialog dialog=null;
    EditText username,email,address,landmark,pincode,password,conpassword,mobile;
    String android_id,state_id,country_id;
    String[] mStringArray,mstrinArraystid;
    String[] mcityStringArray,mcitystrinArraystid;
    String contact_type;
    String uname,uemailid,upassword,uconpassword,umobno,ugender,uaddress,ulandmark,ustate,ucity,upincode;
    EditText emailvd,mobilevd;
    //  public boolean emailbool,mobilebool;
    public String emailbool,mobilebool;
    String frompreview;
    TextView marked;

    String savestateid;
    public  static boolean otpmobile;
    String pname,pemail,ppassword,pconpassword,pmobileno,pgender,paddress,plandmark,pstate,pcity,ppincode;
    String motpp;


    ProgressDialog newmdialog;
    String votp;
    String sender_id;

    String newContactDetail,newcontact_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

             //   otpmobile=true;
                Log.d("Text", messageText);
                motpp=messageText;
                String  otptext=null,otptext2=null;
                if(motpp!=null && motpp.length()>0) {


                    if (countDownTimerforotp != null) {
                        countDownTimerforotp.cancel();
                    }

                    //LoggerGeneral.info("senderid---" + sender_id + "---" + SmsReceiver.senderid);

                    if(sender_id!=null)
                    {

                        if (sender_id.equals(SmsReceiver.senderid) || sender_id == SmsReceiver.senderid) {


                            otptext = SmsReceiver.smessageBody.replaceAll("[^0-9]", "").trim();

                            LoggerGeneral.info("senderid333---" + sender_id + "---" + SmsReceiver.senderid + "===" + otptext);
                        }
                    }

                 *//*   if (sender_id.equals(SmsReceiver.senderid)) {


                        otptext = SmsReceiver.smessageBody.replaceAll("[^0-9]", "").trim();

                        LoggerGeneral.info("senderid444---" + sender_id + "---" + SmsReceiver.senderid + "===" + otptext);
                    }

                    if (sender_id == SmsReceiver.senderid) {


                        otptext = SmsReceiver.smessageBody.replaceAll("[^0-9]", "").trim();

                        LoggerGeneral.info("senderid5555---" + sender_id + "---" + SmsReceiver.senderid + "===" + otptext);
                    }*//*

                    otptext2 = messageText.replaceAll("[^0-9]", "").trim();
                    //LoggerGeneral.info("senderid1111---" + sender_id + "---" + SmsReceiver.senderid + "===" + otptext);
                    //   if(otpmobile==true ) {

                    if(newmdialog!=null) {
                        newmdialog.dismiss();
                        if (Common.isOnline(context)) {
                            new verifyOtp(newcontact_type, newContactDetail, otptext).execute();
                        }
                        else {
                            Common.showToast(context,"No internet connection");
                        }
                    }

                    LoggerGeneral.info("senderid22---" + otptext);*/
        /*           dialog = new Dialog(Register.this);
                    dialog.setContentView(R.layout.custom_popup);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    stimer = (TextView) dialog.findViewById(R.id.timer);

                    resend = (TextView) dialog.findViewById(R.id.resend);

                    verifymobedtxt = (EditText) dialog.findViewById(R.id.verifymobedtxt);


                    verifymobedtxt.setText(otptext2);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }

                    countDownTimer = new CountDownTimer(59000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            stimer.setVisibility(View.VISIBLE);
                            resend.setVisibility(View.GONE);
                            stimer.setText(" " + "00:" + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            //  stimer.setText(" Re-Send");
                            stimer.setVisibility(View.GONE);
                            resend.setVisibility(View.VISIBLE);
                        }
                    }.start();


                    resend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                            new sendOtp(newcontact_type, newContactDetail).execute();
                            countDownTimer = new CountDownTimer(59000, 1000) {

                                public void onTick(long millisUntilFinished) {

                                    stimer.setVisibility(View.VISIBLE);
                                    resend.setVisibility(View.GONE);
                                    stimer.setText(" " + "00:" + millisUntilFinished / 1000);

                                }

                                public void onFinish() {
                                    stimer.setVisibility(View.VISIBLE);
                                    resend.setVisibility(View.GONE);
                                    //      stimer.setText(" Re-Send");
                                }
                            }.start();
                        }
                    });


                    LinearLayout verify = (LinearLayout) dialog.findViewById(R.id.verifyb);
                    verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            votp = verifymobedtxt.getText().toString().trim();

                            if (votp != null && votp.length() > 0) {

                                LoggerGeneral.info("newcd---" + newContactDetail);

                                new verifyOtp(newcontact_type, newContactDetail, votp).execute();

                            } else {
                                Common.showToast(context, "Enter OTP");
                            }

                            countDownTimer.cancel();
                        }
                    });

                    dialog.show();
*/


         //       }




           //     Toast.makeText(Register.this, "Message: " + messageText, Toast.LENGTH_LONG).show();
        /*    }
        });*/
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_register);



        context = Register.this;

        appPreferences = AppPreferences.getAppPreferences(context);

        secs=31;

/*
        emailbool=false;
        mobilebool=false;
*/

       /* emailbool="0";
        mobilebool = "0";
*/
    /*    appPreferences.putString("emailbool",emailbool);
        appPreferences.putString("mobilebool",mobilebool);*/

        statesarray = new ArrayList<StateModel>();

        cityarray = new ArrayList<CityModel>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title = ((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Register");
        title.setTypeface(Common.font(context, "arial.ttf"), 1);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        loadViewForCode();
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_View1);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);

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


        register = getIntent().getStringExtra("register");
        LoggerGeneral.info("register----" + register);

        gender = (TextView) scrollView.getPullRootView().findViewById(R.id.gender);
        //    gender = (TextView)findViewById(R.id.gender);
        gender.setTypeface(Common.font(context, "arial.ttf"));

        state = (TextView) scrollView.getPullRootView().findViewById(R.id.state);

        state.setTypeface(Common.font(context, "arial.ttf"));
        //    state  = (TextView)findViewById(R.id.state);
        city = (TextView) scrollView.getPullRootView().findViewById(R.id.city);
        //     city   = (TextView)findViewById(R.id.city);
        city.setTypeface(Common.font(context, "arial.ttf"));

        loginRegister = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.lregister);
        bitmap = BitmapFactory.decodeResource(Register.this.getResources(),
                R.mipmap.fallb);

        rphoto = (ImageView) findViewById(R.id.primage);

        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        //   mImage  = getStringImage(strbitmap);

        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                uname =username.getText().toString().trim();
                uemailid = email.getText().toString().trim();
                upassword = password.getText().toString().trim().replace(" ","");
                uconpassword = conpassword.getText().toString().trim().replace(" ","");
                umobno = mobile.getText().toString().trim();
                uaddress = address.getText().toString().trim();
                ulandmark = landmark.getText().toString().trim();
                upincode = pincode.getText().toString().trim();




                if (rphoto.getDrawable() instanceof BitmapDrawable) {
                    strbitmap = ((BitmapDrawable) rphoto.getDrawable()).getBitmap();
                } else {
                    Drawable d = rphoto.getDrawable();
                    strbitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    d.draw(canvas);
                }

                LoggerGeneral.info("ImageView"+strbitmap);
                Intent preview = new Intent(Register.this, RegisterPreview.class);
                // preview.putExtra("primage", mImage);
                preview.putExtra("btimage", strbitmap);

                preview.putExtra("username",uname);
                preview.putExtra("email",uemailid);
                preview.putExtra("password",upassword);
                preview.putExtra("conpassword",uconpassword);
                preview.putExtra("mobile",umobno);
                preview.putExtra("gender",gender.getText().toString());
                preview.putExtra("address",uaddress);
                preview.putExtra("landmark",ulandmark);
                preview.putExtra("State",state.getText().toString());
                preview.putExtra("City",city.getText().toString());
                preview.putExtra("pincode",upincode);

                //Registerfromhere




                if(uname!=null && uname.length()>0) {


                    if (uemailid != null && uemailid.length() > 0) {

                        if(Common.emailValidator(uemailid)) {

//                         if(emailbool==true){
                               if (appPreferences.getString("emailbool","") == "1") {

                            if (upassword != null && upassword.length() > 0) {

                                if (upassword.length() > 7) {

                                    if (uconpassword!=null && uconpassword.length()>0){
                                        if (upassword.equals(uconpassword)) {

                                            if (umobno != null && umobno.length() > 0) {
                                                if (umobno.length() == 10 && !umobno.matches("[0]+")) {
                                                    if (appPreferences.getString("mobilebool", "") == "1") {
                                                    if(upincode.length()>0)
                                                    {
                                                        if(!upincode.matches("[0]+")  && upincode.length()==6)
                                                        {
                                                            startActivity(preview);
                                                            finish();
                                                        }
                                                        else {
                                                            Common.showToast(context, "Enter a valid pincode");
                                                        }
                                                    }
                                                    else {

                                                        //   if (mobilebool == true) {

                                                        //        if (appPreferences.getString("mobilebool", "") == "1") {

                                                        //       if (!(city.getText().toString().equalsIgnoreCase("Select City")) && state.length()>0) {

                                                        startActivity(preview);
                                                        finish();
                                                    } /* } else {
                                                                Common.showToast(context, "Select City");
                                                            }
*/
                                                        } else {
                                                            Common.showToast(context, "Please verify your mobile number");

                                                        }

                                                } else {

                                                    Common.showToast(context, "Enter a valid mobile number");
                                                }

                                            } else {
                                                Common.showToast(context, "Enter  mobile number");
                                            }

                                        } else {
                                            Common.showToast(context, "Password does not match the confirm password");

                                        }

                                    }else {
                                        Common.showToast(context, "Enter confirm password");
                                    }

                                }
                                else {
                                    Common.showToast(context, "Enter password of minimum 8 characters");
                                }

                            } else {
                                Common.showToast(context, "Enter password");
                            }

                            } else {
                                Common.showToast(context, "Please verify your email id");

                            }
                        }
                        else {

                            Common.showToast(context,"Enter a valid email id");
                        }
                    }
                    else {
                        Common.showToast(context,"Enter email id");
                    }

                }
                else {
                    Common.showToast(context, "Enter name");
                }

            }
        });



        //  OverScrollDecoratorHelper.setUpOverScroll(scrollView);


        state.setOnClickListener(this);
        city.setOnClickListener(this);

        gender.setOnClickListener(this);
        //   verifydialog = (TextView)findViewById(R.id.verifydialog);
        verifydialog = (TextView) scrollView.getPullRootView().findViewById(R.id.verifydialog);
        String udata = "Verify your mobile number";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        verifydialog.setText(content);
        verifydialog.setTypeface(Common.font(context, "arial.ttf"));
        verifydialog.setTextColor(getResources().getColor(R.color.color_primary));



        verifydialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //  mobilebool=false;
                mobilebool="0";
                appPreferences.putString("mobilebool",mobilebool);
                if (mobile.getVisibility() == View.VISIBLE) {
                    umobno = mobile.getText().toString().trim();
                }
                if (mobilevd.getVisibility() == View.VISIBLE) {
                    umobno = mobilevd.getText().toString().trim();
                    LoggerGeneral.info("inhiddmob");
                }
                contact_type = "mobile";

                if(umobno!=null &&umobno.length()>0) {
                    if(umobno.length()==10) {
                        if(!umobno.matches("[0]+"))
                        {
                        if (Common.isOnline(context)) {

                            otpmobile = false;
                            new sendOtp(contact_type, umobno).execute();
                        } else {
                            Common.showToast(context, "No internet connection");
                        }
                    }
                        else {
                            Common.showToast(context,"Enter a valid mobile number");
                        }
                    }
                    else {
                        Common.showToast(context,"Enter a valid mobile number");
                    }
                }
                else {
                    Common.showToast(context,"Enter mobile number");
                }



            }



        });



        verifymailid = (TextView) scrollView.getPullRootView().findViewById(R.id.verifymailid);
        String udata1 = "Verify your email id";
        SpannableString content1 = new SpannableString(udata1);
        content1.setSpan(new UnderlineSpan(), 0, udata1.length(), 0);
        verifymailid.setText(content1);
        verifymailid.setTypeface(Common.font(context, "arial.ttf"));
        verifymailid.setTextColor(getResources().getColor(R.color.color_primary));
        verifymailid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                emailbool=false;
                emailbool="0";
                appPreferences.putString("emailbool",emailbool);
                //  appPreferences.putBoolean("emailbool",emailbool);


                if (email.getVisibility() == View.VISIBLE) {
                    uemailid = email.getText().toString().trim();
                }
                if (emailvd.getVisibility() == View.VISIBLE) {
                    uemailid = emailvd.getText().toString().trim();
                    LoggerGeneral.info("inhidd");
                }
                contact_type = "email";

                if (uemailid != null && uemailid.length() > 0) {

                    if (Common.emailValidator(uemailid)) {

                        if (Common.isOnline(context)) {
                            new sendOtp(contact_type, uemailid).execute();
                        } else {
                            Common.showToast(context, "No internet ");
                        }
                    } else {
                        Common.showToast(context, "Enter a valid email id");
                    }
                } else {
                    Common.showToast(context, "Enter email id");
                }


            }
        });



        camera = (RelativeLayout) findViewById(R.id.camera);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        rbitmap = (Bitmap) getIntent().getParcelableExtra("prbtimage");




        if(rbitmap!=null){
            rphoto.setImageBitmap(rbitmap);
        }



        username = (EditText)scrollView.getPullRootView().findViewById(R.id.username);
        email = (EditText)scrollView.getPullRootView().findViewById(R.id.email);
        emailvd  =(EditText)scrollView.getPullRootView().findViewById(R.id.emailvd);

        Spanned sptextemailvd = Html.fromHtml("<font color='#707070'>" + "Email Id" + "</font>" + " <font color='#FF0000'>" + "*" +"</font>");

        emailvd.setHint(sptextemailvd);

        email.setVisibility(View.VISIBLE);
        emailvd.setVisibility(View.GONE);
        address = (EditText)scrollView.getPullRootView().findViewById(R.id.address);
        landmark = (EditText)scrollView.getPullRootView().findViewById(R.id.landmark);
        pincode = (EditText)scrollView.getPullRootView().findViewById(R.id.pincode);
        mobile = (EditText)scrollView.getPullRootView().findViewById(R.id.mobile);
        mobilevd = (EditText)scrollView.getPullRootView().findViewById(R.id.mobilevd);
        mobile.setVisibility(View.VISIBLE);
        mobilevd.setVisibility(View.GONE);
        password  = (EditText)scrollView.getPullRootView().findViewById(R.id.password);
        conpassword = (EditText)scrollView.getPullRootView().findViewById(R.id.conpassword);



        InputFilter filter = new InputFilter() {
            boolean canEnterSpace = false;

            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                if(password.getText().toString().equals(""))
                {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);

                    if (!Character.isLetterOrDigit(currentChar) || currentChar == '_'||Character.isLetterOrDigit(currentChar) ) {
                  //  if (Character.isLetter(currentChar) ) {
                        builder.append(currentChar);
                        canEnterSpace = true;
                    }

                    if(Character.isWhitespace(currentChar) && canEnterSpace  ) {
                        Common.showToast(context,"Password should not contain spaces and period");
                        builder.append("");
                    }


                }
                return builder.toString();
            }

        };


        password.setFilters(new InputFilter[]{filter});




        InputFilter filter1 = new InputFilter() {
            boolean canEnterSpace = false;

            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                if(conpassword.getText().toString().equals(""))
                {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);

                 //   if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                    if (!Character.isLetterOrDigit(currentChar) || currentChar == '_'||Character.isLetterOrDigit(currentChar) ) {
                        builder.append(currentChar);
                        canEnterSpace = true;
                    }

                    if(Character.isWhitespace(currentChar) && canEnterSpace) {
                        Common.showToast(context,"Password should not contain spaces and period");
                        builder.append("");
                    }


                }
                return builder.toString();
            }

        };


        conpassword.setFilters(new InputFilter[]{filter1});

        Spanned sptextname = Html.fromHtml("<font color='#707070'>" + "Name" + "</font>" + " <font color='#FF0000'>" + "*" +"</font>");

        username.setHint(sptextname);

        username.setTypeface(Common.font(context, "arial.ttf"));


        Spanned sptextpass = Html.fromHtml("<font color='#707070'>" + "Password" + "</font>" + " <font color='#FF0000'>" + "*" +"</font>");

        password.setHint(sptextpass);

        email.setTypeface(Common.font(context, "arial.ttf"));

        Spanned sptextemail = Html.fromHtml("<font color='#707070'>" + "Email Id" + "</font>" + " <font color='#FF0000'>" + "*" +"</font>");

        email.setHint(sptextemail);

        Spanned sptextconpass = Html.fromHtml("<font color='#707070'>" + "Confirm Password" + "</font>" + " <font color='#FF0000'>" + "*" +"</font>");

        conpassword.setHint(sptextconpass);

        Spanned sptextmob= Html.fromHtml("<font color='#707070'>" + "Mobile No." + "</font>" + " <font color='#FF0000'>" + "*" +"</font>");

        mobile.setHint(sptextmob);

        mobilevd.setHint(sptextmob);

        address.setTypeface(Common.font(context, "arial.ttf"));

        landmark.setTypeface(Common.font(context, "arial.ttf"));

        mobile.setTypeface(Common.font(context, "arial.ttf"));

        pincode.setTypeface(Common.font(context, "arial.ttf"));


        frompreview = getIntent().getStringExtra("frompreview");

        username.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub

                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            LoggerGeneral.info("555enter55555====" + username.length());
                            if (username.length() < 50) {
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
        });

        if(frompreview!=null){



            pname = getIntent().getStringExtra("username");
            pemail = getIntent().getStringExtra("email");
            pmobileno = getIntent().getStringExtra("mobile");
            pgender = getIntent().getStringExtra("gender");
            paddress = getIntent().getStringExtra("address");
            plandmark = getIntent().getStringExtra("landmark");
            pstate = getIntent().getStringExtra("State");
            pcity = getIntent().getStringExtra("City");
            ppincode = getIntent().getStringExtra("prpincode");


            if(pname!=null){
                username.setText(pname);
            }

            if(pemail!=null){
                email.setText(pemail);

            }


            if(pmobileno!=null){
                mobile.setText(pmobileno);
            }

            if(pgender!=null){
                gender.setText(pgender);
            }

            if(paddress!=null){
                address.setText(paddress);
            }
            if(plandmark!=null){
                landmark.setText(plandmark);
            }
            if(pstate!=null){
                state.setText(pstate);
            }
            if(pcity!=null){
                city.setText(pcity);
            }
            if(ppincode!=null){
                pincode.setText(ppincode);
            }

            // username.setText();



        }

        if(email.getVisibility()==View.VISIBLE){
            email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        emailbool="0";
                        appPreferences.putString("emailbool",emailbool);


                        //  if(email.getText().toString()!=)

                        //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                    }else {
                        //  Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        if(mobile.getVisibility()==View.VISIBLE){
            mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        mobilebool="0";
                        appPreferences.putString("mobilebool",mobilebool);


                        //   Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                    }else {
                        //   Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

//        Fields with * are required

        marked = (TextView)scrollView.getPullRootView().findViewById(R.id.marked);

        Spanned sptext = Html.fromHtml("<font color='#707070'>" + "Fields with" + "</font>" + " <font color='#FF0000'>" + "*" +"</font>"+"<font color='#707070'>" + " are required" +"</font>");

        marked.setText(sptext);

    }


    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_View1);
        View headView = LayoutInflater.from(this).inflate(R.layout.register_header, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.register_zoomview, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.register_contentview, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
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
                cityarray = new ArrayList<CityModel>();
                if(state_id==null){
                    state_id=savestateid;
                }
                if(Common.isOnline(context)){
                    new getCity().execute();
                }
                else {
                    Common.showToast(context,"No internet connection !");
                }

                break;
            case R.id.gender:
                onClickgender();

                break;
        }
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

            mDialog = new ProgressDialog(Register.this,ProgressDialog.THEME_HOLO_DARK);
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

                        String stateid = jsonObject.getString("state_id");

                        String CountryId  = jsonObject.getString("country_id");

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
        LoggerGeneral.info("arrayyy---"+mStringArray+"---"+arr.size());
        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                state.setText(mStringArray[which]);
                //  cityArray = hashMap.get(state.getText().toString());
                city.setText("");

                if(state_id!=null){
                    state_id =null;
                }
                state_id = mstrinArraystid[which];

                savestateid = state_id;

                LoggerGeneral.info("showstateid"+state_id);
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

            mDialog = new ProgressDialog(Register.this,ProgressDialog.THEME_HOLO_DARK);
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


    public void onClickList() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(letters, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                state.setText(letters[which]);
                cityArray = hashMap.get(state.getText().toString());
                city.setText("Select City");
                //  city.setText(cityArray[0]);
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

            Common.showToast(context,"Select state first");
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


    class  sendOtp extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String contact_type;
        String contact_detail;
        String votp;

        public sendOtp(String contact_type,String contact_detail){

            this.contact_type = contact_type;
            this.contact_detail=contact_detail;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.SendOtp;
            //      String url= Constants.Base+Constants.SendTestOtp;
            JSONObject object = new JSONObject();
            try {

                object.put("contact_type",contact_type);
                object.put("contact_detail",contact_detail);

                LoggerGeneral.info("JsonObjectPrint" + object.toString());

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


            if(contact_type.equalsIgnoreCase("mobile")){
                newmdialog = new ProgressDialog(Register.this, ProgressDialog.THEME_HOLO_DARK);
                newmdialog.setMessage("Please wait...");
                newmdialog.show();
                newmdialog.setCancelable(false);
                newmdialog.setCanceledOnTouchOutside(false);
            }

            if(contact_type.equalsIgnoreCase("email")) {
                mDialog = new ProgressDialog(Register.this, ProgressDialog.THEME_HOLO_DARK);
                mDialog.setMessage("Please wait...");
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
            }
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresultsstate" + results);
            //mDialog.dismiss();

            if(results!=null) {

                if(contact_type.equalsIgnoreCase("mobile")){

                    try {


                        JSONObject meta = results.getJSONObject("meta");

                        int status_code = meta.getInt("status_code");
                        String otptext=null;

                        if (status_code == 0) {
                            //    JSONObject data = results.getJSONObject("data");

                            //   int status = data.getInt("status");

                            //      if (status == 0) {



                            //      JSONObject ndata = data.getJSONObject("data");

                            //      final String contact_detail = ndata.getString("contact_detail");
                            //      final String otp = ndata.getString("otp");

                             sender_id = results.getString("sender_id");


                             newContactDetail=contact_detail;

                             newcontact_type=contact_type;



                            LoggerGeneral.info("senderid0000---"+sender_id+"---"+SmsReceiver.senderid);

                                if (newmdialog != null){
                                    newmdialog.dismiss();


                                    Common.showToast(context,"Otp has been sent to your mobile number");

                                    dialog = new Dialog(Register.this);
                                    dialog.setContentView(R.layout.custom_popup);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                    stimer = (TextView) dialog.findViewById(R.id.timer);

                                    resend = (TextView) dialog.findViewById(R.id.resend);

                                    verifymobedtxt = (EditText) dialog.findViewById(R.id.verifymobedtxt);


                                    if (countDownTimer != null) {
                                        countDownTimer.cancel();
                                    }

                                    countDownTimer = new CountDownTimer(59000, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            stimer.setVisibility(View.VISIBLE);
                                            resend.setVisibility(View.GONE);
                                            stimer.setText(" " + "00:" + millisUntilFinished / 1000);
                                        }

                                        public void onFinish() {
                                            //  stimer.setText(" Re-Send");
                                            stimer.setVisibility(View.GONE);
                                            resend.setVisibility(View.VISIBLE);
                                        }
                                    }.start();


                                    resend.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog.dismiss();
                                            new sendOtp(contact_type, contact_detail).execute();
                                            countDownTimer = new CountDownTimer(59000, 1000) {

                                                public void onTick(long millisUntilFinished) {

                                                    stimer.setVisibility(View.VISIBLE);
                                                    resend.setVisibility(View.GONE);
                                                    stimer.setText(" " + "00:" + millisUntilFinished / 1000);

                                                }

                                                public void onFinish() {
                                                    stimer.setVisibility(View.VISIBLE);
                                                    resend.setVisibility(View.GONE);
                                                    //      stimer.setText(" Re-Send");
                                                }
                                            }.start();
                                        }
                                    });


                                    LinearLayout verify = (LinearLayout) dialog.findViewById(R.id.verifyb);
                                    verify.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            votp = verifymobedtxt.getText().toString().trim();

                                            if (votp != null && votp.length() > 0) {

                                                LoggerGeneral.info("newcd---" + newContactDetail);

                                                new verifyOtp(contact_type, contact_detail, votp).execute();

                                            } else {
                                                Common.showToast(context, "Enter OTP");
                                            }

                                            countDownTimer.cancel();
                                        }
                                    });

                                    dialog.show();


                                }
                                //   stimer.setText(" Re-Send");





                            //      }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                if(contact_type.equalsIgnoreCase("email")){



                    try {


                        JSONObject meta = results.getJSONObject("meta");

                        int status_code = meta.getInt("status_code");


                        if (status_code == 0) {
                            //        JSONObject data = results.getJSONObject("data");

                            //       int status = data.getInt("status");

                            //       if (status == 0) {
                            mDialog.dismiss();

                            dialog = new Dialog(Register.this);
                            dialog.setContentView(R.layout.custompopup_email);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setCanceledOnTouchOutside(true);
                            stimer = (TextView) dialog.findViewById(R.id.timer);
                            resend = (TextView) dialog.findViewById(R.id.resend);
                            verifymobedtxt = (EditText) dialog.findViewById(R.id.verifymobedtxt);

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }

                            countDownTimer = new CountDownTimer(59000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    stimer.setVisibility(View.VISIBLE);
                                    resend.setVisibility(View.GONE);
                                    stimer.setText(" " + "00:" + millisUntilFinished / 1000);
                                }

                                public void onFinish() {
                                    stimer.setVisibility(View.GONE);
                                    resend.setVisibility(View.VISIBLE);
                                    //  stimer.setText(" Re-Send");
                                }
                            }.start();


            /*    if(stimer.getText().toString().equalsIgnoreCase(" Re-Send")) {
                    stimer.setClickable(true);
                }
                else
                {
                    stimer.setClickable(false);
                }*/
                            resend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {



                                    dialog.dismiss();

                                    new sendOtp(contact_type, contact_detail).execute();

                                    countDownTimer = new CountDownTimer(59000, 1000) {

                                        public void onTick(long millisUntilFinished) {

                                            stimer.setVisibility(View.VISIBLE);
                                            resend.setVisibility(View.GONE);
                                            stimer.setText(" " + "00:" + millisUntilFinished / 1000);

                                        }

                                        public void onFinish() {
                                            stimer.setVisibility(View.GONE);
                                            resend.setVisibility(View.VISIBLE);
                                            //   stimer.setText(" Re-Send");
                                        }
                                    }.start();
                                }
                            });

                            LinearLayout verify = (LinearLayout) dialog.findViewById(R.id.verifyb);


                            verify.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //      dialog.dismiss();


                                    votp = verifymobedtxt.getText().toString().trim();


                                    if(votp!=null && votp.length()>0) {
                                        new verifyOtp(contact_type, contact_detail, votp).execute();

                                    }
                                    else {
                                        Common.showToast(context,"Enter OTP");
                                    }
                                    countDownTimer.cancel();
                                }
                            });
                            dialog.show();

                            //         }
                        }
                    }catch (JSONException e){

                    }

                }
            }
        }

    }

    class  verifyOtp extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String contact_type;
        String contact_detail;
        String votp;

        public verifyOtp(String contact_type,String contact_detail,String votp){

            this.contact_type = contact_type;
            this.contact_detail=contact_detail;
            this.votp = votp;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.VerifyOtp;

            JSONObject object = new JSONObject();
            try {

                object.put("contact_type",contact_type);
                object.put("contact_detail",contact_detail);
                object.put("otp",votp);
                LoggerGeneral.info("JsonObjectPrint" + object.toString());

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

            mDialog = new ProgressDialog(Register.this,ProgressDialog.THEME_HOLO_DARK);
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
            mDialog.dismiss();

            if(results!=null){

                try {


                    JSONObject meta = results.getJSONObject("meta");

                    int status_code = meta.getInt("status_code") ;


                    if(status_code==0){

                        if(dialog!=null) {
                            dialog.dismiss();
                        }
                        String message = meta.getString("message") ;

                        if(contact_type.equalsIgnoreCase("mobile")) {
                            Common.showToast(context, "Mobile number successfully verified");






/*
                            mobile.setHint(umobno);
                            mobile.setClickable(false);
                            mobile.setKeyListener(null);
                            mobile.setPressed(false);
                            mobile.setFocusable(false);
                            mobile.setEnabled(false);*/

                            //   mobilebool=true;
                            mobilebool="1";
                            appPreferences.putString("mobilebool", mobilebool);


                            verifydialog.setText("Verified");
                            verifydialog.setEnabled(false);

                            mobile.setVisibility(View.GONE);
                            mobilevd.setVisibility(View.VISIBLE);
                            mobilevd.setClickable(false);
                            mobilevd.setFocusable(false);
                            mobilevd.setText(umobno);

                            //  mobilevd.setEnabled(false);


                        }
                        if(contact_type.equalsIgnoreCase("email")){
                            Common.showToast(context, "Email id  successfully verified");

                            verifymailid.setText("Verified");
                            verifymailid.setEnabled(false);
                         /*   uemailid = email.getText().toString().trim();
                            email.setHint(uemailid);
                            email.setClickable(false);
                            email.setKeyListener(null);
                            email.setPressed(false);
                            email.setFocusable(false);
                            email.setEnabled(false);
*/

                            //     emailbool=true;
//                            appPreferences.putBoolean("emailbool",emailbool);

                            emailbool="1";
                            appPreferences.putString("emailbool", emailbool);
                            email.setVisibility(View.GONE);
                            emailvd.setVisibility(View.VISIBLE);
                            emailvd.setText(uemailid);

                            LoggerGeneral.info("showemailtxt---"+uemailid);

                        }

                    }

                    if(status_code==1){

                        Common.showToast(context,"Enter a valid OTP");


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent back = new Intent(Register.this,Login.class);
        startActivity(back);
        finish();
    }
    private void requestPermission(){

        if(permissionChecker==1)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionChecker=0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

            } else {
                permissionChecker=0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        }
    }


    private boolean checkPermission2(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){

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

        final Dialog mBottomSheetDialog = new Dialog (Register.this,
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

        Bitmap resizedBMP = getResizedBitmap(newBMP, 500,500);


        ByteArrayOutputStream bs = new ByteArrayOutputStream();

		/*FileOutputStream bs;
		try {
			bs =  new FileOutputStream(f);
			resizedBMP.compress(Bitmap.CompressFormat.JPEG,100, bs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/

        resizedBMP.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        mBitmap = BitmapFactory.decodeByteArray(
                bs.toByteArray(), 0,
                bs.toByteArray().length);
        // FileOutputStream out = new FileOutputStream(new File();)

        /*if(Common.isOnline(context)) {
            new GetImageUpload().execute();
        }
        else {
            Common.showToast(context,"No internet connection");
        }*/
        //   editimage.setImageBitmap(mBitmap);

        rphoto.setImageBitmap(newBMP);
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
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

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


            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 2);
            intent.putExtra("aspectY", 2);
            intent.putExtra("scale", true);
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
}

