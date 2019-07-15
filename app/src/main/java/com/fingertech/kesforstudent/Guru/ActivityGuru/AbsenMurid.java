package com.fingertech.kesforstudent.Guru.ActivityGuru;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fingertech.kesforstudent.Controller.Auth;
import com.fingertech.kesforstudent.Guru.ActivityGuru.AdapterAbsen.AdapterAttidudes;
import com.fingertech.kesforstudent.Guru.ActivityGuru.AdapterAbsen.AdapterCodeAbsen;
import com.fingertech.kesforstudent.Guru.ActivityGuru.AdapterAbsen.AdapterDetailAbsen;
import com.fingertech.kesforstudent.Guru.AdapterGuru.AdapterAbsen;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen.ModelAbsenGuru;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen.ModelArrayAbsen;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen.ModelDataAttidude;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen.ModelDetailAbsen;
import com.fingertech.kesforstudent.Masuk;
import com.fingertech.kesforstudent.R;
import com.fingertech.kesforstudent.Rest.ApiClient;
import com.fingertech.kesforstudent.Rest.JSONResponse;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsenMurid extends AppCompatActivity {
    CardView btninfo;
    RecyclerView rv_absen;
    List<ModelDataAttidude> modelDataAttidudes = new ArrayList<>();
    AdapterAbsen adapterAbsen;
    List<ModelAbsenGuru> modelAbsenGuruList = new ArrayList<>();
//    List<ModelCodeAttidude> modelCodeAttidudes = new ArrayList<>();
    List<ModelArrayAbsen> modelArrayAbsenList = new ArrayList<>();
    ModelDetailAbsen modelDetailAbsen;
    ModelAbsenGuru modelAbsenGuru;
    EditText et_search;
    ModelDataAttidude modelDataAttidude;
//    ModelCodeAttidude modelCodeAttidude;
    ModelArrayAbsen modelArrayAbsen;
    AdapterCodeAbsen adapterCodeAbsen;
    Toolbar toolbar;
    Auth mApiInterface;
    String  schedule_id,authorization,school_code,member_id,scyear_id, classroom,code,nama,absent,bgcolor,attidude,absentcode,absentwarna;
    int status,statusattidude;
    SharedPreferences sharedpreferences;

    public static final String TAG_EMAIL        = "email";
    public static final String TAG_MEMBER_ID    = "member_id";
    public static final String TAG_FULLNAME     = "fullname";
    public static final String TAG_MEMBER_TYPE  = "member_type";
    public static final String TAG_TOKEN        = "token";
    public static final String TAG_SCHOOL_CODE  = "school_code";
    public static final String TAG_CLASS_ID     = "classroom_id";
    public static final String TAG_YEAR_ID      = "scyear_id";
    String nis;

    JsonElement jsonElement;
    JSONObject jsonObject,dataobject;
    JSONArray absenObject,absenArray,attendanceArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.absen_murid);
        btninfo             = findViewById(R.id.Cv_informasi);
        rv_absen            = findViewById(R.id.rv_absenguru);
        toolbar             = findViewById(R.id.toolbarAbsen);
        mApiInterface       = ApiClient.getClient().create(Auth.class);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        sharedpreferences   = getSharedPreferences(Masuk.my_shared_preferences, Context.MODE_PRIVATE);
        authorization       = sharedpreferences.getString(TAG_TOKEN,"");
        member_id           = sharedpreferences.getString(TAG_MEMBER_ID,"");
        scyear_id           = sharedpreferences.getString(TAG_YEAR_ID,"");
        school_code         = sharedpreferences.getString(TAG_SCHOOL_CODE,"");
        classroom           = sharedpreferences.getString(TAG_CLASS_ID,"");
        schedule_id = "500";
        classroom   = "1";
        GetStudent();
        Dialog();

    }

    public void Dialog(){

        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder showdialog = new AlertDialog.Builder(AbsenMurid.this);
                TextView btnclose;
                View view = getLayoutInflater().inflate(R.layout.layout_info_absen,null);
                btnclose = view.findViewById(R.id.btnclose);


                showdialog.setView(view);
                AlertDialog dialog = showdialog.create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
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

    private void GetStudent(){
        Call<JSONResponse.Absen> call = mApiInterface.kes_classroom_absent_get(authorization,school_code,member_id,scyear_id,classroom,schedule_id);
        call.enqueue(new Callback<JSONResponse.Absen>() {
            @Override
            public void onResponse(Call<JSONResponse.Absen> call, Response<JSONResponse.Absen> response) {
                Log.d("suksesabsent",response.code()+"");

                if (response.isSuccessful()){
                    JSONResponse.Absen resource = response.body();
                    status                      = resource.status;
                    code                        = resource.code;
                    if (status==1 && code.equals("DTS_SCS_0001")){
                        modelAbsenGuruList.clear();
                        for (JSONResponse.StudentAbsentItem studentAbsentItem : response.body().getData().getStudentAbsent()){
                            nama    = studentAbsentItem.getFullname();
                            nis     = studentAbsentItem.getMemberCode();
                            for (JSONResponse.AbsenDetailItem dataAbsen : studentAbsentItem.getAbsenDetail()){
                                for (JSONResponse.AttendanceDetailItem attendanceDetailItem : dataAbsen.getAttendanceDetail()){
                                    absentcode  = attendanceDetailItem.getTypeText();
                                    absentwarna = attendanceDetailItem.getBgcolor();
                                    modelArrayAbsen = new ModelArrayAbsen();
                                    modelArrayAbsen.setCodeabsen(absentcode);
                                    modelArrayAbsen.setWarna(absentwarna);
                                    modelArrayAbsen.setNis(nis);
                                    modelArrayAbsenList.add(modelArrayAbsen);
                                }
                            }

                            modelAbsenGuru = new ModelAbsenGuru();
                            modelAbsenGuru.setNama(nama);
                            modelAbsenGuru.setNis(nis);
                            modelAbsenGuru.setModelArrayAbsenList(modelArrayAbsenList);
                            modelAbsenGuruList.add(modelAbsenGuru);
                        }
                        adapterAbsen = new AdapterAbsen(AbsenMurid.this,modelAbsenGuruList);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(AbsenMurid.this);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        rv_absen.setLayoutManager(layoutManager);
                        rv_absen.setAdapter(adapterAbsen);

//                        adapterAbsen.setOnItemClickListener(new AdapterAbsen.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                CardView btnnext,btnback,iv_close;
//                                AdapterDetailAbsen adapterDetailAbsen;
//                                ViewPager viewpager;
//
//                                view     = getLayoutInflater().inflate(R.layout.activity_detail_absen_guru,null);
//                                btnnext  = view.findViewById(R.id.btnnext);
//                                btnback  = view.findViewById(R.id.btnback);
//                                viewpager= view.findViewById(R.id.pagerabsen);
//                                iv_close    = view.findViewById(R.id.iv_close);
//                                final Dialog mBottomSheetDialog = new Dialog(AbsenMurid.this);
//                                mBottomSheetDialog.setContentView(view);
//                                mBottomSheetDialog.setCancelable(true);
//                                mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
//                                        LinearLayout.LayoutParams.MATCH_PARENT);
//                                mBottomSheetDialog.getWindow().setGravity(Gravity.CENTER);
//                                mBottomSheetDialog.show();
//
//                                iv_close.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        mBottomSheetDialog.dismiss();
//                                    }
//                                });
//
//                            }
//                        });


                    }

                }
            }

            @Override
            public void onFailure(Call<JSONResponse.Absen> call, Throwable t) {
                Log.e("eror_absen",t.toString());
            }
        });

    }

}
