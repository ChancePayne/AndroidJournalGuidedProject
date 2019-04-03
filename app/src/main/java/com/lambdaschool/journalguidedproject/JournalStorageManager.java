package com.lambdaschool.journalguidedproject;

import android.content.Context;

import java.util.ArrayList;

// S03M03-3 Create Manager class and replace all references to repository with this class
public class JournalStorageManager {

    // S03M03-4 Add method implementations to fix error messages
    public JournalStorageManager(Context context) {

    }

    public ArrayList<JournalEntry> readAllEntries() {
        return JournalFirebaseDAO.readAllEntries();
    }

    public void createEntry(JournalEntry entry) {
        JournalFirebaseDAO.createEntry(entry);
    }

    public void updateEntry(JournalEntry entry) {
        // TODO: Add update to network
    }
}
