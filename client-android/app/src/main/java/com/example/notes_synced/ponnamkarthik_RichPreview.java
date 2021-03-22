package com.example.notes_synced;

import android.os.AsyncTask;
import android.webkit.URLUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// stolen from https://github.com/ponnamkarthik/RichLinkPreview/blob/master/richlinkpreview/src/main/java/io/github/ponnamkarthik/richlinkpreview/RichPreview.java

public class ponnamkarthik_RichPreview {

    ponnamkarthik_MetaData ponnamkarthikMetaData;
    ponnamkarthik_ResponseListener ponnamkarthikResponseListener;
    String url;

    public ponnamkarthik_RichPreview(ponnamkarthik_ResponseListener ponnamkarthikResponseListener) {
        this.ponnamkarthikResponseListener = ponnamkarthikResponseListener;
        ponnamkarthikMetaData = new ponnamkarthik_MetaData();
    }

    public void getPreview(String url){
        this.url = url;
        new getData().execute();
    }

    private class getData extends AsyncTask<Void , Void , Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect(url)
                        .timeout(30*1000)
                        .get();

                Elements elements = doc.getElementsByTag("meta");

                // getTitle doc.select("meta[property=og:title]")
                String title = doc.select("meta[property=og:title]").attr("content");

                if(title == null || title.isEmpty()) {
                    title = doc.title();
                }
                ponnamkarthikMetaData.setTitle(title);

                //getDescription
                String description = doc.select("meta[name=description]").attr("content");
                if (description.isEmpty() || description == null) {
                    description = doc.select("meta[name=Description]").attr("content");
                }
                if (description.isEmpty() || description == null) {
                    description = doc.select("meta[property=og:description]").attr("content");
                }
                if (description.isEmpty() || description == null) {
                    description = "";
                }
                ponnamkarthikMetaData.setDescription(description);


                // getMediaType
                Elements mediaTypes = doc.select("meta[name=medium]");
                String type = "";
                if (mediaTypes.size() > 0) {
                    String media = mediaTypes.attr("content");

                    type = media.equals("image") ? "photo" : media;
                } else {
                    type = doc.select("meta[property=og:type]").attr("content");
                }
                ponnamkarthikMetaData.setMediatype(type);


                //getImages
                Elements imageElements = doc.select("meta[property=og:image]");
                if(imageElements.size() > 0) {
                    String image = imageElements.attr("content");
                    if(!image.isEmpty()) {
                        ponnamkarthikMetaData.setImageurl(resolveURL(url, image));
                    }
                }
                if(ponnamkarthikMetaData.getImageurl().isEmpty()) {
                    String src = doc.select("link[rel=image_src]").attr("href");
                    if (!src.isEmpty()) {
                        ponnamkarthikMetaData.setImageurl(resolveURL(url, src));
                    } else {
                        src = doc.select("link[rel=apple-touch-icon]").attr("href");
                        if(!src.isEmpty()) {
                            ponnamkarthikMetaData.setImageurl(resolveURL(url, src));
                            ponnamkarthikMetaData.setFavicon(resolveURL(url, src));
                        } else {
                            src = doc.select("link[rel=icon]").attr("href");
                            if(!src.isEmpty()) {
                                ponnamkarthikMetaData.setImageurl(resolveURL(url, src));
                                ponnamkarthikMetaData.setFavicon(resolveURL(url, src));
                            }
                        }
                    }
                }

                //Favicon
                String src = doc.select("link[rel=apple-touch-icon]").attr("href");
                if(!src.isEmpty()) {
                    ponnamkarthikMetaData.setFavicon(resolveURL(url, src));
                } else {
                    src = doc.select("link[rel=icon]").attr("href");
                    if(!src.isEmpty()) {
                        ponnamkarthikMetaData.setFavicon(resolveURL(url, src));
                    }
                }

                for(Element element : elements) {
                    if(element.hasAttr("property")) {
                        String str_property = element.attr("property").toString().trim();
                        if(str_property.equals("og:url")) {
                            ponnamkarthikMetaData.setUrl(element.attr("content").toString());
                        }
                        if(str_property.equals("og:site_name")) {
                            ponnamkarthikMetaData.setSitename(element.attr("content").toString());
                        }
                    }
                }

                if(ponnamkarthikMetaData.getUrl().equals("") || ponnamkarthikMetaData.getUrl().isEmpty()) {
                    URI uri = null;
                    try {
                        uri = new URI(url);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    if(url == null) {
                        ponnamkarthikMetaData.setUrl(url);
                    } else {
                        ponnamkarthikMetaData.setUrl(uri.getHost());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                ponnamkarthikResponseListener.onError(new Exception("No Html Received from " + url + " Check your Internet " + e.getLocalizedMessage()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ponnamkarthikResponseListener.onData(ponnamkarthikMetaData);
        }
    }

    private String resolveURL(String url, String part) {
        if(URLUtil.isValidUrl(part)) {
            return part;
        } else {
            URI base_uri = null;
            try {
                base_uri = new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            base_uri = base_uri.resolve(part);
            return base_uri.toString();
        }
    }

}