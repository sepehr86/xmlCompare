package com.company.IosVersion;

import java.io.*;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.*;

public class IosFormatCompare {

    private Map<String, String> fileToMap(String path) throws IOException {
        List<String> lineArray = readFileToLineArray(path);
        HashMap<String, String> map = new HashMap<>();
        for (String line : lineArray) {
            String key = "", value = "";
            boolean isKey = true;
            for (char c : line.toCharArray()) {
                if (isKey) {
                    if (c != '=') {
                        key += c;
                    } else if (c == '=') {
                        isKey = false;
                    }
                } else {
                    value += c;
                }
            }
            map.put(key, value);
        }
        return map;
    }


    private List<String> readFileToLineArray(String f) throws IOException {

        List<String> lineArr = new ArrayList<>();
        Scanner scanner = new Scanner(Paths.get(f), "UTF16");
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            lineArr.add(line);

        }

        scanner.close();
        return lineArr;
    }

    public void compareIosFiles(String legacy, String localiZable, File result) throws IOException {

        Map<String, String> mapOfLegacy = fileToMap(legacy);
        Map<String, String> mapOfLocalizable = fileToMap(localiZable);
        List<String> bb = new ArrayList<>();
        List<String> noneExistElementsInLocalizable = new ArrayList<>();
        List<String> noneExistElementInLegacy = new ArrayList<>();
        List<String> removedlist = new ArrayList<>();
        List<String> sameKeyDifValue = new ArrayList<>();

        for (String map : mapOfLegacy.keySet()) {

            String localivalue = mapOfLocalizable.get(map);
            String LegacyValue = mapOfLegacy.get(map);
            if (localivalue == null || !LegacyValue.equals(localivalue)) {
                bb.add(map + "=" + LegacyValue);
                Collections.sort(bb, Collator.getInstance());
                System.out.println("same key is: " + map + "\n value of LegacyFile is: " + LegacyValue + "\n" + "Value of LocalizableFile is: " + localivalue);
                System.out.println("----------------");

            }
            if (localivalue == null) {
                noneExistElementsInLocalizable.add(map + "=" + LegacyValue);
            }
            if (localivalue != null && LegacyValue.equals(localivalue)) {
                if(map==""){
                    continue;
                }
                removedlist.add(map + "=" + LegacyValue);
            }
            if (localivalue!= null && !LegacyValue.equals(localivalue)){
                sameKeyDifValue.add(map + "=" +LegacyValue);
            }
        }
        if (result.exists()) {
            result.delete();
        }
        for (String localiKey : mapOfLocalizable.keySet()) {
            String legacyValue = mapOfLegacy.get(localiKey), LocaliValue = mapOfLocalizable.get(localiKey);

            if (legacyValue == null) {

                noneExistElementInLegacy.add(localiKey + "=" + LocaliValue);
            }

        }

        FileWriter removedFileWriter = new FileWriter("c:/dev/result/IosRemovedLog.strings", true);
        FileWriter log1writer = new FileWriter("c:/dev/result/noneExistElementInLegacy.strings", true);
        FileWriter writer = new FileWriter(result, true);
        FileWriter logWriter = new FileWriter("c:/dev/result/noneExistElementsInLocalizable.strings", true);
        FileWriter sameKeyDifValuewriter = new FileWriter("c:/dev/result/SameKeyDifValue.strings",true);

        for (String str : bb) {

            writer.write(str + "\n" + System.getProperty("line.separator", "\r\n"));

        }
        for (String str1 : noneExistElementsInLocalizable) {
            logWriter.write(str1 + "\n" + System.getProperty("line.separator", "\r\n"));
        }
        for (String str2 : noneExistElementInLegacy) {
            log1writer.write(str2 + "\n" + System.getProperty("line.separator", "\r\n"));
        }
        for (String str3:removedlist) {
            removedFileWriter.write(str3+"\n" + System.getProperty("line.separator", "\r\n"));
        }
        for (String str4:sameKeyDifValue) {
            sameKeyDifValuewriter.write(str4+"\n" + System.getProperty("line.separator","\r\n"));
        }

        writer.close();
        logWriter.close();
        log1writer.close();
        removedFileWriter.close();
        sameKeyDifValuewriter.close();
    }


}









