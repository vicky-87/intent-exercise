package id.ac.polinema.intentexercise;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private EditText inputname;
    private EditText inputemail;
    private EditText inputpassword;
    private EditText inputconfirm_password;
    private EditText inputhomepage;
    private EditText inputabout;

    private static final String TAG = RegisterActivity.class.getCanonicalName();
    private static final int GALLERY_REQUEST_CODE = 1;
    private ImageView avatarImage;
    private ImageView image_profile;

    public static final String FULLNAME_KEY = "fullname";
    public static final String EMAIL_KEY = "email";
    public static final String HOMEPAGE_KEY = "homepage";
    public static final String ABOUT_KEY = "about";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        avatarImage = findViewById(R.id.image_profile);

        inputname = findViewById(R.id.text_fullname);
        inputemail = findViewById(R.id.text_email);
        inputpassword = findViewById(R.id.text_password);
        inputconfirm_password = findViewById(R.id.text_confirm_password);
        inputhomepage = findViewById(R.id.text_homepage);
        inputabout = findViewById(R.id.text_about);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        image_profile.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                if (requestCode == GALLERY_REQUEST_CODE) {
                                    if (data != null) {
                                        try {
                                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                            String picturePath = cursor.getString(columnIndex);
                                            image_profile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                            cursor.close();
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if (requestCode == GALLERY_REQUEST_CODE) {
                            if (data != null) {
                                try {
                                    Uri imageUrl = data.getData();
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUrl);
                                    avatarImage.setImageBitmap(bitmap);
                                } catch (IOException e) {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                                    image_profile.setImageBitmap(bitmap);
                                } catch (IOException e) {
                                    Toast.makeText(this, "Can't load image", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }


    public void handleGambar(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public void handleChangeAvatar(View view) {
        selectImage(RegisterActivity.this);
    }
    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void handleOk(View view) {
        String fullnameText = inputname.getText().toString();
        String emailText = inputemail.getText().toString();
        String passwordText = inputpassword.getText().toString();
        String confirmText = inputconfirm_password.getText().toString();
        String homepageText = inputhomepage.getText().toString();
        String aboutText = inputabout.getText().toString();

        if (!(fullnameText).equals("") && !(emailText).equals("") && !(passwordText).equals("") && !(confirmText).equals("") && !(homepageText).equals("") && !(aboutText).equals("")) {
            if ((passwordText).equals(confirmText)) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("FULLNAME_KEY", fullnameText);
                intent.putExtra("EMAIL_KEY", emailText);
                intent.putExtra("PASSWORD_KEY", passwordText);
                intent.putExtra("CONFIRM_KEY", confirmText);
                intent.putExtra("HOMEPAGE_KEY", homepageText);
                intent.putExtra("ABOUT_KEY", aboutText);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Password dan Confirm password harus sama !", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Tolong isi semua data !", Toast.LENGTH_SHORT).show();
        }
    }
}
