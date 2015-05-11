# OurPillsTalk
The Our Pills Talk source files relating to COMP355 project. Branches exist for each developer assigned to the project.

Some brief information about classes;

Activities:
MainActivity
ScanActivity
ScanHistoryActivity
HelpActivity
SettingsActivity
EmergencyActivity
ZBarScannerActivity - the QR scanner

Input/Output (use these classes for all input/output, if you need to create new files or preference types create methods in these classes):
FileIO - used for saving files, getting files and various helper functions to do with fileIO.
Important methods:
saveQRScan(String scanText, Context context) - save QR Scan result, file names are automatically appended to the scan index
getIndexArray(Context context) - returns the scan file names for all undeleted scan files on the phone as an array
getCompleteScanFile(String fileName, boolean stripScanName, Context context) - returns the file as a string given the file name

SharedPreferenceIO - saving preferences ie toggle hide duplicate scans, language choice etc.

NOTE:
There are some private classes in the activities that deal with creating lists, spinners (Adaptors) and translation (Async).
They are mainly private classes to simplify the apps class structure and to minimise confusion. 
