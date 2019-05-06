package com.lambdaschool.journalguidedproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DetailsActivity extends BaseActivity {

    private static final int IMAGE_REQUEST_CODE = 50;

    private JournalEntry entry;

    private TextView  dateView;
    private EditText  entryTextView;
    private SeekBar   dayRatingView;
    private ImageView dayImageView;

    private JournalStorageManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        manager = new JournalStorageManager(this);

//        createJournalEntry();
        Intent intent = getIntent();
        entry = (JournalEntry) intent.getSerializableExtra(JournalEntry.TAG);
        if(entry == null) {
            entry = new JournalEntry();
        }

        dateView = findViewById(R.id.journal_entry_date);
        dateView.setText(entry.getStringDate());

        entryTextView = findViewById(R.id.journal_entry_text);
        entryTextView.setText(entry.getEntryText());
        entryTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String entryString = s.toString();
                entry.setEntryText(entryString);
            }
        });

        dayRatingView = findViewById(R.id.journal_entry_seekbar);
        dayRatingView.setMax(5);
        dayRatingView.setProgress(entry.getDayRating());
        dayRatingView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    entry.setDayRating(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dayImageView = findViewById(R.id.journal_entry_image);
        /*final Uri imageUri = entry.getImage();
        if(imageUri != null) {
            // TODO: use file I/O to read image from path
//            dayImageView.setImageURI(imageUri);
        }*/
        final Bitmap bitmap = manager.readImage(entry);
        if(bitmap != null) {
            dayImageView.setImageBitmap(bitmap);
        }

        findViewById(R.id.add_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });

        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(JournalEntry.TAG, entry);

                setResult(Activity.RESULT_CANCELED, resultIntent);

                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            if (data != null) {
                Uri dataUri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),dataUri);

                    manager.storeImage(bitmap, entry);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                entry.setImage(dataUri);


                // read from file
                File file = new File(this.getFilesDir(), entry.getFbId());

                FileInputStream fileInputStream = null;
                ArrayList<Byte> readData = new ArrayList<>();
                try {
                    fileInputStream = new FileInputStream(file.getPath());
//                    final InputStream inputStream = getContentResolver().openInputStream(dataUri);
                    byte read;
                    do {
                        read = (byte) fileInputStream.read();
                        readData.add(read);
                    } while(read != -1);

                    final Object[] objects = readData.toArray();
                    final Object[] objects2 = readData.toArray(new Byte[]{});
                    final Byte[] objects3 = new Byte[readData.size()];
                    readData.toArray(objects3);

//                    Bitmap         image   = BitmapFactory.decodeByteArray(objects)

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                dayImageView.setImageURI(dataUri);
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(JournalEntry.TAG, entry);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
