package com.albat.mobachir.attachmentviewer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.albat.mobachir.R;
import com.albat.mobachir.attachmentviewer.loader.DefaultAudioLoader;
import com.albat.mobachir.attachmentviewer.loader.DefaultFileLoader;
import com.albat.mobachir.attachmentviewer.loader.DefaultVideoLoader;
import com.albat.mobachir.attachmentviewer.loader.MediaLoader;
import com.albat.mobachir.attachmentviewer.loader.PicassoImageLoader;
import com.albat.mobachir.attachmentviewer.model.Attachment;
import com.albat.mobachir.attachmentviewer.model.MediaAttachment;
import com.albat.mobachir.attachmentviewer.widgets.ScrollGalleryView;
import com.albat.mobachir.util.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This file is part of the Modulio template
 * For license information, please check the LICENSE
 * file in the root of this project
 *
 * @author Sherdle
 * Copyright 2017
 */
public class AttachmentActivity extends AppCompatActivity {

    private ScrollGalleryView scrollGalleryView;
    private List<MediaLoader> mediaList;

    public static String IMAGES = "images";

    public static void startActivity(Context source, MediaAttachment image){
        Intent intent = new Intent(source, AttachmentActivity.class);
        intent.putExtra(IMAGES, new ArrayList<>(Collections.singleton(image)));
        source.startActivity(intent);
    }

    public static void startActivity(Context source, ArrayList<MediaAttachment> images){
        Intent intent = new Intent(source, AttachmentActivity.class);
        intent.putExtra(IMAGES, images);
        source.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Helper.setStatusBarColor(this, R.color.black);

        ArrayList<Attachment> images = (ArrayList<Attachment>) getIntent().getSerializableExtra(IMAGES);
        scrollGalleryView = findViewById(R.id.scroll_gallery_view);

        Helper.admobLoader(this, findViewById(R.id.adView));

        mediaList = easyInitView(scrollGalleryView, images, getSupportFragmentManager());
    }

    public static List<MediaLoader> easyInitView(ScrollGalleryView view, ArrayList<Attachment> images, FragmentManager fm){

        List<MediaLoader> infos = new ArrayList<>(images.size());
        for (Attachment attachment : images) {
            if (attachment instanceof MediaAttachment) {
                MediaAttachment mediaAttachment = ((MediaAttachment) attachment);
                if (mediaAttachment.getMime().contains(MediaAttachment.MIME_PATTERN_IMAGE))
                    infos.add(new PicassoImageLoader(mediaAttachment));
                else if (mediaAttachment.getMime().contains(MediaAttachment.MIME_PATTERN_VID))
                    infos.add(new DefaultVideoLoader(mediaAttachment));
                else if (mediaAttachment.getMime().contains(MediaAttachment.MIME_PATTERN_AUDIO))
                    infos.add(new DefaultAudioLoader(mediaAttachment));
                else
                    infos.add(new DefaultFileLoader(mediaAttachment));
            }
        }

        view.setThumbnailSize((int) view.getContext().getResources().getDimension(R.dimen.thumbnail_height))
                .setZoom(true)
                .setFragmentManager(fm)
                .addMedia(infos);

        if (infos.size() == 1){
            view.hideThumbnails(true);
        }

        return infos;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
