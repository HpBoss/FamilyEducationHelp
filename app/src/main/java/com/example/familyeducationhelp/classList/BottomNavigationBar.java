package com.example.familyeducationhelp.classList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.MenuRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.familyeducationhelp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BottomNavigationBar extends View {

    //////////////////////////////////////////////基本变量、属性///////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    //导航栏的button列表
    private List<Item> itemList = new ArrayList<>();
    //总宽度 width
    private int mWidth = 0;
    //总高度 height
    private int mHeight = 0;
    //每个菜单的宽度 item width
    private int mItemWidth = 0;
    //每个菜单的告诉 item height
    private int mItemHeight = 0;
    //整体的上边距
    private int topPadding = dpToPx(getResources(), 3);//getResource():系统默认   3：?
    //整体下边距
    private int bottomPadding = dpToPx(getResources(), 3);
    //文字相对于图标的边距
    private int textTop = dpToPx(getResources(), 3);
    //画笔
    private Paint mPaint;
    // 当前选中的坐标位置
    private int checkedPosition = 0;
    //Item菜单的选中事件
    private OnItemSelectedListener onItemSelectedListener;
    //图标的状态颜色列表
    private ColorStateList itemIconTintRes;
    //文字的状态颜色列表
    private ColorStateList itemColorStateList;
    //是否开启上浮
    private boolean floatingEnable;
    //上浮距离
    private int floatingUpInit;
    private int floatingUp;
    //背景资源
    private Drawable background;
    //菜单的布局文件
    private @MenuRes
    int menuRes;









    /**
     * BottomNavigationBar构造方法
     * @param context
     */
    public BottomNavigationBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }




    /**
     *    ********************保存导航栏元素的信息******************
     * 按钮（属性）：
     * id：？
     * icon:图标
     * drawable:?
     * title:?
     * itleSize：？
     * floating:是否上浮
     * checked：是否被点击
     * checkable：？
     * msgCount：消息条数
     */
    public class Item {
        private int id;
        private StateListDrawable icon;
        private Drawable drawable;
        private String title;
        private int titleSize;
        private boolean floating = false;
        private boolean checked = false;
        private boolean checkable = true;
        private int msgCount = 0;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public boolean isFloating() {
            return floating;
        }

        public boolean isChecked() {
            return checked;
        }

        public boolean isCheckable() {
            return checkable;
        }

        public int getMsgCount() {
            return msgCount;
        }
    }



    /////////////////////////导航栏元素的基本属性设置//////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置Menu 菜单资源文件
     * Menu保存导航栏button的信息
     * @param menuRes
     */
    public void setMenu(@MenuRes int menuRes) {
        this.menuRes = menuRes;
        parseXml(menuRes);//解析保存在menu中的button信息
        format();//对大于3的数据进行截取、背景的设置
        postInvalidate();//刷新View
    }


    /**
     * 设置Item 菜单的   图标颜色   状态列表
     *
     * @param resId 图标颜色状态的资源文件
     */
    public void setItemIconTint(@DrawableRes @ColorRes int resId) {
        this.itemIconTintRes = ResourcesCompat.getColorStateList(getResources(), resId, null);
        parseXml(menuRes);
        format();
        postInvalidate();
    }

    /**
     * 设置Item 菜单的   文字颜色   状态列表
     *
     * @param resId 文字颜色状态的资源文件
     */
    public void setItemColorStateList(@DrawableRes @ColorRes int resId) {
        this.itemColorStateList = ResourcesCompat.getColorStateList(getResources(), resId, null);
        postInvalidate();
    }

    /**
     * 设置是否   开启浮动
     *
     * @param floatingEnable
     */
    public void setFloatingEnable(boolean floatingEnable) {
        this.floatingEnable = floatingEnable;//设置标记
        postInvalidate();
    }

    /**
     * 设置   上浮距离   ，不能超过导航栏高度的1/2
     *
     * @param floatingUp
     */
    public void setFloatingUp(int floatingUp) {
        this.floatingUp = floatingUp;
        postInvalidate();
    }











    ///////////////////////////////初始化、解析数据///////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 解析 menu 的 xml 的文件，得到相关的 导航栏菜单
     *
     * @param xmlRes
     */
    private void parseXml(int xmlRes) {
        try {
            if (xmlRes == 0) {
                return;
            }
            XmlResourceParser xmlParser = getResources().getXml(xmlRes);
            int event = xmlParser.getEventType();   //先获取当前解析器光标在哪
            while (event != XmlPullParser.END_DOCUMENT) {    //如果还没到文档的结束标志，那么就继续往下处理
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //一般都是获取标签的属性值，所以在这里数据你需要的数据
                        if (xmlParser.getName().equals("item")) {
                            Item item = new Item();
                            for (int i = 0; i < xmlParser.getAttributeCount(); i++) {
                                //两种方法获取属性值
                                if ("id".equalsIgnoreCase(xmlParser.getAttributeName(i))) {
                                    item.id = xmlParser.getAttributeResourceValue(i, 0);
                                } else if ("icon".equalsIgnoreCase(xmlParser.getAttributeName(i))) {
                                    int drawableId = xmlParser.getAttributeResourceValue(i, 0);
                                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), drawableId, null);
                                    item.drawable = drawable.getConstantState().newDrawable();
                                    StateListDrawable stateListDrawable = new StateListDrawable();
                                    if (drawable instanceof StateListDrawable) {
                                        stateListDrawable = (StateListDrawable) drawable;
                                        stateListDrawable.setState(new int[]{android.R.attr.state_checked});
                                        stateListDrawable.mutate();
                                    } else {
                                        Drawable selectedDrawable = tintListDrawable(drawable, itemIconTintRes);
                                        selectedDrawable.setState(new int[]{android.R.attr.state_checked});
                                        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, selectedDrawable.getCurrent());
                                        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, selectedDrawable.getCurrent());
                                        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, selectedDrawable.getCurrent());
                                        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, selectedDrawable.getCurrent());
                                        selectedDrawable.setState(new int[]{});
                                        stateListDrawable.addState(new int[]{}, selectedDrawable.getCurrent());
                                    }
                                    item.icon = stateListDrawable;
                                } else if ("title".equalsIgnoreCase(xmlParser.getAttributeName(i))) {
                                    item.title = xmlParser.getAttributeValue(i);
                                } else if ("floating".equalsIgnoreCase(xmlParser.getAttributeName(i))) {
                                    item.floating = xmlParser.getAttributeBooleanValue(i, false);
                                } else if ("checked".equalsIgnoreCase(xmlParser.getAttributeName(i))) {
                                    item.checked = xmlParser.getAttributeBooleanValue(i, false);
                                } else if ("checkable".equalsIgnoreCase(xmlParser.getAttributeName(i))) {
                                    item.checkable = xmlParser.getAttributeBooleanValue(i, false);
                                }
                            }
                            if (item.checkable && item.checked) {
                                checkedPosition = itemList.size();
                            }
                            itemList.add(item);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                event = xmlParser.next();   //将当前解析器光标往下一步移
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理数据   背景
     */
    private void format() {
        if (itemList.size() > 3) {
            //subList 取出集合中的子集合，从位置0，取到位置4
            itemList = itemList.subList(0, 3);
        }
        if (getBackground() != null && getBackground() instanceof ColorDrawable) {
            background = getBackground();
        } else if (getBackground() instanceof StateListDrawable) {
            background =  getBackground();
        } else if (getBackground() instanceof GradientDrawable) {
            background = getBackground();
        } else {
            background = new ColorDrawable(Color.WHITE);
        }
//setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 初始化，获取该View的自定义属性，以及item 列表
     *
     * @param context      上下文
     * @param attrs        属性
     * @param defStyleAttr 默认样式
     */
    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StyleBottomLayout);
            itemIconTintRes = ta.getColorStateList(R.styleable.StyleBottomLayout_itemIconTints);
            itemColorStateList = ta.getColorStateList(R.styleable.StyleBottomLayout_itemTextColors);

            //字体颜色的改变
            if (itemIconTintRes == null) {
//                itemIconTintRes = ResourcesCompat.getColorStateList(getResources(), R.color.colorAccent, null);
                itemIconTintRes = ResourcesCompat.getColorStateList(getResources(), R.color.colorWhite, null);
            }
            if (itemColorStateList == null) {
//                itemColorStateList = ResourcesCompat.getColorStateList(getResources(), R.color.colorAccent, null);
                itemColorStateList = ResourcesCompat.getColorStateList(getResources(), R.color.colorWhite, null);
            }
            floatingEnable = ta.getBoolean(R.styleable.StyleBottomLayout_floatingEnable, false);
            floatingUp = floatingUpInit = (int) ta.getDimension(R.styleable.StyleBottomLayout_floatingUp, 0);
            int xmlRes = ta.getResourceId(R.styleable.StyleBottomLayout_menu, 0);
            parseXml(xmlRes);
        }
        format();
    }

    /**
     * 当初始化布局以后，进行默认选中
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setSelected(checkedPosition);
    }














    ////////////////////////////////监听触碰事件////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    //设置选中、非选中的切换
    public void setSelected(int position) {
        Item item = itemList.get(position);
        if (item.checkable) {
            if (checkedPosition >= 0) {
                //将之前的按钮选中状态改为未选中
                itemList.get(checkedPosition).checked = false;//checkPosition:当前被选中的位置
            }
            item.checked = true;//当前位置被选中
            checkedPosition = position;
        }
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(itemList.get(position), position);
        }
        postInvalidate();//刷新View
    }


    /**
     * 设置选中的监听事件
     *   *******************导航栏的的监听事件Listener使用该函数*******************
     * @param onItemSelectedListener
     */
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }


    /**
     * 选中监听事件的接口
     * 定义一个onItemSelectedListener变量
     */
    public interface OnItemSelectedListener {
        void onItemSelected(Item item, int position);
    }


    /**
     * 触摸事件监听
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double x = (double) event.getRawX();
        double y = (double) event.getRawY();
        //获取控件在屏幕的位置
        int[] location = new int[2];
        getLocationOnScreen(location);
        int locationY = location[1];
        y = y - locationY;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return true;
//                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < itemList.size(); i++) {
                    Item item = itemList.get(i);
                    int startTop = 0;
                    if (!item.floating) {
                        startTop = getPaddingTop() + floatingUp;
                    }
                    if (x > getPaddingLeft() + mItemWidth * i && x < getPaddingLeft() + mItemWidth * (i + 1)) {
                        //图片文字内容宽度
                        int width = mItemHeight - topPadding - bottomPadding;
                        //图片文字内容高度
                        int height = mItemHeight - topPadding - bottomPadding;
                        if (!TextUtils.isEmpty(item.title)) {
                            int color = item.checked ? itemColorStateList.getColorForState(new int[]{android.R.attr.state_checked}, itemColorStateList.getDefaultColor()) : itemColorStateList.getDefaultColor();
                            createTextPaint(item.titleSize == 0 ? dpToPx(getResources(), 14) : item.titleSize, color);
                            int textHeight = getTextHeight(item.title, mPaint);
                            int textY = startTop + height - textHeight / 4;//上边距+图片文字内容高度
                            int w = textY - textHeight / 2 - topPadding;
//                        width = height = height - textHeight - textTop;
                        }
                        int centerX = getPaddingLeft() + i * mItemWidth + (mItemWidth - width) / 2 + width / 2;
                        int centerY = mItemHeight / 2-24;
                        int r = mItemHeight / 2-24;
                        if (y >= floatingUp || (item.floating && isInCircle(centerX, centerY, r, (int) x, (int) y))) {
                            setSelected(i);
                        }
                    }
                }
                postInvalidate();
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * 判断触摸位置是否在圆形内部
     *
     * @param vCenterX 圆形的 X 坐标
     * @param vCenterY 圆形的 Y 坐标
     * @param r        圆形的半径
     * @param touchX   触摸位置的 X 坐标
     * @param touchY   触摸位置的 Y 坐标
     * @return
     */
    private boolean isInCircle(int vCenterX, int vCenterY, int r, int touchX, int touchY) {
        //点击位置x坐标与圆心的x坐标的距离
        int distanceX = Math.abs(vCenterX - touchX);
        //点击位置y坐标与圆心的y坐标的距离
        int distanceY = Math.abs(vCenterY - touchY);
        //点击位置与圆心的直线距离
        int distanceZ = (int) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

        //如果点击位置与圆心的距离大于圆的半径，证明点击位置没有在圆内
        if (distanceZ > r) {
            return false;
        }
        return true;
    }











    ///////////////////////////////////图型的设置、文字的设置//////////////////////
    //////////////////////////////////////////////////////////////////////////////
    /**
     * 更改图片颜色
     *
     * @param drawable
     * @param colors
     * @return
     */
    public Drawable tintListDrawable(Drawable drawable, ColorStateList colors) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.MULTIPLY);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * 创建图形的画笔
     *
     * @param color 画笔颜色
     * @return
     */
    private Paint createPaint(int color) {
        Paint mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);//设置抗锯齿功能 true表示抗锯齿 false则表示不需要这功能
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);
        return mPaint;
    }


    /**
     * 画出每一个Item导航菜单
     *
     * @param canvas
     * @param item
     */
    private void drawItem(Canvas canvas, Item item, int position) {
        if (item == null) {
            return;
        }
        //图片文字内容宽度
        int width = mItemHeight - topPadding - bottomPadding;
        //图片文字内容高度
        int height = mItemHeight - topPadding - bottomPadding;
        int startTop = 0;
        if (!item.floating) {
            startTop = topPadding;
        } else {
            startTop = topPadding - floatingUp;
            width = width + floatingUp;
            height = height + floatingUp;
        }
        if (!TextUtils.isEmpty(item.title)) {
            int color = item.checked ? itemColorStateList.getColorForState(new int[]{android.R.attr.state_checked}, itemColorStateList.getDefaultColor()) : itemColorStateList.getDefaultColor();
            if (!item.checkable) {
                color = itemColorStateList.getColorForState(new int[]{android.R.attr.state_checked}, itemColorStateList.getDefaultColor());
            }
            createTextPaint(item.titleSize == 0 ? dpToPx(getResources(), 14) : item.titleSize, color);
            int textHeight = getTextHeight(item.title, mPaint);
            int textY = startTop + height - textHeight / 4;//上边距+图片文字内容高度
            int w = textY - textHeight / 2 - topPadding;
            width = height = height - textHeight - textTop;
            canvas.drawText(item.title, position * mItemWidth + getPaddingLeft() + mItemWidth / 2, textY, mPaint);
        }
        //图标
        if (item.icon != null) {
            Rect to = new Rect();
            to.left = getPaddingLeft() + position * mItemWidth + (mItemWidth - width) / 2;
            to.top = startTop;
            to.right = to.left + width;
            to.bottom = topPadding + height;
            if (item.floating) {
                to.bottom = (int) (topPadding + height - floatingUp);
            }
            Drawable drawable;
            if (item.checkable) {
                if (item.checked) {
                    item.icon.setState(new int[]{android.R.attr.state_checked});
                    drawable = item.icon.getCurrent();
                } else {
                    item.icon.setState(new int[]{});
                    drawable = item.icon.getCurrent();
                }
            } else {
                drawable = item.drawable;
            }
            drawable.setBounds(to);
            drawable.draw(canvas);
        }
        //消息原点
        if (item.msgCount != 0) {
            int x = 0;
            int y = 0;
            int r = 0;
            if (item.msgCount > 0) {
                createTextPaint(item.titleSize == 0 ? dpToPx(getResources(), 9) : item.titleSize, Color.WHITE);
                r = getTextWidth("99+", mPaint) / 2 + 1;
                String count = "";
                if (item.msgCount > 99) {
                    count = "99+";
                    createTextPaint(item.titleSize == 0 ? dpToPx(getResources(), 8) : item.titleSize, Color.WHITE);
                } else {
                    count = String.valueOf(item.msgCount);
                }
                x = getPaddingLeft() + position * mItemWidth + (mItemWidth - width) / 2 + width - r / 4;
                y = startTop + r - r / 3;
                Paint paint = createPaint(Color.RED);
                canvas.drawCircle(x, y, r, paint);
                canvas.drawText(count, x, y + r / 2, mPaint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(dpToPx(getResources(), 1));
                canvas.drawCircle(x, y, r, paint);
            } else {
                r = 9;
                x = getPaddingLeft() + position * mItemWidth + (mItemWidth - width) / 2 + width - r;
                y = startTop + r;
                Paint paint = createPaint(Color.RED);
                canvas.drawCircle(x, y, r, paint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(dpToPx(getResources(), 1));
                canvas.drawCircle(x, y, r, paint);
            }

        }
    }


    /**
     * 获取文字高度
     *
     * @param text  文字
     * @param paint 画笔
     * @return
     */
    private int getTextHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * 创建文字类型的画笔
     *
     * @param textSize  文字大小
     * @param textColor 文字颜色
     * @return
     */
    private Paint createTextPaint(int textSize, int textColor) {
        if (mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setColor(textColor);//设置画笔的颜色
        mPaint.setTextSize(textSize);//设置文字大小
//        mPaint.setStrokeWidth(2);//设置画笔的宽度
        mPaint.setAntiAlias(true);//设置抗锯齿功能 true表示抗锯齿 false则表示不需要这功能
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        return mPaint;
    }












    ///////////////////////////////////绘图//////////////////////
    //////////////////////////////////////////////////////////////////////////////
    Bitmap drawable2Bitmap(Drawable drawable) {
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
            Bitmap bitmap;
            if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    /**
     * 画出上浮图标的背景
     *
     * @param canvas
     */
    private void drawFloating(Canvas canvas) {
        if (!floatingEnable) {
            return;
        }
        if (itemList.size() > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                Item item = itemList.get(i);
                if (item.floating) {
                    int startTop = 0;
                    //图片文字内容宽度
                    int width = mItemHeight - topPadding - bottomPadding;
                    //图片文字内容高度
                    int height = mItemHeight - topPadding - bottomPadding;
                    startTop = topPadding;
                    if (!TextUtils.isEmpty(item.title)) {
                        int color = item.checked ? itemColorStateList.getColorForState(new int[]{android.R.attr.state_checked}, itemColorStateList.getDefaultColor()) : itemColorStateList.getDefaultColor();
                        createTextPaint(item.titleSize == 0 ? dpToPx(getResources(), 14) : item.titleSize, color);
                        int textHeight = getTextHeight(item.title, mPaint);
                        int textY = startTop + height - textHeight / 4;//上边距+图片文字内容高度
                        int w = textY - textHeight / 2 - topPadding;
//                        width = height = height - textHeight - textTop;
                    }
                    int x = getPaddingLeft() + i * mItemWidth + (mItemWidth - width) / 2 + width / 2;
                    int y = mItemHeight / 2-24;
                    int r = mItemHeight / 2-24;
                    //圆的背景色
                    Paint paint = createPaint(Color.parseColor("#39C2D0"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        paint.setColorFilter(background.getColorFilter());
                    }
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(x, y, r, paint);
                }
            }
        }
    }

    /**
     * 进行绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!floatingEnable) {
            floatingUp = 0;
        } else {
            floatingUp = floatingUpInit;
        }
        Bitmap bitmap = drawable2Bitmap(background);
        canvas.drawBitmap(bitmap,0,floatingUpInit,mPaint);
        Rect rectInit = new Rect();
        rectInit.set(0, floatingUpInit, mWidth, mHeight);
        //画背景
        background.setBounds(rectInit);
        background.draw(canvas);

        //画Floating
        drawFloating(canvas);
        //画出所有导航菜单
        if (itemList.size() > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                Item item = itemList.get(i);
                drawItem(canvas, item, i);
            }
        }

    }













    ///////////////////////////////////界面的测量和适配//////////////////////
    //////////////////////////////////////////////////////////////////////////////
    /**
     * 获取文字宽度
     *
     * @param text  文字
     * @param paint 画笔
     * @return
     */
    private int getTextWidth(String text, Paint paint) {
        Rect rect = new Rect(); // 文字所在区域的矩形
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    /**
     * 尺寸测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mItemWidth = (mWidth - getPaddingLeft() - getPaddingRight()) / itemList.size();
        topPadding = getPaddingTop();
        bottomPadding = getPaddingBottom();
        mHeight += floatingUpInit;
        topPadding = getPaddingTop() + floatingUpInit;
        mItemHeight = mHeight > mItemWidth ? mItemWidth : mHeight;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.getMode(heightMeasureSpec)));
    }

    /**
     * 获取布局参数
     *
     * @return
     */
    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        ViewGroup.LayoutParams params = super.getLayoutParams();
        return params;
    }

    /**
     * 设置布局参数
     *
     * @param params
     */
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        //控制浮起高度不超过导航栏高度的一半
        floatingUp = floatingUp > params.height / 2 ? params.height / 2 : floatingUp;
        //LinearLayout布局
        if (params instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) params;
            layoutParams.topMargin = layoutParams.topMargin - floatingUp;
            //相对布局
        } else if (params instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) params;
            layoutParams.topMargin = layoutParams.topMargin - floatingUp;
        } else if (params instanceof FrameLayout.LayoutParams) {
            //FrameLayout布局
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) params;
            layoutParams.topMargin = layoutParams.topMargin - floatingUp;
        }
        super.setLayoutParams(params);
    }












    /**
     * dp转换为px
     *
     * @param resources
     * @param dpValue
     * @return
     */
    public static int dpToPx(Resources resources, float dpValue) {
        float scale = resources.getDisplayMetrics().density;//获得当前屏幕密度
        return (int) (dpValue * scale + 0.5f);
    }




}
