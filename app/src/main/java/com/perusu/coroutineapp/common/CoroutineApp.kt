package com.perusu.coroutineapp.common

import android.app.Application
import android.database.Cursor
import android.provider.ContactsContract

private var application: CoroutineApp? = null

class CoroutineApp : Application() {

    companion object {
        fun getApplicationContext(): CoroutineApp {
            return application!!
        }

        fun createCursor(): Cursor {
            val projection = arrayOf(
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Data.DISPLAY_NAME_PRIMARY,
                ContactsContract.Data.STARRED,
                ContactsContract.Data.PHOTO_URI,
                ContactsContract.Data.PHOTO_THUMBNAIL_URI,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.IN_VISIBLE_GROUP
            )

           return application?.contentResolver?.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                null,
                null,
                null
            )!!
        }


        fun createEmailCursor(contactId : String): Cursor {
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.MIMETYPE
            )
           return  application?.contentResolver?.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                ContactsContract.Data.CONTACT_ID + "=?",
                arrayOf("388"),
                null
            )!!
        }

    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }


}