package com.example.assignment_phone

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.telecom.ConnectionRequest
import android.telecom.TelecomManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_PERMISSION_CODE = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val registrar = MyPhoneAccountRegistrar(this) // 'this' is the context (Activity)
        registrar.registerPhoneAccount()

        val phoneNumberEditText: EditText = findViewById(R.id.phone_number_edit_text)
        val makeCallButton: Button = findViewById(R.id.make_call_button)

        makeCallButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                initiateOutgoingCall(phoneNumber)
            } else {
                Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
            }
        }

        val setDefaultButton: Button = findViewById(R.id.set_default_button)

        setDefaultButton.setOnClickListener {
            // Get TelecomManager instance
            val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager

            // Check if your app is the default dialer
            val currentDialer = telecomManager.defaultDialerPackage
            val thisApp = packageName

            // If it's not the default dialer, prompt the user to set it
            if (currentDialer != thisApp) {
                // Open settings to allow the user to change the default dialer
                val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                startActivity(intent)
            } else {
                // If it's already the default dialer, show a confirmation
                showToast("Your app is already the default dialer.")
            }
        }


        findViewById<MaterialButton>(R.id.btn_contacts).setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btn_call_logs).setOnClickListener {
            startActivity(Intent(this, CallLogActivity::class.java))
        }

    }
    private fun checkAndRequestPermissions(): Boolean {
        val permissions = arrayOf(
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.MANAGE_OWN_CALLS
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        return if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missingPermissions.toTypedArray(), REQUEST_PERMISSION_CODE)
            false
        } else {
            true
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions are required to make a call.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun initiateOutgoingCall(phoneNumber: String) {
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager

        try {
            val uri = Uri.fromParts("tel", phoneNumber, null)
            val callIntent = Intent(Intent.ACTION_CALL, uri)
            startActivity(callIntent)
        } catch (e: SecurityException) {
            Toast.makeText(this, "Permission denied. Cannot initiate call.", Toast.LENGTH_SHORT).show()
        }
    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

