package com.fingertech.kesforstudent.Guru.ActivityGuru.AdapterAbsen;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fingertech.kesforstudent.Controller.Auth;
import com.fingertech.kesforstudent.Guru.ActivityGuru.AbsenMurid;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen.ModelDataAttidude;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsen.ModelDetailAbsen;
import com.fingertech.kesforstudent.Masuk;
import com.fingertech.kesforstudent.R;
import com.fingertech.kesforstudent.Rest.ApiClient;
import com.fingertech.kesforstudent.Rest.JSONResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AdapterDetailAbsen extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    private List<ModelDetailAbsen> modelDetailAbsenList;
    int statusattidude;
    SharedPreferences sharedPreferences;
    String authorization,school_code,member_id,codeattidude,attidudename,attidudegradecode,scyear_id;
    List<ModelDataAttidude> modelDataAttidudes = new ArrayList<>();
    ModelDataAttidude modelDataAttidude;
    public static final String TAG_EMAIL        = "email";
    public static final String TAG_MEMBER_ID    = "member_id";
    public static final String TAG_FULLNAME     = "fullname";
    public static final String TAG_MEMBER_TYPE  = "member_type";
    public static final String TAG_TOKEN        = "token";
    public static final String TAG_SCHOOL_CODE  = "school_code";
    public static final String TAG_CLASS_ID     = "classroom_id";
    public static final String TAG_YEAR_ID      = "scyear_id";
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    public AdapterDetailAbsen(Context context, List<ModelDetailAbsen> viewItemlist) {
        this.context = context;
        this.modelDetailAbsenList = viewItemlist;
    }





    @Override
    public int getCount() {
        return modelDetailAbsenList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, Object object) {
        return (view==object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Auth mApiInterface;

        inflater                    = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view                   = inflater.inflate(R.layout.fragmentsabsen,container,false);
        TextView namaanak           = view.findViewById(R.id.tv_nama);
        TextView nis                = view.findViewById(R.id.tv_nis);
        RecyclerView rv_attidude    = view.findViewById(R.id.rv_fragmentabsen);
        mApiInterface               = ApiClient.getClient().create(Auth.class);
//        sharedPreferences           = context.getSharedPreferences(Masuk.my_shared_preferences,Context.MODE_PRIVATE);
//        authorization               = sharedPreferences.getString(TAG_TOKEN,"");
//        member_id                   = sharedPreferences.getString(TAG_MEMBER_ID,"");
//        scyear_id                   = sharedPreferences.getString(TAG_YEAR_ID,"");
//        school_code                 = sharedPreferences.getString(TAG_SCHOOL_CODE,"");
        authorization               ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImZhamFyYWRpcHJhc3RAZ21haWwuY29tIiwibWVtYmVyX2lkIjoiODUzIiwiZnVsbG5hbWUiOiJNb25hbGlzYSIsIm1lbWJlcl90eXBlIjoiMyJ9.GDytEt9XgLGPzAMUUyC5YkDSE378H2i-T-b-q8_w4U4";
        member_id                   ="777";
        scyear_id                   ="1";
        school_code                 ="bpk02";
        Call<JSONResponse.Attidude> Callat = mApiInterface.kes_attitude_get(authorization,school_code,member_id,scyear_id);
        Callat.enqueue(new Callback<JSONResponse.Attidude>() {
            @Override
            public void onResponse(Call<JSONResponse.Attidude> call, Response<JSONResponse.Attidude> response) {
                Log.d("attidude",response.code()+"");
                modelDataAttidudes.clear();
                if (response.isSuccessful()){
                    JSONResponse.Attidude resourceattidude = response.body();
                    statusattidude = resourceattidude.statusattidude;
                    codeattidude   = resourceattidude.codeattidude;
                    if (statusattidude==1 && codeattidude.equals("DTS_SCS_0001")){
                        for (int at = 0; at<response.body().getDataattidude().size();at++){
                            attidudename = response.body().getDataattidude().get(at).getAttitude_grade_name();
                            modelDataAttidude = new ModelDataAttidude();
                            modelDataAttidude.setAttitude_name(attidudename);
                            modelDataAttidudes.add(modelDataAttidude);
                            Log.d("nama",attidudename.toString());
                            for (int i = 0; i < response.body().getDataattidude().get(at).getData().size();i++)
                            {
                                attidudegradecode = response.body().getDataattidude().get(at).getData().get(i).getAttitude_grade_code();
//

                            }
                        }
                        AdapterAttidudes    adapterAttidudes    = new AdapterAttidudes(context,modelDataAttidudes);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rv_attidude.setLayoutManager(linearLayoutManager);
                        rv_attidude.setAdapter(adapterAttidudes);
                    }

                }

            }

            @Override
            public void onFailure(Call<JSONResponse.Attidude> call, Throwable t) {

            }
        });




        ModelDetailAbsen viewitem = modelDetailAbsenList.get(position);
        Log.d("item",viewitem.getNis()+"");
        namaanak.setText(viewitem.getNama());
        nis.setText(viewitem.getNis());
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}