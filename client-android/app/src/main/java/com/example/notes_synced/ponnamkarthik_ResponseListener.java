package com.example.notes_synced;

// stolen from https://github.com/ponnamkarthik/RichLinkPreview/blob/master/richlinkpreview/src/main/java/io/github/ponnamkarthik/richlinkpreview/ResponseListener.java

public interface ponnamkarthik_ResponseListener {

    void onData(ponnamkarthik_MetaData ponnamkarthikMetaData);

    void onError(Exception e);
}