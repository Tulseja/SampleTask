package com.onemoreerror.sampletask.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onemoreerror.sampletask.Constants.AppConstants;
//import com.onemoreerror.sampletask.ContactsModel.ContactVO;
import com.onemoreerror.sampletask.ImageUtilities.ImageUtils;
import com.onemoreerror.sampletask.PermissionUtils.PermissionChecker;
import com.onemoreerror.sampletask.R;
import com.onemoreerror.sampletask.application.AppApplication;
import com.onemoreerror.sampletask.db.Contact;
import com.onemoreerror.sampletask.db.ContactDao;
import com.onemoreerror.sampletask.db.DaoSession;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.data;

public class AddNewContactActivity extends AppCompatActivity {


    Contact contact;
    DaoSession daoSession;
    boolean createNew = false;


    @BindView(R.id.add_new_contact_new_iv)
    ImageView addImage ;

//    ContactVO objToSend ;

    Uri capturedImageUri = null;

    @BindView(R.id.add_new_contact_name_tv)
    MaterialEditText contactName ;

    @BindView(R.id.add_new_contact_number_tv)
    MaterialEditText contactNumber ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);
        ButterKnife.bind(this);
        daoSession = ((AppApplication) getApplication()).getDaoSession();

        handleIntent(getIntent());
    }
    private void handleIntent(Intent intent) {
        createNew = intent.getBooleanExtra("create",false);

        //// This means we are editing a grocery item
        if(!createNew){
            contact = (Contact) intent.getSerializableExtra("contact");
            contactName.setText(contact.getName());
            contactNumber.setText(String.valueOf(contact.getNumber()));
            Bitmap bitmap = null ;
//            Log.e("AK",""+data.getContactImage());
            Uri capturedImageUri = Uri.parse(contact.getImage());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(addImage.getContext().getContentResolver(),capturedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap compressedBitmap = ImageUtils.getInstant().getCompressedBitmap(bitmap);
            addImage.setImageBitmap(compressedBitmap);
//            ivEditable.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.add_new_save_btn)
    public void SaveButtonPressed(){
//        if(capturedImageUri == null){
//            Toast.makeText(this,"Please Set an Image.",Toast.LENGTH_LONG).show();
//        }
//        if(contactNumber.getText() == null){
//            Toast.makeText(this,"Name Field Shouldn't be Empty.",Toast.LENGTH_LONG).show();
//            return ;
//        }
//        if(contactName.getText() == null){
//            Toast.makeText(this,"Number Field shouldn't be Empty.",Toast.LENGTH_LONG).show();
//            return ;
//        }
//
//        objToSend = new ContactVO();
//        if(capturedImageUri != null) {
//            Log.e("AK",""+capturedImageUri.toString());
//            objToSend.setContactImage(capturedImageUri.toString());
//        }
//        if(contactName.getText() != null)
//            objToSend.setContactName(contactName.getText().toString());
//        if(contactNumber.getText() != null)
//            objToSend.setContactNumber(contactNumber.getText().toString());
//
//        Intent returnIntent = new Intent();
//        Bundle b = new Bundle();
//        b.putParcelable(AppConstants.DATA_PASSING_TAGS.SEND_CONTACT_OBJ , objToSend);
//        returnIntent.putExtras(b);
//        setResult(Activity.RESULT_OK,returnIntent);
        if(createNew){
            insertContact();
        } else{
            if(contact != null) {
                updateContact(contact.getId());
            }
        }
        finish();
    }

    @OnClick(R.id.add_new_contact_new_iv)
    public void addImagePressed(){
        setUpDialogForCamera();

    }
    public void setUpDialogForCamera() {
        final CharSequence[] items = {"Take Photo", "Choose Photo From Library", "Cancel"};
        //check Read Write Permission
        final boolean readWritePermission = PermissionChecker.checkReadWriteStoragePermission(AddNewContactActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewContactActivity.this);
        builder.setTitle("Take an Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = PermissionChecker.checkCameraPermission(AddNewContactActivity.this);
                if (items[item].equals("Take Photo")) {
                    if (result && readWritePermission)
                        cameraIntent();
                } else if (items[item].equals("Choose Photo From Library")) {
                    if (result && readWritePermission)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, AppConstants.REQUEST_TAGS.SELECT_IMAGE_FROM_GALLERY);
    }

    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        capturedImageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(takePictureIntent, AppConstants.REQUEST_TAGS.REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppConstants.REQUEST_TAGS.REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this,"Please Provide Permissions To Access Device Camera",Toast.LENGTH_LONG).show();
                }
                break;
            case AppConstants.REQUEST_TAGS.REQUEST_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this,"Please Provide Permissions To Store Captured Images.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_TAGS.REQUEST_CAMERA && resultCode == RESULT_OK) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Uri imageFilePathUri = null;
            if (capturedImageUri != null) {
                Cursor imageCursor = getContentResolver().query(capturedImageUri, filePathColumn, null, null, null);
                if (imageCursor != null && imageCursor.moveToFirst()) {
                    int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);
                    String filePath = imageCursor.getString(columnIndex);
                    imageCursor.close();
                    imageFilePathUri = filePath != null ? Uri.parse(filePath) : null;
                }
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),capturedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap newBitmap = ImageUtils.getInstant().getCompressedBitmap(bitmap);
                addImage.setImageBitmap(newBitmap);
            }
        }
        if (requestCode == AppConstants.REQUEST_TAGS.SELECT_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            // Uri is already there for this file. no need to create a new one.
            capturedImageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),capturedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap newBitmap = ImageUtils.getInstant().getCompressedBitmap(bitmap);
            addImage.setImageBitmap(newBitmap);
        }
    }

    private void insertContact() {
        ContactDao contactDao = daoSession.getContactDao();
        Contact contact = new Contact();
        contact.setName(contactName.getText().toString());
        contact.setNumber(contactNumber.getText().toString());
        if(capturedImageUri != null){
            contact.setImage(capturedImageUri.toString());
        }
        else {
            contact.setImage("null");
        }
        contactDao.insert(contact);
        Toast.makeText(this, "Contact inserted", Toast.LENGTH_SHORT).show();
    }
    private void updateContact(long id){
        ContactDao contactDao = daoSession.getContactDao();
        Contact contact = new Contact();
        contact.setId(id);
        contact.setName(contactName.getText().toString());
        contact.setNumber(contactNumber.getText().toString());
        contactDao.saveInTx(contact);
        Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}
