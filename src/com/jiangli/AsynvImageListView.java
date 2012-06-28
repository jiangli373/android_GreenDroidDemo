package com.jiangli;


import greendroid.image.ChainImageProcessor;
import greendroid.image.ImageProcessor;
import greendroid.image.MaskImageProcessor;
import greendroid.image.ScaleImageProcessor;
import greendroid.widget.AsyncImageView;
import java.util.LinkedList;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class AsynvImageListView extends ListActivity implements OnScrollListener{
	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;

	private LinkedList<String> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.pull_to_refresh_list);

			mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
			mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh() {
					mPullRefreshListView.setLastUpdatedLabel(DateUtils.formatDateTime(getApplicationContext(),
							System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
									| DateUtils.FORMAT_ABBREV_ALL));
					// Do work to refresh the list here.
					new GetDataTask().execute();
				}
			});

			mListItems = new LinkedList<String>();
			 setListAdapter(new MyAdapter(this));
		        getListView().setOnScrollListener(this);
		}


		private class GetDataTask extends AsyncTask<Void, Void, String[]> {

			@Override
			protected String[] doInBackground(Void... params) {
				// Simulates a background job.
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
				}
				return null;
			}

			@Override
			protected void onPostExecute(String[] result) {
				mListItems.addFirst("Added after refresh...");
				mPullRefreshListView.onRefreshComplete();
				super.onPostExecute(result);
			}
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			menu.add(0, MENU_MANUAL_REFRESH, 0, "Manual Refresh");
			menu.add(0, MENU_DISABLE_SCROLL, 1,
					mPullRefreshListView.isDisableScrollingWhileRefreshing() ? "Enable Scrolling while Refreshing"
							: "Disable Scrolling while Refreshing");
			menu.add(0, MENU_SET_MODE, 0,
					mPullRefreshListView.getMode() == Mode.BOTH ? "Change to MODE_PULL_DOWN"
							: "Change to MODE_PULL_BOTH");
			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public boolean onPrepareOptionsMenu(Menu menu) {
			MenuItem disableItem = menu.findItem(MENU_DISABLE_SCROLL);
			disableItem
					.setTitle(mPullRefreshListView.isDisableScrollingWhileRefreshing() ? "Enable Scrolling while Refreshing"
							: "Disable Scrolling while Refreshing");

			MenuItem setModeItem = menu.findItem(MENU_SET_MODE);
			setModeItem.setTitle(mPullRefreshListView.getMode() == Mode.BOTH ? "Change to MODE_PULL_DOWN"
					: "Change to MODE_PULL_BOTH");

			return super.onPrepareOptionsMenu(menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			switch (item.getItemId()) {
				case MENU_MANUAL_REFRESH:
					new GetDataTask().execute();
					mPullRefreshListView.setRefreshing(false);
					break;
				case MENU_DISABLE_SCROLL:
					mPullRefreshListView.setDisableScrollingWhileRefreshing(!mPullRefreshListView
							.isDisableScrollingWhileRefreshing());
					break;
				case MENU_SET_MODE:
					mPullRefreshListView
							.setMode(mPullRefreshListView.getMode() == Mode.BOTH ? Mode.PULL_DOWN_TO_REFRESH
									: Mode.BOTH);
					break;
			}

			return super.onOptionsItemSelected(item);
		}
	 //

	    private static class MyAdapter extends BaseAdapter {

	        private static final String BASE_URL_PREFIX = "http://www.cyrilmottier.com/files/greendroid/images/image";
	        private static final String BASE_URL_SUFFIX = ".png";
	        private static final StringBuilder BUILDER = new StringBuilder();

	        private final String mImageForPosition;

	        static class ViewHolder {
	            public AsyncImageView imageView;
	            public TextView textView;
	            public TextView textContentView;
	            public StringBuilder textBuilder = new StringBuilder();
	        }

	        private LayoutInflater mInflater;
	        private ImageProcessor mImageProcessor;

	        public MyAdapter(Context context) {
	            mInflater = LayoutInflater.from(context);
	            mImageForPosition = context.getString(R.string.image_for_position);

	            prepareImageProcessor(context);
	        }

	        private void prepareImageProcessor(Context context) {
	            
	            final int thumbnailSize = context.getResources().getDimensionPixelSize(R.dimen.thumbnail_size);
	            final int thumbnailRadius = context.getResources().getDimensionPixelSize(R.dimen.thumbnail_radius);

	            if (Math.random() >= 0.5f) {
	                //@formatter:off
	                mImageProcessor = new ChainImageProcessor(
	                        new ScaleImageProcessor(thumbnailSize, thumbnailSize, ScaleType.FIT_XY),
	                        new MaskImageProcessor(thumbnailRadius));
	                //@formatter:on
	            } else {
	                
	                Path path = new Path();
	                path.moveTo(thumbnailRadius, 0);
	                
	                path.lineTo(thumbnailSize - thumbnailRadius, 0);
	                path.lineTo(thumbnailSize, thumbnailRadius);
	                path.lineTo(thumbnailSize, thumbnailSize - thumbnailRadius);
	                path.lineTo(thumbnailSize - thumbnailRadius, thumbnailSize);
	                path.lineTo(thumbnailRadius, thumbnailSize);
	                path.lineTo(0, thumbnailSize - thumbnailRadius);
	                path.lineTo(0, thumbnailRadius);
	                
	                path.close();
	                
	                Bitmap mask = Bitmap.createBitmap(thumbnailSize, thumbnailSize, Config.ARGB_8888);
	                Canvas canvas = new Canvas(mask);
	                
	                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	                paint.setStyle(Style.FILL_AND_STROKE);
	                paint.setColor(Color.RED);
	                
	                canvas.drawPath(path, paint);
	                
	                //@formatter:off
	                mImageProcessor = new ChainImageProcessor(
	                        new ScaleImageProcessor(thumbnailSize, thumbnailSize, ScaleType.FIT_XY),
	                        new MaskImageProcessor(mask));
	                //@formatter:on
	            }
	        }

	        public int getCount() {
	            return 100;
	        }

	        public Object getItem(int position) {
	            return null;
	        }

	        public long getItemId(int position) {
	            return position;
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {

	            ViewHolder holder;

	            if (convertView == null) {
	                convertView = mInflater.inflate(R.layout.item_show, parent, false);
	                holder = new ViewHolder();
	                holder.imageView = (AsyncImageView) convertView.findViewById(R.id.async_image);
	                holder.imageView.setImageProcessor(mImageProcessor);
	                holder.textView = (TextView) convertView.findViewById(R.id.text);
	                holder.textContentView = (TextView)convertView.findViewById(R.id.textContent);
	                convertView.setTag(holder);
	            } else {
	                holder = (ViewHolder) convertView.getTag();
	            }

	            BUILDER.setLength(0);
	            BUILDER.append(BASE_URL_PREFIX);
	            BUILDER.append(position);
	            BUILDER.append(BASE_URL_SUFFIX);
	            holder.imageView.setUrl(BUILDER.toString());

	            final StringBuilder textBuilder = holder.textBuilder;
	            textBuilder.setLength(0);
	            textBuilder.append(mImageForPosition);
	            textBuilder.append(position);
	            holder.textView.setText(textBuilder);
	            holder.textContentView.setText("这是简单的介绍"+position);

	            return convertView;
	        }
	    }

	    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
	    }

	    public void onScrollStateChanged(AbsListView listView, int scrollState) {
	        if (getListView() == listView) {
	            searchAsyncImageViews(listView, scrollState == OnScrollListener.SCROLL_STATE_FLING);
	        }
	    }

	    private void searchAsyncImageViews(ViewGroup viewGroup, boolean pause) {
	        final int childCount = viewGroup.getChildCount();
	        for (int i = 0; i < childCount; i++) {
	            AsyncImageView image = (AsyncImageView) viewGroup.getChildAt(i).findViewById(R.id.async_image);
	            if (image != null) {
	                image.setPaused(pause);
	            }
	        }
	    }
	
}
