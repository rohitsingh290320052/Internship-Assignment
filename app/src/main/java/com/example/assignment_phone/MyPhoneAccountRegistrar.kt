package com.example.assignment_phone

import android.content.ComponentName
import android.content.Context
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telecom.ConnectionService
import android.net.Uri
import androidx.core.content.ContextCompat.getSystemService


class MyPhoneAccountRegistrar(private val context: Context) {
    private lateinit var telecomManager: TelecomManager

    fun registerPhoneAccount() {
        val phoneAccountHandle = PhoneAccountHandle(
            ComponentName(context, CallService::class.java), // Use context here
            "MyPhoneAccount"
        )

        val phoneAccount = PhoneAccount.builder(phoneAccountHandle, "MyPhoneAccount")
            .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER)
            .setAddress(Uri.fromParts("tel", "1234567890", null)) // Set a default phone number or any other details
            .build()

        // Use context.getSystemService to correctly get the TelecomManager instance
        telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        telecomManager.registerPhoneAccount(phoneAccount)
    }
}

