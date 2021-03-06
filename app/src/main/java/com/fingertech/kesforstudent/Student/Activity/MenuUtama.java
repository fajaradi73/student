package com.fingertech.kesforstudent.Student.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.fingertech.kesforstudent.MainActivity;
import com.fingertech.kesforstudent.NotifikasiActivity;
import com.fingertech.kesforstudent.Service.FirebaseMessaging;
import com.fingertech.kesforstudent.TentangKami;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearSnapHelper;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fingertech.kesforstudent.Masuk;
import com.fingertech.kesforstudent.Setting.Setting_Activity;
import com.fingertech.kesforstudent.Student.Adapter.HariAdapter.JumatAdapter;
import com.fingertech.kesforstudent.Student.Adapter.HariAdapter.KamisAdapter;
import com.fingertech.kesforstudent.Student.Adapter.HariAdapter.RabuAdapter;
import com.fingertech.kesforstudent.Student.Adapter.HariAdapter.SabtuAdapter;
import com.fingertech.kesforstudent.Student.Adapter.HariAdapter.SelasaAdapter;
import com.fingertech.kesforstudent.Student.Adapter.HariAdapter.SeninAdapter;
import com.fingertech.kesforstudent.Controller.Auth;
import com.fingertech.kesforstudent.Student.Fragment.MenuDuaFragment;
import com.fingertech.kesforstudent.Student.Fragment.MenuSatuFragment;
import com.fingertech.kesforstudent.Student.Model.HariModel.JadwalJumat;
import com.fingertech.kesforstudent.Student.Model.HariModel.JadwalKamis;
import com.fingertech.kesforstudent.Student.Model.HariModel.JadwalRabu;
import com.fingertech.kesforstudent.Student.Model.HariModel.JadwalSabtu;
import com.fingertech.kesforstudent.Student.Model.HariModel.JadwalSelasa;
import com.fingertech.kesforstudent.Student.Model.HariModel.JadwalSenin;
import com.fingertech.kesforstudent.R;
import com.fingertech.kesforstudent.Rest.ApiClient;
import com.fingertech.kesforstudent.Rest.JSONResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuUtama extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ViewPager ParentPager;
    private FragmentAdapter fragmentAdapter;
    public static int PAGE_COUNT = 2;
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    InkPageIndicator inkPageIndicator;
    View header;
    TextView tv_profile,title_jadwal;
    CircleImageView image_profile;
    ProgressDialog dialog;

    public static final String TAG_EMAIL = "email";
    public static final String TAG_MEMBER_ID = "member_id";
    public static final String TAG_FULLNAME = "fullname";
    public static final String TAG_MEMBER_TYPE = "member_type";
    public static final String TAG_TOKEN = "token";
    public static final String TAG_SCHOOL_CODE = "school_code";
    public static final String my_viewpager_preferences = "my_viewpager_preferences";

    private Date dates,date_now;
    private SimpleDateFormat jamformat  = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
    private SimpleDateFormat tanggalFormat  = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
    private DateFormat times_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    SharedPreferences sharedpreferences,sharedViewpager,sharedLesson;
    public static final String my_lesson_preferences = "my_lesson_preferences";
    String picture, Base_anak;
    String authorization, memberid, username, member_type, fullname, school_code;
    Auth mApiInterface;
    int status;
    String code;

    private List<JadwalSenin> itemlist = new ArrayList<>();
    private List<JadwalSelasa> itemselasa = new ArrayList<>();
    private List<JadwalRabu> itemRabu = new ArrayList<>();
    private List<JadwalKamis> itemKamis = new ArrayList<>();
    private List<JadwalJumat> itemJumat = new ArrayList<>();
    private List<JadwalSabtu> itemSabtu = new ArrayList<>();
    SeninAdapter seninAdapter;
    SelasaAdapter selasaAdapter;
    RabuAdapter rabuAdapter;
    KamisAdapter kamisAdapter;
    JumatAdapter jumatAdapter;
    SabtuAdapter sabtuAdapter;
    private Boolean clicked = false;

    String classroom_id;
    String days_name;
    String mapel;
    int lamber;
    String jamber;
    String jam_mulai;
    String jam_selesai;
    String guru, daysid, day_type, day_status;
    String date, day,scyear_id,edulevel_id,cources_id,warna_mapel,status_profile;
    RecyclerView rv_senin, rv_selasa, rv_rabu, rv_kamis, rv_jumat, rv_sabtu;
    CoordinatorLayout coordinatorLayout;
    TextView tv_hint;
    CardView btn_logout;
    int mNotifCount = 0;
    View actionView;
    TextView countmenu;
    private BroadcastReceiver statusReceiver;
    private IntentFilter mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_utama);
        toolbar             = findViewById(R.id.toolbar);
        navigationView      = findViewById(R.id.nav_view);
//        fab                 = findViewById(R.id.fab);
        drawer              = findViewById(R.id.drawer_layout);
        ParentPager         = findViewById(R.id.PagerUtama);
        tv_hint             = findViewById(R.id.no_jadwal);
        fragmentAdapter     = new FragmentAdapter(getSupportFragmentManager());
        inkPageIndicator    = findViewById(R.id.indicators);
        header              = navigationView.getHeaderView(0);
        tv_profile          = header.findViewById(R.id.tv_profil);
        image_profile       = header.findViewById(R.id.image_profile);
        mApiInterface       = ApiClient.getClient().create(Auth.class);
        rv_senin            = findViewById(R.id.rv_senin);
        rv_selasa           = findViewById(R.id.rv_selasa);
        rv_rabu             = findViewById(R.id.rv_rabu);
        rv_kamis            = findViewById(R.id.rv_kamis);
        rv_jumat            = findViewById(R.id.rv_jumat);
        rv_sabtu            = findViewById(R.id.rv_sabtu);
        coordinatorLayout   = findViewById(R.id.menu_utama);
        title_jadwal        = findViewById(R.id.title_jadwal);
        btn_logout          = findViewById(R.id.btn_logout);

        Base_anak           = ApiClient.BASE_IMAGE;

        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(Masuk.my_shared_preferences, Context.MODE_PRIVATE);
        authorization   = sharedpreferences.getString(TAG_TOKEN, "");
        memberid        = sharedpreferences.getString(TAG_MEMBER_ID, "");
        username        = sharedpreferences.getString(TAG_EMAIL, "");
        fullname        = sharedpreferences.getString(TAG_FULLNAME, "");
        member_type     = sharedpreferences.getString(TAG_MEMBER_TYPE, "");
        school_code     = sharedpreferences.getString(TAG_SCHOOL_CODE, "");
        scyear_id       = sharedpreferences.getString("scyear_id","");
        sharedViewpager = getSharedPreferences(my_viewpager_preferences,Context.MODE_PRIVATE);
        sharedLesson        = getSharedPreferences(my_lesson_preferences,Context.MODE_PRIVATE);
        mNotifCount   = sharedpreferences.getInt("counting",0);
        ParentPager.setAdapter(fragmentAdapter);
        inkPageIndicator.setViewPager(ParentPager);

//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).show());

//        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fabseed);
//        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
//            @Override
//            public boolean onMenuItemSelected(MenuItem menuItem) {
//                //TODO: Start some activity
//                return false;
//            }
//        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        image_profile.setOnClickListener(v -> {
            Intent intent = new Intent(MenuUtama.this, ProfileAnak.class);
            startActivityForResult(intent,1);
        });

        navigationView.setNavigationItemSelectedListener(this);

        get_profile();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss", Locale.getDefault());
        date = df.format(Calendar.getInstance().getTime());


        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date dater = null;
        try {
            dater = inFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", new Locale("in","ID"));
        df.setTimeZone(TimeZone.getDefault());
        day = outFormat.format(dater);


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("coba", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    // Log and toast
                    String msg = getString(R.string.msg_token_fmt, token);
                    Log.d("firebase_Token", msg);
                });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihan();
            }
        });
    }

    private void pilihan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuUtama.this,R.style.DialogTheme);
        builder.setTitle("Log out");
        builder.setMessage("Apakah anda ingin keluar?");
        builder.setIcon(R.drawable.ic_alarm);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String counting = intent.getStringExtra("counting");
            if (counting != null){
                if (counting.equals("true")){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt("counting",mNotifCount);
                    editor.apply();
                    mNotifCount++;
                    invalidateOptionsMenu();
                }
            }
        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        startService(new Intent(getBaseContext(), FirebaseMessaging.class));
    }

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(MenuUtama.this).registerReceiver(broadcastReceiver, new IntentFilter("NOW"));
    }

    @Override
    protected void onPause() {
        if(mIntent != null) {
            unregisterReceiver(statusReceiver);
            mIntent = null;
        }
        super.onPause();
    }
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        stopService(new Intent(getBaseContext(), MyService.class));
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_utama, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        actionView  = menu.findItem(R.id.action_cart).getActionView();
        countmenu   = actionView.findViewById(R.id.cart_badge);
        if (mNotifCount == 0){
            countmenu.setVisibility(View.GONE);
        }else {
            countmenu.setVisibility(View.VISIBLE);
            countmenu.setText(String.valueOf(mNotifCount));
        }
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            mNotifCount = 0;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("counting",0);
            editor.apply();
            Intent intent = new Intent(MenuUtama.this, NotifikasiActivity.class);
            intent.putExtra("counting", mNotifCount);
            startActivityForResult(intent,1234);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_beranda) {
        } else if (id == R.id.nav_user) {
            Intent intent = new Intent(MenuUtama.this, ProfileAnak.class);
            startActivityForResult(intent,1);
        } else if (id == R.id.nav_tentang) {
            Intent intent = new Intent(MenuUtama.this, TentangKami.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_pengaturan){
            Intent intent = new Intent(MenuUtama.this, Setting_Activity.class);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {


        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    send_data();
                    return new MenuSatuFragment();
                case 1:
                    send_data2();
                    return new MenuDuaFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1){
                status_profile = data.getStringExtra("status");
                if (status_profile != null){
                    get_profile();
                }
            }
            else if (requestCode == 1234){
                mNotifCount = 0;
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("counting",0);
                editor.apply();
                invalidateOptionsMenu();
                refresh();
            }
        }
    }

    public void refresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    public void get_profile() {
        progressBar();
        showDialog();
        Call<JSONResponse.GetProfile> call = mApiInterface.kes_profile_get(authorization, school_code.toLowerCase(), memberid);
        call.enqueue(new Callback<JSONResponse.GetProfile>() {
            @Override
            public void onResponse(Call<JSONResponse.GetProfile> call, Response<JSONResponse.GetProfile> response) {
                Log.d("profile_sukses", response.code() + "");
                hideDialog();
                if (response.isSuccessful()) {
                    JSONResponse.GetProfile resource = response.body();
                    status = resource.status;
                    if (status == 1) {
                        fullname        = response.body().getData().getFullname();
                        picture         = response.body().getData().getPicture();
                        classroom_id    = response.body().getData().getClassroom_id();
                        edulevel_id     = response.body().getData().getEdulevel_id();
                        tv_profile.setText(fullname);
                        String imageFile = Base_anak + picture;
                        Glide.with(MenuUtama.this).load(imageFile).into(image_profile);
                        Jadwal_pelajaran();
                        send_data();
                        send_data2();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("status",null);
                        editor.apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse.GetProfile> call, Throwable t) {
                Log.e("onFailure_profile", t.toString());
                hideDialog();
            }
        });
    }

    public void send_data(){
        Bundle bundle = new Bundle();
        if (bundle != null) {
            SharedPreferences.Editor editor = sharedViewpager.edit();
            editor.putString("member_id",memberid);
            editor.putString("school_code",school_code);
            editor.putString("authorization",authorization);
            editor.putString("classroom_id",classroom_id);
            editor.putString("fullname",fullname);
            editor.putString("edulevelid",edulevel_id);
            editor.apply();
            bundle.putString("member_id", memberid);
            bundle.putString("school_code", school_code);
            bundle.putString("authorization", authorization);
            bundle.putString("classroom_id", classroom_id);
            bundle.putString("fullname",fullname);
            bundle.putString("edulevelid",edulevel_id);
            MenuSatuFragment menuSatuFragment = new MenuSatuFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragMenuSatu, menuSatuFragment);
            fragmentTransaction.commitAllowingStateLoss();
            menuSatuFragment.setArguments(bundle);
        }else {
            Toast.makeText(MenuUtama.this,"harap refresh kembali",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public void send_data2(){
        Bundle bundle = new Bundle();
        if (bundle != null) {
            SharedPreferences.Editor editor = sharedViewpager.edit();
            editor.putString("member_id",memberid);
            editor.putString("school_code",school_code);
            editor.putString("authorization",authorization);
            editor.putString("classroom_id",classroom_id);
            editor.putString("fullname",fullname);
            editor.putString("edulevelid",edulevel_id);
            editor.apply();
            bundle.putString("member_id", memberid);
            bundle.putString("school_code", school_code);
            bundle.putString("authorization", authorization);
            bundle.putString("classroom_id", classroom_id);
            bundle.putString("fullname",fullname);
            bundle.putString("edulevelid",edulevel_id);
            MenuDuaFragment menuSatuFragment = new MenuDuaFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragMenuDua, menuSatuFragment);
            fragmentTransaction.commitAllowingStateLoss();
            menuSatuFragment.setArguments(bundle);
        }else {
            Toast.makeText(MenuUtama.this,"harap refresh kembali",Toast.LENGTH_LONG).show();
        }
    }
    private void Jadwal_pelajaran() {

            Call<JSONResponse.JadwalPelajaran> call = mApiInterface.kes_class_schedule_get(authorization, school_code.toLowerCase(), memberid, classroom_id);

            call.enqueue(new Callback<JSONResponse.JadwalPelajaran>() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<JSONResponse.JadwalPelajaran> call, final Response<JSONResponse.JadwalPelajaran> response) {
                    Log.i("Pelajaran_sukses", response.code() + "");
                    if (response.isSuccessful()) {
                        JSONResponse.JadwalPelajaran resource = response.body();

                        status = resource.status;
                        code = resource.code;
                        JadwalSenin jadwalSenin = null;
                        JadwalSelasa jadwalSelasa = null;
                        JadwalRabu jadwalRabu = null;
                        JadwalKamis jadwalKamis = null;
                        JadwalJumat jadwalJumat = null;
                        JadwalSabtu jadwalSabtu = null;
                        if (status == 1 && code.equals("CSCH_SCS_0001")) {
                            for (int i = 0; i < response.body().getData().getClass_schedule().size(); i++) {
                                JSONResponse.JadwalData jadwalData = resource.data.getClass_schedule().get(i);
                                days_name   = jadwalData.getDayName();
                                day_status  = jadwalData.getDayStatus();
                                daysid      = jadwalData.getDayid();
                                day_type    = jadwalData.getDayType();
                                switch (days_name) {
                                    case "Senin": {
                                        for (int o = 0; o < response.body().getData().getClass_schedule().get(i).getScheduleClass().size(); o++) {
                                            mapel       = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesName();
                                            jam_mulai   = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezOk();
                                            jam_selesai = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezFinish();
                                            jamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getScheduleTime();
                                            lamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getLessonDuration();
                                            guru        = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTeacherName();
                                            warna_mapel = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCources_colour();
                                            cources_id  = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesId();
                                            jadwalSenin = new JadwalSenin();
                                            jadwalSenin.setDay_name(days_name);
                                            jadwalSenin.setFullname(guru);
                                            jadwalSenin.setCources_name(mapel);
                                            jadwalSenin.setDuration(String.valueOf(lamber));
                                            jadwalSenin.setJam_mulai(jam_mulai);
                                            jadwalSenin.setCources_id(cources_id);
                                            jadwalSenin.setCources_color(warna_mapel);
                                            jadwalSenin.setJam_selesai(jam_selesai);
                                            itemlist.add(jadwalSenin);
                                        }
                                        seninAdapter = new SeninAdapter(itemlist,"menu");
                                        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
                                        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                        rv_senin.addOnScrollListener(new CenterScrollListener());
                                        rv_senin.setHasFixedSize(true);
                                        rv_senin.setLayoutManager(layoutManager);
                                        rv_senin.setAdapter(seninAdapter);
                                        seninAdapter.setOnItemClickListener(new SeninAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                cources_id = itemlist.get(position).getCources_id();
                                                SharedPreferences.Editor editor = sharedLesson.edit();
                                                editor.putString("school_code", school_code);
                                                editor.putString("authorization", authorization);
                                                editor.putString("classroom_id", classroom_id);
                                                editor.putString("student_id", memberid);
                                                editor.putString("cources_id", cources_id);
                                                editor.apply();
                                                Intent intent = new Intent(MenuUtama.this, LessonReview.class);
                                                intent.putExtra("authorization", authorization);
                                                intent.putExtra("school_code", school_code.toLowerCase());
                                                intent.putExtra("student_id", memberid);
                                                intent.putExtra("classroom_id", classroom_id);
                                                intent.putExtra("cources_id", cources_id);
                                                startActivity(intent);
                                            }
                                        });
                                        break;
                                    }
                                    case "Selasa": {
                                        for (int o = 0; o < response.body().getData().getClass_schedule().get(i).getScheduleClass().size(); o++) {
                                            mapel       = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesName();
                                            jam_mulai   = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezOk();
                                            jam_selesai = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezFinish();
                                            jamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getScheduleTime();
                                            lamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getLessonDuration();
                                            guru        = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTeacherName();
                                            warna_mapel = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCources_colour();
                                            cources_id  = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesId();
                                            jadwalSelasa = new JadwalSelasa();
                                            jadwalSelasa.setFullname(guru);
                                            jadwalSelasa.setDay_name(days_name);
                                            jadwalSelasa.setCources_name(mapel);
                                            jadwalSelasa.setDuration(String.valueOf(lamber));
                                            jadwalSelasa.setJam_mulai(jam_mulai);
                                            jadwalSelasa.setCources_id(cources_id);
                                            jadwalSelasa.setCources_color(warna_mapel);
                                            jadwalSelasa.setJam_selesai(jam_selesai);
                                            itemselasa.add(jadwalSelasa);
                                        }
                                        LinearSnapHelper snapHelper = new LinearSnapHelper();
                                        selasaAdapter = new SelasaAdapter(itemselasa,"menu");
                                        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
                                        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                        rv_selasa.addOnScrollListener(new CenterScrollListener());
                                        rv_selasa.setHasFixedSize(true);
                                        rv_selasa.setLayoutManager(layoutManager);
                                        rv_selasa.setAdapter(selasaAdapter);
                                        selasaAdapter.setOnItemClickListener(new SelasaAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                cources_id = itemselasa.get(position).getCources_id();
                                                SharedPreferences.Editor editor = sharedLesson.edit();
                                                editor.putString("school_code", school_code);
                                                editor.putString("authorization", authorization);
                                                editor.putString("classroom_id", classroom_id);
                                                editor.putString("student_id", memberid);
                                                editor.putString("cources_id", cources_id);
                                                editor.apply();
                                                Intent intent = new Intent(MenuUtama.this, LessonReview.class);
                                                intent.putExtra("authorization", authorization);
                                                intent.putExtra("school_code", school_code.toLowerCase());
                                                intent.putExtra("student_id", memberid);
                                                intent.putExtra("classroom_id", classroom_id);
                                                intent.putExtra("cources_id", cources_id);
                                                startActivity(intent);
                                            }
                                        });
                                        break;
                                    }
                                    case "Rabu": {
                                        for (int o = 0; o < response.body().getData().getClass_schedule().get(i).getScheduleClass().size(); o++) {
                                            mapel       = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesName();
                                            jam_mulai   = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezOk();
                                            jam_selesai = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezFinish();
                                            jamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getScheduleTime();
                                            lamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getLessonDuration();
                                            guru        = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTeacherName();
                                            warna_mapel = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCources_colour();
                                            cources_id  = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesId();
                                            jadwalRabu = new JadwalRabu();
                                            jadwalRabu.setFullname(guru);
                                            jadwalRabu.setDay_name(days_name);
                                            jadwalRabu.setCources_name(mapel);
                                            jadwalRabu.setCources_color(warna_mapel);
                                            jadwalRabu.setCources_id(cources_id);
                                            jadwalRabu.setDuration(String.valueOf(lamber));
                                            jadwalRabu.setJam_mulai(jam_mulai);
                                            jadwalRabu.setJam_selesai(jam_selesai);
                                            itemRabu.add(jadwalRabu);
                                        }
                                        LinearSnapHelper snapHelper = new LinearSnapHelper();
                                        rabuAdapter = new RabuAdapter(itemRabu,"menu");
                                        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
                                        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                        rv_rabu.addOnScrollListener(new CenterScrollListener());
                                        rv_rabu.setHasFixedSize(true);
                                        rv_rabu.setLayoutManager(layoutManager);
                                        rv_rabu.setAdapter(rabuAdapter);
                                        rabuAdapter.setOnItemClickListener(new RabuAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                cources_id = itemRabu.get(position).getCources_id();
                                                SharedPreferences.Editor editor = sharedLesson.edit();
                                                editor.putString("school_code", school_code);
                                                editor.putString("authorization", authorization);
                                                editor.putString("classroom_id", classroom_id);
                                                editor.putString("student_id", memberid);
                                                editor.putString("cources_id", cources_id);
                                                editor.apply();
                                                Intent intent = new Intent(MenuUtama.this, LessonReview.class);
                                                intent.putExtra("authorization", authorization);
                                                intent.putExtra("school_code", school_code.toLowerCase());
                                                intent.putExtra("student_id", memberid);
                                                intent.putExtra("classroom_id", classroom_id);
                                                intent.putExtra("cources_id", cources_id);
                                                startActivity(intent);
                                            }
                                        });
                                        break;
                                    }
                                    case "Kamis": {
                                        for (int o = 0; o < response.body().getData().getClass_schedule().get(i).getScheduleClass().size(); o++) {
                                            mapel       = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesName();
                                            jam_mulai   = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezOk();
                                            jam_selesai = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezFinish();
                                            jamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getScheduleTime();
                                            lamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getLessonDuration();
                                            guru        = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTeacherName();
                                            warna_mapel = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCources_colour();
                                            cources_id  = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesId();
                                            jadwalKamis = new JadwalKamis();
                                            jadwalKamis.setFullname(guru);
                                            jadwalKamis.setDay_name(days_name);
                                            jadwalKamis.setCources_name(mapel);
                                            jadwalKamis.setCources_color(warna_mapel);
                                            jadwalKamis.setDuration(String.valueOf(lamber));
                                            jadwalKamis.setJam_mulai(jam_mulai);
                                            jadwalKamis.setCources_id(cources_id);
                                            jadwalKamis.setJam_selesai(jam_selesai);
                                            itemKamis.add(jadwalKamis);
                                        }
                                        LinearSnapHelper snapHelper = new LinearSnapHelper();
                                        kamisAdapter = new KamisAdapter(itemKamis,"menu");
                                        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
                                        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                        rv_kamis.addOnScrollListener(new CenterScrollListener());
                                        rv_kamis.setHasFixedSize(true);
                                        rv_kamis.setLayoutManager(layoutManager);
                                        rv_kamis.setAdapter(kamisAdapter);
                                        kamisAdapter.setOnItemClickListener(new KamisAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                cources_id = itemKamis.get(position).getCources_id();
                                                SharedPreferences.Editor editor = sharedLesson.edit();
                                                editor.putString("school_code", school_code);
                                                editor.putString("authorization", authorization);
                                                editor.putString("classroom_id", classroom_id);
                                                editor.putString("student_id", memberid);
                                                editor.putString("cources_id", cources_id);
                                                editor.apply();
                                                Intent intent = new Intent(MenuUtama.this, LessonReview.class);
                                                intent.putExtra("authorization", authorization);
                                                intent.putExtra("school_code", school_code.toLowerCase());
                                                intent.putExtra("student_id", memberid);
                                                intent.putExtra("classroom_id", classroom_id);
                                                intent.putExtra("cources_id", cources_id);
                                                startActivity(intent);
                                            }
                                        });
                                        break;
                                    }
                                    case "Jumat": {
                                        for (int o = 0; o < response.body().getData().getClass_schedule().get(i).getScheduleClass().size(); o++) {
                                            mapel       = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesName();
                                            jam_mulai   = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezOk();
                                            jam_selesai = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezFinish();
                                            jamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getScheduleTime();
                                            lamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getLessonDuration();
                                            guru        = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTeacherName();
                                            warna_mapel = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCources_colour();
                                            cources_id  = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesId();
                                            jadwalJumat = new JadwalJumat();
                                            jadwalJumat.setFullname(guru);
                                            jadwalJumat.setDay_name(days_name);
                                            jadwalJumat.setCources_name(mapel);
                                            jadwalJumat.setCources_color(warna_mapel);
                                            jadwalJumat.setDuration(String.valueOf(lamber));
                                            jadwalJumat.setJam_mulai(jam_mulai);
                                            jadwalJumat.setCources_id(cources_id);
                                            jadwalJumat.setJam_selesai(jam_selesai);
                                            itemJumat.add(jadwalJumat);
                                        }
                                        LinearSnapHelper snapHelper = new LinearSnapHelper();
                                        jumatAdapter = new JumatAdapter(itemJumat,"menu");
                                        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
                                        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                        rv_jumat.addOnScrollListener(new CenterScrollListener());
                                        rv_jumat.setHasFixedSize(true);
                                        rv_jumat.setLayoutManager(layoutManager);
                                        rv_jumat.setAdapter(jumatAdapter);
                                        jumatAdapter.setOnItemClickListener(new JumatAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                cources_id = itemJumat.get(position).getCources_id();
                                                SharedPreferences.Editor editor = sharedLesson.edit();
                                                editor.putString("school_code", school_code);
                                                editor.putString("authorization", authorization);
                                                editor.putString("classroom_id", classroom_id);
                                                editor.putString("student_id", memberid);
                                                editor.putString("cources_id", cources_id);
                                                editor.apply();
                                                Intent intent = new Intent(MenuUtama.this, LessonReview.class);
                                                intent.putExtra("authorization", authorization);
                                                intent.putExtra("school_code", school_code.toLowerCase());
                                                intent.putExtra("student_id", memberid);
                                                intent.putExtra("classroom_id", classroom_id);
                                                intent.putExtra("cources_id", cources_id);
                                                startActivity(intent);
                                            }
                                        });
                                        break;
                                    }
                                    case "Sabtu": {
                                        for (int o = 0; o < response.body().getData().getClass_schedule().get(i).getScheduleClass().size(); o++) {
                                            mapel       = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesName();
                                            jam_mulai   = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezOk();
                                            jam_selesai = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTimezFinish();
                                            jamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getScheduleTime();
                                            lamber      = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getLessonDuration();
                                            guru        = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getTeacherName();
                                            warna_mapel = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCources_colour();
                                            cources_id  = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(o).getCourcesId();
                                            jadwalSabtu = new JadwalSabtu();
                                            jadwalSabtu.setFullname(guru);
                                            jadwalSabtu.setDay_name(days_name);
                                            jadwalSabtu.setCources_color(warna_mapel);
                                            jadwalSabtu.setCources_name(mapel);
                                            jadwalSabtu.setCources_id(cources_id);
                                            jadwalSabtu.setDuration(String.valueOf(lamber));
                                            jadwalSabtu.setJam_mulai(jam_mulai);
                                            jadwalSabtu.setJam_selesai(jam_selesai);
                                            itemSabtu.add(jadwalSabtu);
                                        }
                                        LinearSnapHelper snapHelper = new LinearSnapHelper();
                                        sabtuAdapter = new SabtuAdapter(itemSabtu,"menu");
                                        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
                                        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                                        rv_sabtu.addOnScrollListener(new CenterScrollListener());
                                        rv_sabtu.setHasFixedSize(true);
                                        rv_sabtu.setLayoutManager(layoutManager);
                                        rv_sabtu.setAdapter(sabtuAdapter);
                                        sabtuAdapter.setOnItemClickListener(new SabtuAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                cources_id = itemSabtu.get(position).getCources_id();
                                                SharedPreferences.Editor editor = sharedLesson.edit();
                                                editor.putString("school_code", school_code);
                                                editor.putString("authorization", authorization);
                                                editor.putString("classroom_id", classroom_id);
                                                editor.putString("student_id", memberid);
                                                editor.putString("cources_id", cources_id);
                                                editor.apply();
                                                Intent intent = new Intent(MenuUtama.this, LessonReview.class);
                                                intent.putExtra("authorization", authorization);
                                                intent.putExtra("school_code", school_code.toLowerCase());
                                                intent.putExtra("student_id", memberid);
                                                intent.putExtra("classroom_id", classroom_id);
                                                intent.putExtra("cources_id", cources_id);
                                                startActivity(intent);
                                            }
                                        });
                                        break;
                                    }
                                }
                                if (days_name.equals(day)) {
                                    jam_mulai = response.body().getData().getClass_schedule().get(i).getScheduleClass().get(response.body().getData().getClass_schedule().get(i).getScheduleClass().size() - 1).getTimezFinish();
                                }
                            }
                            String tanggal = tanggalFormat.format(Calendar.getInstance().getTime());
                            // Set car item title.
                            String jam_sekarang = jamformat.format(Calendar.getInstance().getTime());
                            try {
                                date_now = times_format.parse(tanggal + " " + jam_sekarang + ":00");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Long times_now = date_now.getTime();
                            try {
                                dates = times_format.parse(tanggal + " " + jam_mulai + ":00");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Long times_start = dates.getTime();

                            switch (day) {
                                case "Senin":
                                    if (itemlist.size() == 0) {
                                        title_jadwal.setText("Jadwal besok");
                                        Snackbar.make(coordinatorLayout, "Tidak ada jadwal hari ini", Snackbar.LENGTH_LONG).show();
                                        rv_selasa.setVisibility(View.VISIBLE);
                                    } else {
                                        if (times_now > times_start) {
                                            title_jadwal.setText("Jadwal besok");
                                            if (itemselasa.size() == 0){
                                                tv_hint.setVisibility(View.VISIBLE);
                                                tv_hint.setText("Tidak ada jadwal untuk besok");
                                            }else {
                                                rv_selasa.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            title_jadwal.setText("Jadwal hari ini");
                                            rv_senin.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    break;
                                case "Selasa":
                                    if (itemselasa.size() == 0) {
                                        title_jadwal.setText("Jadwal besok");
                                        Snackbar.make(coordinatorLayout, "Tidak ada jadwal hari ini", Snackbar.LENGTH_LONG).show();
                                        rv_rabu.setVisibility(View.VISIBLE);
                                    } else {
                                        if (times_now > times_start) {
                                            title_jadwal.setText("Jadwal besok");
                                            if (itemRabu.size() == 0){
                                                tv_hint.setVisibility(View.VISIBLE);
                                                tv_hint.setText("Tidak ada jadwal untuk besok");
                                            }else {
                                                rv_rabu.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            title_jadwal.setText("Jadwal hari ini");
                                            rv_selasa.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    break;
                                case "Rabu":
                                    if (itemRabu.size() == 0) {
                                        title_jadwal.setText("Jadwal besok");
                                        Snackbar.make(coordinatorLayout, "Tidak ada jadwal hari ini", Snackbar.LENGTH_LONG).show();
                                        rv_kamis.setVisibility(View.VISIBLE);
                                    } else {
                                        if (times_now > times_start) {
                                            title_jadwal.setText("Jadwal besok");
                                            if (itemKamis.size() == 0){
                                                tv_hint.setVisibility(View.VISIBLE);
                                                tv_hint.setText("Tidak ada jadwal untuk besok");
                                            }else {
                                                rv_kamis.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            title_jadwal.setText("Jadwal hari ini");
                                            rv_rabu.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    break;
                                case "Kamis":
                                    if (itemKamis.size() == 0) {
                                        title_jadwal.setText("Jadwal besok");
                                        Snackbar.make(coordinatorLayout, "Tidak ada jadwal hari ini", Snackbar.LENGTH_LONG).show();
                                        rv_jumat.setVisibility(View.VISIBLE);
                                    } else {
                                        if (times_now > times_start) {
                                            title_jadwal.setText("Jadwal besok");
                                            if (itemJumat.size() == 0){
                                                tv_hint.setVisibility(View.VISIBLE);
                                                tv_hint.setText("Tidak ada jadwal untuk besok");
                                            }else {
                                                rv_jumat.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            title_jadwal.setText("Jadwal hari ini");
                                            rv_kamis.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    break;
                                case "Jumat":
                                    if (itemJumat.size() == 0) {
                                        title_jadwal.setText("Jadwal besok");
                                        Snackbar.make(coordinatorLayout, "Tidak ada jadwal hari ini", Snackbar.LENGTH_LONG).show();
                                        rv_sabtu.setVisibility(View.VISIBLE);
                                    } else {
                                        if (times_now > times_start) {
                                            title_jadwal.setText("Jadwal besok");
                                            if (itemSabtu.size() == 0){
                                                tv_hint.setVisibility(View.VISIBLE);
                                                tv_hint.setText("Tidak ada jadwal untuk besok");
                                            }else {
                                                rv_sabtu.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            title_jadwal.setText("Jadwal hari ini");
                                            rv_jumat.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    break;
                                case "Sabtu":
                                    if (itemSabtu.size() == 0) {
                                        title_jadwal.setText("Jadwal besok");
                                        Snackbar.make(coordinatorLayout, "Tidak ada jadwal hari ini", Snackbar.LENGTH_LONG).show();
                                        rv_senin.setVisibility(View.VISIBLE);
                                    } else {
                                        if (times_now > times_start) {
                                            title_jadwal.setText("Jadwal hari Senin");
                                            if (itemlist.size() == 0){
                                                tv_hint.setVisibility(View.VISIBLE);
                                                tv_hint.setText("Tidak ada jadwal untuk besok");
                                            }else {
                                                rv_senin.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            title_jadwal.setText("Jadwal hari ini");
                                            rv_sabtu.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    break;
                                case "Minggu":
                                    if (itemlist.size() == 0) {
                                        title_jadwal.setText("Jadwal selasa");
                                        Snackbar.make(coordinatorLayout, "Tidak ada jadwal hari ini", Snackbar.LENGTH_LONG).show();
                                        rv_selasa.setVisibility(View.VISIBLE);
                                    } else {
                                        if (times_now > times_start) {
                                            title_jadwal.setText("Jadwal selasa");
                                            if (itemselasa.size() == 0){
                                                tv_hint.setVisibility(View.VISIBLE);
                                                tv_hint.setText("Tidak ada jadwal untuk besok");
                                            }else {
                                                rv_selasa.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            title_jadwal.setText("Jadwal besok");
                                            rv_senin.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse.JadwalPelajaran> call, Throwable t) {
                    Log.e("onFailure", t.toString());
                }

            });


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
        dialog = new ProgressDialog(MenuUtama.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
    }

    public void logout(){
        Call<JSONResponse.Logout> call = mApiInterface.kes_logout_post(authorization,school_code.toLowerCase(),memberid);
        call.enqueue(new Callback<JSONResponse.Logout>() {
            @Override
            public void onResponse(Call<JSONResponse.Logout> call, Response<JSONResponse.Logout> response) {
                Log.d("Sukses",response.code()+"");
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        status  = response.body().status;
                        code    = response.body().code;
                        if (status == 1 && code.equals("DTS_SCS_0001")) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(Masuk.session_status, false);
                            editor.putString(TAG_EMAIL, null);
                            editor.putString(TAG_MEMBER_ID, null);
                            editor.putString(TAG_FULLNAME, null);
                            editor.putString(TAG_MEMBER_TYPE, null);
                            editor.putString(TAG_TOKEN, null);
                            editor.apply();
                            Intent intent = new Intent(MenuUtama.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse.Logout> call, Throwable t) {
                Log.e("gagal",t.toString());
            }
        });
    }
}
