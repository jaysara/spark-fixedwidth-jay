package com.fixedwidth.jay.x.extlib;

import com.fixedwidth.jay.x.utils.SparkColumn;

import java.io.Serializable;


public class Metro2Segment implements Serializable {
    // private static transient Logger log = LoggerFactory.getLogger(Metro2Segment.class);
  
    private byte[] recordBytes;
    int startOffset;
    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    int length;

    @SparkColumn(name = "segment")
    public byte[] getRecordBytes() {
        return recordBytes;
    }

    public void setRecordBytes(byte[] recordBytes) {
        this.recordBytes = recordBytes;
    }
}
