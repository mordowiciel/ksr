package main.featureExtraction;

import java.util.Iterator;

class NGramIterator implements Iterator<String> {

    private final String str;
    private final int n;
    int pos = 0;

    public NGramIterator(int n, String str) {
        this.n = n;
        this.str = str;
    }

    public boolean hasNext() {
        return pos < str.length() - n + 1;
    }

    public String next() {
        return str.substring(pos, pos++ + n);
    }
}
