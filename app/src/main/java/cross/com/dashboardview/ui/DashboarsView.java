package cross.com.dashboardview.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by cross on 17/1/27.
 */

public class DashboarsView extends View {
    private Canvas canvas;
    private int mWidth, mHeigh, BaseWidth;
    private List<HashMap<String, Object>> ListMethod = new ArrayList<>();
    private float percent = 0;
    private ValueAnimator valueAnimator;
    public DashboarsView(Context context) {
        super(context);
    }

    public DashboarsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DashboarsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeigh = getMeasuredHeight();
        if (mWidth > mHeigh) {
            BaseWidth = getMeasuredHeight();
        } else {
            BaseWidth = getMeasuredWidth();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.translate(mWidth / 2, mHeigh / 2);
        Builder();
    }
          //画圆弧
    private void Arc(int radius, float startAngle, float sweepAngle, float Width, int Color) {
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(Width);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color);
        paint.setDither(true);
        canvas.drawArc(rectF, startAngle - 90, sweepAngle, false, paint);
    }
          //画刻度
    private void Scale(float startX, float stopX, float startAngle, float sweepAngle, float Width, int Color, int Number) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(Width);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color);
        paint.setDither(true);
        canvas.save();
        canvas.rotate(startAngle, 0, 0);
        canvas.drawLine(startX, 0, stopX, 0, paint);
        float rAngle = sweepAngle / (Number - 1);
        for (int i = 1; i < Number; i++) {
            canvas.rotate(rAngle, 0, 0);
            canvas.drawLine(startX, 0, stopX, 0, paint);
        }
        canvas.restore();
    }
           //画刻度值
    private void ScaleText(float startY, float startAngle, float sweepAngle, float TextSize, int Color, String[] textScale) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(TextSize);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color);
        paint.setDither(true);
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        float MeasuredHeight = (fontMetricsInt.bottom - fontMetricsInt.top) * 2;
        int baseline = (int) (-startY + (MeasuredHeight / 2) + (-MeasuredHeight - fontMetricsInt.bottom + fontMetricsInt.top) / 2 - fontMetricsInt.top);
        canvas.save();
        canvas.rotate(startAngle, 0, 0);
        float rAngle = sweepAngle / (textScale.length - 1);
        canvas.drawText(textScale[0], -getTextViewLength(paint, textScale[0]) / 2, baseline, paint);
        for (int i = 1; i < textScale.length; i++) {
            canvas.rotate(rAngle, 0, 0);
            canvas.drawText(textScale[i], -getTextViewLength(paint, textScale[i]) / 2, baseline, paint);
        }
        canvas.restore();
    }
          //画游标
    private void Pointer(float startAngle, float sweepAngle, int Color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color);
        paint.setDither(true);
        int mMinCircleRadius = BaseWidth / 18;
        canvas.save();
        float angel = startAngle + (sweepAngle * percent / 100);
        canvas.rotate(angel, 0, 0);
        Path path = new Path();
        path.moveTo(0, 0);
        RectF rectF1 = new RectF(-mMinCircleRadius / 2, -mMinCircleRadius / 2, mMinCircleRadius / 2, mMinCircleRadius / 2);
        path.arcTo(rectF1, 0, 180);
        path.lineTo(0, (float) -(BaseWidth * 0.35));
        path.lineTo(mMinCircleRadius / 2, 0);
        path.close();
        canvas.drawPath(path, paint);
        canvas.restore();
    }
         //画多个圆弧
    private void Arcs(int radius, float[][] Angle, float Width, int[] Color) {
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(Width);
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        for (int i = 0; i < Color.length; i++) {
            paint.setColor(Color[i]);
            canvas.drawArc(rectF, Angle[i][0] - 90, Angle[i][1], false, paint);
        }
    }
          //添加背景图片
    private void BitmapBackground(double widthPrecent, int id) {
        Resources r = this.getContext().getResources();
        Bitmap bmp = BitmapFactory.decodeResource(r, id);
        Rect mSrcRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        int left = -BaseWidth / 2;
        int top = -(int) (BaseWidth / 2 - BaseWidth * widthPrecent);
        int right = BaseWidth / 2;
        int bottom = (int) (BaseWidth / 2 + BaseWidth * widthPrecent);
        Rect mDestRect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(bmp, mSrcRect, mDestRect, null);
    }
          //添加游标图片
    private void BitmapPointer(float startAngle, float sweepAngle, double widthPercent, double heighPercent, int id) {
        Resources r = this.getContext().getResources();
        Bitmap bmp = BitmapFactory.decodeResource(r, id);
        Rect mSrcRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        canvas.save();
        float angel = startAngle + (sweepAngle * percent / 100);
        canvas.rotate(angel, 0, 0);
        int left = -(int) (BaseWidth * widthPercent);
        int top = -(int) (BaseWidth * heighPercent);
        int right = BaseWidth / 40;
        int bottom = 0;
        Rect mDestRect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(bmp, mSrcRect, mDestRect, null);
        canvas.restore();
    }

    private float getTextViewLength(Paint paint, String text) {
        if (TextUtils.isEmpty(text))
            return 0;
        float textLength = paint.measureText(text);
        return textLength;
    }

    private void Builder() {
        for (int i = 0; i < ListMethod.size(); i++) {
            HashMap<String, Object> ParameterMap = ListMethod.get(i);
            int type = (int) ParameterMap.get("type");
            switch (type) {
                case 1:
                    Arc(getRadius((double) ParameterMap.get("radiusPercent")), (float) ParameterMap.get("startAngle"), (float) ParameterMap.get("sweepAngle")
                            , getWidth((double) ParameterMap.get("widthPercent")), (int) ParameterMap.get("Color"));
                    break;
                case 2:
                    Scale(getWidth((double) ParameterMap.get("startXPercent")), getWidth((double) ParameterMap.get("stopXPercent")),
                            (float) ParameterMap.get("startAngle"), (float) ParameterMap.get("sweepAngle"),
                            getWidth((double) ParameterMap.get("widthPercent")), (int) ParameterMap.get("Color"),
                            (int) ParameterMap.get("Number"));
                    break;
                case 3:
                    ScaleText(getWidth((double) ParameterMap.get("startXPercent")), (float) ParameterMap.get("startAngle"), (float) ParameterMap.get("sweepAngle"),
                            getWidth((double) ParameterMap.get("widthPercent")), (int) ParameterMap.get("Color"), (String[]) ParameterMap.get("textScale"));

                    break;
                case 4:
                    Pointer((float) ParameterMap.get("startAngle"), (float) ParameterMap.get("sweepAngle"), (int) ParameterMap.get("Color"));
                    break;
                case 5:
                    Arcs(getRadius((double) ParameterMap.get("radiusPercent")), (float[][]) ParameterMap.get("Angle"),
                            getWidth((double) ParameterMap.get("widthPercent")), (int[]) ParameterMap.get("Color"));
                    break;
                case 6:
                    BitmapBackground((double) ParameterMap.get("widthPrecent"), (int) ParameterMap.get("id"));
                    break;
                case 7:
                    BitmapPointer((float) ParameterMap.get("startAngle"), (float) ParameterMap.get("sweepAngle"),
                            (double) ParameterMap.get("widthPercent"), (double) ParameterMap.get("heighPercent"),
                            (int) ParameterMap.get("id"));
                    break;
            }
        }

    }

    public void DrawArc(double radiusPercent, float startAngle, float sweepAngle, double widthPercent, int Color) {
        HashMap<String, Object> ParameterMap = new HashMap<>();
        ParameterMap.put("radiusPercent", radiusPercent);
        ParameterMap.put("startAngle", startAngle);
        ParameterMap.put("sweepAngle", sweepAngle);
        ParameterMap.put("widthPercent", widthPercent);
        ParameterMap.put("Color", Color);
        ParameterMap.put("type", 1);
        ListMethod.add(ParameterMap);
    }

    public void DrawScale(double startXPercent, double stopXPercent, float startAngle, float sweepAngle, double widthPercent, int Color, int Number) {
        HashMap<String, Object> ParameterMap = new HashMap<>();
        ParameterMap.put("startXPercent", startXPercent);
        ParameterMap.put("stopXPercent", stopXPercent);
        ParameterMap.put("startAngle", startAngle);
        ParameterMap.put("sweepAngle", sweepAngle);
        ParameterMap.put("widthPercent", widthPercent);
        ParameterMap.put("Color", Color);
        ParameterMap.put("Number", Number);
        ParameterMap.put("type", 2);
        ListMethod.add(ParameterMap);
    }

    public void DrawTextScale(double startXPercent, float startAngle, float sweepAngle, double widthPercent, int Color, String[] textScale) {
        HashMap<String, Object> ParameterMap = new HashMap<>();
        ParameterMap.put("startXPercent", startXPercent);
        ParameterMap.put("startAngle", startAngle);
        ParameterMap.put("sweepAngle", sweepAngle);
        ParameterMap.put("widthPercent", widthPercent);
        ParameterMap.put("Color", Color);
        ParameterMap.put("textScale", textScale);
        ParameterMap.put("type", 3);
        ListMethod.add(ParameterMap);
    }

    public void DrawPointer(float startAngle, float sweepAngle, int Color) {
        HashMap<String, Object> ParameterMap = new HashMap<>();
        ParameterMap.put("startAngle", startAngle);
        ParameterMap.put("sweepAngle", sweepAngle);
        ParameterMap.put("Color", Color);
        ParameterMap.put("type", 4);
        ListMethod.add(ParameterMap);
    }

    public void DrawArcs(double radiusPercent, float[][] Angle, double widthPercent, int[] Color) {
        HashMap<String, Object> ParameterMap = new HashMap<>();
        ParameterMap.put("radiusPercent", radiusPercent);
        ParameterMap.put("Angle", Angle);
        ParameterMap.put("widthPercent", widthPercent);
        ParameterMap.put("Color", Color);
        ParameterMap.put("type", 5);
        ListMethod.add(ParameterMap);
    }

    public void DrawBackgrond(int id, double widthPrecent) {
        HashMap<String, Object> ParameterMap = new HashMap<>();
        ParameterMap.put("id", id);
        ParameterMap.put("widthPrecent", widthPrecent);
        ParameterMap.put("type", 6);
        ListMethod.add(ParameterMap);
    }

    public void DrawPointerPicture(float startAngle, float sweepAngle, double widthPercent, double heighPercent, int id) {
        HashMap<String, Object> ParameterMap = new HashMap<>();
        ParameterMap.put("startAngle", startAngle);
        ParameterMap.put("sweepAngle", sweepAngle);
        ParameterMap.put("widthPercent", widthPercent);
        ParameterMap.put("heighPercent", heighPercent);
        ParameterMap.put("id", id);
        ParameterMap.put("type", 7);
        ListMethod.add(ParameterMap);
    }

    private int getRadius(double percent) {
        return (int) (percent * BaseWidth);
    }

    private float getWidth(double percent) {
        return (float) (percent * BaseWidth);
    }

    public void setAnimator(float percent, long time) {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofFloat(0, percent).setDuration(time);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                DashboarsView.this.percent = (float) animation.getAnimatedValue();
                invalidate();

            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();

    }
    public void setAnimator(float percent) {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofFloat(percent, percent).setDuration(100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                DashboarsView.this.percent = (float) animation.getAnimatedValue();
                invalidate();

            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();

    }
}
