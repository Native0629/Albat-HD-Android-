package com.albat.mobachir.attachmentviewer.model;

/**
 * This file is part of the Universal template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */

public class MediaAttachment extends Attachment {

    private String url;
    private String thumburl;
    private String description;
    private String mime;

    //Mimes
    public static String MIME_PATTERN_IMAGE = "image/";
    public static String MIME_PATTERN_VID = "video/";
    public static String MIME_PATTERN_AUDIO = "audio/";

    public MediaAttachment(String url, String mime, String thumbnailUrl, String description){
        if (description != null && !description.equals("")){
            this.description = description;
        }

        this.mime = mime;
        this.url = url;
        this.thumburl = thumbnailUrl;
    }

    public static MediaAttachment withImage(String url){
        return new MediaAttachment(url, MIME_PATTERN_IMAGE, null, null);
    }

    public static MediaAttachment withVideo(String url){
        return new MediaAttachment(url, MIME_PATTERN_VID, null, null);
    }

    public static MediaAttachment withAudio(String url){
        return new MediaAttachment(url, MIME_PATTERN_AUDIO, null, null);
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumburl;
    }

    public String getMime() {
        return mime;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return "Url: " + url + " Mime: " +  mime;
    }

}
