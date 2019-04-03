package com.lambdaschool.journalguidedproject;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

// S03M03-5 create class to access firebase database
public class JournalFirebaseDAO {
    private static final String TAG = "JournalFirebaseDAO";

    // S03M03-6 add components to build urls for database
    private static final String BASE_URL = "https://journaldemo-7fabe.firebaseio.com";
    private static final String ENTRIES_OBJECT = "/entries";
    private static final String URL_ENDING = ".json";

    private static final String READ_ALL_URL = BASE_URL + ENTRIES_OBJECT + URL_ENDING;

    // TODO: CREATE


    // TODO: READ ONE
    // TODO: READ ALL
    // S03M03-7 write method to read all entries from database
    public static ArrayList<JournalEntry> readAllEntries() {
        final ArrayList<JournalEntry> resultList = new ArrayList<>();

                final String result = NetworkAdapter.httpRequest(READ_ALL_URL, null);
                try {
                    JSONObject topJson = new JSONObject(result);
                    for (Iterator<String> it = topJson.keys(); it.hasNext(); ) {
                        String key = it.next();

                        final JSONObject jsonEntry= topJson.getJSONObject(key);
                        int date = jsonEntry.getInt("date");
                        int dayRating = jsonEntry.getInt("day_rating");
                        String entryText = jsonEntry.getString("entry_text");
                        String image = jsonEntry.getString("image");
                        String id = key;

                        resultList.add(
                                new JournalEntry(
                                        Integer.toString(date),
                                        entryText,
                                        image,
                                        dayRating,
                                        id));
                    }
                    Log.i(TAG, "Finished parsing all entries");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

        return resultList;
    }

    // TODO: UPDATE
    // TODO: DELETE
}
