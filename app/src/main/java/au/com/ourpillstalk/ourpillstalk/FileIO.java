package au.com.ourpillstalk.ourpillstalk;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.memetix.mst.language.Language;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Elliott on 1/04/15.
 */
public class FileIO {
    public static final String SCAN_FILE_INDEX_NAME = "SCAN_INDEX";
    public static final String SCAN_FILE_NAME = "SCAN#";

    public static final String LANGUAGE_TRANSLATION_FILE_NAME = "LANGUAGE_TRANSLATION";
    public static final String AVAILABLE_LANGUAGES_FILE_NAME = "AVAILABLE_LANGUAGES";
    public static final String AVAILABLE_LANGUAGES_CODES_FILE_NAME = "AVAILABLE_LANGUAGES_CODES";
    public static final String USER_INFORMATION_FILE_NAME = "USER_INFORMATION";

    static final String PAT_NAME = "pat_name";
    static final String DRUG_NAME = "drug_name";
    static final String EXP_INSTRUC = "exp_instruc";
    static final String SCRIPT_ID = "script_id";
    static final String PHARM_NAME = "pharm_name";
    ////

    private static void saveAvailableLanguagesCodes(ArrayList<Language> availableLanguages, Context context) {
        String header = AVAILABLE_LANGUAGES_CODES_FILE_NAME + "_" + getDateString() + "\n\n";
        String body = "";
        for(int i = 0; i < availableLanguages.size(); i++) {
            body = body + availableLanguages.get(i).toString() +"\n";
        }
        writeToFile(AVAILABLE_LANGUAGES_CODES_FILE_NAME, header + body, context);
    }

    public static ArrayList<String> getAvailableLanguagesCodes(Context context) {
        String availableLanguagesBody = getFileBody(AVAILABLE_LANGUAGES_CODES_FILE_NAME, context);
        String[] languages = availableLanguagesBody.split("\n");

        ArrayList<String> availableLanguages = new ArrayList<String>(languages.length);
        for(int i = 0; i < languages.length; i++) {
            availableLanguages.add(languages[i]);
        }

        return availableLanguages;
    }

    public static void saveAvailableLanguages(ArrayList<String> availableLanguages, ArrayList<Language> availableLanguagesCodes, Context context) {
        saveAvailableLanguagesCodes(availableLanguagesCodes, context);
        String header = AVAILABLE_LANGUAGES_FILE_NAME + "_" + getDateString() + "\n\n";
        String body = "";
        for(int i = 0; i < availableLanguages.size(); i++) {
            body = body + availableLanguages.get(i) +"\n";
        }
        writeToFile(AVAILABLE_LANGUAGES_FILE_NAME, header + body, context);
    }

    public static ArrayList<String> getAvailableLanguages(Context context) {
        String availableLanguagesBody = getFileBody(AVAILABLE_LANGUAGES_FILE_NAME, context);
        String[] languages = availableLanguagesBody.split("\n");

        ArrayList<String> availableLanguages = new ArrayList<String>(languages.length);
        for(int i = 0; i < languages.length; i++) {
            availableLanguages.add(languages[i]);
        }

        return availableLanguages;
    }

    public static void saveUserInformation(String[] userInfo, Context context) {
        String header = USER_INFORMATION_FILE_NAME + "_" + getDateString() + "\n\n";
        String body = "";
        for(int i = 0; i < userInfo.length; i++) {
            body = body + userInfo[i] +"\n";
        }
        writeToFile(USER_INFORMATION_FILE_NAME, header + body, context);
    }


    public static String[] getUserInformationArray(Context context) {
        String userInfoBody = getFileBody(USER_INFORMATION_FILE_NAME, context);

        String[] userInfo = userInfoBody.split("\n");
        return userInfo;
    }
    public static String getUserInformation(Context context) {
        String userInfoBody = getFileBody(USER_INFORMATION_FILE_NAME, context);
        if(!userInfoBody.equals("\n\n\n\n\n")) {
            return userInfoBody;
        } else {
            return "";
        }
    }

    public static String[] searchIndexedScansForKeyWord(String keyWord, String[] index, Context context) {
        ArrayList<String> scanMatch = new ArrayList<String>();

        for (int i = 0; i < index.length; i++) {
            String scan = getCompleteFile(index[i], context).toLowerCase();
            if (scan.contains(keyWord.toLowerCase())) {
                scanMatch.add(index[i]);
            }
        }

        if (scanMatch.size() != 0) {
            String[] scanMatches = new String[scanMatch.size()];
            for (int i = 0; i < scanMatch.size(); i++) {
                scanMatches[i] = scanMatch.get(i);
            }
            return scanMatches;
        } else {
            String[] notFound = {"-1"};
            return notFound;
        }
    }

    public static String getUserInformationWithDescription(Context context) {
        String[] userInfo = FileIO.getUserInformationArray(context);
        String name = "Name: " + userInfo[0] + "\n";
        String city = "City: " + userInfo[1] + "\n";
        String suburb = "Suburb: " + userInfo[2] + "\n";
        String phone = "Contact No.: " + userInfo[3] + "\n";
        String medication = "Medication: " + userInfo[4];

        String formatUserInfo = name + city + suburb + phone + medication;
        return formatUserInfo;
    }



    private static boolean saveHeaderToFile(String fileName,  String headerData, Context context) {
        headerData = headerData + "\n\n";
        return writeToFile(fileName, headerData, context);
    }

    private static boolean saveBodyToFile(String fileName,  String fileData, Context context) {
        String fileHeader = getFileHeader(fileName, context);
        String fileHeaderAndBody = fileHeader + fileData;
        Toast.makeText(context, fileHeaderAndBody, Toast.LENGTH_LONG).show();
        return writeToFile(fileName, fileHeaderAndBody, context);
    }

    /**
     * Saves a file, given a file name and its contents.
     * If the file already exists, it is overwritten
     *
     * @param fileName
     * @param fileData
     * @param context
     * @return true on success
     */
    private static boolean writeToFile(String fileName, String fileData, Context context) {
        deleteFile(fileName, context);
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(fileData.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the contents of a file given a file name.
     * @param fileName
     * @param context
     * @return
     */
    private static String getCompleteFile(String fileName, Context context) {
        String fileData = "";
        try {
            InputStream in = new BufferedInputStream(context.openFileInput(fileName));
            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer);
            in.close();
            fileData = writer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return fileData;
    }
    /**
     * Returns a string representation of the contents of a file given a file name.
     * @param fileName
     * @param context
     * @return
     */
    private static String getCompleteScanFile(String fileName, boolean stripScanName, Context context) {
        String fileData = "";
        try {
            InputStream in = new BufferedInputStream(context.openFileInput(fileName));
            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer);
            in.close();
            fileData = writer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if(stripScanName) {
            fileData = fileData.split("_", 2)[1];
        }
        String[] splitFileData = fileData.split("\n\n", 2);
        String header = splitFileData[0];
        String body = splitFileData[1];



        String processedBody = "";

        if(isPrescriptionScanXML(body)) {
            HashMap<String, String> parseXml = parseXMLQRScan(body);
            processedBody = getScanInfoToDisplay(parseXml);
            //Toast.makeText(context, body, Toast.LENGTH_SHORT).show();
        } else {
            processedBody = body;
        }

        return (header + "\n\n" + processedBody);


    }

    public static void deleteScanIndex(Context context) {
        deleteFile(SCAN_FILE_INDEX_NAME, context);
    }
    /**
     * Deletes a file given a file name
     * @param fileName
     * @param context
     */
    private static void deleteFile(String fileName, Context context) {
        File file = new File(context.getFilesDir(), fileName);
        file.delete();
    }

    /**
     * Appends a given file name to the scan index (SCAN_FILE_INDEX_NAME) on a new line
     * @param fileName
     * @param context
     * @return
     */
    private static boolean addFileNameToScanIndex(String fileName, Context context) {
        String index = getCompleteFile(SCAN_FILE_INDEX_NAME, context);
        index = index + fileName +"\n";
        return writeToFile(SCAN_FILE_INDEX_NAME, index, context);
    }

    /**
     * Sets the number of scans that have been saved given a integer
     * @param scanNum
     * @param context
     */
    /*private static void setScanNum(int scanNum, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SharedPreferencesIO.KEY_SCAN_TOTAL, scanNum);
        editor.commit();
    }*/

    /**
     * Gets the number of scans that have been saved
     * @param context
     * @return
     */
    /*private static int getScanNum(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        return prefs.getInt(SharedPreferencesIO.KEY_SCAN_TOTAL, 0);
    }*/

    /**
     * Returns the date as a string in the format D/M/Y_H:M:S
     * @return the date as a String
     */
    private static String getDateString() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());

        int hourInt = c.get(Calendar.HOUR_OF_DAY);
        String hour = String.valueOf(hourInt);
        if(hourInt < 10) {
            hour = "0" + String.valueOf(hourInt);
        }

        int minuteInt = c.get(Calendar.MINUTE);
        String minute = String.valueOf(minuteInt);
        if(minuteInt < 10) {
            minute = "0" + String.valueOf(minuteInt);
        }

        int dayMonthInt = c.get(Calendar.DAY_OF_MONTH);
        String dayMonth = String.valueOf(dayMonthInt);
        if(dayMonthInt < 10) {
            dayMonth = "0" + String.valueOf(dayMonthInt);
        }

        int monthInt = c.get(Calendar.MONTH);
        String month = String.valueOf(monthInt);
        if(monthInt < 10) {
            month = "0" + String.valueOf(monthInt);
        }

        int yearInt = c.get(Calendar.YEAR);
        String year = String.valueOf(yearInt);

        String dateString = hour + ":" + minute + " " + dayMonth + "/" + month + "/" + year;

        return dateString;
    }

    /**
     * Saves a QR code scan to a file named SCAN#N (where N is the amount of saved scans number of total scans).
     * The file name and the date of when the scan was saved, is put as a header before the content body.
     * The header and body is separated by a blank line (\n).
     *
     * After creating the file, the name of the file is appended to the scan index on a new line.
     * @param scanText
     * @param context
     * @return
     */
    public static boolean saveQRScan(String scanText, Context context) {
        int scanTotalNum = SharedPreferencesIO.getScanTotal(context); //getScanNum(context);
        String fileName = SCAN_FILE_NAME + String.valueOf(scanTotalNum);
        String fileHeader = fileName + "_" + getDateString()+"\n\n";
        String fileBody = scanText;
        //Toast.makeText(context, fileHeader + fileBody, Toast.LENGTH_LONG).show();
        if(writeToFile(fileName, fileHeader + fileBody, context)) {
            int incScanNum = scanTotalNum + 1;
            //setScanNum(incScanNum, context);
            SharedPreferencesIO.setScanTotal(incScanNum, context);
            if(addFileNameToScanIndex(fileName, context)) {
                return true;
            } else {
                Toast.makeText(context, "Failed to update index", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(context, "Failed to write scan to file", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static String[] getCompleteFiles(String[] fileNames, Context context) {

        String[] allFiles = new String[fileNames.length];

        for(int i = 0; i < fileNames.length; i++) {
            allFiles[i] = getCompleteFile(fileNames[i], context);
        }

        return allFiles;
    }

    public static String[] getCompleteScanFiles(String[] fileNames, boolean stripScanName, Context context) {

        String[] allFiles = new String[fileNames.length];

        for(int i = 0; i < fileNames.length; i++) {
            allFiles[i] = getCompleteScanFile(fileNames[i], stripScanName, context);
        }

        return allFiles;
    }

    /**
     * Returns a scan index with all duplicate scans removed
     * @param context
     * @return
     */
    public static String[] getRemovedDuplicateIndex(Context context) {
        String[] index = getIndexArray(context);
        ArrayList<String> oldIndex = new ArrayList<String>();
        ArrayList<String> newIndex = new ArrayList<String>();

        for(int i = 0; i < index.length; i++) {
            newIndex.add(index[(index.length - 1) - i]);
        }

        for(int i = 0; i < newIndex.size(); i++) {
            for(int j = i; j < newIndex.size(); j++) {
                if(getFileBody(newIndex.get(i), context).equals(getFileBody(newIndex.get(j), context)) && j != i) {
                    newIndex.remove(j);
                    j--;
                }
            }
        }

        String duplicateIndex = "";
        String[] removedDuplicateIndex = new String[newIndex.size()];
        for(int i = 0; i < newIndex.size(); i++) {
            removedDuplicateIndex[i] = newIndex.get((newIndex.size()-1) - i);
            duplicateIndex += newIndex.get(i) +"\n";
        }
        return removedDuplicateIndex;
    }

    /**
     *
     * @param translation array of length 5
     * @param languageCode
     * @param context
     */
    public static  void saveMainMenuTranslation(String[] translation, String languageCode, Context context) {
        FileOutputStream fos = null;
        String header = languageCode + "\n\n";
        String translationNewLine = header;
        if(translation.length != 5) {
            Toast.makeText(context, "translation not length 5", Toast.LENGTH_LONG).show();
            return;
        }

        for(int i = 0; i < translation.length; i++) {
            translationNewLine = translationNewLine + translation[i] + "\n";
        }

        //Toast.makeText(context, translationNewLine, Toast.LENGTH_LONG).show();

        try {
            fos = context.openFileOutput(LANGUAGE_TRANSLATION_FILE_NAME, Context.MODE_PRIVATE);
            fos.write(translationNewLine.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the header of the language translation file, the language code.
     * @param context
     * @return
     */
    public static String getSavedTranslationLanguageCode(Context context) {
        String languageCode = getFileHeader(LANGUAGE_TRANSLATION_FILE_NAME, context);
        if(languageCode.length() != 0) {
            Log.e("Translation code", languageCode);
            return languageCode;

        } else {
            languageCode = "-1";
            return languageCode;
        }

    }

    /**
     * Returns an array representation of the translated main menu. I.E:
     * [0] = Scan
     * [1] = Last Scan
     * .
     * .
     * [4] = Help
     * @param context
     * @return
     */
    public static String[] getMainMenuTranslation(Context context) {
        String translation = getFileBody(LANGUAGE_TRANSLATION_FILE_NAME, context);
        String[] translationArray = translation.split("\n");
        if(translationArray.length == 5) {
            return translationArray;
        } else {
            translationArray = new String[1];
            translationArray[0] = "-1";
            return translationArray;
        }

    }

    public static String[] getIndexInverted(String[] scanIndex) {
        String[] showScansInverted;
        showScansInverted = new String[scanIndex.length];
        for(int i = 0; i < scanIndex.length; i++) {
            showScansInverted[i] = scanIndex[(scanIndex.length - 1) - i];
        }
        return showScansInverted;
    }
    ///


    /**
     * Saves a scanTextView string to file with the name SCAN#(number of total scans).
     * The date of the scanTextView followed by a blank line is inserted before the scanText.
     * After creating the file, the name of the file is appended to the scanTextView index on a new line.
     *
     * @param scanText
     * @param context
     */
    @Deprecated
    public static void saveScanOld(String scanText, Context context) {
        //eraseFilesAndPref(context);
        //toastStoredFiles(context);
        //getIndexArray(context);
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        int scanTotalNum = prefs.getInt(SharedPreferencesIO.KEY_SCAN_TOTAL, 0);
        String fileName = SCAN_FILE_NAME + String.valueOf(scanTotalNum);
        String fileData = fileName + " DATE: " + getDateString() + "\n\n" + scanText;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(fileData.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SharedPreferences.Editor editor = prefs.edit();
            int newScanTotalNum = scanTotalNum + 1;
            editor.putInt(SharedPreferencesIO.KEY_SCAN_TOTAL, newScanTotalNum);
            editor.commit();
            updateIndex(fileName, context);
        }
    }


    /**
     * All scanTextView file names are stored in an index file named SCAN_FILE_INDEX_NAME
     * @param fileName file name to be put in index
     * @param context
     */
    @Deprecated
    private static void updateIndex(String fileName,  Context context) {
        FileOutputStream fos = null;
        String fileNameNewLine = "";

        fileNameNewLine = fileName + "\n";
        /*if(!getIndexArray(context)[0].equals("")) {
            fileNameNewLine = "\n" + fileName;
        } else {
            //dont add \n if no scanTextView in index
            fileNameNewLine = fileName;
        }*/

        try {
            fos = context.openFileOutput(SCAN_FILE_INDEX_NAME, Context.MODE_APPEND);
            fos.write(fileNameNewLine.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /*SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
            int indexSize = prefs.getInt(SharedPreferencesIO.SCAN_INDEX_SIZE, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SharedPreferencesIO.SCAN_INDEX_SIZE, indexSize + 1);
            editor.commit();*/
        }
    }


    /**
     * Removes the scanTextView name from the index
     * @param scanName
     * @param context
     */
    public static void deleteScanInIndex(String scanName, Context context) {
        String indexData = readFile(context, SCAN_FILE_INDEX_NAME);
        String newIndexData = "";

        String[] index = getIndexArray(context);
        ArrayList<String> newIndex = new ArrayList<String>();

        for(int i = 0; i < index.length; i++) {
            if(!index[i].equals(scanName)) {
                newIndex.add(index[i] + "\n");
                /*
                if(i != 0) {
                    //newIndex.add("\n" + index[i]);

                } else {
                    newIndex.add(index[i]);
                }*/
            }
        }

        for(int i = 0; i < newIndex.size(); i++) {
            newIndexData = newIndexData + newIndex.get(i);
        }

        /*if(getIndexArray(context)[0].equals() ) {
            fileNameNewLine = "\n" + fileName;
        } else {
            //dont add \n if no scanTextView in index
            fileNameNewLine = fileName;
        }*/

        //one scan left
        //newIndexData = indexData.replace(scanName + "\n", "");
        //newIndexData = indexData.replace("\n" + scanName, "");

        //not sure if needed to delete index
        File scanIndex = new File(context.getFilesDir(), SCAN_FILE_INDEX_NAME);
        scanIndex.delete();

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(SCAN_FILE_INDEX_NAME, Context.MODE_PRIVATE);
            fos.write(newIndexData.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /*SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
            int indexSize = prefs.getInt(SharedPreferencesIO.SCAN_INDEX_SIZE, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SharedPreferencesIO.SCAN_INDEX_SIZE, indexSize - 1);
            editor.commit();*/
        }
    }

    /**
     * Reads the scan index into an array
     * @param context
     * @return the scan file names in an array
     */
    @Deprecated
    public static String[] getIndexArray(Context context) {
        String fileData = "";
        try {
            InputStream in = new BufferedInputStream(context.openFileInput(SCAN_FILE_INDEX_NAME));
            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer);
            in.close();
            fileData = writer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        String[] index;

        if(fileData.length() == 0) {
            index = new String[1];
            index[0] = "-1";
        }
        else {
            index = fileData.split("\n");
        }
        //Toast.makeText(context, "Index length: " + String.valueOf(index.length)+ "\n"+ index[0], Toast.LENGTH_LONG).show();
        return index;

    }

    /**
     * Returns the file header of a given file name
     * @param fileName
     * @param context
     * @return
     */
    public static String getFileHeader(String fileName, Context context) {
        String fileData = "";
        try {
            InputStream in = new BufferedInputStream(context.openFileInput(fileName));
            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer);
            in.close();
            fileData = writer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String fileHeader = fileData.split("\n\n", 2)[0];
        return fileHeader;
    }

    /**
     * Reads all file bodies into an array of Strings
     * @param fileNames
     * @param context
     * @return
     */
    public static String[] getFileBodies(String[] fileNames, Context context) {


        String[] allFileBody = new String[fileNames.length];

        for(int i = 0; i < fileNames.length; i++) {
            allFileBody[i] = getFileBody(fileNames[i], context);
        }

        return allFileBody;
    }

    /**
     * Strips the file header (ie name of scanTextView and the date) and returns a string of the body
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getFileBody(String fileName, Context context) {
        String fileData = "";
        try {
            InputStream in = new BufferedInputStream(context.openFileInput(fileName));
            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer);
            in.close();
            fileData = writer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String fileBody = fileData.split("\n\n", 2)[1];

        if(isPrescriptionScanXML(fileBody)) {
            HashMap<String, String> parseXml = parseXMLQRScan(fileBody);
            return getScanInfoToDisplay(parseXml);

        } else {
            return fileBody;
        }
    }

    private static boolean isPrescriptionScanXML(String fileBody) {
        return fileBody.startsWith("<prescription>");
    }
    /**
     * XML format:
     *<prescription>
     *  <pat_name></>
     *  <drug_name></>
     *  <exp_instruc></>
     *  <script_id></>
     *  <pharm_name></>
     *<prescription/>
     *
     * Returns a formatted string containing patient name, drug name and instructions
     * @param xml
     */
    private static HashMap<String,String> parseXMLQRScan(String xml) {
        HashMap<String, String> prescriptionHashMap = new HashMap<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            while(parser.getEventType()!= XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    if (parser.getName().equals(PAT_NAME)) {
                        prescriptionHashMap.put(PAT_NAME, parser.nextText());
                    } else if (parser.getName().equals(DRUG_NAME)) {
                        prescriptionHashMap.put(DRUG_NAME, parser.nextText());

                    } else if (parser.getName().equals(EXP_INSTRUC)) {
                        prescriptionHashMap.put(EXP_INSTRUC, parser.nextText());

                    } else if (parser.getName().equals(SCRIPT_ID)) {
                        prescriptionHashMap.put(SCRIPT_ID, parser.nextText());

                    } else if (parser.getName().equals(PHARM_NAME)) {
                        prescriptionHashMap.put(PHARM_NAME, parser.nextText());
                    }
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prescriptionHashMap;
    }

    private static String getScanInfoToDisplay(HashMap<String, String> parseXml) {
        return parseXml.get(DRUG_NAME).replace("+", " ") + "\n\n" + parseXml.get(PAT_NAME) + ".\n" + parseXml.get(EXP_INSTRUC) +".";
    }


    /**
     * Prints a toast to screen of the contents of the file name provided
     * and returns the file data as a string.
     * @param context
     * @param fileName
     * @return the file data
     */
    public static String readFile(Context context, String fileName) {
        String fileData = "";
        try {
            InputStream in = new BufferedInputStream(context.openFileInput(fileName));
            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer);
            fileData = writer.toString();
            //Toast.makeText(context, fileData, Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }

    /**
     * Prints a toast of all the stored files that are saved in this apps /data/data/[app name] directory.
     * Also returns all files stored as a string.
     * @param context
     */
    public static String toastStoredFiles(Context context) {
        /*String dirPath = context.getFilesDir().getAbsolutePath() + File.separator + "Files";
        File projectDir = new File(Environment.getDataDirectory().getPath());
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }*/
        File[] f = context.getFilesDir().listFiles();
        String files = "";
        for(int i = 0; i < f.length; i++) {
            files = files + "\n" + f[i].getAbsolutePath().split("files/")[1];
        }
        Toast.makeText(context, files, Toast.LENGTH_LONG).show();

        return files;
    }






    /**
     * Deletes all stored preferences and files for the app
     * @param context
     */
    public static void eraseFilesAndPref(Context context) {
        File[] f =  context.getFilesDir().listFiles();

        for(int i = 0; i < f.length; i++) {
            f[i].delete();
        }

        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SharedPreferencesIO.KEY_SCAN_TOTAL, 0);
        /*editor.putInt(SharedPreferencesIO.SCAN_INDEX_SIZE, 0);*/
        editor.commit();
    }
}
