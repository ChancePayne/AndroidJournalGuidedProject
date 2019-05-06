package com.lambdaschool.journalguidedproject;

import android.content.Context;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JournalFilesRepo implements JournalRepoInterface{
    Context context;

    public JournalFilesRepo(Context context) {
        this.context = context;
    }

    @Override
    public void createEntry(JournalEntry entry) {
        String jsonString = entry.toJsonObject().toString();

        writeToFile(entry.getDate() + ".json", jsonString);
    }

    @Override
    public JournalEntry readEntry(int id) {
        return null;
    }

    @Override
    public ArrayList<JournalEntry> readAllEntries() {
        final ArrayList<String> fileList = getFileList();
        final ArrayList<JournalEntry> entries = new ArrayList<>(fileList.size());
        for(String fileName: fileList) {
            String jsonContent = readFromFile(fileName);
            try {
                entries.add(new JournalEntry(new JSONObject(jsonContent)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return entries;
    }

    @Override
    public void updateEntry(JournalEntry entry) {

    }

    @Override
    public void deleteEntry(JournalEntry entry) {

    }

    public boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }

    private File getStorageDirectory() {
        // get cache directory
//        return context.getCacheDir();

        // get Internal directory
        return context.getFilesDir();

        // get External Directory
        /*if(isExternalStorageWriteable()) {
//            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Journals");
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if(!directory.exists() && !directory.mkdirs()) {
                // didn't create
                return context.getCacheDir();
            } else {
                return directory;
            }
        } else {
            return context.getCacheDir();
        }*/

    }

    private String readFromFile(String fileName) {
        File inputFile = new File(getStorageDirectory(), fileName);
        StringBuilder readData = new StringBuilder();
        FileReader reader = null;
        try {
            reader = new FileReader(inputFile);
            int next = reader.read();
            while(next != -1) {
                readData.append((char)next);
                next = reader.read();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return readData.toString();
    }

    private Byte[] readFromFileBytes(String fileName) {
        File file = new File(getStorageDirectory(), fileName);

        FileInputStream fileInputStream = null;
        ArrayList<Byte> readData        = new ArrayList<>();
        try {
            fileInputStream = new FileInputStream(file.getPath());
            byte read;
            do {
                read = (byte) fileInputStream.read();
                readData.add(read);
            } while(read != -1);

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
        final Byte[] data = new Byte[readData.size()];
        readData.toArray(data);
        return data;
    }

    private void writeToFile(String fileName, String text) {
        File directory = getStorageDirectory();
        File outputFile = new File(directory, fileName);

        FileWriter writer = null;
        try {
            writer = new FileWriter(outputFile);
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeToFile(String fileName, byte[] bytes) {
        try {
            // write to file
            File writeFile = new File(getStorageDirectory(), fileName);

            // get bitmap bytes
            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            final byte[] bytes = stream.toByteArray();
            bitmap.recycle();*/

            final String     path             = writeFile.getPath();
            FileOutputStream fileOutputStream = new FileOutputStream(path);

            fileOutputStream.write(bytes);
            fileOutputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

private ArrayList<String> getFileList() {
    ArrayList<String> fileNames = new ArrayList<>();

    final File filesDir = getStorageDirectory();

    final String[] list = filesDir.list();
    if(list != null) {
        for (String name : list) {
            if (name.contains(".json")) {
                fileNames.add(name);
            }
        }
    }

    return fileNames;
}
}
