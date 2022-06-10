package com.ifnoelse.pdf;

import com.ifnoelse.pdf.bookmark.Bookmark;
import com.ifnoelse.pdf.bookmark.SeqAsLevelBookmarkGenerator;
import org.junit.Test;

import java.util.List;

public class PDFUtilTest {

    @Test
    public void testFetchChinaPub() {
        //Get catalog information for books
        String contents = PDFContents.getContentsByUrl("http://product.china-pub.com/223565");

        GeneratingRequest req = GeneratingRequest.of(".", "14", contents, false);

        //Add a table of contents to a book
        List<Bookmark> marks = new SeqAsLevelBookmarkGenerator().generateBookmark(req);
        System.out.println(marks);
    }

}