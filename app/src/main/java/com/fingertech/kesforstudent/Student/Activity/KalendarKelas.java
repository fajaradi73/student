package com.fingertech.kesforstudent.Student.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.fingertech.kesforstudent.Student.Adapter.CalendarAdapter;
import com.fingertech.kesforstudent.Controller.Auth;
import com.fingertech.kesforstudent.Student.Model.CalendarModel;
import com.fingertech.kesforstudent.R;
import com.fingertech.kesforstudent.Rest.ApiClient;
import com.fingertech.kesforstudent.Rest.JSONResponse;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.pepperonas.materialdialog.MaterialDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KalendarKelas extends AppCompatActivity {

    CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormat     = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    private SimpleDateFormat bulanFormat    = new SimpleDateFormat("MM", Locale.getDefault());
    private SimpleDateFormat tahunFormat    = new SimpleDateFormat("yyyy", Locale.getDefault());
    private SimpleDateFormat formattanggal  = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    TextView month_calender;
    ImageView left_month,right_month;
    Auth mApiInterface;
    int status;
    String code;
    String authorization,school_code,student_id,classroom_id,calendar_month,calendar_year;
    RecyclerView recyclerView;
    CalendarAdapter calendarAdapter;
    List<Event> eventList,events;

    List<JSONResponse.DataCalendar> calendarList;

    CalendarModel calendarModel;
    List<CalendarModel> calendarModelList;
    Date date;
    String calendar_id,calendar_type,calendar_colour,calendar_time,calendar_date,calendar_title;
    Toolbar toolbar;
    LinearLayout kalendar;
    SharedPreferences sharedPreferences;
    String hari;
    String tanggal,jam,deskripsi,judul,guru,warna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kalendar_kelas);

        compactCalendarView = findViewById(R.id.compactcalendar_view);
        month_calender      = findViewById(R.id.month_calender);
        left_month          = findViewById(R.id.left_calender);
        right_month         = findViewById(R.id.right_calender);
        mApiInterface       = ApiClient.getClient().create(Auth.class);
        recyclerView        = findViewById(R.id.recylceview_calendar);
        toolbar             = findViewById(R.id.toolbar_kalendar);
        kalendar            = findViewById(R.id.no_kalendar);

        sharedPreferences   = getSharedPreferences(MenuUtama.my_viewpager_preferences, Context.MODE_PRIVATE);
        authorization       = sharedPreferences.getString("authorization",null);
        school_code         = sharedPreferences.getString("school_code",null);
        student_id          = sharedPreferences.getString("member_id",null);
        classroom_id        = sharedPreferences.getString("classroom_id",null);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        compactCalendarView.setUseThreeLetterAbbreviation(true);

        month_calender.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        calendar_month = bulanFormat.format(compactCalendarView.getFirstDayOfCurrentMonth());
        if(calendar_month.substring(0,1).equals("0"))
        {
            calendar_month = calendar_month.substring(1);
        }
        hari    = formattanggal.format(Calendar.getInstance().getTime());
        calendar_year   = tahunFormat.format(compactCalendarView.getFirstDayOfCurrentMonth());
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                hari = formattanggal.format(dateClicked);
                if (calendarModelList != null) {
                    calendarModelList.clear();
                    if (calendarList != null) {
                        for (JSONResponse.DataCalendar calendar : calendarList) {
                            Calendar cal = Calendar.getInstance();
                            calendar_date = calendar.getCalendar_date();
                            calendar_time = calendar.getCalendar_time();
                            calendar_type = calendar.getCalendar_type();
                            calendar_colour = calendar.getCalendar_colour();
                            if (calendar_date.equals(hari)) {
                                if (calendar_date.equals(hari)) {
                                    if (calendar_type.equals("-1") || calendar_type.equals("-2")) {
                                        calendarModel = new CalendarModel();
                                        calendarModel.setCalendar_id(String.valueOf(calendar.getCalendar_id()));
                                        calendarModel.setCalendar_time("Seharian");
                                        calendarModel.setCalendar_date(converDate(calendar.getCalendar_date()));
                                        String sentence = calendar.getCalendar_title();
                                        String replaced = sentence.replace("Hari Libur - ", "");
                                        calendarModel.setCalendar_title(replaced);
                                        calendarModel.setCalendar_color(calendar_colour);
                                        calendarModel.setCalendar_desc(calendar.getCalendar_desc());
                                        calendarModel.setCalendar_type(calendar_type);
                                        calendarModelList.add(calendarModel);
                                    } else {
                                        calendarModel = new CalendarModel();
                                        calendarModel.setCalendar_id(String.valueOf(calendar.getCalendar_id()));
                                        calendarModel.setCalendar_time(calendar.getCalendar_time());
                                        calendarModel.setCalendar_date(converDate(calendar.getCalendar_date()));
                                        calendarModel.setCalendar_title(calendar.getCalendar_title());
                                        calendarModel.setCalendar_desc(calendar.getCalendar_desc());
                                        calendarModel.setCalendar_type(calendar_type);
                                        calendarModel.setCalendar_color(calendar_colour);
                                        calendarModelList.add(calendarModel);
                                    }
                                }
                                if (compactCalendarView.getEvents(dateClicked) != null) {
                                    kalendar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (compactCalendarView.getEvents(dateClicked).size() == 0) {
                                    kalendar.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            }
                            calendarAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if (eventList != null){
                    compactCalendarView.removeAllEvents();
                    compactCalendarView.removeEvents(eventList);
                }
                if (events != null){
                    compactCalendarView.removeAllEvents();
                    compactCalendarView.removeEvents(events);
                }
                month_calender.setText(dateFormat.format(firstDayOfNewMonth));
                calendar_month = bulanFormat.format(firstDayOfNewMonth);
                if(calendar_month.substring(0,1).equals("0"))
                {
                    calendar_month = calendar_month.substring(1);
                }
                calendar_year   = tahunFormat.format(firstDayOfNewMonth);
                dapat_calendar();

            }
        });
        left_month.setOnClickListener(v -> compactCalendarView.scrollLeft());
        right_month.setOnClickListener(v -> compactCalendarView.scrollRight());
        calendarModelList = new ArrayList<CalendarModel>();
        dapat_calendar();

        calendarAdapter = new CalendarAdapter(calendarModelList);
        calendarAdapter.setOnItemClickListener((view, position) -> {
            if (calendarModelList.get(position).getCalendar_type().equals("-1")) {
                new MaterialDialog.Builder(KalendarKelas.this)
                        .title(calendarModelList.get(position).getCalendar_title())
                        .message("Seharian")
                        .positiveText("Ok")
                        .show();
            }
            else if (calendarModelList.get(position).getCalendar_type().equals("-2")){
                new MaterialDialog.Builder(KalendarKelas.this)
                        .title(calendarModelList.get(position).getCalendar_title())
                        .message(calendarModelList.get(position).getCalendar_desc())
                        .positiveText("Ok")
                        .show();
            }else {
                calendar_date   = calendarModelList.get(position).getCalendar_date();
                calendar_time   = calendarModelList.get(position).getCalendar_time();
                calendar_id     = calendarModelList.get(position).getCalendar_id();
                calendar_colour = calendarModelList.get(position).getCalendar_color();
                openBottomSheet();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(KalendarKelas.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(calendarAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(calendarAdapter)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .count(10)
                .load(R.layout.item_calendar)
                .show(); //default count is 10
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                skeletonScreen.hide();
            }
        }, 3000);
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
    public void dapat_calendar(){
        Call<JSONResponse.ClassCalendar> call = mApiInterface.kes_class_calendar_get(authorization.toString(),school_code.toLowerCase(),student_id.toString(),classroom_id.toString(),calendar_month.toString(),calendar_year.toString());
        call.enqueue(new Callback<JSONResponse.ClassCalendar>() {
            @Override
            public void onResponse(Call<JSONResponse.ClassCalendar> call, Response<JSONResponse.ClassCalendar> response) {
                Log.i("KES",response.code() + "");

                JSONResponse.ClassCalendar resource = response.body();
                status = resource.status;
                code   = resource.code;
                if (status == 1 & code.equals("DTS_SCS_0001")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    kalendar.setVisibility(View.GONE);
                    if (response.body() != null && response.body().getData() != null) {
                        calendarList = response.body().getData();
                        if (calendarModelList != null) {
                            calendarModelList.clear();
                            for (JSONResponse.DataCalendar calendar : calendarList) {
                                Calendar cal = Calendar.getInstance();
                                calendar_date = calendar.getCalendar_date();
                                calendar_time = calendar.getCalendar_time();
                                calendar_type = calendar.getCalendar_type();
                                calendar_colour = calendar.getCalendar_colour();
                                if (calendar_date.equals(hari)){
                                    if (calendar_date.equals(hari)){
                                        if (calendar_type.equals("-1") || calendar_type.equals("-2")) {
                                            calendarModel = new CalendarModel();
                                            calendarModel.setCalendar_id(String.valueOf(calendar.getCalendar_id()));
                                            calendarModel.setCalendar_time("Seharian");
                                            calendarModel.setCalendar_date(converDate(calendar.getCalendar_date()));
                                            String sentence = calendar.getCalendar_title();
                                            String replaced = sentence.replace("Hari Libur - ", "");
                                            calendarModel.setCalendar_title(replaced);
                                            calendarModel.setCalendar_color(calendar_colour);
                                            calendarModel.setCalendar_desc(calendar.getCalendar_desc());
                                            calendarModel.setCalendar_type(calendar_type);
                                            calendarModelList.add(calendarModel);
                                        } else {
                                            calendarModel = new CalendarModel();
                                            calendarModel.setCalendar_id(String.valueOf(calendar.getCalendar_id()));
                                            calendarModel.setCalendar_time(calendar.getCalendar_time());
                                            calendarModel.setCalendar_date(converDate(calendar.getCalendar_date()));
                                            calendarModel.setCalendar_title(calendar.getCalendar_title());
                                            calendarModel.setCalendar_desc(calendar.getCalendar_desc());
                                            calendarModel.setCalendar_type(calendar_type);
                                            calendarModel.setCalendar_color(calendar_colour);
                                            calendarModelList.add(calendarModel);
                                        }
                                    }
                                    if (compactCalendarView.getEvents(Calendar.getInstance().getTime())!=null){
                                        kalendar.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    if (compactCalendarView.getEvents(Calendar.getInstance().getTime()).size() == 0){
                                        kalendar.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }
                                calendarAdapter.notifyDataSetChanged();
                                if (calendar_type.equals("-1") || calendar_type.equals("-2")) {
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    try {
                                        date = format.parse(calendar_date);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    cal.setTime(date);
                                    setToMidnight(cal);
                                    long times = cal.getTimeInMillis();
                                    events = getEventList(calendar_colour,times);
                                    compactCalendarView.addEvents(events);

                                }else {
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
                                    try {
                                        date = format.parse(calendar_date + " " + calendar_time);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    cal.setTime(date);
                                    setToMidnight(cal);
                                    long timee = cal.getTimeInMillis();
                                    eventList = getevent(calendar_colour,timee);
                                    compactCalendarView.addEvents(eventList);
                                }
                            }
                        }
                    }
                }
                else if (status == 0 && code.equals("DTS_ERR_0001")){
                    kalendar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JSONResponse.ClassCalendar> call, @NonNull Throwable t) {
                Log.e("onFailure",t.toString());
            }
        });
    }
    public static String removeWords(String word ,String remove) {
        return word.replace(remove,"");
    }
    private List<Event> getevent(String color,long timeinMilis){
        return Collections.singletonList(new Event(Color.parseColor(color), timeinMilis));
    }
    private List<Event>getEventList(String color,long timeinMilis){
        return Collections.singletonList(new Event(Color.parseColor(color), timeinMilis));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    String converDate(String tanggal){
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());

        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd",Locale.getDefault());
        try {
            return newDateFormat.format(calendarDateFormat.parse(tanggal));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    private void openBottomSheet(){

        View view = getLayoutInflater().inflate(R.layout.kalendar_detail, null);
        TextView tv_tanggal       = view.findViewById(R.id.tanggal);
        TextView tv_deskripsi     = view.findViewById(R.id.deskripsi);
        TextView tv_judul         = view.findViewById(R.id.judul);
        TextView tv_guru          = view.findViewById(R.id.dibuat);
        ImageView iv_background   = view.findViewById(R.id.color_calendar);
        ImageView iv_calendar     = view.findViewById(R.id.color_calender);
        ImageView iv_people       = view.findViewById(R.id.color_people);
        CardView iv_close         = view.findViewById(R.id.iv_close);

        final Dialog mBottomSheetDialog = new Dialog(KalendarKelas.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        mBottomSheetDialog.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        Call<JSONResponse.CalendarDetail> call = mApiInterface.kes_calendar_detail_get(authorization,school_code.toLowerCase(),calendar_id);
        call.enqueue(new Callback<JSONResponse.CalendarDetail>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<JSONResponse.CalendarDetail> call, Response<JSONResponse.CalendarDetail> response) {
                Log.i("onRespone",response.code()+"");
                if (response.isSuccessful()) {
                    JSONResponse.CalendarDetail resource = response.body();
                    status = resource.status;
                    code = resource.code;
                    if (status == 1 && code.equals("DTS_SCS_0001")) {
                        tanggal     = response.body().getData().getCalendar_date();
                        jam         = response.body().getData().getTimez();
                        deskripsi   = response.body().getData().getCalendar_desc();
                        judul       = response.body().getData().getCalendar_title();
                        guru        = response.body().getData().getCreated_by();
                        iv_background.setColorFilter(Color.parseColor(calendar_colour));
                        tv_tanggal.setText(converttanggal(tanggal)+" . "+jam);
                        tv_deskripsi.setText(deskripsi);
                        tv_judul.setText(judul);
                        tv_guru.setText(guru);
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse.CalendarDetail> call, Throwable t) {
                Log.e("calendarEror",t.toString());
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
    }

    String converttanggal(String tanggal) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("in", "ID"));

        SimpleDateFormat newDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("in", "ID"));
        try {
            return newDateFormat.format(calendarDateFormat.parse(tanggal));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
