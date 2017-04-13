package com.dev.lakik.expresspos.Model;

/**
 * Created by ppash on 13.04.2017.
 */

public class Credit {

    public static enum Source {GITHUB, STACKOVERFLOW};

    private Source source;
    private String title;
    private String description;
    private String url;

    public Credit(Source source, String title, String description, String url) {
        this.source = source;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
