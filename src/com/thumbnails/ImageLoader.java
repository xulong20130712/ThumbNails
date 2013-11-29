package com.thumbnails;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class ImageLoader {
	// 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动）
//	public LinkedHashMap<String, SoftReference<Bitmap>> imageCache = new LinkedHashMap<String, SoftReference<Bitmap>>();
	private ArrayList<String> urls= new ArrayList<String>();
	private ArrayList<SoftReference<Bitmap>> images= new ArrayList<SoftReference<Bitmap>>();
	private ExecutorService executorService = Executors.newFixedThreadPool(10); // 固定五个线程来执行任务
	private ImageCallBack callBack;
	public Bitmap loadDrawable(final String imageUrl, final int width, final int height, final ImageCallBack callBack, final Handler handler) {
		// 如果缓存过就从缓存中取出数据
//		if (imageCache.containsKey(imageUrl)) {
//			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
//			if (softReference.get() != null) {
//				return softReference.get();
//			}
//		}
		if(urls.contains(imageUrl)) {
			
			SoftReference<Bitmap> softReference = images.get(urls.indexOf(imageUrl));
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
		executorService.submit(new Thread() {
			public void run() {
				try {
					
					final Bitmap drawable = loadImageFromUrl(imageUrl,width, height);
//					imageCache.put(imageUrl, new SoftReference<Bitmap>(drawable));
					urls.add(imageUrl);
					images.add(new SoftReference<Bitmap>(drawable));
					Message msg= new Message();
					msg.obj= drawable;
					handler.sendMessage(msg);
				} catch (Exception e) {
					
					throw new RuntimeException(e);
				}
			}
		});
		return null;
	}

	// 从网络上取数据方法
	protected Drawable loadImageFromUrl(String imageUrl, boolean flag) {
		try {
			// 测试时，模拟网络延时，实际时这行代码不能有
			SystemClock.sleep(2000);

//			return Drawable.createFromStream(new URL(imageUrl).openStream(),
//					"image.png");
			
			return Drawable.createFromPath(imageUrl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Bitmap loadImageFromUrl(String path, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(path, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	
	public interface ImageCallBack {
		
		public void imageLoaded(final Bitmap drawable);
	}
}