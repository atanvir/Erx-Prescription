package com.erxprescriptionuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.databinding.AdapterPaymentBinding;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
    private Context context;
    private List<ResponseBean> list;
    public PaymentAdapter(Context context, List<ResponseBean> list)
    {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     AdapterPaymentBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_payment,parent,false);
     return new PaymentAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvPrescriptionId.setText(context.getString(R.string.prescription_id)+" "+list.get(position).getPrescriptionData().getPrescriptionNumber());
        holder.binding.tvPaymentId.setText(list.get(position).getPaymentId()+"\n"+context.getString(R.string.payment_type)+" "+list.get(position).getPaymentType()+
                "\n"+""+context.getString(R.string.payment_status)+" "+list.get(position).getStatus()+"\n"+context.getString(R.string.payment_date)+" "+list.get(position).getDate());
        holder.binding.tvAmount.setText(context.getString(R.string.amount_paid)+" "+context.getString(R.string.aed)+" "+list.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterPaymentBinding binding;
        public MyViewHolder(@NonNull AdapterPaymentBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
            CommonUtils.showAnimation(binding.clMain);
        }
    }
}

