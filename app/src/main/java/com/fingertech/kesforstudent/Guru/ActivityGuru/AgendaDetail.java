package com.fingertech.kesforstudent.Guru.ActivityGuru;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fingertech.kesforstudent.Controller.Auth;
import com.fingertech.kesforstudent.CustomView.SnappyRecycleView;
import com.fingertech.kesforstudent.Guru.AdapterGuru.AdapterAgenda;
import com.fingertech.kesforstudent.Guru.AdapterGuru.AdapterDataTanggal;
import com.fingertech.kesforstudent.Guru.ModelGuru.AgendaModelTanggal;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAgenda;
import com.fingertech.kesforstudent.R;
import com.fingertech.kesforstudent.Rest.ApiClient;
import com.fingertech.kesforstudent.Rest.JSONResponse;
import com.fingertech.kesforstudent.Masuk;
import com.kcode.bottomlib.BottomDialog;
import com.pepperonas.materialdialog.MaterialDialog;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.stone.vega.library.VegaLayoutManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaDetail extends AppCompatActivity  implements AdapterAgenda.OnImageClickListener {

    String authorization,school_code,member_id,scyear_id,edulevel_id,semester_id,cources_id,cources_name,code;
    public static final String TAG_EMAIL        = "email";
    public static final String TAG_MEMBER_ID    = "member_id";
    public static final String TAG_FULLNAME     = "fullname";
    public static final String TAG_MEMBER_TYPE  = "member_type";
    public static final String TAG_TOKEN        = "token";
    public static final String TAG_SCHOOL_CODE  = "school_code";

    SharedPreferences sharedpreferences;
    IndefinitePagerIndicator indefinitePagerIndicator;
    Toolbar toolbar;
    RecyclerView rv_agenda;
    RecyclerView rvtanggal;
    Auth mApiInterface;
    private DateFormat dateFormat           = new SimpleDateFormat("yyyy-MM", new Locale("in", "ID"));
    private SimpleDateFormat bulanFormat    = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
    DateFormat df                           = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat fmt                    = new SimpleDateFormat("dd MMMM yyyy",new Locale("in","ID"));
    SimpleDateFormat tanggalformat          = new SimpleDateFormat("d",new Locale("in","ID"));
    SimpleDateFormat bulanformat            = new SimpleDateFormat("M",new Locale("in","ID"));
    SimpleDateFormat tahunformat            = new SimpleDateFormat("yyyy",new Locale("in","ID"));
    SimpleDateFormat parsetanggal           = new SimpleDateFormat("yyyy-M-d",new Locale("in","ID"));
    SimpleDateFormat formattanggal          = new SimpleDateFormat("yyyy-MM-dd",new Locale("in","ID"));
    SimpleDateFormat formatfull             = new SimpleDateFormat("yyyy-MM-dd HH:mm",new Locale("in","ID"));
    SimpleDateFormat hariformat             = new SimpleDateFormat("EEEE", new Locale("in","ID"));
    DateFormat ds                           = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    int status;
    String tanggalagenda,hariagenda,bulan_sekarang,date,semester,start_date,end_date,color,tanggal_agenda,type_agenda,desc_agenda,content_agenda,id,type_data;
    ProgressDialog dialog;
    TextView tv_hint_agenda,tvsemester,tvtanggalsemester,tv_hint_agenda_hari;
    int day;
    List<JSONResponse.DataAgenda> dataAgendaList;
    String tanggal,bulan,tahun,hari;
    SlidingUpPanelLayout slidingUpPanelLayout;
    ImageView arrow;
    LinearLayout drag;
    DatePicker datePicker;
    String tanggal_awal,tanggal_akhir,tanggalawalan,tanggalakhiran;
    Date dateawal,dateakhir,bulannow,bulanpicker,datenow,datejam;
    Long times_awal,times_akhir,times_sekarang,times_picker;
    CardView btn_pilih;
    String tanggals;

    AdapterAgenda adapterAgenda;
    AdapterDataTanggal adapterDataTanggal;
    AgendaModelTanggal agendaModelTanggal;
    ModelAgenda modelAgenda;
    List<ModelAgenda>modelAgendaList = new ArrayList<>();
    List<AgendaModelTanggal> agendaModelTanggalList = new ArrayList<>();
    LinearLayout ll_agenda,no_ajaran;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_guru_view);

        indefinitePagerIndicator    = findViewById(R.id.recyclerview_pager_indicator);
        rvtanggal                   = findViewById(R.id.rv_tanggalrv);
        tvsemester                  = findViewById(R.id.tv_semestersagenda);
        tvtanggalsemester           = findViewById(R.id.tvtanggalagenda);
        toolbar                     = findViewById(R.id.toolbar_agenda);
        rv_agenda                   = findViewById(R.id.rv_agenda);
        tv_hint_agenda              = findViewById(R.id.hint_agenda);
        slidingUpPanelLayout        = findViewById(R.id.sliding_layout);
        arrow                       = findViewById(R.id.arrow);
        drag                        = findViewById(R.id.dragView);
        datePicker                  = findViewById(R.id.datePicker);
        btn_pilih                   = findViewById(R.id.btn_pilih);
        tv_hint_agenda_hari         = findViewById(R.id.hint_harian);
        ll_agenda                   = findViewById(R.id.ll_agenda);
        no_ajaran                   = findViewById(R.id.hint_ajaran);
        mApiInterface               = ApiClient.getClient().create(Auth.class);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        sharedpreferences   = getSharedPreferences(Masuk.my_shared_preferences, Context.MODE_PRIVATE);
        authorization       = sharedpreferences.getString(TAG_TOKEN,null);
        member_id           = sharedpreferences.getString(TAG_MEMBER_ID,null);
        scyear_id           = sharedpreferences.getString("scyear_id",null);
        school_code         = sharedpreferences.getString(TAG_SCHOOL_CODE,null);
        edulevel_id         = sharedpreferences.getString("classroom_id",null);
        cources_id          = sharedpreferences.getString("cources_id",null);
        cources_name        = sharedpreferences.getString("cources_name",null);

        date = df.format(Calendar.getInstance().getTime());
        bulan_sekarang = bulanFormat.format(Calendar.getInstance().getTime());
        try {
            bulannow    = dateFormat.parse(bulan_sekarang);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        times_sekarang = bulannow.getTime();
        Calendar calendar = Calendar.getInstance();
        day  = calendar.get(Calendar.DAY_OF_WEEK);

        tanggal = tanggalformat.format(Calendar.getInstance().getTime());
        bulan   = bulanformat.format(Calendar.getInstance().getTime());
        tahun   = tahunformat.format(Calendar.getInstance().getTime());
        tanggalawalan   = formattanggal.format(Calendar.getInstance().getTime());
        tanggals        = fmt.format(Calendar.getInstance().getTime());

        Check_Semester();
        dapat_Agenda();

        slidingUpPanelLayout.setFadeOnClickListener(view -> {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            arrow.setImageResource(R.drawable.ic_up_arrow);
        });
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState, SlidingUpPanelLayout.PanelState panelState1) {
                if (panelState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
                    arrow.setImageResource(R.drawable.ic_up_arrow);
                }else if (panelState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                    arrow.setImageResource(R.drawable.ic_down_arrow);
                }
            }
        });

        drag.setOnClickListener(v -> {
            if (slidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                arrow.setImageResource(R.drawable.ic_up_arrow);
            }else if (slidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                arrow.setImageResource(R.drawable.ic_down_arrow);
            }
        });

        Calendar calendars = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendars.get(Calendar.YEAR), calendars.get(Calendar.MONTH), calendars.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tahun           = String.valueOf(year);
                bulan           = String.valueOf(monthOfYear+1);
                tanggal         = String.valueOf(dayOfMonth);
                try {
                    bulannow    = dateFormat.parse(bulan_sekarang);
                    bulanpicker = dateFormat.parse(convertDate(year,monthOfYear));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                times_sekarang = bulannow.getTime();
                times_picker   = bulanpicker.getTime();
            }
        });
        btn_pilih.setOnClickListener(v -> {
            ll_agenda.setVisibility(View.VISIBLE);
            no_ajaran.setVisibility(View.GONE);
            if (times_sekarang.equals(times_picker)){
                rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal)-1);
            }else {
                datatanggal(Integer.parseInt(tahun),Integer.parseInt(bulan));
                if (Integer.parseInt(tanggal)-5 < 0){
                    rvtanggal.smoothScrollToPosition(0);
                }else {
                    rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal)-5);
                }
                bulan_sekarang = convertDate(Integer.parseInt(tahun),Integer.parseInt(bulan)-1);
                dapat_Agenda();
            }
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            arrow.setImageResource(R.drawable.ic_up_arrow);
        });

        datatanggal(Integer.parseInt(tahun),Integer.parseInt(bulan));

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        adapterDataTanggal = new AdapterDataTanggal(agendaModelTanggalList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AgendaDetail.this, LinearLayoutManager.HORIZONTAL, false);
        indefinitePagerIndicator.attachToRecyclerView(rvtanggal);
        rvtanggal.setOnFlingListener(new SnappyRecycleView(AgendaDetail.this).getOnFlingListener());
        snapHelper.attachToRecyclerView(rvtanggal);
        rvtanggal.setLayoutManager(layoutManager);
        rvtanggal.setAdapter(adapterDataTanggal);
        rvtanggal.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int horizontalScrollRange = recyclerView.computeHorizontalScrollRange();
                int scrollOffset = recyclerView.computeHorizontalScrollOffset();
                int currentItem = 0;
                float itemWidth = horizontalScrollRange * 1.0f / agendaModelTanggalList.size();
                itemWidth = (itemWidth == 0) ? 1.0f : itemWidth;
                if (scrollOffset != 0) {
                    currentItem = Math.round(scrollOffset / itemWidth);
                }
                currentItem = (currentItem < 0) ? 0 : currentItem;
                currentItem = (currentItem >= agendaModelTanggalList.size()) ? agendaModelTanggalList.size() - 1 : currentItem;
                tanggals = agendaModelTanggalList.get(currentItem).getDate();
                if (dataAgendaList!=null) {
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(fmt.parse(tanggals));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    hari = tanggalformat.format(calendar.getTime());
                    if (modelAgendaList != null) {
                        modelAgendaList.clear();
                        if (contains(dataAgendaList, convertTanggal(tanggals))) {
                            for (JSONResponse.DataAgenda dataAgenda : dataAgendaList) {
                                if (dataAgenda.getDate().equals(convertTanggal(tanggals))) {
                                    type_agenda     = dataAgenda.getType();
                                    desc_agenda     = dataAgenda.getDesc();
                                    color           = dataAgenda.getAgenda_color();
                                    content_agenda  = dataAgenda.getAgenda_desc();
                                    id              = dataAgenda.getId();
                                    type_data       = dataAgenda.getType_data();
                                    tanggal_agenda  = dataAgenda.getDate();
                                    modelAgenda = new ModelAgenda();
                                    modelAgenda.setColour(color);
                                    modelAgenda.setId(id);
                                    modelAgenda.setType_data(type_data);
                                    modelAgenda.setDate(tanggal_agenda);
                                    modelAgenda.setType(type_agenda);
                                    modelAgenda.setDesc(desc_agenda);
                                    modelAgenda.setContent(content_agenda);
                                    modelAgendaList.add(modelAgenda);
                                    rv_agenda.setVisibility(View.VISIBLE);
                                    tv_hint_agenda_hari.setVisibility(View.GONE);
                                }
                            }
                            adapterAgenda = new AdapterAgenda(AgendaDetail.this,modelAgendaList,mApiInterface,authorization,member_id,school_code,AgendaDetail.this);
                            rv_agenda.setOnFlingListener(null);
                            rv_agenda.setLayoutManager(new VegaLayoutManager());
                            rv_agenda.setAdapter(adapterAgenda);
                            adapterAgenda.notifyDataSetChanged();
                            adapterAgenda.setOnItemClickListener(new AdapterAgenda.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String type = modelAgendaList.get(position).getType();
                                    String desc = modelAgendaList.get(position).getDesc();
                                    String content = modelAgendaList.get(position).getContent();
                                    new MaterialDialog.Builder(AgendaDetail.this)
                                            .title(type)
                                            .message(desc + "\n\n" + content)
                                            .positiveText("Ok")
                                            .show();
                                }
                            });
                        } else {
                            rv_agenda.setVisibility(View.GONE);
                            tv_hint_agenda_hari.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

    }


    String convertDate(int year, int month) {
        String temp = year + "-" + (month + 1);
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("yyyy-MM",Locale.getDefault());
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM",Locale.getDefault());
        try {
            return newDateFormat.format(calendarDateFormat.parse(temp));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    String convertTanggal(String tahun){
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());

        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy",new Locale("in","ID"));
        try {
            return calendarDateFormat.format(newDateFormat.parse(tahun));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    boolean contains(List<JSONResponse.DataAgenda> list,String posisi) {
        for (JSONResponse.DataAgenda item : list) {
            if (item.getDate().equals(posisi)) {
                return true;
            }
        }
        return false;
    }

    private void Check_Semester() {
        Call<JSONResponse.CheckSemester> call = mApiInterface.kes_check_semester_get(authorization.toString(), school_code.toString().toLowerCase(), edulevel_id.toString(), date.toString());
        call.enqueue(new Callback<JSONResponse.CheckSemester>() {
            @Override
            public void onResponse(Call<JSONResponse.CheckSemester> call, final Response<JSONResponse.CheckSemester> response) {
                Log.i("KES_semester", response.code() + "");
                if (response.isSuccessful()) {
                    JSONResponse.CheckSemester resource = response.body();
                    status  = resource.status;
                    code    = resource.code;
                    if (status == 1 && code.equals("DTS_SCS_0001")){
                        semester_id = response.body().getData();
                        dapat_semester();
                        if (semester_id.equals("0")){
                            ll_agenda.setVisibility(View.GONE);
                            no_ajaran.setVisibility(View.VISIBLE);
                        }else {
                            ll_agenda.setVisibility(View.VISIBLE);
                            no_ajaran.setVisibility(View.GONE);
                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<JSONResponse.CheckSemester> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void dapat_semester() {
        Call<JSONResponse.ListSemester> call = mApiInterface.kes_list_semester_get(authorization.toString(), school_code.toLowerCase(), edulevel_id.toString());
        call.enqueue(new Callback<JSONResponse.ListSemester>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<JSONResponse.ListSemester> call, final Response<JSONResponse.ListSemester> response) {
                Log.i("dapat_Semester", response.code() + "");

                if (response.isSuccessful()) {
                    JSONResponse.ListSemester resource = response.body();

                    status = resource.status;
                    code = resource.code;
                    Log.d("semester",semester_id+"");
                    if (status == 1 && code.equals("DTS_SCS_0001")) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            Collections.sort(response.body().getData(), new Comparator<JSONResponse.DataSemester>() {
                                @Override
                                public int compare(JSONResponse.DataSemester o1, JSONResponse.DataSemester o2) {
                                    return (o1.getStart_date().compareTo(o2.getStart_date()));
                                }
                            });
                            tanggal_awal    = response.body().getData().get(0).getStart_date();
                            tanggal_akhir   = response.body().getData().get(response.body().getData().size()-1).getEnd_date();
                            try {
                                dateawal    = df.parse(tanggal_awal);
                                dateakhir   = df.parse(tanggal_akhir);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            times_awal  = dateawal.getTime();
                            times_akhir = dateakhir.getTime();
                            if (times_sekarang > times_akhir){
                                datePicker.updateDate(Integer.parseInt(convertTahun(tanggal_akhir)),Integer.parseInt(convertBulan(tanggal_akhir))-1,Integer.parseInt(convertDate(tanggal_akhir)));
                            }
                            datePicker.setMaxDate(times_akhir);
                            datePicker.setMinDate(times_awal);

                            if (semester_id.equals("0")){
                                tvsemester.setVisibility(View.GONE);
                                tvtanggalsemester.setText("Tahun ajaran telah selesai");
                            }else if (response.body().getData().get(i).getSemester_id().equals(semester_id)) {
                                semester    = response.body().getData().get(i).getSemester_name();
                                start_date  = response.body().getData().get(i).getStart_date();
                                end_date    = response.body().getData().get(i).getEnd_date();
                                tvsemester.setVisibility(View.VISIBLE);
                                tvsemester.setText("Semester "+semester);
                                tvtanggalsemester.setText(converttanggal(start_date)+" sampai "+converttanggal(end_date));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse.ListSemester> call, Throwable t) {
                Log.d("onFailure", t.toString());
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }

        });
    }

    String converttanggal(String tanggal) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("in", "ID"));

        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));
        try {
            String e = newDateFormat.format(calendarDateFormat.parse(tanggal));
            return e;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void showDialog() {
        if (!dialog.isShowing())
            dialog.show();
        dialog.setContentView(R.layout.progressbar);
    }
    private void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
        dialog.setContentView(R.layout.progressbar);
    }
    public void progressBar(){
        dialog = new ProgressDialog(AgendaDetail.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
    }

    private void dapat_Agenda(){
        progressBar();
        showDialog();
        Call<JSONResponse.ListAgenda> call = mApiInterface.kes_class_agenda_teacher_get(authorization,school_code.toLowerCase(),member_id,edulevel_id,cources_id,bulan_sekarang, String.valueOf(day-1));
        call.enqueue(new Callback<JSONResponse.ListAgenda>() {
            @Override
            public void onResponse(Call<JSONResponse.ListAgenda> call, Response<JSONResponse.ListAgenda> response) {
                Log.d("Sukses agenda",response.code()+"");
                hideDialog();
                if (response.isSuccessful()){
                    JSONResponse.ListAgenda resource = response.body();
                    status  = resource.status;
                    code    = resource.code;
                    if (status == 1 && code.equals("AGN_SCS_0001")){
                        indefinitePagerIndicator.setVisibility(View.VISIBLE);
                        rv_agenda.setVisibility(View.VISIBLE);
                        rvtanggal.setVisibility(View.VISIBLE);
                        tv_hint_agenda.setVisibility(View.GONE);
                        dataAgendaList = response.body().getData();
                        if (contains(dataAgendaList, convertTanggal(tanggals))) {
                            for (JSONResponse.DataAgenda dataAgenda : dataAgendaList) {
                                if (dataAgenda.getDate().equals(convertTanggal(tanggals))) {
                                    type_agenda     = dataAgenda.getType();
                                    desc_agenda     = dataAgenda.getDesc();
                                    color           = dataAgenda.getAgenda_color();
                                    content_agenda  = dataAgenda.getAgenda_desc();
                                    id              = dataAgenda.getId();
                                    type_data       = dataAgenda.getType_data();
                                    tanggal_agenda  = dataAgenda.getDate();
                                    modelAgenda = new ModelAgenda();
                                    modelAgenda.setColour(color);
                                    modelAgenda.setId(id);
                                    modelAgenda.setType_data(type_data);
                                    modelAgenda.setDate(tanggal_agenda);
                                    modelAgenda.setType(type_agenda);
                                    modelAgenda.setDesc(desc_agenda);
                                    modelAgenda.setContent(content_agenda);
                                    modelAgendaList.add(modelAgenda);
                                    rv_agenda.setVisibility(View.VISIBLE);
                                    tv_hint_agenda_hari.setVisibility(View.GONE);
                                }
                            }
                        }else {
                            rv_agenda.setVisibility(View.GONE);
                            tv_hint_agenda_hari.setVisibility(View.VISIBLE);
                        }
                        String tanngall = formatfull.format(Calendar.getInstance().getTime());
                        if (Integer.parseInt(tanggal) == agendaModelTanggalList.size() - 1) {
                            rvtanggal.scrollToPosition(Integer.parseInt(tanggal));
                            rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal));
                            rvtanggal.smoothScrollBy(1, 0);
                            datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan) - 1, Integer.parseInt(tanggal));
                        } else {
                            rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal) -1);
                            rvtanggal.scrollToPosition(Integer.parseInt(tanggal) - 1);
                            rvtanggal.smoothScrollBy(1, 0);
                            datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan) - 1, Integer.parseInt(tanggal));
                        }
                        //// format agenda
//                        if (response.body().getClass_schedule().size() > 0) {
//                            if (response.body().getClass_schedule().get(0).getScheduleClass().size() > 0) {
//                                tanggalakhiran = response.body().getClass_schedule().get(0).getScheduleClass().get(0).getTimezFinish();
//                                try {
//                                    datenow = ds.parse(tanngall);
//                                    datejam = ds.parse(tanggalawalan + " " + tanggalakhiran);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                Long timesnow = datenow.getTime();
//                                Long timesakhir = datejam.getTime();
//                                if (agendaModelTanggalList != null) {
//                                    if (timesnow > timesakhir) {
//                                        if (Integer.parseInt(tanggal) > agendaModelTanggalList.size() - 1) {
//                                            tanggal = "0";
//                                            Calendar cal = Calendar.getInstance();
//                                            try {
//                                                cal.setTime(parsetanggal.parse(tahun+"-"+ String.valueOf(Integer.parseInt(bulan) + 1)+"-"+tanggal));
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//                                            bulan_sekarang  = bulanFormat.format(cal.getTime());
//                                            tanggals        = fmt.format(cal.getTime());
//                                            day  = cal.get(Calendar.DAY_OF_WEEK);
//                                            bulan = String.valueOf(Integer.parseInt(bulan) + 1);
//                                            dapat_Agenda();
//                                            datatanggal(Integer.parseInt(tahun), Integer.parseInt(bulan));
////                                            rvtanggal.scrollToPosition(1);
////                                            rvtanggal.smoothScrollToPosition(0);
////                                            rvtanggal.smoothScrollBy(1, 0);
////                                            datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan), 0);
//                                        } else {
//                                            if (Integer.parseInt(tanggal) == agendaModelTanggalList.size() - 1) {
//                                                rvtanggal.scrollToPosition(Integer.parseInt(tanggal));
//                                                rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal));
//                                                rvtanggal.smoothScrollBy(1, 0);
//                                                datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan) - 1, Integer.parseInt(tanggal));
//                                            } else {
//                                                rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal) -1);
//                                                rvtanggal.scrollToPosition(Integer.parseInt(tanggal) - 1);
//                                                rvtanggal.smoothScrollBy(1, 0);
//                                                datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan) - 1, Integer.parseInt(tanggal));
//                                            }
//                                        }
//                                    } else {
//                                        rvtanggal.scrollToPosition(Integer.parseInt(tanggal) - 1);
//                                        rvtanggal.smoothScrollBy(1, 0);
//                                    }
//                                }
//                            } else {
//                                if (Integer.parseInt(tanggal) > agendaModelTanggalList.size() - 1) {
//                                    datatanggal(Integer.parseInt(tahun), Integer.parseInt(bulan) + 1);
//                                    rvtanggal.scrollToPosition(1);
//                                    rvtanggal.smoothScrollToPosition(0);
//                                    rvtanggal.smoothScrollBy(1, 0);
//                                    datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan), 0);
//                                } else {
//                                    if (Integer.parseInt(tanggal) == agendaModelTanggalList.size() - 1) {
//                                        rvtanggal.scrollToPosition(Integer.parseInt(tanggal) - 1);
//                                        rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal) - 1);
//                                        rvtanggal.smoothScrollBy(1, 0);
//                                        datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan) - 1, Integer.parseInt(tanggal));
//                                    } else {
//                                        rvtanggal.scrollToPosition(Integer.parseInt(tanggal));
//                                        rvtanggal.smoothScrollBy(1, 0);
//                                        datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan) - 1, Integer.parseInt(tanggal));
//                                    }
//                                }
//                            }
//                        }else {
//                            if (Integer.parseInt(tanggal) > agendaModelTanggalList.size() - 1) {
//                                datatanggal(Integer.parseInt(tahun), Integer.parseInt(bulan) + 1);
//                                rvtanggal.scrollToPosition(1);
//                                rvtanggal.smoothScrollToPosition(0);
//                                rvtanggal.smoothScrollBy(1, 0);
//                                datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan), 0);
//                            } else {
//                                if (Integer.parseInt(tanggal) == agendaModelTanggalList.size() - 1) {
//                                    rvtanggal.scrollToPosition(Integer.parseInt(tanggal) - 1);
//                                    rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal) - 1);
//                                    rvtanggal.smoothScrollBy(1, 0);
//                                    datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan) - 1, Integer.parseInt(tanggal));
//                                } else {
//                                    rvtanggal.scrollToPosition(Integer.parseInt(tanggal) -1);
//                                    rvtanggal.smoothScrollBy(1, 0);
//                                    datePicker.updateDate(Integer.parseInt(tahun), Integer.parseInt(bulan) - 1, Integer.parseInt(tanggal));
//                                }
//                            }
//                        }
                    } else {
                        indefinitePagerIndicator.setVisibility(View.GONE);
                        rv_agenda.setVisibility(View.GONE);
                        rvtanggal.setVisibility(View.GONE);
                        tv_hint_agenda.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<JSONResponse.ListAgenda> call, Throwable t) {
                Log.d("gagal",t.toString());
                hideDialog();
            }
        });
    }

    private void datatanggal(int tahun,int bulan){
        Calendar cal = Calendar.getInstance();
        cal.set(tahun,bulan - 1, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (agendaModelTanggalList!=null) {
            agendaModelTanggalList.clear();
            for (int i = 0; i < daysInMonth; i++) {
                tanggalagenda   = fmt.format(cal.getTime());
                hariagenda      = hariformat.format(cal.getTime());
                cal.add(Calendar.DAY_OF_MONTH, 1);
                agendaModelTanggal = new AgendaModelTanggal();
                agendaModelTanggal.setDate(tanggalagenda);
                agendaModelTanggal.setHari(hariagenda);
                agendaModelTanggalList.add(agendaModelTanggal);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.item_add:
                select_buat();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agenda, menu);
        return true;
    }
    private void select_buat(){
        BottomDialog dialog = BottomDialog.newInstance("",new String[]{"Buat Tugas Baru", "Buat Agenda Baru",});
        dialog.show(getSupportFragmentManager(),"dialog");
        dialog.setListener(new BottomDialog.OnClickListener() {
            @Override
            public void click(int position) {
                if (position == 0){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("authorization",authorization);
                    editor.putString("member_id",member_id);
                    editor.putString("school_code",school_code);
                    editor.putString("scyear_id",scyear_id);
                    editor.putString("classroom_id",edulevel_id);
                    editor.putLong("times_awal",times_awal);
                    editor.putLong("times_akhir",times_akhir);
                    editor.apply();
                    Intent intent = new Intent(AgendaDetail.this,TambahTugas.class);
                    intent.putExtra("authorization",authorization);
                    intent.putExtra("member_id",member_id);
                    intent.putExtra("school_code",school_code);
                    intent.putExtra("scyear_id",scyear_id);
                    intent.putExtra("classroom_id",edulevel_id);
                    intent.putExtra("times_awal",times_awal);
                    intent.putExtra("times_akhir",times_akhir);
                    startActivityForResult(intent,2);
                }else if (position == 1){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("authorization",authorization);
                    editor.putString("member_id",member_id);
                    editor.putString("school_code",school_code);
                    editor.putString("scyear_id",scyear_id);
                    editor.putString("classroom_id",edulevel_id);
                    editor.putLong("times_awal",times_awal);
                    editor.putLong("times_akhir",times_akhir);
                    editor.putString("cources_id",cources_id);
                    editor.putString("cources_name",cources_name);
                    editor.apply();
                    Intent intent = new Intent(AgendaDetail.this,TambahAgenda.class);
                    intent.putExtra("authorization",authorization);
                    intent.putExtra("member_id",member_id);
                    intent.putExtra("school_code",school_code);
                    intent.putExtra("scyear_id",scyear_id);
                    intent.putExtra("classroom_id",edulevel_id);
                    intent.putExtra("times_awal",times_awal);
                    intent.putExtra("times_akhir",times_akhir);
                    intent.putExtra("cources_id",cources_id);
                    intent.putExtra("cources_name",cources_name);
                    startActivityForResult(intent,1);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("request_code",requestCode+"/"+resultCode);
        if (requestCode == 1 || requestCode == 2) {
            if(resultCode == RESULT_OK) {
                ll_agenda.setVisibility(View.VISIBLE);
                no_ajaran.setVisibility(View.GONE);
                String agenda_date = data.getStringExtra("date");
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(formattanggal.parse(agenda_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bulan_sekarang = bulanFormat.format(cal.getTime());
                try {
                    bulanpicker    = dateFormat.parse(bulan_sekarang);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                times_picker   = bulanpicker.getTime();
                day     = cal.get(Calendar.DAY_OF_WEEK);
                tanggal = tanggalformat.format(cal.getTime());
                bulan   = bulanformat.format(cal.getTime());
                tahun   = tahunformat.format(cal.getTime());
                tanggals        = fmt.format(cal.getTime());
                if (times_sekarang.equals(times_picker)){
                    rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal) - 1);
                    dapat_Agenda();
                }else {
                    datatanggal(Integer.parseInt(tahun),Integer.parseInt(bulan));
                    if (Integer.parseInt(tanggal)-5 < 0){
                        rvtanggal.smoothScrollToPosition(0);
                    }else {
                        rvtanggal.smoothScrollToPosition(Integer.parseInt(tanggal)-5);
                    }
                    bulan_sekarang = convertDate(Integer.parseInt(tahun),Integer.parseInt(bulan)-1);
                    dapat_Agenda();
                }
            }
        }
    }

    String convertBulan(String date) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MM",Locale.getDefault());
        try {
            return newDateFormat.format(calendarDateFormat.parse(date));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    String convertTahun(String date) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy",Locale.getDefault());
        try {
            return newDateFormat.format(calendarDateFormat.parse(date));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    String convertDate(String date) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd",Locale.getDefault());
        try {
            return newDateFormat.format(calendarDateFormat.parse(date));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onImageClick(String change) {
        if (change.equals("change")){
            tanggal = hari;
            dapat_Agenda();
        }
    }
}
