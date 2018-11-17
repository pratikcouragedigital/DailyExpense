package com.pratik.dailyexpense.Backup;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import com.pratik.dailyexpense.sqlLite.DataBaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class Backup_Restore {

    String mainFolderName = "DailyExpense";
    String strDirectory;

    private DataBaseHelper mydb;


    public void importDB(Activity activity) {
        try {
            String state = Environment.getExternalStorageState();
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                String currentDBPath = "//data/com.mobitechs.dailyexpense/databases/DailyExpense";
                String backupDBPath = "/DailyExpense/backup/DailyExpenseBackup";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
//                File backupDB = new File(Environment.getExternalStorageDirectory()
//                        .getAbsolutePath() + File.separator + "TeaDiaryBackup");

                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(activity, "Import Successful!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Import Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void exportDB(Activity activity) {
        try{
//            mydb = new DataBaseHelper(activity);
//            String a = mydb.getDatabaseName();
//            Toast.makeText(activity, a+"", Toast.LENGTH_SHORT).show();

            File dbFile = new File(Environment.getDataDirectory() + "/data/com.mobitechs.dailyexpense/databases/DailyExpense");
            File exportDir = new File(Environment.getExternalStorageDirectory()+"/DailyExpense/backup");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, dbFile.getName()+"Backup");

            file.createNewFile();
            FileChannel inChannel = new FileInputStream(dbFile).getChannel();  //fails here
            FileChannel outChannel = new FileOutputStream(file).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            }
            Toast.makeText(activity, "Backup Successful!", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(activity, "Backup Failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
