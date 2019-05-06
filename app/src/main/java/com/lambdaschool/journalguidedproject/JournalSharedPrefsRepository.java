package com.lambdaschool.journalguidedproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;

// responsible for managing journal long term storage
public class JournalSharedPrefsRepository implements JournalRepoInterface{
    private static final String JOURNAL_PREFERENCES = "JournalPreferences";

    private static final String ID_LIST_KEY           = "id_list";
    private static final String ENTRY_ITEM_KEY_PREFIX = "entry_";
    private static final String NEXT_ID_KEY           = "next_id";

    private SharedPreferences prefs;

    public JournalSharedPrefsRepository(Context context) {
        prefs = context.getSharedPreferences(JOURNAL_PREFERENCES, Context.MODE_PRIVATE);
    }

    // create a new entry
    public void createEntry(JournalEntry entry) {
        // read list of entry ids
        ArrayList<String> ids = getListOfIds();

        if (entry.getCacheId() == null && !ids.contains(entry.getCacheId())) {
            // new entry
            SharedPreferences.Editor editor = prefs.edit();

            int nextId = prefs.getInt(NEXT_ID_KEY, 0);
            entry.setCacheId(Integer.toString(nextId));
            // store updated next id
            editor.putInt(NEXT_ID_KEY, ++nextId);

            // add id to list of ids

            ids.add(entry.getCacheId());
            // store updated id list
            StringBuilder newIdList = new StringBuilder();
            for (String id : ids) {
                newIdList.append(id).append(",");
            }

            editor.putString(ID_LIST_KEY, newIdList.toString());

            // store new entry
            editor.putString(ENTRY_ITEM_KEY_PREFIX + entry.getCacheId(), entry.toCsvString());
            editor.apply();
        } else {
            updateEntry(entry);
        }

        // save entry
    }

    private ArrayList<String> getListOfIds() {
        String            idList  = prefs.getString(ID_LIST_KEY, "");
        String[]          oldList = idList.split(",");
        ArrayList<String> ids     = new ArrayList<>(oldList.length);
        if (!idList.equals("")) {
            ids.addAll(Arrays.asList(oldList));
        }
        return ids;
    }

    // read an existing entry
    public JournalEntry readEntry(int id) {
        String entryCsv = prefs.getString(ENTRY_ITEM_KEY_PREFIX + id, "invalid");
        if (!entryCsv.equals("invalid")) {
            JournalEntry entry = new JournalEntry(entryCsv);
            return entry;
        } else {
            return null;
        }
    }

    // read all entries
    public ArrayList<JournalEntry> readAllEntries() {
        // read list of entry ids
        final ArrayList<String> listOfIds = getListOfIds();

        // step through that list and read each entry
        ArrayList<JournalEntry> entryList = new ArrayList<>();
        for (String id : listOfIds) {
            final JournalEntry entry = readEntry(Integer.parseInt(id));
            if(entry != null) {
                entryList.add(entry);
            }
        }
        return entryList;
    }

    // edit an existing entry
    public void updateEntry(JournalEntry entry) {
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ENTRY_ITEM_KEY_PREFIX + entry.getCacheId(), entry.toCsvString());
        editor.apply();
    }

    @Override
    public void deleteEntry(JournalEntry entry) {
        // TODO: Add delete
    }

    @Override
    public void createImage(byte[] bytes, String name) {

    }

    @Override
    public Bitmap readImage(String name) {
        return null;
    }


    // delete an entry
}
