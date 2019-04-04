package com.lambdaschool.journalguidedproject;

import android.content.Context;

import java.util.ArrayList;

// S03M03-3 Create Manager class and replace all references to repository with this class
public class JournalStorageManager {

    JournalSharedPrefsRepository repo;

    // S03M03-4 Add method implementations to fix error messages
    public JournalStorageManager(Context context) {
        repo = new JournalSharedPrefsRepository(context);
    }

    public ArrayList<JournalEntry> readAllEntries() {
        final ArrayList<JournalEntry> journalEntries = new ArrayList<>();
        final ArrayList<JournalEntry> networkEntries = JournalFirebaseDAO.readAllEntries();
        final ArrayList<JournalEntry> cacheEntries = repo.readAllEntries();

        journalEntries.addAll(cacheEntries);
        for (JournalEntry entry: networkEntries) {
            if(!journalEntries.contains(entry)) {
                journalEntries.add(entry);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(JournalEntry entry: journalEntries) {
                    if(!networkEntries.contains(entry)) {
                        JournalFirebaseDAO.createEntry(entry);
                    }

                    if(!cacheEntries.contains(entry)) {
                        repo.createEntry(entry);
                    }
                }
            }
        }).start();

        return journalEntries;
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
}
