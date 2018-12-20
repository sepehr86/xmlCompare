package com.company;


import com.company.AndroidVersion.AndroidFormatCompare;
import com.company.IosVersion.IosFormatCompare;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Chose your file version to compare: ");
        System.out.println("1.Android version\n2.Ios version");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                System.out.println("Please enter the dictionary folder path: ");
                String DICTIONARY_FILE_PATH = scanner.nextLine();
                System.out.println("Please enter th Core folder path: ");
                String CORE_FILE_PATH = scanner.nextLine();
                System.out.println("Please enter the result path which the result going to be saved: ");
                String RESULT_FILE_NAME = scanner.nextLine();
                System.out.println("Please enter the logfile path where result going to be saved: ");
                String RESULT_LOGFILE_PATH = scanner.nextLine();

                AndroidFormatCompare androidFormatCompare = new AndroidFormatCompare();
                androidFormatCompare.compareXML(CORE_FILE_PATH, DICTIONARY_FILE_PATH, RESULT_FILE_NAME,RESULT_LOGFILE_PATH);
                break;
            case "2":
                System.out.println("Please enter the Legacy folder path: ");
                String legacyFile_PATH = scanner.nextLine();
                System.out.println("Please enter the Localizable folder path: ");
                String localizable_PATH = scanner.nextLine();
                System.out.println("Please enter the result path which the result going to be saved: ");
                String result_PATH = scanner.nextLine();

                IosFormatCompare iosCompare =new IosFormatCompare();
                iosCompare.compareIosFiles(legacyFile_PATH,localizable_PATH,new File(result_PATH));
                break;

        }

    }

}





