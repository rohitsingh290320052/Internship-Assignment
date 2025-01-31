package com.example.assignment_phone

import android.net.Uri
import android.os.Bundle
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log

class CallService : ConnectionService() {

    override fun onCreateIncomingConnection(
        phoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection? {
        Log.d("CallService", "Incoming call received")

        // Create a new instance of your custom Connection class
        val connection = MyConnection()
        connection.setAddress(request?.address, TelecomManager.PRESENTATION_ALLOWED)
        connection.setCallerDisplayName("Incoming Caller", TelecomManager.PRESENTATION_ALLOWED)
        connection.setExtras(Bundle())
        connection.setRinging()

        return connection
    }

    override fun onCreateOutgoingConnection(
        phoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection? {
        Log.d("CallService", "Outgoing call initiated")

        // Create a new instance of your custom Connection class
        val connection = MyConnection()
        connection.setAddress(request?.address, TelecomManager.PRESENTATION_ALLOWED)
        connection.setExtras(Bundle())
        connection.setDialing()


        return connection
    }

    override fun onCreateIncomingConnectionFailed(
        phoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        Log.e("CallService", "Failed to create incoming connection")
    }

    override fun onCreateOutgoingConnectionFailed(
        phoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        Log.e("CallService", "Failed to create outgoing connection")
    }
}

class MyConnection : Connection() {

    override fun onAnswer() {
        Log.d("MyConnection", "Call answered")
        setActive() // Changes the state of the call to active
    }

    override fun onReject() {
        Log.d("MyConnection", "Call rejected")
        setDisconnected(DisconnectCause(DisconnectCause.REJECTED))
        destroy() // Cleans up the call object
    }

    override fun onDisconnect() {
        Log.d("MyConnection", "Call disconnected")
        setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
        destroy()
    }
}
