package com.erxprescriptionuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.databinding.AdapterLeftChatBinding;
import com.erxprescriptionuser.databinding.AdapterNotificationBinding;
import com.erxprescriptionuser.databinding.AdpterRightChatBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyHolder> {
    private Context context;
    private List<ResponseBean> list;
    final int RECEIVER=2;
    final int SENDER=1;

    public ChatAdapter(Context context,List<ResponseBean> list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public int getItemViewType(int position) {
     if(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.ID).equalsIgnoreCase(list.get(position).getSenderId()))
     {
         return SENDER;
     }else
     {
         return RECEIVER;
     }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==RECEIVER){
        AdapterLeftChatBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.adapter_left_chat,parent,false);
        return new ChatAdapter.MyHolder(binding);
        }else
        {
        AdpterRightChatBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.adpter_right_chat,parent,false);
        return new ChatAdapter.MyHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        switch (holder.getItemViewType())
        {
            case RECEIVER:
            holder.receiverBinding.tvReceiverMsg.setText(list.get(position).getMessage());
            holder.receiverBinding.tvReceiverTime.setText(CommonUtils.getTimeAgo(list.get(position).getCreatedAt()));
            break;

            case SENDER:
            holder.senderBinding.tvSenderMsg.setText(list.get(position).getMessage());
            holder.senderBinding.tvSenderTime.setText(CommonUtils.getTimeAgo(list.get(position).getCreatedAt()));
            break;
        }
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        AdapterLeftChatBinding receiverBinding;

        public MyHolder(@NonNull AdapterLeftChatBinding itemView) {
            super(itemView.getRoot());
            receiverBinding=itemView;
            CommonUtils.showAnimation(receiverBinding.mainCl);
        }


        AdpterRightChatBinding senderBinding;
        public MyHolder(@NonNull AdpterRightChatBinding itemView) {
            super(itemView.getRoot());
            senderBinding=itemView;
            CommonUtils.showAnimation(senderBinding.mainCl);

        }
    }
}
