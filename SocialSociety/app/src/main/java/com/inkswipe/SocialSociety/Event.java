package com.inkswipe.SocialSociety;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import model.EventListmodel;
import util.AppPreferences;
import util.CalendarUtils;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;


/**
 * A simple {@link Fragment} subclass.
 */
public class Event extends Fragment {


    Context context;
    Calendar myCalendar;
    CustomCalendarView calendarView;
    String[] mStringArray,mstrinArraystid;
    LinearLayout createevent,archived;
    Calendar currentCalendar;
    String SocietyId;

    AppPreferences appPreferences;


    String eid;
    String esociety_id;
    String etitle;
    String eevent_date;
    String edescription;
    String eevent_image;
    String eaddress;
    String elandmark;
    String estate;
    String ecity;
    String epostal_code;
    String eshared_with;
    String ecreated_by;
    String ecreated_on;
    String estatus;
    String eevent_image_url;
    String euser_name;
    String euser_profile_image;
    String event_date_time;
    String Datess;
    String getSocietyId;
    String days_pass;

    String jeventDate;
    List<EventListmodel> evenlist;

    EventListmodel eventModel;

    String Dates[] =  new String []{"03-10-2016","30-10-2016","04-10-2016","12-10-2016","24-10-2016","26-10-2016","24-09-2016","19-09-2016","11-09-2016","15-09-2016","1-09-2016","27-11-2016","8-11-2016","1-11-2016","10-11-2016","16-11-2016","21-11-2016","23-11-2016","10-12-2016","25-12-2016","4-12-2016","12-12-2016","20-08-2016","4-08-2016","12-08-2016","16-12-2016","21-12-2016","23-12-2016"};

    /*String November[] =  new String []{"11","27-11-2016","29-11-2016","1-11-2016","10-11-2016","20-11-2016","24-11-2016"};

    String December[] =  new String []{"12","28-12-2016","30-12-2016","4-12-2016","12-12-2016","24-12-2016","26-12-2016"};
    String September[] =  new String []{"27-09-2016","29-09-2016","1-09-2016","10-09-2016","20-09-2016","24-09-2016"};*/

  //  String dates[] =  new String []{"27"}



    ArrayList<String>eventdatesht,passdates;

    static GetEventDates eventAsyncTask;

    @SuppressLint("ValidFragment")
    public Event(String SocietyId) {

        this.SocietyId = SocietyId;


        LoggerGeneral.info("Society event id22---12"+SocietyId);

        // Required empty public constructor
    }

    public Event( ) {


        // Required empty public constructor
    }

//AA:8A:89:FE:17:C5:2C:1E:6F:91:97:3B:62:9A:F7:8C:D1:D8:A2:E2

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_event, container, false);

        context = getActivity();
        appPreferences =AppPreferences.getAppPreferences(context);
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        myCalendar = Calendar.getInstance();

        evenlist = new ArrayList<EventListmodel>();

        passdates = new ArrayList<String>();


        calendarView = (CustomCalendarView)view.findViewById(R.id.calendar_view);
        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
              //  LoggerGeneral.info("indateselected1");

                if (!CalendarUtils.isPastDay(date)) {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    //    selectedDateTv.setText("Selected date is " + df.format(date));

                } else {
                    //   selectedDateTv.setText("Selected date is disabled!");
                }


                SimpleDateFormat mdf = new SimpleDateFormat("yyyy-MM-dd");
                Intent eventList = new Intent(context, EventsList.class);

            if(mStringArray!=null) {
                if (mStringArray.length > 0) {
                    for (int i = 0; i <= mStringArray.length - 1; i++) {
                        if (mStringArray[i].contains(mdf.format(date))) {

                            eventList.putExtra("event", "yesevent");
                            eventList.putExtra("dateofevent", mdf.format(date));
                            eventList.putExtra("SocietyId", SocietyId);

                            Common.internet_check=0;
                            startActivity(eventList);
                            getActivity().finish();

                            //  LoggerGeneral.info("indateselected1" + "-----" + mStringArray.toString() + mdf.format(date) + "---" + mStringArray[0] + "===" + mStringArray[1]);
                            break;
                        }
                        // else{
                        if (!(mStringArray[i].contains(mdf.format(date)))) {

                            eventList.putExtra("event", "noevent");
                            eventList.putExtra("dateofevent", mdf.format(date));
                            eventList.putExtra("SocietyId", SocietyId);
                            Common.internet_check=0;
                            startActivity(eventList);

                            getActivity().finish();
                            //   LoggerGeneral.info("indateselected2"+"-----"+mStringArray.toString()+mdf.format(date)+"---"+mStringArray[0]+"==="+mStringArray[1]);
                        }
                    }
                }

                if (mStringArray.length == 0) {
                    eventList.putExtra("event", "noevent");
                    eventList.putExtra("dateofevent", mdf.format(date));
                    eventList.putExtra("SocietyId", SocietyId);
                    Common.internet_check=0;
                    startActivity(eventList);

                    getActivity().finish();

                }
            }

                else{
                eventList.putExtra("event", "noevent");
                eventList.putExtra("dateofevent", mdf.format(date));
                eventList.putExtra("SocietyId", SocietyId);
                Common.internet_check=0;
                startActivity(eventList);

                getActivity().finish();
            }
                        }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");

                SimpleDateFormat mdf = new SimpleDateFormat("dd-MM-yyyy");

                CalendarUtils.isMonthSame(date);

                String newDate = mdf.format(date.getMonth());

                SimpleDateFormat dateFormat = new SimpleDateFormat( "LLLL", Locale.getDefault());
                dateFormat.format(date);

                String monthname=(String)android.text.format.DateFormat.format("MMMM", date);

                LoggerGeneral.info("showmonthchanged000---" + CalendarUtils.isMonthSame(date)+"==="+String.valueOf(date.getMonth())+"---"+newDate+"---"+mdf.format(date)+"---"+monthname);
                if(CalendarUtils.isMonthSame(date)){
                    LoggerGeneral.info("showmonthchanged111---"+CalendarUtils.isMonthSame(date));
                }
                if(!CalendarUtils.isMonthSame(date)){
                    LoggerGeneral.info("showmonthchanged222----"+CalendarUtils.isMonthSame(date));
                }

            }
        });


        eventdatesht = new ArrayList<String>();

        if(Common.isOnline(context)){

            eventAsyncTask = new GetEventDates();
            eventAsyncTask.execute();

            //   new GetEventDates().execute();
        }
        else{


            Common.showToast(context,"No internet connection");
        }





        //Highlight dates proper
     /* List<DayDecorator> decorators = new ArrayList<DayDecorator>();
        decorators.add(new HighletedColorDecorator(Dates));
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);*/

        createevent = (LinearLayout)view.findViewById(R.id.createevent);
        createevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createevent = new Intent(context, CreateEvent.class);
                createevent.putExtra("SocietyId",SocietyId);
                startActivity(createevent);


            }
        });

        archived = (LinearLayout)view.findViewById(R.id.archived);
        archived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent archive = new Intent(context, Archivedevent.class);
                archive.putExtra("SocietyId",SocietyId);

                startActivity(archive);

            }
        });

            return view;
    }

    class  GetEventDates extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getEventDates;

            JSONObject object = new JSONObject();
            try {

                object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));



                LoggerGeneral.info("JsonObjectPrintEvent" + object.toString());

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

            mDialog = new ProgressDialog(getActivity(),ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Processing...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }
        @Override
        protected void onCancelled(){

            super.onCancelled();
            mDialog.dismiss();
            LoggerGeneral.info("onCancelled");

        }
        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresultsstate" + results);
            //mDialog.dismiss();

            if(results!=null){

                try {


                    appPreferences.putString("eventResponse",results.toString());
                    JSONObject meta = results.getJSONObject("meta");

                    int status_code = meta.getInt("status_code");

                    int account_status = 1;
                    if(meta.has("account_status")){
                        if(!meta.isNull("account_status")){
                            account_status = meta.getInt("account_status");
                        }
                    }


                    if(account_status==1){
                        if (status_code == 0) {


                            JSONArray jsonArray = results.getJSONArray("data");



                            for(int i=0;i<=jsonArray.length()-1;i++){

                                JSONObject jsonObject  = jsonArray.getJSONObject(i);

                                eid                    = jsonObject.getString("id");
                                esociety_id            = jsonObject.getString("society_id");
                                etitle                 = jsonObject.getString("title");
                                eevent_date            = jsonObject.getString("event_date");


                                /*String oldFormat= "yyyy-MM-dd hh:mm:ss";
                                String newFormat= "dd-MM-yyyy";
                                String formatedDate = "";
                                String formatedDate2 = "";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
                                Date myDate = null;
                                Date mtDate2=null;
                                try {
                                    myDate = dateFormat.parse(eevent_date);
                                    //mtDate2 = dateFormat.parse(event_date_time);
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }

                                SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
                                formatedDate = timeFormat.format(myDate);
                                formatedDate2 = timeFormat.format(myDate);


                                LoggerGeneral.info("dateformat---" + formatedDate+"---"+myDate+"---"+eevent_date);
                             //   SimpleDateFormat mdf = new SimpleDateFormat("dd-MM-yyyy");*/


                                edescription           = jsonObject.getString("description");
                                eevent_image           = jsonObject.getString("event_image");
                                eaddress               = jsonObject.getString("address");
                                elandmark              = jsonObject.getString("landmark");
                                estate                 = jsonObject.getString("state");
                                ecity                  = jsonObject.getString("city");
                                epostal_code           = jsonObject.getString("postal_code");
                                eshared_with           = jsonObject.getString("shared_with");
                                ecreated_by            = jsonObject.getString("created_by");
                                ecreated_on            = jsonObject.getString("created_on");
                                estatus                = jsonObject.getString("status");
                                eevent_image_url       = jsonObject.getString("event_image_url");
                                event_date_time        = jsonObject.getString("event_date_time");
                                euser_name             = jsonObject.getString("user_name");
                                euser_profile_image    = jsonObject.getString("user_profile_image");
                                Datess                 = jsonObject.getString("date");
                                days_pass              = jsonObject.getString("days_pass");


                                eventModel = new EventListmodel();



                                eventModel.setEid(eid);
                                eventModel.setEsociety_id(esociety_id);
                                eventModel.setEtitle(etitle);
                                eventModel.setEevent_date(eevent_date);
                                eventModel.setEdescription(edescription);
                                eventModel.setEevent_image(eevent_image);
                                eventModel.setEaddress(eaddress);
                                eventModel.setElandmark(elandmark);
                                eventModel.setEstate(estate);
                                eventModel.setEcity(ecity);
                                eventModel.setEpostal_code(epostal_code);
                                eventModel.setEshared_with(eshared_with);
                                eventModel.setEcreated_by(ecreated_by);
                                eventModel.setEcreated_on(ecreated_on);
                                eventModel.setEstatus(estatus);
                                eventModel.setEevent_image_url(eevent_image_url);
                                eventModel.setEuser_name(euser_name);
                                eventModel.setEuser_profile_image(euser_profile_image);
                                eventModel.setEvent_date_time(event_date_time);
                                eventModel.setsDate(Datess);
                                eventModel.setDays_pass(days_pass);

                                evenlist.add(eventModel);



                            }

                            onCaledarDisplay();
                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(context,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        Common.internet_check=0;
                        startActivity(intent);
                        ((Activity)context).finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


            }
        }

    }


    public void onCaledarDisplay(){


               ArrayList<String>arr= new ArrayList<String>();

                ArrayList<String>arrst = new ArrayList<String>();
                LoggerGeneral.info("showstarr"+evenlist.size()+"---"+evenlist.toString());
                for(int i=0;i<=evenlist.size()-1;i++) {


                    String array = evenlist.get(i).getsDate();


                    arr.add(array);

                    passdates.add(i,array);


                }

                mStringArray = new String[arr.size()];
                mStringArray = arr.toArray(mStringArray);


                //eventdatesht.add(mStringArray[i]);

                Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());


                calendarView.setFirstDayOfWeek(Calendar.MONDAY);

                //Show/hide overflow days of a month
                calendarView.setShowOverflowDate(false);

                //call refreshCalendar to update calendar the view

                List<DayDecorator> decorators = new ArrayList<DayDecorator>();
                decorators.add(new HighletedColorDecorator(mStringArray));
                calendarView.setDecorators(decorators);
                calendarView.refreshCalendar(currentCalendar);
                LoggerGeneral.info("arrayyy---" + mStringArray + "---" + arr.size());



    }

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);

        }
    };
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);


    }
    private class HighletedColorDecorator implements DayDecorator {

        String [] dates;




        public HighletedColorDecorator(String[]dates){
            this.dates = dates;
        }

        @Override
        public void decorate(DayView dayView) {

          /*  if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = Color.parseColor("#a9afb9");
              //  dayView.setBackgroundColor(color);
                  dayView.setBackgroundResource(R.drawable.calendarcirlce);
            }
*/


            SimpleDateFormat mdf = new SimpleDateFormat("yyyy-MM-dd");

                LoggerGeneral.info("indecorat4"+(CalendarUtils.isTodayDay(dayView.getDate()))+"---"+dayView.getDate()+"==="+mdf.format(dayView.getDate()));
                for(int i=0;i<=mStringArray.length-1;i++) {
                    //     if(October[i].contains(String.valueOf(mdf.format(dayView.getDate())))) {
             //       if(October[i].contains("03-10-2016")) {
                    //    LoggerGeneral.info("indecorat5" + (CalendarUtils.isTodayDay(dayView.getDate())) + "---" + dayView.getDate() + "===" + mdf.format(dayView.getDate()));

                      //  dayView.setBackgroundResource(R.drawable.calendarcirlce);



                        if (mdf.format(dayView.getDate()).equals(mStringArray[i])) {
                            dayView.setBackgroundResource(R.drawable.calendarcirlce);

                        }

               //     }
                }

            }



    }


    private class NewHighletedColorDecorator implements DayDecorator {

      //  String [] dates;

        ArrayList <String>datesht ;



        public NewHighletedColorDecorator(ArrayList <String>datesht){
            this.datesht = datesht;
        }

        @Override
        public void decorate(DayView dayView) {

          /*  if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = Color.parseColor("#a9afb9");
              //  dayView.setBackgroundColor(color);
                  dayView.setBackgroundResource(R.drawable.calendarcirlce);
            }
*/


            SimpleDateFormat mdf = new SimpleDateFormat("dd-MM-yyyy");

          //  LoggerGeneral.info("indecorat4"+(CalendarUtils.isTodayDay(dayView.getDate()))+"---"+dayView.getDate()+"==="+mdf.format(dayView.getDate()));
            for(int i=0;i<=datesht.size();i++) {
                //     if(October[i].contains(String.valueOf(mdf.format(dayView.getDate())))) {
                //       if(October[i].contains("03-10-2016")) {
                //    LoggerGeneral.info("indecorat5" + (CalendarUtils.isTodayDay(dayView.getDate())) + "---" + dayView.getDate() + "===" + mdf.format(dayView.getDate()));

                //  dayView.setBackgroundResource(R.drawable.calendarcirlce);



                if (mdf.format(dayView.getDate()).equals(datesht.get(i))) {
                    dayView.setBackgroundResource(R.drawable.calendarcirlce);

                }

                //     }
            }

        }



    }
}

























  /*  new DatePickerDialog(context, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //DO SOMETHING
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        }, Calendar.YEAR, Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH).show();*/
// Listener




//Calendar Popup Working
 /* DatePickerDialog datePicker = new DatePickerDialog(context,
                R.style.DialogTheme, datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);

        datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent eventList = new Intent(context, EventsList.class);
                        startActivity(eventList);

                    }
                });
        datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent eventList = new Intent(context,DashBoard.class);
                        startActivity(eventList);

                    }
                });
        datePicker.show();

        calendar working popup                    */