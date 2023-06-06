package com.moutamid.rurovision.sticker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.moutamid.rurovision.activity.ConstantMethod;

import com.moutamid.rurovision.R;
import com.moutamid.rurovision.sticker.repeater.RepeaterStickr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StickerListActivity extends AppCompatActivity {

    RecyclerView rvStickerOptions;
    TextView tvTitle;
    int currentPosition;
    String currentName;
    int nLastTimeInertedSticker;
    private ArrayList<String> nStickerArrayList = new ArrayList<>();
    RepeaterStickr nStickerRepeater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_list);

        rvStickerOptions = findViewById(R.id.rvStickerOptions);
        tvTitle = findViewById(R.id.tvTitle);

        currentPosition = getIntent().getIntExtra("curPos",0);
        currentName = getIntent().getStringExtra("curName");

        tvTitle.setText(currentName);

        nLastTimeInertedSticker = 0;
        String zipName = getZipName(currentPosition);
        loadStaticSticker(zipName);
        nStickerArrayList.clear();
        nStickerArrayList = loadStickerStorage();
        rvStickerOptions.setLayoutManager(new GridLayoutManager(StickerListActivity.this,4, RecyclerView.VERTICAL,false));
        nStickerRepeater = new RepeaterStickr(StickerListActivity.this, nStickerArrayList);
        rvStickerOptions.setAdapter(nStickerRepeater);

        nStickerRepeater.setClickListener((path, position) -> {

            nLastTimeInertedSticker = nLastTimeInertedSticker + 1;

            Bitmap decodeFile = BitmapFactory.decodeFile(new File(path).getAbsolutePath());
            Drawable drawable = new BitmapDrawable(getResources(), decodeFile);

            onClickApp("com.whatsapp",decodeFile);
        });

    }

    public void onClickApp(String pack, Bitmap bitmap) {
        PackageManager pm = StickerListActivity.this.getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(StickerListActivity.this.getContentResolver(), bitmap, "Title", null);
            Uri imageUri = Uri.parse(path);

            @SuppressWarnings("unused")
            PackageInfo info = pm.getPackageInfo(pack, PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.setPackage(pack);
            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            waIntent.putExtra(Intent.EXTRA_TEXT, pack);
            StickerListActivity.this.startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(StickerListActivity.this, "App not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    private String getZipName(int pos) {
        String zipName = null;
        if (pos == 0) {
            zipName = "sti__bb.zip";
        } else if (pos == 1) {
            zipName = "sti__b_day.zip";
        } else if (pos == 2) {
            zipName = "sti__cartton.zip";
        } else if (pos == 3) {
            zipName = "sti__eat.zip";
        } else if (pos == 4) {
            zipName = "sti__hawolinn.zip";
        } else if (pos == 5) {
            zipName = "sti__couple.zip";
        } else if (pos == 6) {
            zipName = "sti__sound.zip";
        } else if (pos == 7) {
            zipName = "sti__sl.zip";
        } else if (pos == 8) {
            zipName = "sti__media.zip";
        } else if (pos == 9) {
            zipName = "sti__trsprt.zip";
        } else if (pos == 10) {
            zipName = "sti__trav.zip";
        }
        return zipName;
    }

    private void loadStaticSticker(String zipName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ConstantMethod.getStickerPath(this));
        if (new File(stringBuilder.toString()).exists()) {
            for (File file2 : new File(stringBuilder.toString()).listFiles()) {
                if (!file2.getAbsolutePath().contains("thumb")) {
                    file2.delete();
                }
            }
        }
        if (new File(stringBuilder.toString()).exists()) {
            try {
                InputStream open = getAssets().open(zipName);
                stringBuilder = new StringBuilder();
                stringBuilder.append(ConstantMethod.getStickerPath(this));
                boolean b = unZipFile(open, stringBuilder.toString());
                Log.e("FileZipOperation", String.valueOf(b));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean unZipFile(InputStream inputStream, String str) {
        byte[] bArr = new byte[2048];
        try {
            ZipInputStream nZipInputStream = new ZipInputStream(inputStream);
            while (true) {
                ZipEntry nextEntry = nZipInputStream.getNextEntry();
                if (nextEntry != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unzipping ");
                    stringBuilder.append(nextEntry.getName());
                    Log.e("FileZipOperation", stringBuilder.toString());
                    if (nextEntry.isDirectory()) {
                        Log.e("FileZipOperation", str + " " + nextEntry.getName());
                    } else {
                        File file = new File(str, nextEntry.getName());
                        if (!file.exists()) {
                            if (file.createNewFile()) {
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                while (true) {
                                    int read = nZipInputStream.read(bArr);
                                    if (read == -1) {
                                        break;
                                    }
                                    fileOutputStream.write(bArr, 0, read);
                                }
                                nZipInputStream.closeEntry();
                                fileOutputStream.close();
                            } else {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Failed to create file ");
                                stringBuilder.append(file.getName());
                                Log.e("FileZipOperation", stringBuilder.toString());
                            }
                        }
                    }
                } else {
                    nZipInputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<String> loadStickerStorage() {
        ArrayList<String> nStickerArrayList = new ArrayList<>();
        nStickerArrayList.clear();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ConstantMethod.getStickerPath(this));
        File file = new File(stringBuilder.toString());
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (!file2.getAbsolutePath().contains("thumb")) {
                    nStickerArrayList.add(file2.getAbsolutePath());
                }
            }
        }
        return nStickerArrayList;
    }

    public void backclickStickerList(View view) {
        onBackPressed();
    }
}