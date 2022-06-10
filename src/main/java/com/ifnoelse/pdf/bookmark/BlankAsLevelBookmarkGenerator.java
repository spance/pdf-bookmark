package com.ifnoelse.pdf.bookmark;

import com.ifnoelse.pdf.GeneratingRequest;

import java.util.ArrayList;
import java.util.List;

public class BlankAsLevelBookmarkGenerator implements BookmarkGenerator {

    final MenuParser parser = new MenuParser();

    @Override
    public List<Bookmark> generateBookmark(GeneratingRequest req) {
        List<Bookmark> bookmarks = new ArrayList<>();
        Bookmark lastRoot = null;
        for (String ln : req.readByRow()) {
            MenuParser.Result res = parser.parse(ln);
            if (res.level == 0) {
                lastRoot = new Bookmark(res.level, res.getTitle(), res.getPage());
                bookmarks.add(lastRoot);
            } else {
                if (lastRoot == null) throw new IllegalStateException("bad indent");
                Bookmark parent = lastRoot;
                for (int i = 1; i < res.level; i++) {
                    List<Bookmark> children = parent.getSubBookMarks();
                    parent = children.get(children.size() - 1);
                }
                parent.addSubBookMark(new Bookmark(res.level, res.getTitle(), res.getPage()));
            }
        }
        return bookmarks;
    }
}
