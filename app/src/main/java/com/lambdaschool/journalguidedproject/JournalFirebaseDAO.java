package com.lambdaschool.journalguidedproject;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

// S03M03-5 create class to access firebase database
public class JournalFirebaseDAO {
    private static final String TAG = "JournalFirebaseDAO";

    // S03M03-6 add components to build urls for database
    private static final String BASE_URL       = "https://journaldemo-7fabe.firebaseio.com";
    private static final String ENTRIES_OBJECT = "/entries";
    private static final String URL_ENDING     = ".json";

    private static final String READ_ALL_URL = BASE_URL + ENTRIES_OBJECT + URL_ENDING;
    private static final String CREATE_URL   = BASE_URL + ENTRIES_OBJECT + URL_ENDING;
    private static final String SINGLE_ENTRY = BASE_URL + ENTRIES_OBJECT + "/%s" + URL_ENDING;

    // TODO: CREATE
    public static void createEntry(final JournalEntry entry) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> headerProps = new HashMap<>();
                headerProps.put("Content-Type", "application/json");

                String result = NetworkAdapter.httpRequest(
                        String.format(CREATE_URL, entry.getId()),
                        NetworkAdapter.POST,
                        entry.toJsonObject(),
                        headerProps);
                try {
                    entry.setId(new JSONObject(result).getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // TODO: READ ONE
    // TODO: READ ALL
    // S03M03-7 write method to read all entries from database
    public static ArrayList<JournalEntry> readAllEntries() {
        // TODO: Why isn't it grabbing all entries
        final ArrayList<JournalEntry> resultList = new ArrayList<>();

        final String result = NetworkAdapter.httpRequest(READ_ALL_URL);
        try {
            JSONObject topJson = new JSONObject(result);
            for (Iterator<String> it = topJson.keys(); it.hasNext(); ) {
                String key = it.next();
                try {
                    final JSONObject jsonEntry = topJson.getJSONObject(key);
                    int              date      = jsonEntry.getInt("date");
                    int              dayRating = jsonEntry.getInt("day_rating");
                    String           entryText = jsonEntry.getString("entry_text");
                    String           image     = jsonEntry.getString("image");
                    String           id        = key;

                    resultList.add(
                            new JournalEntry(
                                    date,
                                    entryText,
                                    image,
                                    dayRating,
                                    id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i(TAG, "Finished parsing all entries");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    // TODO: UPDATE
    public static void updateEntry(final JournalEntry entry) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> headerProps = new HashMap<>();
                headerProps.put("Content-Type", "application/json");

                String result = NetworkAdapter.httpRequest(
                        String.format(SINGLE_ENTRY, entry.getId()),
                        NetworkAdapter.PUT,
                        entry.toJsonObject(),
                        headerProps);

                // could check result for successful update
            }
        }).start();
    }

    // TODO: DELETE
    public static void deleteEntry(final JournalEntry entry) {
        // TODO: Connect to delete button
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = NetworkAdapter.httpRequest(
                        String.format(SINGLE_ENTRY, entry.getId()),
                        NetworkAdapter.DELETE);

                // could check result for successful update
            }
        }).start();
    }
}
