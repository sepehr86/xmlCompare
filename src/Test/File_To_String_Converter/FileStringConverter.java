package Test.File_To_String_Converter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileStringConverter {

    private File expectedFile;
    private File resultFile;
    private String expectedFileString;
    private String resultFileString;

    /*
    Get the resultFile and expectedFile and convert them to string
     */
    public void FileStringConverter(String expectedPath, String resultPath) throws IOException {

        expectedFile = new File(expectedPath);
        resultFile = new File(resultPath);

        expectedFileString = FileUtils.readFileToString(expectedFile, "UTF-8");
        expectedFileString = expectedFileString.replace("\n", "");
        expectedFileString = expectedFileString.replace("\r", "");
        expectedFileString = expectedFileString.replace(" ", "");

        resultFileString = FileUtils.readFileToString(resultFile, "UTF-8");
        resultFileString = resultFileString.replace("\n", "");
        resultFileString = resultFileString.replace("\r", "");
        resultFileString = resultFileString.replace(" ", "");
    }

    public File getExpectedFile() {
        return expectedFile;
    }


    public File getResultFile() {
        return resultFile;
    }


    public String getExpectedFileString() {
        return expectedFileString;
    }


    public String getResultFileString() {
        return resultFileString;
    }


}
