package com.ifnoelse.pdf.remote;

import com.ifnoelse.pdf.PDFContents;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChinaPubContentProviderTest {

    @Test
    public void testFetch() {
        System.out.println(PDFContents.getContentsByUrl("http://product.china-pub.com/3684420"));
    }
}