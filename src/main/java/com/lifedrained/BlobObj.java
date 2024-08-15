package com.lifedrained;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Blob;

public class BlobObj implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name, pos;

    public BlobObj(String name, String pos) {
        this.name = name;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public String getPos() {
        return pos;
    }
}
