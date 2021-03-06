package com.fingertech.kesforstudent.Guru.AdapterGuru.AdapterAbsensi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fingertech.kesforstudent.Controller.Auth;
import com.fingertech.kesforstudent.Guru.ActivityGuru.AbsenMurid;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen.ModelAtitude;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen.ModelDataAttidude;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen.ModelDetailAbsen;
import com.fingertech.kesforstudent.Masuk;
import com.fingertech.kesforstudent.R;
import com.fingertech.kesforstudent.Rest.ApiClient;
import com.fingertech.kesforstudent.Rest.JSONResponse;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AdapterDetailAbsen extends PagerAdapter implements AdapterCode.OnImageClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<ModelDetailAbsen> modelDetailAbsenList;
    private AbsenMurid absenMurid;
    private CardView btn_next,btn_back,btn_simpan;
    private ViewPager viewPager;
    private RecyclerView rv_attidude;
    private TextView namaanak,nis;
    private Auth mApiInterface;

    private View views;

    private String  schedule_id,authorization,school_code,member_id,scyear_id, classroom,code,nama,student_id,schedule_date,attidude,absentcode,absentwarna;
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

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("in","ID"));
    private Dialog dialog;
    private Activity activity;
    private JSONArray jsonArraya,array;

    private String absen;

    public AdapterDetailAbsen(Context context, List<ModelDetailAbsen> viewItemlist, ViewPager viewPager, View views, String schedule_id, Dialog dialog,Activity activity,String absen) {
        this.context            = context;
        this.modelDetailAbsenList = viewItemlist;
        this.viewPager          = viewPager;
        this.views              = views;
        this.schedule_id        = schedule_id;
        this.dialog             = dialog;
        this.activity           = activity;
        this.absen              = absen;
    }
    @Override
    public int getCount() {
        return modelDetailAbsenList.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, Object object) {
        return (view==object);
    }
    private String[] grade     = new String[]{"1","2","3"};
    private String[] gradeid     = new String[]{"1","6","11"};
    EditText editText;
    JSONObject absenObject;

    RoundRectView roundRectView;
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater            = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view           = inflater.inflate(R.layout.fragments_absen,container,false);
        namaanak            = view.findViewById(R.id.tv_nama);
        nis                 = view.findViewById(R.id.tv_nis);
        rv_attidude         = view.findViewById(R.id.rv_absen);
        btn_back            = view.findViewById(R.id.btnback);
        btn_next            = view.findViewById(R.id.btnnext);
        btn_simpan          = view.findViewById(R.id.btn_simpan);
        mApiInterface       = ApiClient.getClient().create(Auth.class);
        editText            = view.findViewById(R.id.et_komentar);
        roundRectView       = view.findViewById(R.id.cv_komentar);

        sharedpreferences   = context.getSharedPreferences(Masuk.my_shared_preferences, Context.MODE_PRIVATE);
        authorization       = sharedpreferences.getString(TAG_TOKEN,"");
        member_id           = sharedpreferences.getString(TAG_MEMBER_ID,"");
        scyear_id           = sharedpreferences.getString(TAG_YEAR_ID,"");
        school_code         = sharedpreferences.getString(TAG_SCHOOL_CODE,"");
        classroom           = sharedpreferences.getString(TAG_CLASS_ID,"");

        schedule_date   = dateFormat.format(Calendar.getInstance().getTime());

        ModelDetailAbsen viewitem = modelDetailAbsenList.get(position);
        namaanak.setText(viewitem.getNama());
        nis.setText(viewitem.getNis());
        AdapterAttidudes adapterAttidudes = new AdapterAttidudes(context, viewitem.getModelDataAttidudeList(),this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_attidude.setHasFixedSize(true);
        rv_attidude.setLayoutManager(layoutManager);
        rv_attidude.setAdapter(adapterAttidudes);
        container.addView(view);
        if (position == 0){
            btn_back.setVisibility(View.INVISIBLE);
        }else if (position == modelDetailAbsenList.size()-1){
            btn_back.setVisibility(View.GONE);
            btn_next.setVisibility(View.GONE);
            btn_simpan.setVisibility(View.VISIBLE);
        }else {
            btn_simpan.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
            btn_back.setVisibility(View.VISIBLE);
        }
        roundRectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (absenObject != null) {
                    String ids = modelDetailAbsenList.get(0).getModelDataAttidudeList().get(postion).getAttitudeid();
                    Log.d("datas", absenObject + "/"+ids);
                }else {

                    Log.d("datas", "data" + "kosong");
                }
                viewPager.setCurrentItem(position + 1, true);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(position - 1, true);
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonArraya = new JSONArray();
                for (int i = 0 ; i < modelDetailAbsenList.size(); i ++){
                    student_id  = modelDetailAbsenList.get(i).getId();
                    JSONArray jsonArray = new JSONArray();
                    if (modelDetailAbsenList.get(i).getModelDataAttidudeList() != null || modelDetailAbsenList.get(i).getModelDataAttidudeList().size() > 0 ) {
                        for (int o = 1; o < modelDetailAbsenList.get(i).getModelDataAttidudeList().size(); o++) {
                            String ids = modelDetailAbsenList.get(i).getModelDataAttidudeList().get(o).getAttitudeid();
                            JSONObject jsonObject1 = new JSONObject();
                            try {
                                jsonObject1.put("attitudeid", ids);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            for (int p = 1; p < modelDetailAbsenList.get(i).getModelDataAttidudeList().get(o).getModelAttidudeList().size(); p++) {
                                String id = modelDetailAbsenList.get(i).getModelDataAttidudeList().get(o).getModelAttidudeList().get(p).getId();
                                if (id.equals(ids)) {
                                    String gradeid = modelDetailAbsenList.get(i).getModelDataAttidudeList().get(o).getModelAttidudeList().get(p).getId_atitude();
                                    try {
                                        jsonObject1.put("gradeid", String.valueOf(Integer.valueOf(gradeid) - 4));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            jsonArray.put(jsonObject1);
                        }
                    }

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id",student_id);
                        jsonObject.put("absentStatus","1");
                        jsonObject.put("absentType","6");
                        jsonObject.put("detail",jsonArray);
                        jsonObject.put("komentar","");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArraya.put(jsonObject);
                }
                if (absen.equals("insert")) {
                    sendPost();
                }else if (absen.equals("update")){
                    updatePost();
                }
            }
        });

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

    private void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ApiClient.BASE_API+"teachers/kes_insertdb_student_attendance");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setRequestProperty("Authorization",authorization);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("school_code",school_code.toLowerCase());
                    jsonParam.put("teacher_id",member_id);
                    jsonParam.put("classroom_id",classroom);
                    jsonParam.put("schedule_time_id",schedule_id);
                    jsonParam.put("schedule_date",schedule_date);
                    jsonParam.put("studentAbsent",jsonArraya);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());
                    if (conn.getResponseMessage().equals("OK")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FancyToast.makeText(context,"Sukses absen",Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                dialog.dismiss();
                            }
                        });
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    private void updatePost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ApiClient.BASE_API+"teachers/kes_updatedb_student_attendance");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setRequestProperty("Authorization",authorization);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("school_code",school_code.toLowerCase());
                    jsonParam.put("teacher_id",member_id);
                    jsonParam.put("classroom_id",classroom);
                    jsonParam.put("schedule_time_id",schedule_id);
                    jsonParam.put("schedule_date",schedule_date);
                    jsonParam.put("studentAbsent",jsonArraya);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());
                    if (conn.getResponseMessage().equals("OK")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FancyToast.makeText(context,"Sukses update absen",Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                dialog.dismiss();
                            }
                        });
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    int postion;

    @Override
    public void onImageClick(JSONObject jsonObject,int position) {
        this.postion = position;
        absenObject = new JSONObject();
        absenObject = jsonObject;
    }
}