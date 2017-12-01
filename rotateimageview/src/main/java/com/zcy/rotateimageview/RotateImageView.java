package com.zcy.rotateimageview;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 自带旋转动画的ImageView
 * 动画的实现不借助原生动画
 * 而是通过旋转画布来实现
 *
 * Created by zcy on 2017/11/30.
 */

public class RotateImageView extends ImageView {

    private int ANIMATION_SPEED = 60;

    private int mCurrentDegree = 0;

    private int mStartDegree = 0;

    private boolean mEnableAnimation = false;

    private Drawable mDrawable;

    private long mAnimationStartTime = 0;

    public RotateImageView(Context context) {
        super(context);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setRotateDegree() {
        if (mEnableAnimation) {
            mStartDegree = mCurrentDegree;
            mAnimationStartTime = AnimationUtils.currentAnimationTimeMillis();
            invalidate();
        }
    }

    public void setSpeed(int speed) {
        if (speed > 0) {
            this.ANIMATION_SPEED = speed;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        Rect bounds = drawable.getBounds();
        int w = bounds.right - bounds.left;
        int h = bounds.bottom - bounds.top;
        if (w == 0 || h == 0) {
            return;
        }
        if (mEnableAnimation) {
            long time = AnimationUtils.currentAnimationTimeMillis();
            int deltaTime = (int) (time - mAnimationStartTime);
            int degree = mStartDegree + ANIMATION_SPEED
                    * (deltaTime > 0 ? deltaTime : -deltaTime) / 1000;
            degree = degree >= 0 ? degree % 360 : degree % 360 + 360;
            mCurrentDegree = degree;
            postInvalidateDelayed(20);
        }
        int saveCount = canvas.save();
        canvas.translate(width / 2, height / 2);
        canvas.rotate(mCurrentDegree);
        canvas.translate(-w / 2, -h / 2);
        drawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    public void setRotate(boolean isRotate) {
        if (isRotate == mEnableAnimation) {
            return;
        }
        mEnableAnimation = isRotate;
        if (isRotate) {
            setRotateDegree();
        }
    }

    public boolean isRotating() {
        return mEnableAnimation;
    }

    private int width, height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        if (null == drawable || drawable == mDrawable) {
            return;
        }
        convert(drawable);
    }

    private void convert(Drawable drawable) {
        mDrawable = drawable;
        Bitmap bitmap = drawable2Bitmap(drawable);
        if (null != bitmap) {
            bitmap = circleCrop(bitmap);
            super.setImageDrawable(new BitmapDrawable(bitmap));
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        Drawable drawable = new BitmapDrawable(bm);
        if (drawable == mDrawable) {
            return;
        }
        super.setImageBitmap(bm);
    }


    @Override
    public void setImageURI(@Nullable Uri uri) {
        String scheme = uri.getScheme();
        Drawable drawable = null;
        if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            // 什么都不想干
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)
                || ContentResolver.SCHEME_FILE.equals(scheme)) {
            try {
                drawable = Drawable.createFromStream(
                        getContext().getContentResolver().openInputStream(uri),
                        null);
            } catch (Exception e) {
                Log.w("ImageView", "Unable to open content: " + uri, e);
            }
        } else {
            drawable = Drawable.createFromPath(uri.toString());
        }
        if (null != drawable && drawable != mDrawable) {
            convert(drawable);
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (drawable != mDrawable) {
            convert(drawable);
        }
    }


    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    private Bitmap circleCrop(Bitmap source) {

        if (source == null) {
            return null;
        }

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

}
