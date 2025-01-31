package com.example.assignment_phone

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class CallLogActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_log)

        val callLogsTextView: TextView = findViewById(R.id.call_logs_text)

        // Check if permission is granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            fetchCallLogs(callLogsTextView)
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG), PERMISSION_REQUEST_CODE)
        }
    }

    private fun fetchCallLogs(callLogsTextView: TextView) {
        val cursor: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val callLogString = StringBuilder()
        cursor?.use {
            while (it.moveToNext()) {
                // Safely get the index for the columns
                val numberColumnIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
                val typeColumnIndex = it.getColumnIndex(CallLog.Calls.TYPE)

                // Ensure columns exist
                if (numberColumnIndex != -1) {
                    val number = it.getString(numberColumnIndex)
                    callLogString.append("Number: $number, ")
                } else {
                    callLogString.append("Number: Unknown, ")
                }

                if (typeColumnIndex != -1) {
                    val type = it.getInt(typeColumnIndex)
                    callLogString.append("Type: $type\n")
                } else {
                    callLogString.append("Type: Unknown\n")
                }
            }
        }

        callLogsTextView.text = callLogString.toString()
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch the call logs
                val callLogsTextView: TextView = findViewById(R.id.call_logs_text)
                fetchCallLogs(callLogsTextView)
            } else {
                // Permission denied, show a toast message
                Toast.makeText(this, "Permission denied to read call logs", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
