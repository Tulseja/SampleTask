package com.onemoreerror.sampletask.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.onemoreerror.sampletask.Adapter.AllContactsAdapter;
import com.onemoreerror.sampletask.Constants.AppConstants;
//import com.onemoreerror.sampletask.ContactsModel.ContactVO;
import com.onemoreerror.sampletask.ImageUtilities.ImageUtils;
import com.onemoreerror.sampletask.PermissionUtils.PermissionChecker;
import com.onemoreerror.sampletask.PreferenceUtil.ZPreferences;
import com.onemoreerror.sampletask.R;
import com.onemoreerror.sampletask.application.AppApplication;
import com.onemoreerror.sampletask.db.Contact;
import com.onemoreerror.sampletask.db.ContactDao;
import com.onemoreerror.sampletask.db.DaoSession;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_all_contacts_rv)
    RecyclerView rvContacts;

    //    List<ContactVO> contactVOList ;
    List<Contact> contactList = new ArrayList<>();
    ProgressDialog pd;
    DaoSession daoSession;
    ContactDao contactDao = null;
    AllContactsAdapter allContactsAdapter ;
    boolean manageDocPermission ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        daoSession = ((AppApplication) getApplication()).getDaoSession();
        contactDao = daoSession.getContactDao();

        getIsPermitted();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        manageDocPermission = PermissionChecker.checkManageDocsPermission(this);
//        if(manageDocPermission) {
            fetchContactList();
//        }
    }

    public void getIsPermitted(){
        PermissionChecker.askForContactPermission(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getAllContacts() {
//        contactVOList = new ArrayList();
        Contact contact;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    contact = new Contact();
                    contact.setName(name);
                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        contact.setContactNumber(phoneNumber);
                        contact.setNumber(phoneNumber);
//                        Bitmap bt = ImageUtils.getInstant().getBitmapFromImageView(contactImage);
                        BitmapDrawable drawable = (BitmapDrawable) getDrawable(R.drawable.icon_user_default);
                        Bitmap bitmap = drawable.getBitmap();
                        if(!PermissionChecker.checkStoragePermission(this)) {
                            PermissionChecker.requestStoragePermission(this);
                        }
                        if(bitmap != null) {
                            Uri sampleImageUri = ImageUtils.getInstant().getImageUri(this, bitmap);
                            contact.setImage(sampleImageUri.toString().trim());
                        }
                        else {
                            contact.setImage(null);
                        }
                    }
                    phoneCursor.close();
                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }

                    contactList.add(contact);
                    contactDao.insert(contact);
                }
            }
        }
    }

    @OnClick(R.id.main_add_fab)
    public void fabPressed(){
        Log.e("Ak","Fab Pressed11") ;
        //open add new contact Activity
        addNewContact();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == AppConstants.REQUEST_TAGS.ADD_NEW_CONTACT_TAG ){
//            if(resultCode == Activity.RESULT_OK){
//                //In Case of no Contacts .
//                AllContactsAdapter contactAdapter = new AllContactsAdapter(contactVOList, getApplicationContext());
//                rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                rvContacts.setAdapter(contactAdapter);
//                ContactVO obj = data.getParcelableExtra(AppConstants.DATA_PASSING_TAGS.SEND_CONTACT_OBJ);
//                if(obj != null){
//                    Log.i("AK","Object Delievered !");
////                    Log.e("AK",obj.getContactName());
////                    Log.e("AK",obj.getContactNumber());
//                    contactVOList.add(0,obj);
//                    if(rvContacts != null) {
//                        rvContacts.getAdapter().notifyDataSetChanged();
//                    }
//                }
//                else {
//                    Log.e("AK", "Result Object is null");
//                }
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//                Toast.makeText(this,"Didn't Pass the Object between Activities. ",Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private void fetchContactList(){
        contactList.clear();
        //// Get the entity dao we need to work with.
        ContactDao contactDao = daoSession.getContactDao();
        //// Load all items.
        contactList.addAll(contactDao.loadAll());
        /// Notify our adapter of changes
        if(allContactsAdapter != null ) {
            allContactsAdapter.notifyDataSetChanged();
        }
        else {
            allContactsAdapter= new AllContactsAdapter(contactList, getApplicationContext());
            rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            rvContacts.setAdapter(allContactsAdapter);
            allContactsAdapter.notifyDataSetChanged();
        }
    }
    private void deleteContactItem(long id){
        //// Get the entity dao we need to work with.
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.deleteByKey(id);
        fetchContactList();
    }

    public void addNewContact() {
        // Go to add Contact activity
        Log.e("Ak","Add new intent Called") ;
        Intent intent = new Intent(this,AddNewContactActivity.class);
        intent.putExtra("create",true);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(MainActivity.this,
                        "Loading..", "Please Wait", true, false);
            }// End of onPreExecute method
            @Override
            protected Void doInBackground(Void... params) {
                if(ZPreferences.isFirstLaunch(AppApplication.getInstance().getApplicationContext())) {
                    final boolean readWritePermission = PermissionChecker.checkReadWriteStoragePermission(MainActivity.this);
                    getAllContacts();
                    ZPreferences.setIsFirstLaunch(AppApplication.getInstance().getApplicationContext(), false);
                }
                return null;
            }// End of doInBackground method
            @Override
            protected void onPostExecute(Void result) {
                pd.dismiss();
                allContactsAdapter= new AllContactsAdapter(contactList, getApplicationContext());
                rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rvContacts.setAdapter(allContactsAdapter);
                allContactsAdapter.notifyDataSetChanged();
            }//End of onPostExecute method
        }.execute((Void[]) null);
    }
}
