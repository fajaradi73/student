package com.fingertech.kesforstudent.Guru.ActivityGuru;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fingertech.kesforstudent.R;
import com.fingertech.kesforstudent.Rest.ApiClient;
import com.fingertech.kesforstudent.Student.Activity.LihatPdf;
import com.fingertech.kesforstudent.Util.CheckForSDCard;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.fingertech.kesforstudent.Service.App.ANDROID_CHANNEL_ID;

public class LihatFile extends AppCompatActivity {

    CardView iv_close,iv_unduh;
    String file;
    WebView webView;
    private static final int WRITE_REQUEST_CODE = 300;
    private NotificationManagerCompat notificationManager;
    NotificationCompat.Builder notification;
    int progressMax = 100;
    PendingIntent pendingIntent;
    String extension,base_silabus;
    ProgressBar progressDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        webView     = findViewById(R.id.webview);
        iv_close    = findViewById(R.id.iv_close);
        iv_unduh    = findViewById(R.id.iv_download);
        progressDialog  = findViewById(R.id.progress_bar);
        base_silabus    = ApiClient.BASE_SILABUS;
        file    = getIntent().getStringExtra("file");
        extension   = file.substring(file.lastIndexOf("."));

        webView.setWebViewClient(new AppWebViewClients(progressDialog));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollContainer(false);
        webView.getSettings().setAllowFileAccess(true);

        switch (extension) {
            case ".png":
            case ".jpg":
            case ".jpeg":
                webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                webView.loadDataWithBaseURL(null, "<html><head></head><body><table style=\"width:100%; height:100%;\"><tr><td style=\"text-align:center;\"><img style=\"max-width:100%; height:auto;\"src=\"" + base_silabus + file + "\"></td></tr></table></body></html>", "html/css", "utf-8", null);
                break;
            default:
                webView.loadUrl("https://docs.google.com/viewer?url=" + base_silabus + file+"&embedded=true");
                break;
        }

        iv_close.setOnClickListener(v -> finish());

        notificationManager = NotificationManagerCompat.from(this);

        iv_unduh.setOnClickListener(v -> {
            if (CheckForSDCard.isSDCardPresent()) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        new DownloadFile().execute(base_silabus+file);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(LihatFile.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                //check all needed permissions together
                TedPermission.with(LihatFile.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("Jika Anda menolak izin, Anda tidak dapat menggunakan layanan ini\n\nSilakan aktifkan izin di [Pengaturan] > [Izin]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            } else {
                Toast.makeText(getApplicationContext(),
                        "SD Card not found", Toast.LENGTH_LONG).show();

            }
        });

    }
    public class AppWebViewClients extends WebViewClient {
        private ProgressBar progressBar;

        public AppWebViewClients(ProgressBar progressBar) {
            this.progressBar=progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);progressBar.setVisibility(View.GONE);
        }
    }
    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(LihatFile.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();

            Intent intent = new Intent(LihatFile.this, LihatPdf.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(LihatFile.this, 2, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            notification = new NotificationCompat.Builder(LihatFile.this, ANDROID_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_logo_grey)
                    .setContentTitle("Download")
                    .setContentText("Download in progress")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setProgress(progressMax, 0, true);
            notificationManager.notify(2, notification.build());

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.getDefault()).format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "KES Documents/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("KES", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
            notification.setContentText(Integer.parseInt(progress[0])+" %")
                    .setProgress(progressMax, Integer.parseInt(progress[0]), false);
            notificationManager.notify(2, notification.build());
            Log.d("Loading",progress[0]+"%");
        }

        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();
            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();

            if (message.equals("Something went wrong")) {
                notification.setContentText("Download gagal")
                        .setProgress(0, 0, false)
                        .setOngoing(false)
                        .setContentIntent(pendingIntent);
                notificationManager.notify(2, notification.build());
            }else {
                notification.setContentText("Download selesai")
                        .setProgress(0, 0, false)
                        .setOngoing(false)
                        .setContentIntent(pendingIntent);
                notificationManager.notify(2, notification.build());
            }
        }
    }
}
