package com.example.notes_synced;

// stolen from https://github.com/ponnamkarthik/RichLinkPreview/blob/master/richlinkpreview/src/main/java/io/github/ponnamkarthik/richlinkpreview/ViewListener.java

public interface ponnamkarthik_ViewListener {

    void onSuccess(boolean status);

    void onError(Exception e);
}