package au.com.ourpillstalk.ourpillstalk;


import android.content.Context;
import android.content.SharedPreferences;
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

//convert doctor abbreviations
//capture number of repeats, valid to, when can get another script.
//
public class FileIO {
    public static final String SCAN_FILE_INDEX_NAME = "SCAN_INDEX";
    public static final String SCAN_FILE_NAME = "SCAN#";

    public static final String MAIN_MENU_TRANSLATION_FILE_NAME = "MAIN_MENU_TRANSLATION";


    public static final String AVAILABLE_LANGUAGES_FILE_NAME = "AVAILABLE_LANGUAGES";
    public static final String AVAILABLE_LANGUAGES_CODES_FILE_NAME = "AVAILABLE_LANGUAGES_CODES";
    public static final String USER_INFORMATION_FILE_NAME = "USER_INFORMATION";

    static final String SCAN_NUM_TAG = "scan_num";
    static final String DATE_TAG = "date";

    static final String PRESCRIPTION_TAG = "prescription";
    static final String PAT_NAME_TAG = "pat_name";
    static final String DRUG_NAME_TAG = "drug_name";
    static final String EXP_INSTRUC_TAG = "exp_instruc";
    static final String SCRIPT_ID_TAG = "script_id";
    static final String PHARM_NAME_TAG = "pharm_name";
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
            String title = "";
            switch(i) {
                case 0: title = "name:                    "; break;
                case 1: title = "city/suburb:          "; break;
                case 2: title = "state:                     "; break;
                case 3: title = "contact:                 "; break;
                case 4: title = "medications:        "; break;
                case 5: title = "allergies:               "; break;
            }
            body = body + title + userInfo[i] +"\n";
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
            HashMap<String, String> parseXml = getXMLQRScanMap(body);
            processedBody = getScanInfoToDisplay(parseXml);
            //Toast.makeText(context, body, Toast.LENGTH_SHORT).show();
        } else {
            processedBody = body;
        }

        return (header + "\n\n" + processedBody);


    }

    /*public static void deleteOldScans(int monthsOldLimit, Context context) {
        String[] index = getIndexArray(context);
        for(int i = 0 ; i < index.length; i++) {
            if(getScanFileAge(index[i], context) > monthsOldLimit) {
                deleteScanInIndex(index[i], context);
            }
        }
    }
    private static int getScanFileAge(String fileName, Context context) {
        String header = getFileHeader(fileName, context);

        String fileDate = header.split("_", 2)[1].split("\n", 2)[0];
        int fileMonth = Integer.getInteger(fileDate.split(" ", 2)[1].split("/", 3)[1]);
        int fileYear = Integer.getInteger(fileDate.split(" ", 2)[1].split("/", 3)[2]);

        String currentDate = getDateString();
        int currentMonth = Integer.getInteger(currentDate.split(" ", 2)[1].split("/", 3)[1]);
        int currentYear = Integer.getInteger(currentDate.split(" ", 2)[1].split("/", 3)[2]);

        int monthsOld = (currentYear - fileYear)*12 + (currentMonth - fileMonth);

        return monthsOld;
    }*/
    public static void deleteScanIndex(Context context) {
        String[] index = getIndexArray(context);
        for(int i = 0; i < index.length; i++) {
            deleteFile(index[i], context);
        }
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


        if(isPrescriptionScanXML(scanText)) {
            insertScanMetaInfo(scanText, String.valueOf(scanTotalNum), getDateString());
        }
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

    private static String insertScanMetaInfo(String scanData, String scanNum, String dateString) {
        //return "<" + PRESCRIPTION_TAG + "><" + SCAN_NUM_TAG + ">" + scanNum + "</" + SCAN_NUM_TAG + ">" + "<" + DATE_TAG + ">" + dateString + "</" + DATE_TAG + ">" + scanData.split("<prescription>", 2)[1];
        return scanData;
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
     *<main_menu_translation>
     *     <lang_code></lang_code>
     *     <scan_my_pills></scan_my_pills>
     *     <scan_history></scan_history>
     *     <settings></settings>
     *     <emergency></emergency>
     *     <help></help>
     *</main_menu_translation>
     * @param translation array of length 5
     * @param languageCode
     * @param context
     */
    public static  void saveMainMenuTranslation(String[] translation, String languageCode, Context context) {

        /*String langCode = "<lang_code>" + languageCode + "</lang_code>";
        String scanMyPills = "<scan_my_pills>" + translation[0] + "</scan_my_pills>";
        String scanHistory = "<scan_history>" + translation[1] + "</scan_history>";
        String settings = "<settings>" + translation[2] + "</settings>";
        String emergency = "<emergency>" + translation[3] + "</emergency>";
        String help = "<help>" + translation[4] + "</help>";
        String fileData = "<main_menu_translation>" + langCode + scanMyPills+ scanHistory + settings + emergency + help + "</main_menu_translation>";

        writeToFile(MAIN_MENU_TRANSLATION_FILE_NAME, fileData, context);*/

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

        try {
            fos = context.openFileOutput(MAIN_MENU_TRANSLATION_FILE_NAME, Context.MODE_PRIVATE);
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
   public static String getSavedMainTranslationLanguageCode(Context context) {
        String languageCode = getFileHeader(MAIN_MENU_TRANSLATION_FILE_NAME, context);
        if(languageCode.length() != 0) {
            return languageCode;

        } else {
            languageCode = "-1";
            return languageCode;
        }

    }

    /*public static HashMap<String, String> getXmlHashMap(String fileName, Context context) {
        HashMap<String, String> xmlHashMap = new HashMap<>();
        String fileData = readFile(context, fileName);
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(fileData));
            while(parser.getEventType()!= XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    if (parser.getEventType() == XmlPullParser.TEXT){
                        xmlHashMap.put(parser.getName(), parser.nextText());
                    }
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlHashMap;
    }*/
    /**
     * Returns an array representation of the translated main menu. I.E:
     * [0] = Scan
     * [1] = Last Scan
     * .
     * .
     * [4] = Help
     * @param
     * @return
     */
    public static String[] getMainMenuTranslation(Context context) {
        String translation = getFileBody(MAIN_MENU_TRANSLATION_FILE_NAME, context);
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
            }
        }

        for(int i = 0; i < newIndex.size(); i++) {
            newIndexData = newIndexData + newIndex.get(i);
        }

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

        }

        deleteFile(scanName, context);
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
            HashMap<String, String> parseXml = getXMLQRScanMap(fileBody);
            return getScanInfoToDisplay(parseXml);

        } else {
            return fileBody;
        }
    }

    public static boolean isPrescriptionScanXML(String fileBody) {
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
    private static HashMap<String,String> getXMLQRScanMap(String xml) {
        HashMap<String, String> prescriptionHashMap = new HashMap<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            while(parser.getEventType()!= XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    if (parser.getName().equals(SCAN_NUM_TAG)) {
                        prescriptionHashMap.put(SCAN_NUM_TAG, parser.nextText());
                    } else if (parser.getName().equals(DATE_TAG)) {
                        prescriptionHashMap.put(DATE_TAG, parser.nextText());
                    } else if (parser.getName().equals(PAT_NAME_TAG)) {
                        prescriptionHashMap.put(PAT_NAME_TAG, parser.nextText());
                    } else if (parser.getName().equals(DRUG_NAME_TAG)) {
                        prescriptionHashMap.put(DRUG_NAME_TAG, parser.nextText());
                    } else if (parser.getName().equals(EXP_INSTRUC_TAG)) {
                        prescriptionHashMap.put(EXP_INSTRUC_TAG, parser.nextText());
                    } else if (parser.getName().equals(SCRIPT_ID_TAG)) {
                        prescriptionHashMap.put(SCRIPT_ID_TAG, parser.nextText());
                    } else if (parser.getName().equals(PHARM_NAME_TAG)) {
                        prescriptionHashMap.put(PHARM_NAME_TAG, parser.nextText());
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
        return parseXml.get(DRUG_NAME_TAG).replace("+", " ") + "\n\n" + parseXml.get(PAT_NAME_TAG) + ".\n" + parseXml.get(EXP_INSTRUC_TAG) +".";
    }

    public static String getDrugName(String fileName, Context context) {
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
             return getXMLQRScanMap(fileBody).get(DRUG_NAME_TAG).replace("+", " ").split(" ")[0];
        } else {
            return "";
        }

       // String filePlain = getFileBody(fileName, context);
        //String drugName =  filePlain.split("\n\n")[0];
        //return drugName;
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

    public static String readFile(Context context, String fileName, boolean stripHead) {
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

        if(stripHead && fileData.contains("\n\n")) {
            return fileData.split("\n\n", 2)[1];
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
