package com.example.mywallpaper;

import android.Manifest;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mywallpaper.adapter.SwiperAdapter;
import com.example.mywallpaper.model.WallpaperModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SwiperActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    int position, currentPosition, matchedPosition;
    private SwiperAdapter adapter;
    private List<WallpaperModel> list;
    DatabaseReference reference;
    private String id, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper);
        init();
        list = new ArrayList<>();
        adapter = new SwiperAdapter(this, list);
        viewPager.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    WallpaperModel model = dataSnapshot.getValue(WallpaperModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(position, true);
            }
        }, 200);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                imageUrl = list.get(position).getImage();
                id = list.get(position).getId();
                currentPosition = position;
            }
        });
        clickListener();
    }


    private void clickListener() {

        adapter.onDataPass(new SwiperAdapter.onDataPass() {
            @Override
            public void onImageSave(int position, Bitmap bitmap) {
                Dexter.withContext(SwiperActivity.this)
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    saveImage(bitmap);
                                } else {
                                    Toast.makeText(SwiperActivity.this, "Please allow permission", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();

                            }
                        }).check();
            }

            @Override
            public void onApplyImage(int position, Bitmap bitmap) {
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

                try {
                    manager.setBitmap(bitmap);
                    Toast.makeText(SwiperActivity.this, "Wallpaper set as background", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SwiperActivity.this, "Failed to set as wallpaper", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //save image
    private void saveImage(Bitmap bitmap) {
        String time = "WallpaperImage" + System.currentTimeMillis();

        String imageName = time + ".png";
        //duong dan anh
        File path = Environment.getExternalStorageDirectory();

        File dir = new File(path + "/DCIM/Wallpaper App");
        boolean is = dir.mkdirs();
        Log.d("result", String.valueOf(is));
        File file = new File(dir, imageName);

        OutputStream out;
        try {
            out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            //chuyen doi anh thanh png
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            bos.flush();
            bos.close();
            Toast.makeText(this, "Success to save image !", Toast.LENGTH_SHORT).show();
            ;

        } catch (Exception ex) {
            ex.printStackTrace();
            ;
            Toast.makeText(this, "Failed to save image !", Toast.LENGTH_SHORT).show();
            ;
        }
    }

    private void init() {
        viewPager = findViewById(R.id.viewPager);
        reference = FirebaseDatabase.getInstance().getReference().child("Wallpapers");
        position = getIntent().getIntExtra("position", -1);
    }

}