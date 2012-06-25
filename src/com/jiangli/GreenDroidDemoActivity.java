package com.jiangli;

import greendroid.app.GDListActivity;
import greendroid.widget.ItemAdapter;
import greendroid.widget.item.DescriptionItem;
import greendroid.widget.item.DrawableItem;
import greendroid.widget.item.Item;
import greendroid.widget.item.ProgressItem;
import greendroid.widget.item.SeparatorItem;
import greendroid.widget.item.TextItem;
import greendroid.widget.item.ThumbnailItem;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;

public class GreenDroidDemoActivity extends GDListActivity {
private final Handler mHandler = new Handler();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        List<Item> items = new ArrayList<Item>();
        
        items.add(new SeparatorItem("Class 1"));
        items.add(new ThumbnailItem("Powered paragliding", "aka paramotoring", R.drawable.ic_launcher));
        items.add(new DescriptionItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));
        
        items.add(new SeparatorItem("Class 2"));
        items.add(new DrawableItem("Trikes", R.drawable.ic_launcher));
        items.add(new DescriptionItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));
        
        items.add(new SeparatorItem("Class 3"));
        items.add(new ThumbnailItem("Multi-axis", "Looks like a tiny plane", R.drawable.ic_launcher));
        items.add(new DescriptionItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));
        
        items.add(new SeparatorItem("Class 4"));
        items.add(new ThumbnailItem("Auto-gyro", "A scary helicopter", R.drawable.ic_launcher));
        items.add(new DescriptionItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));
        
        items.add(new SeparatorItem("Class 5"));
        items.add(new DrawableItem("Hot air baloon", R.drawable.ic_launcher));
        items.add(new DescriptionItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in."));
        
        final Item item1 = new SeparatorItem("Class 6");
        final Item item2 = new TextItem("Airbus/Boeing planes");
        final Item item3 = new DescriptionItem("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tempus consequat leo, et tincidunt justo tristique in.");
        items.add(item1);
        items.add(item2);
        items.add(item3);
        
        final ProgressItem progressItem = new ProgressItem("Removing intruders", true);
        items.add(progressItem);
        
        final ItemAdapter adapter = new ItemAdapter(this, items);
        setListAdapter(adapter);
        
        mHandler.postDelayed(new Runnable() {
            public void run() {
                adapter.remove(item1);
                adapter.remove(item2);
                adapter.remove(item3);
                adapter.remove(progressItem);
                adapter.insert(new ThumbnailItem("Ultralight aviation", "List of French 'ULM' classes", R.drawable.ic_launcher), 0);
                adapter.notifyDataSetChanged();
            }
        },8000);
    }
}