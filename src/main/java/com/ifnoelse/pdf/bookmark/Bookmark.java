package com.ifnoelse.pdf.bookmark;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ifnoelse on 2017/2/25 0025.
 */
@Data
public class Bookmark {
    private String seq;
    private int level;
    private int pageIndex = -1;
    private String title;
    private final List<Bookmark> subBookMarks = new ArrayList<>();

    public Bookmark(int level, String title, int pageIndex) {
        this.level = level;
        this.pageIndex = pageIndex;
        this.title = title;
    }

    public Bookmark(String seq, String title, int pageIndex) {
        this.pageIndex = pageIndex;
        this.title = title;
        this.seq = seq;
    }

    public Bookmark(String title) {
        this.title = title;
    }


    public void addSubBookMark(Bookmark kid) {
        subBookMarks.add(kid);
    }

    public void addSubBookMarkBySeq(Bookmark kid) {
        for (Bookmark bookmark : subBookMarks) {
            if (kid.getSeq().startsWith(bookmark.getSeq() + ".")) {
                bookmark.addSubBookMarkBySeq(kid);
                return;
            }
        }
        subBookMarks.add(kid);
    }


    public HashMap<String, Object> outlines() {
        HashMap<String, Object> root = new HashMap<>();
        root.put("Title", (getSeq() != null ? getSeq() + " " : "") + getTitle());
        root.put("Action", "GoTo");
        if (pageIndex >= 0)
            root.put("Page", String.format("%d Fit", pageIndex));

        List<HashMap<String, Object>> children = subBookMarks.stream().map(Bookmark::outlines).collect(Collectors.toList());
        root.put("Kids", children);

        return root;
    }

    @Override
    public String toString() {
        String indent = "- ";
        StringBuilder sb = new StringBuilder();
        if (getSeq() != null) {
            sb.append(getSeq());
            sb.append(" ");
        }

        sb.append(getTitle());

        if (getSubBookMarks() != null && !getSubBookMarks().isEmpty()) {
            for (Bookmark bookmark : getSubBookMarks()) {
                sb.append("\n");
                sb.append(indent);
                sb.append(bookmark.toString().replaceAll(indent, indent + indent));
            }
        }

        return sb.toString();
    }
}
