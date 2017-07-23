package com.onemoreerror.sampletask.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onemoreerror.sampletask.Activity.AddNewContactActivity;
//import com.onemoreerror.sampletask.ContactsModel.ContactVO;
import com.onemoreerror.sampletask.ImageUtilities.ImageUtils;
import com.onemoreerror.sampletask.R;
import com.onemoreerror.sampletask.db.Contact;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder>{

    private List<Contact> contactList;
    private Context mContext;
    public AllContactsAdapter(List<Contact> contactList, Context mContext){
        this.contactList = contactList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_layout, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        ButterKnife.bind(this,view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
//        holder.ivContactImage.set
        holder.setData(contact);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{
        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        ImageView ivEditable ;
        Contact contactData ;

        @OnClick(R.id.contact_edit_iv)
        public void EditButtonPressed(){
            if(contactData != null) {
                Log.e("AK", "Edit Contact");
                proceedToUpdateContact(contactData);
                AllContactsAdapter.this.notifyDataSetChanged();
            } else {
                Log.e("AK","Contact data is null");
            }

        }

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.contact_image_iv);
            tvContactName = (TextView) itemView.findViewById(R.id.contact_name_tv);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.phone_number_tv);
            ivEditable =  (ImageView)itemView.findViewById(R.id.contact_edit_iv);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Contact data){
            this.contactData = data ;
            tvContactName.setText(data.getName());
            tvPhoneNumber.setText(data.getNumber());
            //Setting the bitmap.
            Bitmap bitmap = null ;
            Log.e("AK",""+data.getImage());
            Uri capturedImageUri = Uri.parse(data.getImage());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(ivContactImage.getContext().getContentResolver(),capturedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap compressedBitmap = ImageUtils.getInstant().getCompressedBitmap(bitmap);
            ivContactImage.setImageBitmap(compressedBitmap);
            ivEditable.setVisibility(View.VISIBLE);
        }
        private void proceedToUpdateContact(Contact contact){
            Intent intent = new Intent(ivEditable.getContext(),AddNewContactActivity.class);
            intent.putExtra("create",false);
            intent.putExtra("contact",contact);
            ivEditable.getContext().startActivity(intent);
        }
    }
}