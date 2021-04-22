package com.erxprescriptionuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;

import com.erxprescriptionuser.Model.PrescriptionModelList;
import com.erxprescriptionuser.Model.PrescriptionModelObject;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.ActivityPrescriptionDetailBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityPrescriptionDetailBinding binding;
    private PrescriptionModelList.Data data;
    private String invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_prescription_detail);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        data=getIntent().getParcelableExtra("data");
        binding.tvInvoice.setOnClickListener(this);
        prescriptionDetailApi();
    }

    private void prescriptionDetailApi() {
        try {
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<PrescriptionModelObject> call = anInterface.prescriptionDetail(data.getId());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<PrescriptionModelObject>() {
                @Override
                public void onResponse(Call<PrescriptionModelObject> call, Response<PrescriptionModelObject> response) {
                    if(response.isSuccessful())
                    {
                        PrescriptionModelObject serverResponse = response.body();
                        if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue())) {
                            setUpData(serverResponse.getData());
                        } else if (serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue())) {
                            CommonUtils.showSnackBar(PrescriptionDetailActivity.this, serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PrescriptionModelObject> call, Throwable t) {
                    Log.e("failure",t.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(PrescriptionDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpData(PrescriptionModelObject.Data data) {
        binding.tvName.setText("Speedex");
        String deliveryDate[]=CommonUtils.getDate(data.getUpdatedAt()).split(" ");
        binding.tvDate.setText(deliveryDate[0]+" "+deliveryDate[1]+deliveryDate[2]);
        binding.tvTime.setText(deliveryDate[3]+" "+deliveryDate[4]);
        binding.tvPaymentMethod.setText(data.getPaymentType());
        binding.tvProviderDetails.setText("Name: "+data.getProviderId().getFullName()+"\n\n"+"Mobile Number: " +data.getProviderId().getCountryCode()+" "+data.getProviderId().getMobileNumber());
        binding.ratingBar.setRating(Float.parseFloat(data.getProviderId().getAvgRating()));
        invoice=data.getInvoice();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,getString(R.string.prescription_detail));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvInvoice:
                new DownloadTask(this,invoice);
            break;
        }
    }

    public class DownloadTask {
        private static final String TAG = "Download Task";
        private Context context;

        private String downloadUrl = "", downloadFileName = "";
        private ProgressDialog progressDialog;

        public DownloadTask(Context context, String downloadUrl) {
            this.context = context;
            this.downloadUrl = downloadUrl;
            downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL
            Log.e(TAG, downloadFileName);
            new DownloadingTask(context).execute();
        }

        private class DownloadingTask extends AsyncTask<Void, Void, Void> {
            private Context context;
            File apkStorage = null;
            File outputFile = null;
            public DownloadingTask(Context context)
            {
                this.context=context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void result) {
                try {
                    if (outputFile != null) {
                        progressDialog.dismiss();
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle(R.string.invoice);
                        alertDialogBuilder.setMessage(R.string.downloaded_successfully);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                        alertDialogBuilder.setNegativeButton(R.string.open_pdf,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/ErxPrescription/" + downloadFileName);  // -> filename = maven.pdf
//                                Uri path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);
                  //              Uri path=FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()), "com.erxprescriptionuser" + ".provider", new File(pdfFile.getAbsolutePath()));
                                Uri path=Uri.fromFile(pdfFile);
                                Log.e("path","--->"+ path);
                                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                pdfIntent.setDataAndType(path, "application/pdf");
                                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                try{
                                    context.startActivity(pdfIntent);
                                }catch(ActivityNotFoundException e){
                                    Toast.makeText(context, R.string.no_application_available, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alertDialogBuilder.show();
                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 3000);

                        Log.e(TAG, "Download Failed");

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    //Change button text if exception occurs

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);
                    Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

                }


                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    URL url = new URL(downloadUrl);//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                                + " " + c.getResponseMessage());

                    }


                    //Get File if SD card is present
                    if (new CheckForSDCard().isSDCardPresent()) {

                        apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "ErxPrescription");
                    } else
                        Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                        Log.e(TAG, "Directory Created.");
                    }
                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    //Create New File if not present
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        Log.e(TAG, "File Created");
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location
                    InputStream is = c.getInputStream();//Get InputStream for connection
                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();

                } catch (Exception e) {

                    //Read exception if something went wrong
                    e.printStackTrace();
                    outputFile = null;
                    Log.e(TAG, "Download Error Exception " + e.getMessage());
                }

                return null;
            }
        }
    }

    public class CheckForSDCard {
        //Check If SD Card is present or not method
        public boolean isSDCardPresent() {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        }
    }

}
