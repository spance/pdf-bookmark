package com.ifnoelse.pdf;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class GeneratingRequest {

    private final static String NEW_FILE_SUFFIX = "_with-Menu";
    private XAlert alert;
    private String text;
    private final int offset;
    private final File src;
    private final File dest;
    private final boolean byBlank;

    public List<String> readByRow() {
        return Arrays.asList(text.split("\n"));
    }

    public static GeneratingRequest of(String path, String offsetText, String text, boolean byBlank) {
        String dest;
        int dot = path.lastIndexOf('.');
        if (dot > 0) {
            dest = path.substring(0, dot) + NEW_FILE_SUFFIX + path.substring(dot);
        } else {
            dest = path + NEW_FILE_SUFFIX;
        }

        XAlert alert = null;
        do {
            File srcFile = new File(path);
            File destFile = new File(dest);
            if (!srcFile.exists()) {
                alert = XAlert.MISS_PDF;
                break;
            }

            int offset = 0;
            if (!offsetText.isEmpty()) {
                try {
                    offset = Integer.parseInt(offsetText);
                } catch (NumberFormatException e) {
                    alert = XAlert.BAD_OFFSET;
                    break;
                }
            }

            text = text.trim();
            if (text.isEmpty()) {
                alert = XAlert.MISS_CONTENT;
                break;
            }
            return new GeneratingRequest(null, text, offset, srcFile, destFile, byBlank);
        } while (false);

        return new GeneratingRequest(alert, null, 0, null, null, false);
    }
}
