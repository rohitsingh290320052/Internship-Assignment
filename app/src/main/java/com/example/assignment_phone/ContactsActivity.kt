package com.example.assignment_phone

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ContactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val contactsTextView: TextView = findViewById(R.id.contacts_text)

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )

        val contactsString = StringBuilder()

        cursor?.use {
            while (it.moveToNext()) {
                // Safely check if the column exists
                val nameColumnIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                if (nameColumnIndex != -1) {
                    val name = it.getString(nameColumnIndex)
                    contactsString.append("Name: $name\n")
                } else {
                    contactsString.append("Name: Unknown\n")
                }
            }
        }

        contactsTextView.text = contactsString.toString()
    }
}
