package com.ifnoelse.pdf.bookmark;

import com.ifnoelse.pdf.GeneratingRequest;

import java.util.List;

public interface BookmarkGenerator {

    List<Bookmark> generateBookmark(GeneratingRequest req);
}
