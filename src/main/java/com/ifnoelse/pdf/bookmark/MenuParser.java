package com.ifnoelse.pdf.bookmark;

import lombok.AllArgsConstructor;
import lombok.Data;

public class MenuParser {

    @Data
    @AllArgsConstructor
    static class Result {
        final int level;
        final String title;
        final int page;
    }

    public Result parse(String line) {
        int i = 0;
        int j = line.length() - 1;
        levels:
        for (; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case ' ':
                case '\t':
                    continue;
                default:
                    break levels;
            }
        }
        for (int jj = j; jj > i; jj--) {
            char ch = line.charAt(jj);
            if (!Character.isDigit(ch)) {
                j = jj;
                break;
            }
        }

        String title = line.substring(i, j + 1);
        int no = Integer.parseInt(line.substring(j + 1));
        return new Result(i, title, no);
    }
}
