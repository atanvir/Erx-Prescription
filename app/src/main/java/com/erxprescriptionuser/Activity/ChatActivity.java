package com.erxprescriptionuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.erxprescriptionuser.Adapter.ChatAdapter;
import com.erxprescriptionuser.Model.CommonDataStringModel;
import com.erxprescriptionuser.Model.CommonListModel;
import com.erxprescriptionuser.Model.ResponseBean;
import com.erxprescriptionuser.R;
import com.erxprescriptionuser.Retrofit.ServicesConnection;
import com.erxprescriptionuser.Retrofit.ServicesInterface;
import com.erxprescriptionuser.SharePrefrences.SPreferenceKey;
import com.erxprescriptionuser.SharePrefrences.SharedPreferenceWriter;
import com.erxprescriptionuser.SocketHelper.SocketCallbacks;
import com.erxprescriptionuser.SocketHelper.SocketConnection;
import com.erxprescriptionuser.Utils.CommonUtils;
import com.erxprescriptionuser.Utils.ParamEnum;
import com.erxprescriptionuser.databinding.ActivityChatBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hari.bounceview.BounceView;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements SocketCallbacks, View.OnClickListener, View.OnLayoutChangeListener {
   private ActivityChatBinding binding;
   private Socket socket;
   private String roomId,receiverId,senderId;
   private List<ResponseBean> chatDataList;
   boolean isMessageSend=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_chat);
        binding.lottieAnimationView.setOnClickListener(this);
        binding.rvChat.addOnLayoutChangeListener(this);
        BounceView.addAnimTo(binding.lottieAnimationView);
        connectSocket();
        chatHistoryApi();

    }

    private void connectSocket() {
        roomId=getIntent().getStringExtra("roomId");
        receiverId=getIntent().getStringExtra("receiverId");
        senderId= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.ID);
        try {
            socket=SocketConnection.connect(this);
            if(!socket.connected())
            {
                socket.connect();
            }
            JSONObject object = new JSONObject();
            object.put("roomId", roomId);
            socket.emit(ParamEnum.SOCKET_EVENT_ROOM_JOIN.theValue(), object);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void chatHistoryApi() {
        try {
            Log.e("roomId", "-------->"+roomId);
            ServicesInterface anInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonListModel> call = anInterface.chatHistory(roomId);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<CommonListModel>() {
                @Override
                public void onResponse(Call<CommonListModel> call, Response<CommonListModel> response) {
                    if(response.isSuccessful())
                    {
                        CommonListModel serverResponse=response.body();
                        if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.SUCCESS.theValue()))
                        {
                            chatDataList=serverResponse.getData();
                            binding.rvChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                            binding.rvChat.setAdapter(new ChatAdapter(ChatActivity.this, chatDataList));
                            binding.rvChat.scrollToPosition(binding.rvChat.getAdapter().getItemCount()-1);

                        }else if(serverResponse.getStatus().equalsIgnoreCase(ParamEnum.FAILURE.theValue()))
                        {
                            CommonUtils.showSnackBar(ChatActivity.this,serverResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonListModel> call, Throwable t) {
                    Log.e("failure",t.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,getIntent().getStringExtra("title"));

    }

    @Override
    public void onConnect(Object... args) {
        Log.e("onConnect", "connected");
    }

    @Override
    public void onDisconnect(Object... args) {
        Log.e("onDisconnect", "disconnected");

    }

    @Override
    public void onConnectError(Object... args) {
        Log.e("onConnectError", "connection error");
    }

    @Override
    public void onRoomJoin(Object... args) {
     runOnThread(ParamEnum.SOCKET_EVENT_ROOM_JOIN.theValue(),args);
    }

    @Override
    public void onMessage(Object... args) {
     runOnThread(ParamEnum.SOCKET_EVENT_MESSAGE.theValue(),args);
    }


    public void runOnThread(final String eventName, final Object... responseData) {
        Log.e(""+eventName, responseData[0].toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(eventName.equalsIgnoreCase(ParamEnum.SOCKET_EVENT_ROOM_JOIN.theValue()))
                {

                }else if(eventName.equalsIgnoreCase(ParamEnum.SOCKET_EVENT_MESSAGE.theValue()))
                {
                   isMessageSend=true;
                   ResponseBean data= new Gson().fromJson(responseData[0].toString(),ResponseBean.class);
                   chatDataList.add(data);
                   binding.rvChat.getAdapter().notifyDataSetChanged();
                   binding.rvChat.scrollToPosition(binding.rvChat.getAdapter().getItemCount()-1);
                   if(data.getSenderId().equalsIgnoreCase(SharedPreferenceWriter.getInstance(ChatActivity.this).getString(SPreferenceKey.ID)))
                   {
                       binding.edMessage.setText("");
                   }


                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.lottieAnimationView:
                if(!socket.connected())
                {
                    socket.connect();
                }
            if(isMessageSend) {
                if (binding.edMessage.getText().toString().trim().length() > 0) {
                    isMessageSend = false;
                    binding.lottieAnimationView.playAnimation();
                    JSONObject object = new JSONObject();
                    try {
                        object.put("roomId", roomId);
                        object.put("senderId", senderId);
                        object.put("receiverId", receiverId);
                        object.put("message", binding.edMessage.getText().toString().trim());
                        object.put("messageType", "Text");
                        object.put("receiverToken", getIntent().getStringExtra("token"));
                        socket.emit(ParamEnum.SOCKET_EVENT_MESSAGE.theValue(), object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isMessageSend=true;
                    final View viewPos = findViewById(R.id.myCoordinatorLayout);
                    Snackbar.make(viewPos, R.string.enter_your_message_first , Snackbar.LENGTH_LONG).show();
                }
            }
            break;
        }

    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (bottom < oldBottom) {
            binding.rvChat.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(binding.rvChat.getAdapter()!=null) {
                        if (binding.rvChat.getAdapter().getItemCount() > 0) {
                            binding.rvChat.smoothScrollToPosition(binding.rvChat.getAdapter().getItemCount() - 1);
                        }
                    }
                }
            }, 1);
        }
    }
}
