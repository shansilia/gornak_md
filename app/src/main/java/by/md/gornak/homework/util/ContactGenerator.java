package by.md.gornak.homework.util;


import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import by.md.gornak.homework.model.ApplicationDB;

import static by.md.gornak.homework.model.ApplicationDB.TYPE.PHONE;

public class ContactGenerator {

    private static Activity mActivity;
    private static Uri mUriContact;
    private static String mContactID;

    public static ApplicationDB createContact(Activity activity, Uri uriContact) {
        mActivity = activity;
        mUriContact = uriContact;
        String name = retrieveContactName();
        String number = retrieveContactNumber();
        byte[] image = retrieveContactPhoto();

        return new ApplicationDB(PHONE.toString(), name, -1, number, image);
    }

    private static byte[] retrieveContactPhoto() {
        byte[] image = new byte[]{};
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(mActivity.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(mContactID)));

            if (inputStream != null) {
                Bitmap photo = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                image = stream.toByteArray();
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private static String retrieveContactNumber() {

        String contactNumber = null;

        Cursor cursorID = mActivity.getContentResolver().query(mUriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            mContactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Cursor cursorPhone = mActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{mContactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();
        return contactNumber;
    }

    private static String retrieveContactName() {

        String contactName = null;

        Cursor cursor = mActivity.getContentResolver().query(mUriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();
        return contactName;

    }
}
