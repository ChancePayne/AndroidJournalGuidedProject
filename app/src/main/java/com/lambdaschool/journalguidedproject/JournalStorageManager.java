package com.lambdaschool.journalguidedproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

// S03M03-3 Create Manager class and replace all references to repository with this class
public class JournalStorageManager {

    JournalRepoInterface repo;

    // S03M03-4 Add method implementations to fix error messages
    public JournalStorageManager(Context context) {
//        repo = new JournalSharedPrefsRepository(context);
        repo = new JournalFilesRepo(context);
    }

    public ArrayList<JournalEntry> readAllEntries() {
        final ArrayList<JournalEntry> journalEntries = new ArrayList<>();
        final ArrayList<JournalEntry> networkEntries = JournalFirebaseDAO.readAllEntries();
        final ArrayList<JournalEntry> cacheEntries = repo.readAllEntries();

        journalEntries.addAll(cacheEntries);
        for (JournalEntry entry: networkEntries) {
            if(!checkForEntry(entry, journalEntries)) {
//            if(!journalEntries.contains(entry)) {
                journalEntries.add(entry);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(JournalEntry entry: journalEntries) {
                    if(!checkForEntry(entry, networkEntries)) {
                        JournalFirebaseDAO.createEntry(entry);
                    }

                    if(!checkForEntry(entry, cacheEntries)) {
                        repo.createEntry(entry);
                    }
                }
            }
        }).start();

        return journalEntries;
    }

    public boolean checkForEntry(JournalEntry entry, List<JournalEntry> list) {
        for(int i = 0; i < list.size(); ++i) {
            if(entry.getDate() == list.get(i).getDate()) {
                return true;
            }
        }
        return false;
    }

    public void createEntry(JournalEntry entry) {
        JournalFirebaseDAO.createEntry(entry);
        repo.createEntry(entry);
    }

    public void updateEntry(JournalEntry entry) {
        JournalFirebaseDAO.updateEntry(entry);
        repo.updateEntry(entry);
    }

    public void deleteEntry(JournalEntry entry) {
        JournalFirebaseDAO.deleteEntry(entry);
        // TODO: ADD DELETE
    }


    public void storeImage(Bitmap bitmap, JournalEntry entry) {
        // get bitmap bytes
        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
        final byte[] bytes = stream.toByteArray();*/

        repo.createImage(bitmap, getImageName(entry));

//        bitmap.recycle();



    }

    private String getImageName(JournalEntry entry) {
        return entry.getDate() + ".jpeg";
    }

    public Bitmap readImage(JournalEntry entry) {
        return repo.readImage(getImageName(entry));
        /*final byte[] primitiveBytes = new byte[bytes.length];
        int index = 0;
        for(Byte b: bytes) {
            primitiveBytes[index++] = b;
        }

        final Bitmap bitmap = BitmapFactory.decodeByteArray(primitiveBytes, 0, bytes.length);
        return bitmap;*/
    }
}
