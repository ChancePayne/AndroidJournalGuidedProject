package com.lambdaschool.journalguidedproject;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalEntry implements Serializable {
    public static final String TAG        = "JournalEntry";
    public static final String INVALID_ID = "";

    // S03M03-8 Change ID to a string
    private String entryText, image, id;
    private int dayRating, date;

    public JournalEntry(int date, String entryText, String image, int dayRating, String id) {
        this.date = date;
        this.entryText = entryText;
        this.image = image;
        this.dayRating = dayRating;
        this.id = id;
    }

    public JournalEntry() {
        this.id = INVALID_ID;
        this.entryText = "";
        this.image = "";

        initializeDate();
    }

    public JournalEntry(String id, String entryText) {
        this.id = id;
        this.entryText = entryText;
        this.dayRating = 3;
        this.image = "";

        initializeDate();
    }

    public JournalEntry(String entryText, long epochTimeSeconds, int rating) {
        this.entryText = entryText;
        this.dayRating = rating;

        this.setDate((int) epochTimeSeconds);

        this.id = INVALID_ID;
        this.image = "";
    }

    /*public JournalEntry(String csvString) {
        String[] values = csvString.split(",");
        // check to see if we have the right string
        if (values.length == 5) {
            // handle missing numbers or strings in the number position
            try {
                this.id = values[0];
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            this.date = values[1];
            try {
                this.dayRating = Integer.parseInt(values[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            // allows us to replace commas in the entry text with a unique character and
            // preserve entry structure
            this.entryText = values[3].replace("~@", ",");
            // placeholder for image will maintain csv's structure even with no provided image
            this.image = values[4].equals("unused") ? "" : values[4];
        }
    }*/

    boolean areEqual(JournalEntry a) {
        return this.id == a.id && this.getDayRating() == a.getDayRating();
    }

    // converting our object into a csv string that we can handle in a constructor
    String toCsvString() {
        return String.format("%d,%s,%d,%s,%s",
                             id,
                             date,
                             dayRating,
                             entryText.replace(",", "~@"),
                             image == "" ? "unused" : image);
    }

    private void initializeDate() {
        this.date = (int) (System.currentTimeMillis() / 1000);
    }

    public String getStringDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date       date       = new Date();

        return dateFormat.format(date);
    }

    public int getDate() {
        return this.date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getEntryText() {
        return entryText;
    }

    public void setEntryText(String entryText) {
        this.entryText = entryText;
    }

    public Uri getImage() {
        if (!image.equals("")) {
            return Uri.parse(image);
        } else {
            return null;
        }
    }

    public void setImage(Uri imageUri) {
        this.image = imageUri.toString();
    }

    public int getDayRating() {
        return dayRating;
    }

    public void setDayRating(int dayRating) {
        this.dayRating = dayRating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // S03M03-12 write method to convert entry into jsonObject
    public JSONObject toJsonObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", date);
            jsonObject.put("entry_text", entryText);
            jsonObject.put("image", image);
            jsonObject.put("day_rating", dayRating);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                return new JSONObject(String.format("{\"date\":\"%s\",\"entry_text\":\"%s\",\"image\":\"%s\",\"day_rating\":%d}", date, entryText, image, dayRating));
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }
}
