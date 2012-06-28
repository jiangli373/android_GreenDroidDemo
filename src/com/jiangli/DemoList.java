package com.jiangli;

import greendroid.app.GDListActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ItemAdapter;
import greendroid.widget.NormalActionBarItem;
import greendroid.widget.item.TextItem;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jiangli.*;


public class DemoList extends GDListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ItemAdapter adapter = new ItemAdapter(this);
        adapter.add(createTextItem(R.string.basic_item_label, GreenDroidDemoActivity.class));
        adapter.add(createTextItem(R.string.xml_item_label, XmlItemActivity.class));
        adapter.add(createTextItem(R.string.quick_action_label, QuickActionActivity.class));
        adapter.add(createTextItem(R.string.paged_view_label, PagedViewActivity.class));
        adapter.add(createTextItem(R.string.asynvImageView, AsynvImageView.class));
        adapter.add(createTextItem(R.string.asynvImageListView, AsynvImageListView.class));

        setListAdapter(adapter);

//        addActionBarItem(getActionBar()
//                .newActionBarItem(NormalActionBarItem.class)
//                .setDrawable(new ActionBarDrawable(this, R.drawable.ic_action_bar_info)), R.id.action_bar_view_info);
    }

    private TextItem createTextItem(int stringId, Class<?> klass) {
        final TextItem textItem = new TextItem(getString(stringId));
        textItem.setTag(klass);
        return textItem;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final TextItem textItem = (TextItem) l.getAdapter().getItem(position);
        Intent intent = new Intent(this, (Class<?>) textItem.getTag());
       // intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, textItem.text);
        startActivity(intent);
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
//            case R.id.action_bar_view_info:
//                startActivity(new Intent(this, InfoTabActivity.class));
//                return true;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }
    }
}