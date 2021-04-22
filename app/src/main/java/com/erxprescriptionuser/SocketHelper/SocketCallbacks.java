package com.erxprescriptionuser.SocketHelper;

public  interface SocketCallbacks {
    //Common
   void onConnect(Object... args) ;
   void onDisconnect(Object... args);
   void onConnectError(Object... args);

   //Chat Specifice
   void onRoomJoin(Object... args);
   void onMessage(Object... args);
}
