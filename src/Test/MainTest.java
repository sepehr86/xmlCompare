package Test;

import com.company.AndroidVersion.AndroidFormatCompare;
import Test.File_To_String_Converter.FileStringConverter;
import com.company.IosVersion.IosFormatCompare;
import junit.framework.TestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class MainTest extends TestCase {
    public static String dictFilePathPt, coreFilePathPt, resultFilePathPt, expectedResultFile;
    public static String legacyFilePath, localizableFilePath, iosResultFilePath, iosExpectedFIlePath,resultAndroidLogfile;
    public static AndroidFormatCompare c;
    public static FileStringConverter compare, converter;
    public static IosFormatCompare iosTestObject;


    @BeforeAll
    public static void setup() throws IOException {
        dictFilePathPt = "res/testfiles/test_file_3.xml";
        coreFilePathPt = "res/testfiles/test_file_4.xml";
        resultFilePathPt = "test_result.xml";
        expectedResultFile = "res/testFiles/ExpectedAndroidResultFile.xml";
        legacyFilePath = "res/testfiles/IosTestFile1.strings";
        localizableFilePath = "res/testfiles/IosTestFile2.strings";
        iosResultFilePath = "iosTestResult.strings";
        iosExpectedFIlePath = "res/testfiles/ExpectedIosRusultfile.strings";
        resultAndroidLogfile="res/testfiles/resultAndroidLogfile.xml";
        c = new AndroidFormatCompare();
        c.compareXML(coreFilePathPt, dictFilePathPt, resultFilePathPt,resultAndroidLogfile);
        compare = new FileStringConverter();
        converter = new FileStringConverter();
        iosTestObject = new IosFormatCompare();
        iosTestObject.compareIosFiles(legacyFilePath, localizableFilePath, new File(iosResultFilePath));

    }


    @AfterAll
    public static void teardown() {
        File output = new File("test_result.xml");
        output.delete();

    }


    @Test
    void main() {

        try {

            converter.FileStringConverter(iosResultFilePath, iosExpectedFIlePath);
            compare.FileStringConverter(resultFilePathPt, expectedResultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(compare.getExpectedFile().exists());
        assertTrue(compare.getResultFile().exists());
        assertTrue(compare.getExpectedFileString().equals(compare.getResultFileString()));
        assertTrue(converter.getExpectedFileString().equals(converter.getResultFileString()));

    }


}