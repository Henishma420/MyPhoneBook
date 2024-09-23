// AddContactActivity.java
package com.eles.myphonebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddContactActivity extends Activity {
    private EditText etName;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etAddress;
    private EditText etId; // Add ID field
    private ImageView ivPhoto;
    private Button btnAdd;
    private Button btnChoosePhoto;
    private ContactDatabase contactDatabase;
    private Uri photoUri = null;

    private static final int REQUEST_IMAGE_SELECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etId = findViewById(R.id.et_id);
        ivPhoto = findViewById(R.id.iv_photo);
        btnAdd = findViewById(R.id.btn_add);
        btnChoosePhoto = findViewById(R.id.btn_choose_photo);
        contactDatabase = new ContactDatabase(this);

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_SELECT);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String address = etAddress.getText().toString();
                String id = etId.getText ().toString();

                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(AddContactActivity.this, "Please fill in the mandatory fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (id.isEmpty()) {
                    Toast.makeText(AddContactActivity.this, "Please enter an ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoUri == null) {
                    Toast.makeText(AddContactActivity.this, "Please select a photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    byte[] photoBytes = uriToByteArray(AddContactActivity.this, photoUri);
                    Contact contact = new Contact(id, name, phone, email, address, photoBytes);
                    contactDatabase.addContact(contact.getId(), contact.getName(), contact.getPhone(), contact.getEmail(), contact.getAddress(), photoBytes);

                    // Update the contact
                    ContactManager contactManager = new ContactManager(AddContactActivity.this);
                    contactManager.updateContact(contact, photoUri);

                    Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (IOException e) {
                    Toast.makeText(AddContactActivity.this, "Error loading photo", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            photoUri = data.getData();
            ivPhoto.setImageURI(photoUri);
        }
    }

    private byte[] uriToByteArray(Activity activity, Uri photoUri) throws IOException {
        if (photoUri == null) {
            return null;
        }
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), photoUri);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}