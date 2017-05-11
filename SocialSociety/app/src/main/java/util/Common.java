package util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.inkswipe.SocialSociety.Login;
import com.inkswipe.SocialSociety.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ajinkya on 4/8/2016.
 */
public class Common {
    private static AppPreferences appPrefs;
    public static int internet_check=0;
    public static boolean appcheck=true;
    public static boolean validateMobileno(String inputMobile) {
        String regexStr = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(inputMobile);

        if (matcher.find()) {

            return true;
        }

        return false;

    }

    public static String formatUrl(String url) {
        String urlStringReplace = null;
        String replaceAllSpace = null;
        if (url != null) {
            if (url.contains("\\")) {
                urlStringReplace = url.replace("\\", "//");
            } else {
                urlStringReplace = url;
            }

            if (urlStringReplace.contains(" ")) {
                replaceAllSpace = urlStringReplace.replaceAll(" ", "%20");
            } else {
                replaceAllSpace = urlStringReplace;
            }

            LoggerGeneral.info("Formatted url is " + replaceAllSpace);
        }

        return replaceAllSpace;

    }

    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();

    }

    public static boolean isOnline(Context con) {
        boolean connected = false;
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable()
                    && networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            LoggerGeneral
                    .info("CheckConnectivity Exception: " + e.getMessage());
            LoggerGeneral.info("connectivity" + e.toString());
        }
        return connected;
    }
    public static void showToast(Context context,String toasts)
    {
        final Toast toast=Toast.makeText(context, toasts, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 4000);
    }

    public static String getStandardStringFormateDate(Calendar c) throws Exception {

        // dd/MM/yyyy
        long date = c.getTimeInMillis();
        Date d = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        String conDate = formatter.format(d);
        return conDate;

    }

    public Calendar convertStringToDate(String s) {
        try {
            String FORMAT_DATETIME = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);

            Date date = sdf.parse(s);
            Calendar cal = Calendar.getInstance();

            cal.setTime(date);
            return cal;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getStandardStringMonthFormateDate(Calendar c)
            throws Exception {

        // dd/MM/yyyy
        long date = c.getTimeInMillis();
        Date d = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        String conDate = formatter.format(d);
        return conDate;

    }

    public String getdateformat(Calendar c) throws Exception {

        // dd/MM/yyyy
        long date = c.getTimeInMillis();
        Date d = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        String conDate = formatter.format(d);
        return conDate;

    }
    public static String ConvertDateintoStringFormat(Calendar c) throws Exception {

        // dd/MM/yyyy
        long date = c.getTimeInMillis();
        Date d = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String conDate = formatter.format(d);
        return conDate;

    }

    public String convertDateMonthYear(Calendar c) throws Exception {

        // dd/MM/yyyy
        long date = c.getTimeInMillis();
        Date d = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        String conDate = formatter.format(d);
        return conDate;

    }

    /**
     *
     *
     * @param c
     * @return
     * @throws Exception
     */
    public String convertDateMonthYearSpace(Calendar c) throws Exception {

        // dd/MM/yyyy
        long date = c.getTimeInMillis();
        Date d = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        String conDate = formatter.format(d);
        return conDate;

    }

    public String convertDateMonthYearServicing(Calendar c) throws Exception {

        // dd/MM/yyyy
        long date = c.getTimeInMillis();
        Date d = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        String conDate = formatter.format(d);
        return conDate;

    }
    public CharSequence filter(CharSequence cs, int start,
                               int end, Spanned spanned, int dStart, int dEnd) {
        // TODO Auto-generated method stub
        if(cs.equals("")){ // for backspace
            return cs;
        }
        if(cs.toString().matches("[a-zA-Z ]+")){
            return cs;
        }
        return "";
    }

    /**
     *
     *
     * @param c
     * @return
     * @throws Exception
     */
    public String convertDateMonthYearSpaceOnly(Calendar c) throws Exception {

        // dd/MM/yyyy
        long date = c.getTimeInMillis();
        Date d = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String conDate = formatter.format(d);
        return conDate;

    }

    public String ConvertDateintoMonthyearFormat(Calendar c) throws Exception {

        // dd/MM/yyyy
        long date = c.getTimeInMillis();
        Date d = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd");
        String conDate = formatter.format(d);
        return conDate;

    }
    public static int daysToDeduct(int dayOfWeek) {

        int noOfDays = 0;
        // Sunday 1 Saturady 7
        switch (dayOfWeek) {
            case 1:

                noOfDays = 0;
                break;

            case 2:

                noOfDays = 1;

                break;
            case 3:

                noOfDays = 2;

                break;
            case 4:
                noOfDays = 3;
                break;
            case 5:
                noOfDays = 4;
                break;

            case 6:
                noOfDays = 5;
                break;

            case 7:
                noOfDays = 6;
                break;

            default:
                break;
        }
        return noOfDays;

    }

   /* public static Typeface getTypeface(Context ctxt) {
        Typeface tf = null;

        String language = null;
        getSingleTonPrefs(ctxt);
        language = appPrefs.getString(ctxt.getString(R.string.pref_locale), "");
        if (language.equalsIgnoreCase("en")) {
            tf = Typeface.createFromAsset(ctxt.getAssets(),
                    "OpenSans-Regular.ttf");
        } else if (language.equalsIgnoreCase("hi")) {
            tf = Typeface.createFromAsset(ctxt.getAssets(), "K55.TTF");
        } else if (language.equalsIgnoreCase("kn")) {
            tf = Typeface
                    .createFromAsset(ctxt.getAssets(), "Lohit-Kannada.ttf");
        } else if (language.equalsIgnoreCase("te")) {
            tf = Typeface.createFromAsset(ctxt.getAssets(),
                    "OpenSans-Regular.ttf");
        } else if (language.equalsIgnoreCase("ta")) {
            tf = Typeface.createFromAsset(ctxt.getAssets(),
                    "OpenSans-Regular.ttf");
        } else {
            tf = Typeface.createFromAsset(ctxt.getAssets(),
                    "OpenSans-Regular.ttf");
        }
        return tf;
    }*/
    private static AppPreferences getSingleTonPrefs(Context ctxt) {
        if (appPrefs == null) {
            appPrefs = AppPreferences.getAppPreferences(ctxt);
        }

        return appPrefs;

    }
    public static boolean hasSpecialCharExceptSpace(String x) {
        Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})");
        return p.matcher(x).find();
    }

    public static Typeface font(Context context,String ffName)
    {
        Typeface typeFace;
        if(ffName=="AvenirNextLTProRegular.otf"||ffName.equals("AvenirNextLTProRegular.otf"))
        {
            typeFace=Typeface.createFromAsset(context.getAssets(),"AvenirNextLTProRegular.otf");
            return typeFace;
        }else if(ffName=="UbuntuM.ttf"||ffName.equals("UbuntuM.ttf"))
        {
            typeFace=Typeface.createFromAsset(context.getAssets(),"UbuntuM.ttf");
            return typeFace;
        }else if(ffName=="AvenirMedium.ttf"||ffName.equals("AvenirMedium.ttf"))
        {
            typeFace=Typeface.createFromAsset(context.getAssets(),"AvenirMedium.ttf");
            return typeFace;
        }else if(ffName=="UbuntuB.ttf"||ffName.equals("UbuntuB.ttf"))
        {
            typeFace=Typeface.createFromAsset(context.getAssets(),"UbuntuB.ttf");
            return typeFace;
        }
        else if(ffName=="UbuntuBOLD.ttf"||ffName.equals("UbuntuBOLD.ttf"))
        {
            typeFace=Typeface.createFromAsset(context.getAssets(),"UbuntuBOLD.ttf");
            return typeFace;
        }
        else if(ffName=="AvenirNextLTProBold.otf"||ffName.equals("AvenirNextLTProBold.otf"))
        {
            typeFace=Typeface.createFromAsset(context.getAssets(),"AvenirNextLTProBold.otf");
            return typeFace;
        }
        else if(ffName=="Helvetica.otf"||ffName.equals("Helvetica.otf"))
        {
            typeFace=Typeface.createFromAsset(context.getAssets(),"Helvetica.otf");
            return typeFace;
        }
        else if(ffName=="arial.ttf"||ffName.equals("arial.ttf"))
        {
            typeFace=Typeface.createFromAsset(context.getAssets(),"arial.ttf");
            return typeFace;
        }
        return null;
    }

    public static File getChacheDir(Context context) {

        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(Environment.getExternalStorageDirectory(), "/.SocialSociety");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();

        return cacheDir;

    }

    public static File createNewFileOrOverwrite(File dir, String fileName) {

        File f = new File(dir, fileName);
        if (!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return f;
    }

    public static void saveBitmapToFile(Bitmap image, File f) {

        // ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileOutputStream bos;
        try {
            bos = new FileOutputStream(f);
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap decodeFile(File f) {

        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 100;
            int actual_image_width = o.outWidth, actual_image_height = o.outHeight;
            int scale = 1;
            while (true) {
                if (actual_image_width / 2 < REQUIRED_SIZE || actual_image_height / 2 < REQUIRED_SIZE)
                    break;
                actual_image_width /= 2;
                actual_image_height /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

            try {
                ExifInterface ei = new ExifInterface(f.getAbsolutePath());
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        return rotate(bm, 90);

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        return rotate(bm, 180);

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        return rotate(bm, 270);

                    case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                        return flip(bm, true, false);

                    case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                        return flip(bm, false, true);

                    default:
                        return bm;
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bm;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LoggerGeneral.info("returning null");
        return null;
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static boolean zeroCheck(String no)
    {
        for(int i=0;i<no.length()-1;i++)

        {
            if(String.valueOf(no.charAt(i)).equals("0"))
            {

            }
            else
            {
                return false;
            }
        }

        return true;
    }

    public static void copyDirectory(String sourceFilename , String destinationFilename)
            throws IOException {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {

        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {

            }
        }
    }





}
