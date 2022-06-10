package com.ifnoelse.pdf;

import com.ifnoelse.pdf.bookmark.BlankAsLevelBookmarkGenerator;
import com.ifnoelse.pdf.bookmark.Bookmark;
import com.ifnoelse.pdf.bookmark.SeqAsLevelBookmarkGenerator;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ifnoelse on 2017/2/25 0025.
 */
public class PDFUtil {

    public static void addBookmark(GeneratingRequest req) throws Exception {
        String content = req.getText();
        if (content.trim().startsWith("http")) {
            req.setText(PDFContents.getContentsByUrl(content));
        }

        List<Bookmark> bookmarks;
        if (req.isByBlank()) {
            bookmarks = new BlankAsLevelBookmarkGenerator().generateBookmark(req);
        } else {
            bookmarks = new SeqAsLevelBookmarkGenerator().generateBookmark(req);
        }
        List<HashMap<String, Object>> maps = bookmarks.stream()
                .map(Bookmark::outlines)
                .collect(Collectors.toList());
        addOutlines(req, maps);
        req.setAlert(XAlert.OK.apply(req.getDest().getAbsolutePath()));
    }

    static void addOutlines(GeneratingRequest req, List<HashMap<String, Object>> outlines) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(req.getSrc().getAbsolutePath());
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(req.getDest()));
        stamper.setOutlines(outlines);
        stamper.close();
    }

    static {
        PdfReader.unethicalreading = true;
    }

}
