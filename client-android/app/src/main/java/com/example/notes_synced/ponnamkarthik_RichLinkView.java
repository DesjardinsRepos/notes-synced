package com.example.notes_synced;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
//import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ponna on 16-01-2018.
 */

public class ponnamkarthik_RichLinkView extends RelativeLayout {

    private View view;
    Context context;
    private ponnamkarthik_MetaData meta;

    LinearLayout linearLayout;
    ImageView imageView;
    TextView textViewTitle;
    TextView textViewDesp;
    TextView textViewUrl;

    private String main_url;

    private boolean isDefaultClick = true;

    private ponnamkarthik_RichLinkListener richLinkListener;


    public ponnamkarthik_RichLinkView(Context context) {
        super(context);
        this.context = context;
    }

    public ponnamkarthik_RichLinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ponnamkarthik_RichLinkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    /*
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RichLinkView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }*/

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    public void initView() {
        if(findLinearLayoutChild() != null) {
            this.view = findLinearLayoutChild();
        } else  {
            this.view = this;
            inflate(context, R.layout.ponnamkarthik_link_layout,this);
        }

        linearLayout = (LinearLayout) findViewById(R.id.rich_link_card);
        imageView = (ImageView) findViewById(R.id.rich_link_image);
        textViewTitle = (TextView) findViewById(R.id.rich_link_title);
        textViewDesp = (TextView) findViewById(R.id.rich_link_desp);
        textViewUrl = (TextView) findViewById(R.id.rich_link_url);


        if(meta.getImageurl().equals("") || meta.getImageurl().isEmpty()) {
            imageView.setVisibility(GONE);
        } else {
            imageView.setVisibility(VISIBLE);
            Picasso.get()
                    .load(meta.getImageurl())
                    .into(imageView);
        }

        if(meta.getTitle().isEmpty() || meta.getTitle().equals("")) {
            textViewTitle.setVisibility(GONE);
        } else {
            textViewTitle.setVisibility(VISIBLE);
            textViewTitle.setText(meta.getTitle());
        }
        if(meta.getUrl().isEmpty() || meta.getUrl().equals("")) {
            textViewUrl.setVisibility(GONE);
        } else {
            textViewUrl.setVisibility(VISIBLE);
            textViewUrl.setText(meta.getUrl());
        }
        if(meta.getDescription().isEmpty() || meta.getDescription().equals("")) {
            textViewDesp.setVisibility(GONE);
        } else {
            textViewDesp.setVisibility(VISIBLE);
            textViewDesp.setText(meta.getDescription());
        }


        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDefaultClick) {
                    richLinkClicked();
                } else {
                    if(richLinkListener != null) {
                        richLinkListener.onClicked(view, meta);
                    } else {
                        richLinkClicked();
                    }
                }
            }
        });

    }


    private void richLinkClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(main_url));
        context.startActivity(intent);
    }


    public void setDefaultClickListener(boolean isDefault) {
        isDefaultClick = isDefault;
    }

    public void setClickListener(ponnamkarthik_RichLinkListener richLinkListener1) {
        richLinkListener = richLinkListener1;
    }

    protected LinearLayout findLinearLayoutChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof LinearLayout) {
            return (LinearLayout) getChildAt(0);
        }
        return null;
    }

    public void setLinkFromMeta(ponnamkarthik_MetaData ponnamkarthikMetaData) {
        meta = ponnamkarthikMetaData;
        initView();
    }

    public ponnamkarthik_MetaData getMetaData() {
        return meta;
    }

    public void setLink(String url, final ponnamkarthik_ViewListener ponnamkarthikViewListener) {
        main_url = url;
        ponnamkarthik_RichPreview ponnamkarthikRichPreview = new ponnamkarthik_RichPreview(new ponnamkarthik_ResponseListener() {
            @Override
            public void onData(ponnamkarthik_MetaData metaData) {
                meta = metaData;
                if(!meta.getTitle().isEmpty() || !meta.getTitle().equals("")) {
                    ponnamkarthikViewListener.onSuccess(true);
                }

                initView();
            }

            @Override
            public void onError(Exception e) {
                ponnamkarthikViewListener.onError(e);
            }
        });
        ponnamkarthikRichPreview.getPreview(url);
    }

}