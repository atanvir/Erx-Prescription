package com.erxprescriptionuser.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.erxprescriptionuser.R;


public class ImageGlider
{


    public static void setRoundImage(Context context, ImageView imageView, final ProgressBar progressBar, String url)
    {
        if(progressBar!=null)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if(progressBar!=null) {
                    progressBar.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if(progressBar!=null) {
                    progressBar.setVisibility(View.GONE);
                }
                return false;
            }
        }).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView);



    }



    public  static void setNormalImage(Context context, ImageView userIv, final ProgressBar progressBar, String url)
    {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).placeholder(R.drawable.user_thumbnail).into(userIv);
    }

}
