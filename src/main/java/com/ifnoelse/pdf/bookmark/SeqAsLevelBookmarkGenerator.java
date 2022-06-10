package com.ifnoelse.pdf.bookmark;

import com.ifnoelse.pdf.GeneratingRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeqAsLevelBookmarkGenerator implements BookmarkGenerator {

    private static Pattern bookmarkPattern = Pattern.compile("^[\t\\s　]*?([0-9.]+)?(.*?)/?[\t\\s　]*([0-9]+)[\t\\s　]*?$");
    private static String blankRegex = "[\t\\s　]+";

    public static String replaceBlank(String str) {
        return str.replaceAll(blankRegex, " ").trim();
    }

    /**
     * Add a directory to the pdf file
     *
     * @param bookmarks       Directory content, each list element is a directory content, such as：“1.1 Functional vs. Imperative Data Structures 1”
     * @param pageIndexOffset The pdf file is really the offset between the page number and the directory page number.
     * @param minLens         Legal directory entry minimum length
     * @param maxLens         Legal directory entry maximum length
     * @return Returns a list of bookmarked content
     */
    @Override
    public List<Bookmark> generateBookmark(GeneratingRequest req) {
        List<Bookmark> bookmarkList = new ArrayList<>();
        for (String ln : req.readByRow()) {
            ln = replaceBlank(ln);
            Matcher matcher = bookmarkPattern.matcher(ln);
            if (matcher.find()) {
                String seq = matcher.group(1);
                String title = replaceBlank(matcher.group(2));
                int pageIndex = Integer.parseInt(matcher.group(3));
                if (seq != null && bookmarkList.size() > 0) {
                    Bookmark pre = bookmarkList.get(bookmarkList.size() - 1);
                    if (pre.getSeq() == null || seq.startsWith(pre.getSeq())) {
                        pre.addSubBookMarkBySeq(new Bookmark(seq, title, pageIndex + req.getOffset()));
                    } else {
                        bookmarkList.add(new Bookmark(seq, title, pageIndex + req.getOffset()));
                    }
                } else {
                    bookmarkList.add(new Bookmark(seq, title, pageIndex + req.getOffset()));
                }

            } else {
                bookmarkList.add(new Bookmark(replaceBlank(ln)));
            }
        }
        return bookmarkList;
    }
}
