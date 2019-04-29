package com.lambdaschool.journalguidedproject;

import java.util.ArrayList;

public interface JournalRepoInterface {
    void createEntry(JournalEntry entry);
    JournalEntry readEntry(int id);
    ArrayList<JournalEntry> readAllEntries();
    void updateEntry(JournalEntry entry);
    void deleteEntry(JournalEntry entry);
}
