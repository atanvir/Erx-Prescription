package com.erxprescriptionuser.Utils;

import android.animation.Animator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.erxprescriptionuser.Activity.MainActivity;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.CorneredSort;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class CommonUtils extends AppCompatActivity {
    public static CustomLoader customLoader;

    public static boolean networkConnectionCheck(final Context context) {
        boolean isConnected = isOnline(context);
        return isConnected;
    }
    public static void setToolbar(final AppCompatActivity activity,  String  TitleContent){
       Toolbar toolbar= activity.findViewById(R.id.toolbar);
       TextView title=activity.findViewById(R.id.tvTitle);
       LinearLayout llBack=activity.findViewById(R.id.llBack);
        activity.setSupportActionBar(toolbar);
        title.setText(TitleContent);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }





    public static boolean isOnline(Context context) {
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobile_info != null) {
                if (mobile_info.isConnectedOrConnecting() || wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("" + e);
            return false;
        }
    }



    public static void showSnackBar(Context context,String msg)
    {
        Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setMinimumHeight(10);
        snackBarView.setBackgroundColor(Color.parseColor("#123456"));
        TextView tv = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        tv.setTextSize(13);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }


    public static void showLoadingDialog(Activity activity){
        if(customLoader ==null)
            customLoader = CustomLoader.show(activity, true);

        try {
            customLoader.setCancelable(false);
            customLoader.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void dismissLoadingDialog(){
        try
        {
            if (null != customLoader && customLoader.isShowing()) {
                customLoader.dismiss();
                customLoader =null;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void setSpinner(Context context,List<String> data, Spinner spinner) {
        ArrayAdapter genderArrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, data) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                // Set the text color of spinner item
                tv.setTextColor(Color.TRANSPARENT);

                // Return the view
                return tv;
            }
        };

        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(genderArrayAdapter);
        spinner.performClick();
    }

    public static Uri getImageFileUri(Context context) {
        String capture_dir= Environment.getExternalStorageDirectory() + "/CashBasket/Images/";
        File file = new File(capture_dir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        String path = capture_dir + System.currentTimeMillis() + ".jpg";
        return FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()), "com.erxprescriptionuser" + ".provider", new File(path));
    }

    public static void showToast(Context context, String messgae) {
        Toast toast = Toast.makeText(context, messgae, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String getDate(String dt) {
        String time = "";
        String getDate = dt;
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_format);
            String your_format = new SimpleDateFormat("dd MMMM, yyyy HH:mm:ss aa").format(date);
            time=your_format;
        } catch (Exception e) {
            e.printStackTrace();
        }

            return time;
        }

    public static void hideKeyBoard(Context context, View view) {
        try {
            InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isGPlayServicesOK(Activity activity) {
        int isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (isAvailable == ConnectionResult.SUCCESS) return true;
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)) GoogleApiAvailability.getInstance().getErrorDialog(activity, isAvailable, 1001).show();
        else Toast.makeText(activity, activity.getApplicationContext().getString(R.string.cannot_connect_to_playstore), Toast.LENGTH_SHORT).show();
        return false;
    }



    public  void getFBKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.erxprescriptionuser", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }




    public static boolean getDeviceToken(final Context context)
    {
        final boolean[] ret = {true};
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    ret[0]=true;
                    String auth_token = task.getResult().getToken();
                    Log.e("TOKEN",auth_token);
                    SharedPreferenceWriter.getInstance(context).writeStringValue(SPreferenceKey.TOKEN,auth_token);

                }else
                {
                    Log.e("failed","token");
                    ret[0] =false;
                    getDeviceToken(context);
                }

            }
        });

        return ret[0];

    }


    public static void showAnimation(ConstraintLayout view)
    {
        Animator spruceAnimator = new Spruce
                .SpruceBuilder(view)
                .sortWith(new CorneredSort(/*interObjectDelay=*/10L, /*reversed=*/false, CorneredSort.Corner.TOP_LEFT))
                .animateWith(new Animator[] {DefaultAnimations.shrinkAnimator(view, /*duration=*/300)})
                .start();
    }



    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                new SimpleDateFormat("ddmmyyhhmmss").format(Calendar.getInstance().getTime()), null);
        return Uri.parse(path);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 8;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF,roundPx,roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public   Intent getPickIntent(Context context,Uri cameraOutputUri) {
        final List<Intent> intents = new ArrayList<Intent>();

        if (true) {
            intents.add(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        }

        if (true) {
            setCameraIntents(context,intents, cameraOutputUri);
        }

        if (intents.isEmpty()) return null;
        Intent result = Intent.createChooser(intents.remove(0), null);
        if (!intents.isEmpty()) {
            result.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[] {}));
        }
        return result;


    }

    public  void setCameraIntents(Context context,List<Intent> cameraIntents, Uri output) {
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
            intent.putExtra("uri",output);
            cameraIntents.add(intent);
        }
    }


    public static String getTimeAgo(String dt) {
        String time = "";
        String getDate = dt;
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_format);
            String your_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
            String[] splitted = your_format.split(" ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date endDate = dateFormat.parse(your_format);
            Date startDate = Calendar.getInstance().getTime();
            long differenceDate = startDate.getTime() - endDate.getTime();
            String[] completeDate = splitted[0].split("-");
            String date1 = completeDate[0];
            String month = completeDate[1];
            String year = completeDate[2];
            int days_in_months = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date1)).getActualMaximum(Calendar.DAY_OF_MONTH);


            long secounds = 1000;    // 1 secound
            long min = 60 * secounds;  // 1 min
            long hour = 3600000;      // 1 hour
            long day = 86400000;      // 1 days

            differenceDate=differenceDate-75000;

            long monthdifference = differenceDate / (days_in_months * day);
            long daysDifference = differenceDate / day;
            long hourdifference = differenceDate / hour;
            long mindifference = differenceDate / min;
            long secoundsDiffer = differenceDate / secounds;

            if (monthdifference > 0) time =  monthdifference==1?""+monthdifference+" month ago":monthdifference+ " months ago";
            else if (daysDifference > 0) time = daysDifference==1?""+daysDifference+" day ago":daysDifference+ " days ago";
            else if (hourdifference > 0) time = hourdifference==1?""+hourdifference+" hour ago":hourdifference+ " hours ago";
            else if (mindifference > 0) time = mindifference==1?""+mindifference+" min ago":mindifference+ " mins ago";
            else if (secoundsDiffer > 0) time = secoundsDiffer + " secs ago";
            else time="now";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }
    public static long getTime(String dt) {
        long time = 0;
        String getDate = dt;
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_format);
            String your_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
            String[] splitted = your_format.split(" ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            Date endDate = dateFormat.parse(your_format);
            Date startDate = Calendar.getInstance().getTime();
            long differenceDate = startDate.getTime() - endDate.getTime();
            long secounds = 1000;    // 1 secound
            long secoundsDiffer = differenceDate / secounds;
            if (secoundsDiffer > 0) time = secoundsDiffer;


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static void startActivity(Context context,Class<? extends Object> className) {
        Intent intent = new Intent(context, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void setLocale(Activity context) {
        String langCode=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.LANGUAGE_CODE);
        Locale locale = new Locale(langCode);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        context.getBaseContext().getResources().updateConfiguration(config, context.getBaseContext().getResources().getDisplayMetrics());
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




}
