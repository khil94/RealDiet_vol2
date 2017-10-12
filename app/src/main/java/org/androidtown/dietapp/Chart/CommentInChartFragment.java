package org.androidtown.dietapp.Chart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidtown.dietapp.R;
import org.w3c.dom.Text;

/**
 * Created by zidru on 2017-10-08.
 */

public class CommentInChartFragment extends android.support.v4.app.Fragment{
    private ViewGroup layoutView;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutView = (ViewGroup) inflater.inflate(R.layout.activity_view_histroy_by_text, container, false);

        textView = (TextView) layoutView.findViewById(R.id.text_in_view_history);
        return layoutView;
    }
}
