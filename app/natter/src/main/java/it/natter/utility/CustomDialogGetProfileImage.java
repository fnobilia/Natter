package it.natter.utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

import it.natter.R;
import it.natter.classes.Esito;
import it.natter.classes.ImageService;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.UserService;

/**
 * Created by francesco on 17/03/14.
 */

public class CustomDialogGetProfileImage extends DialogFragment implements View.OnClickListener{

    private Button button_camera;
    private Button button_gallery;

    private ImageView profile;

    private final int REQUEST_CAMERA = 100;
    private final int REQUEST_GALLERY = 10;

    public CustomDialogGetProfileImage(View v){
        this.profile = (ImageView)v.findViewById(R.id.profile_img);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        this.setCancelable(false);

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.custom_dialog_image_profile);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.button_camera = (Button) dialog.findViewById(R.id.button_profile_image_camera);
        this.button_gallery = (Button) dialog.findViewById(R.id.button_profile_image_gallery);

        this.button_camera.setOnClickListener(this);
        this.button_gallery.setOnClickListener(this);

        dialog.show();

        return dialog;
    }

    public void onClick(View v){
        if(v.getContentDescription().toString().equals("camera")){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.png");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(f));
            startActivityForResult(intent,REQUEST_CAMERA);
        }
        else if(v.getContentDescription().toString().equals("gallery")){
            openGallery(REQUEST_GALLERY);
        }
    }

    public void openGallery(int req_code){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose your profile image"), req_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.png")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),btmapOptions);
                    bm = Bitmap.createScaledBitmap(bm, 120, 120, true);

                    Hardware.deleteProfileImage();
                    Hardware.saveProfileImage(bm);

                    contactService();

                    this.profile.setImageBitmap(bm);

                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == REQUEST_GALLERY){
                if(data.getData() == null){
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to get Image!", 500).show();
                }
                else{
                    try{
                        Bitmap bm = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(data.getData()));
                        bm = Bitmap.createScaledBitmap(bm, 120, 120, true);

                        Hardware.deleteProfileImage();
                        Hardware.saveProfileImage(bm);

                        contactService();

                        this.profile.setImageBitmap(bm);
                    }catch(FileNotFoundException e){
                        Toast.makeText(getActivity().getApplicationContext(),e.getMessage(), 500).show();
                    }
                }
            }
        }

        this.dismiss();
    }

    private void contactService(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                Looper.prepare();

                Context context = getActivity().getApplicationContext();

                DataBaseHelper databaseHelper = new DataBaseHelper(context);
                final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                String id = (new Integer(Dao.getIdProfile(db))).toString();
                String image = Hardware.fromImageToString(context);

                Esito esito = UserService.updateProfileImage(new ImageService(id,image));
            }
        }).start();
    }
}