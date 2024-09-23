package com.eles.myphonebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private List<Contact> contacts;
    private ContactManager contactManager;

    public ContactAdapter(Context context, List<Contact> contacts, ContactManager contactManager) {
        super(context, 0, contacts);
        this.context = context;
        this.contacts = contacts;
        this.contactManager = contactManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.tvPhone = convertView.findViewById(R.id.tv_phone);
            viewHolder.tvEmail = convertView.findViewById(R.id.tv_email);
            viewHolder.tvAddress = convertView.findViewById(R.id.tv_address);
            viewHolder.ivPhoto = convertView.findViewById(R.id.iv_photo);
            viewHolder.btnDelete = convertView.findViewById(R.id.btn_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact contact = contacts.get(position);

        viewHolder.tvName.setText(contact.getName());
        viewHolder.tvPhone.setText(contact.getPhone());
        viewHolder.tvEmail.setText(contact.getEmail());
        viewHolder.tvAddress.setText(contact.getAddress());
        viewHolder.ivPhoto.setImageBitmap(contact.getPhotoBitmap());

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactManager.deleteContact(contact);
                contacts.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvPhone;
        TextView tvEmail;
        TextView tvAddress;
        ImageView ivPhoto;
        Button btnDelete;
    }
}