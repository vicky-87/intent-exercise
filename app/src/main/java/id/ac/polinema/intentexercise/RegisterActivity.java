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

    public static final String FULLNAME_KEY = "fullname";
    public static final String EMAIL_KEY = "email";
    public static final String HOMEPAGE_KEY = "homepage";
    public static final String ABOUT_KEY = "about";
    public static final String IMAGE_KEY = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputname = findViewById(R.id.text_fullname);
        inputemail = findViewById(R.id.text_email);
        inputpassword = findViewById(R.id.text_password);
        inputconfirm_password = findViewById(R.id.text_confirm_password);
        inputhomepage = findViewById(R.id.text_homepage);
        inputabout = findViewById(R.id.text_about);
        avatarImage = findViewById(R.id.image_profile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                try {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    avatarImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Toast.makeText(this, "Can't load image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    public void handleChangeAvatar(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
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
                avatarImage.buildDrawingCache();
                Bitmap image= avatarImage.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("IMAGE_KEY", image);
                intent.putExtras(extras);

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
