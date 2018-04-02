package com.majd.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * Created by majd on 3/21/18.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {


    public ArticleAdapter(Context context) {

        super(context, 0);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article article = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_item, parent, false);

        }

        TextView title = convertView.findViewById(R.id.title);
        TextView author = convertView.findViewById(R.id.author);
        TextView date = convertView.findViewById(R.id.date);
        TextView section = convertView.findViewById(R.id.section_name);

        title.setText(article.getTitle());
        author.setText(article.getAuthorName());
        date.setText(article.getPublishedDate().substring(0, 10));
        section.setText(article.getSectionName());


        return convertView;


    }
}
