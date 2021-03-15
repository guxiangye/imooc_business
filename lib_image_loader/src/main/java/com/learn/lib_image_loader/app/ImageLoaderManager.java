package com.learn.lib_image_loader.app;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.learn.lib_image_loader.R;
import com.learn.lib_image_loader.image.CustomRequestListener;
import com.learn.lib_image_loader.image.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片加载库,与外界的微医通信类
 * 支持为各种 view, notification, appwidget, view group
 */
public class ImageLoaderManager {
    /**
     * 私有化构造函数
     */
    private ImageLoaderManager() {}
    public static class SingletonHolder {
        private static ImageLoaderManager instance = new ImageLoaderManager();
    }

    /**
     * 单例
     * @return ImageLoaderManager
     */
    public static ImageLoaderManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 为 ImageView加载图片
     * @param imageView imageView
     * @param url url
     * @param requestListener 进度
     */
    public void displayImageForView(ImageView imageView, String url, CustomRequestListener requestListener) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .apply(initCommonRequestOption())
                .load(url)
                .transition(BitmapTransitionOptions.withCrossFade()) // 效果
                .listener(requestListener)
                .into(imageView);
    }

    /**
     * 为 ImageView加载图片
     */
    public void displayImageForView(ImageView imageView, String url) {
        this.displayImageForView(imageView, url, null);
    }

    /**
     * 为 imageView 加载圆形图片
     * @param imageView imageView
     * @param url url
     */
    public void displayImageForCircle(final ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .apply(initCommonRequestOption())
                .load(url)
                .into(new BitmapImageViewTarget(imageView) {
                    // 将 imageView 包装成 target 对象, 创建圆形 drawable
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                .create(imageView.getResources(), resource);
                        drawable.setCircular(true);
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

    /**
     * 为viewGroup 加载图片
     * @param viewGroup viewGroup
     * @param url url
     */
    public void displayImageForViewGroup(final ViewGroup viewGroup, String url) {
        Glide.with(viewGroup.getContext())
                .asBitmap()
                .apply(initCommonRequestOption())
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        final Bitmap bitmap = resource;
                        Observable.just(bitmap).map(new Function<Bitmap, Object>() {
                            @Override
                            public Object apply(Bitmap bitmap) throws Exception {
                                // 将 bitmap 进行高斯模糊, 并转为 drawable
                                return new BitmapDrawable(Utils.doBlur(bitmap, 100, true));
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        Drawable drawable = (Drawable) o;
                                        viewGroup.setBackground(drawable);
                                    }
                                });

                    }
                });
    }

    public void displayImageForNotification(Context context, RemoteViews rv, int id,
                                            Notification notification, int NOTIFICATION_ID, String url) {
        this.displayImageForTarget(
                context,
                initNotificationTarget(context, id, rv, notification, NOTIFICATION_ID),
                url);
    }

    /**
     * 为非 View 加载图片
     */
    private void displayImageForTarget(Context context, Target<Bitmap> target, String url) {
        Glide.with(context)
                .asBitmap()
                .apply(initCommonRequestOption())
                .load(url)
                .transition(BitmapTransitionOptions.withCrossFade())
                .fitCenter()
                .into(target);
    }

    /**
     * 初始化 notification target
     */
    private NotificationTarget initNotificationTarget(Context context, int id, RemoteViews rv, Notification notification, int NOTIFICATION_ID) {
        return new NotificationTarget(context, id, rv, notification, NOTIFICATION_ID);
    }

    @SuppressLint("CheckResult")
    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        // 占位图
        options.placeholder(R.mipmap.b4y)
                .error(R.mipmap.b4y) // 错误
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // 自动
                .skipMemoryCache(false) // 内存缓存
                .priority(Priority.NORMAL); // 优先级
        return options;
    }
}
