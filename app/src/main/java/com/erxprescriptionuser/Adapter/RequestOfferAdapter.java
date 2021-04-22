package com.erxprescriptionuser.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.erxprescriptionuser.Fragment.AcceptOfferFragment;
import com.erxprescriptionuser.Fragment.RejectOfferFragment;
import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ImageGlider;
import com.erxprescriptionuser.Utils.SwitchFragment;
import com.erxprescriptionuser.databinding.AdapterRequestOfferBinding;

import java.util.List;

import hari.bounceview.BounceView;

public class RequestOfferAdapter extends RecyclerView.Adapter<RequestOfferAdapter.MyViewHolder> {

    private Context context;
    private List<ResponseBean> list;

    public RequestOfferAdapter(Context context,List<ResponseBean> list)
    {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      AdapterRequestOfferBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_request_offer,parent, false);
      return new RequestOfferAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.replaceFrame2.setId(View.generateViewId());
        ImageGlider.setNormalImage(context,holder.binding.ciUser, holder.binding.progressBar, list.get(position).getProviderData().getProfilePic());
        holder.binding.tvAmount.setText(list.get(position).getAmount());
        holder.binding.tvCurrency.setText(context.getString(R.string.aed));
        holder.binding.tvRating.setText(""+list.get(position).getProviderData().getAvgRating());
        holder.binding.tvName.setText(list.get(position).getProviderData().getFullName());
        holder.binding.tvMessage.setText(list.get(position).getMessage());
//        if(list.get(position).getMessage().equalsIgnoreCase("")) {
//            holder.binding.tvMessage.setVisibility(View.GONE);
//        }


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       public AdapterRequestOfferBinding binding;
        public MyViewHolder(@NonNull AdapterRequestOfferBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            CommonUtils.showAnimation(binding.constraintLayout);
            BounceView.addAnimTo(binding.btnAccept);
            BounceView.addAnimTo(binding.btnReject);
            binding.btnAccept.setOnClickListener(this);
            binding.btnReject.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("prescriptionOfferId", list.get(getAdapterPosition()).getId());
            bundle.putString("currency",list.get(getAdapterPosition()).getCurrency());
            bundle.putString("amount",list.get(getAdapterPosition()).getAmount());
            bundle.putInt("FragmentId",binding.replaceFrame2.getId());

            switch (v.getId())
            {

                case R.id.btnAccept:
                Fragment acceptOfferFragment=new AcceptOfferFragment();
                acceptOfferFragment.setArguments(bundle);
                loadFrament(acceptOfferFragment);
                break;

                case R.id.btnReject:
                Fragment rejectOfferFragment=new RejectOfferFragment();
                rejectOfferFragment.setArguments(bundle);
                loadFrament(rejectOfferFragment);
                break;
            }
        }

        public void loadFrament(Fragment fragment)
        {
            SwitchFragment.changeFragment(((AppCompatActivity)context).getSupportFragmentManager(),fragment,binding.replaceFrame2.getId(),false,true);
        }
    }
}
