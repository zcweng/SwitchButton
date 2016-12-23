package com.suke.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;


/**
 * SwitchButton.
 *
 * <attr name="sb_shadow_radius" format="reference|dimension"/>
 * <attr name="sb_shadow_offset" format="reference|dimension"/>
 * <attr name="sb_shadow_color" format="reference|color"/>
 * <attr name="sb_uncheck_color" format="reference|color"/>
 * <attr name="sb_checked_color" format="reference|color"/>
 *
 * <attr name="sb_border_width" format="reference|dimension"/>
 * <attr name="sb_checkline_color" format="reference|color"/>
 * <attr name="sb_checkline_width" format="reference|dimension"/>
 * <attr name="sb_uncheckcircle_color" format="reference|color"/>
 * <attr name="sb_uncheckcircle_width" format="reference|dimension"/>
 * <attr name="sb_uncheckcircle_radius" format="reference|dimension"/>
 *
 * <attr name="sb_checked" format="reference|boolean"/>
 * <attr name="sb_shadow_effect" format="reference|boolean"/>
 * <attr name="sb_effect_duration" format="reference|integer"/>
 * <attr name="sb_button_color" format="reference|color"/>
 * <attr name="sb_show_indicator" format="reference|boolean"/>
 *
 */
public class SwitchButton extends View implements Checkable {
    private static final int DEFAULT_WIDTH = dp2pxInt(58);
    private static final int DEFAULT_HEIGHT = dp2pxInt(36);

    /**
     * 动画状态：
     * 1.静止
     * 2.进入拖动
     * 3.处于拖动
     * 4.拖动-复位
     * 5.拖动-切换
     * 6.点击切换
     * **/
    private final int ANIMATE_STATE_NONE = 0;
    private final int ANIMATE_STATE_PENDING_DRAG = 1;
    private final int ANIMATE_STATE_DRAGING = 2;
    private final int ANIMATE_STATE_PENDING_RESET = 3;
    private final int ANIMATE_STATE_PENDING_SETTLE = 4;
    private final int ANIMATE_STATE_SWITCH = 5;

    public SwitchButton(Context context) {
        super(context);
        init(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public final void setPadding(int left, int top, int right, int bottom) {
        //do nothing
        throw new RuntimeException("illegal call : method [setPadding]");
    }

    /**
     * 初始化参数
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = null;
        if(attrs != null){
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        }

        shadowEffect = optBoolean(typedArray,
                R.styleable.SwitchButton_sb_shadow_effect,
                true);

        uncheckCircleColor = optColor(typedArray,
                R.styleable.SwitchButton_sb_uncheckcircle_color,
                0XffAAAAAA);//0XffAAAAAA;

        uncheckCircleWidth = optPixelSize(typedArray,
                R.styleable.SwitchButton_sb_uncheckcircle_width,
                dp2pxInt(1.5f));//dp2pxInt(1.5f);

        uncheckCircleOffsetX = dp2px(10);

        uncheckCircleRadius = optPixelSize(typedArray,
                R.styleable.SwitchButton_sb_uncheckcircle_radius,
                dp2px(4));//dp2px(4);

        checkedLineOffsetX = dp2px(4);
        checkedLineOffsetY = dp2px(4);

        shadowRadius = optPixelSize(typedArray,
                R.styleable.SwitchButton_sb_shadow_radius,
                dp2pxInt(2.5f));//dp2pxInt(2.5f);

        shadowOffset = optPixelSize(typedArray,
                R.styleable.SwitchButton_sb_shadow_offset,
                dp2pxInt(1.5f));//dp2pxInt(1.5f);

        shadowColor = optColor(typedArray,
                R.styleable.SwitchButton_sb_shadow_color,
                0X33000000);//0X33000000;

        uncheckColor = optColor(typedArray,
                R.styleable.SwitchButton_sb_uncheck_color,
                0XffDDDDDD);//0XffDDDDDD;

        checkedColor = optColor(typedArray,
                R.styleable.SwitchButton_sb_checked_color,
                0Xff51d367);//0Xff51d367;

        borderWidth = optPixelSize(typedArray,
                R.styleable.SwitchButton_sb_border_width,
                dp2pxInt(1));//dp2pxInt(1);

        checkLineColor = optColor(typedArray,
                R.styleable.SwitchButton_sb_checkline_color,
                Color.WHITE);//Color.WHITE;

        checkLineWidth = optPixelSize(typedArray,
                R.styleable.SwitchButton_sb_checkline_width,
                dp2pxInt(1f));//dp2pxInt(1.0f);

        checkLineLength = dp2px(6);

        int buttonColor = optColor(typedArray,
                R.styleable.SwitchButton_sb_button_color,
                Color.WHITE);//Color.WHITE;

        int effectDuration = optInt(typedArray,
                R.styleable.SwitchButton_sb_effect_duration,
                300);//300;

        isChecked = optBoolean(typedArray,
                R.styleable.SwitchButton_sb_checked,
                false);

        showIndicator = optBoolean(typedArray,
                R.styleable.SwitchButton_sb_show_indicator,
                true);

        background = optColor(typedArray,
                R.styleable.SwitchButton_sb_background,
                Color.WHITE);//Color.WHITE;

        enableEffect = optBoolean(typedArray,
                R.styleable.SwitchButton_sb_enable_effect,
                true);

        if(typedArray != null){
            typedArray.recycle();
        }


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonPaint.setColor(buttonColor);

        if(shadowEffect){
            buttonPaint.setShadowLayer(
                    shadowRadius,
                    0, shadowOffset,
                    shadowColor);
        }


        viewState = new ViewState();
        beforeState = new ViewState();
        afterState = new ViewState();

        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(effectDuration);
        valueAnimator.setRepeatCount(0);

        valueAnimator.addUpdateListener(animatorUpdateListener);
        valueAnimator.addListener(animatorListener);


        super.setPadding(0,0,0,0);
        super.setOnClickListener(innerOnClickListener);
        super.setOnLongClickListener(innerOnLongClickListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.UNSPECIFIED
                || widthMode == MeasureSpec.AT_MOST){
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_WIDTH, MeasureSpec.EXACTLY);
        }
        if(heightMode == MeasureSpec.UNSPECIFIED
                || heightMode == MeasureSpec.AT_MOST){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    class ViewState {
        float buttonX;
        int checkStateColor;
        int checkLineColor;
        float radius;
        ViewState(){}
        private void copy(ViewState source){
            this.buttonX = source.buttonX;
            this.checkStateColor = source.checkStateColor;
            this.checkLineColor = source.checkLineColor;
            this.radius = source.radius;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        float viewPadding = Math.max(shadowRadius + shadowOffset, borderWidth);

        height = h - viewPadding - viewPadding;
        width = w - viewPadding - viewPadding;

        viewRadius = height * .5f;
        buttonRadius = viewRadius - borderWidth;

        left = viewPadding;
        top = viewPadding;
        right = w - viewPadding;
        bottom = h - viewPadding;

        centerX = (left + right) * .5f;
        centerY = (top + bottom) * .5f;

        buttonMinX = left + viewRadius;
        buttonMaxX = right - viewRadius;

        if(isChecked()){
            setCheckedViewState(viewState);
        }else{
            setUncheckViewState(viewState);
        }

        isUiInited = true;

        postInvalidate();

    }

    private void setUncheckViewState(ViewState viewState){
        viewState.radius = 0;
        viewState.checkStateColor = uncheckColor;
        viewState.checkLineColor = Color.TRANSPARENT;
        viewState.buttonX = buttonMinX;
    }

    private void setCheckedViewState(ViewState viewState){
        viewState.radius = viewRadius;
        viewState.checkStateColor = checkedColor;
        viewState.checkLineColor = checkLineColor;
        viewState.buttonX = buttonMaxX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStrokeWidth(borderWidth);
        paint.setStyle(Paint.Style.FILL);
        //绘制白色背景
        paint.setColor(background);
        drawRoundRect(canvas,
                left, top, right, bottom,
                viewRadius, paint);
        //绘制关闭状态的边框
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(uncheckColor);
        drawRoundRect(canvas,
                left, top, right, bottom,
                viewRadius, paint);

        //绘制小圆圈
        if(showIndicator){
            drawUncheckIndicator(canvas);
        }

        //绘制开启背景色
        float des = viewState.radius * .5f;//[0-backgroundRadius*0.5f]
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(viewState.checkStateColor);
        paint.setStrokeWidth(borderWidth + des * 2f);
        drawRoundRect(canvas,
                left + des, top + des, right - des, bottom - des,
                viewRadius, paint);

        //绘制按钮左边绿色长条遮挡
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        drawArc(canvas,
                left, top,
                left + 2 * viewRadius, top + 2 * viewRadius,
                90, 180, paint);
        canvas.drawRect(
                left + viewRadius, top,
                viewState.buttonX, top + 2 * viewRadius,
                paint);

        //绘制小线条
        if(showIndicator){
            drawCheckedIndicator(canvas);
        }

        //绘制按钮
        drawButton(canvas, viewState.buttonX, centerY);
    }


    /**
     * 绘制选中状态指示器
     * @param canvas
     */
    protected void drawCheckedIndicator(Canvas canvas) {
        drawCheckedIndicator(canvas,
                viewState.checkLineColor,
                checkLineWidth,
                left + viewRadius - checkedLineOffsetX, centerY - checkLineLength,
                left + viewRadius - checkedLineOffsetY, centerY + checkLineLength,
                paint);
    }


    /**
     * 绘制选中状态指示器
     * @param canvas
     * @param color
     * @param lineWidth
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param paint
     */
    protected void drawCheckedIndicator(Canvas canvas,
                                      int color,
                                      float lineWidth,
                                      float sx, float sy, float ex, float ey,
                                      Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(lineWidth);
        canvas.drawLine(
                sx, sy, ex, ey,
                paint);
    }

    /**
     * 绘制关闭状态指示器
     * @param canvas
     */
    private void drawUncheckIndicator(Canvas canvas) {
        drawUncheckIndicator(canvas,
                uncheckCircleColor,
                uncheckCircleWidth,
                right - uncheckCircleOffsetX, centerY,
                uncheckCircleRadius,
                paint);
    }


    /**
     * 绘制关闭状态指示器
     * @param canvas
     * @param color
     * @param lineWidth
     * @param centerX
     * @param centerY
     * @param radius
     * @param paint
     */
    protected void drawUncheckIndicator(Canvas canvas,
                                      int color,
                                      float lineWidth,
                                      float centerX, float centerY,
                                      float radius,
                                      Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(lineWidth);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    private void drawArc(Canvas canvas,
                 float left, float top,
                 float right, float bottom,
                 float startAngle, float sweepAngle,
                 Paint paint){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(left, top, right, bottom,
                    startAngle, sweepAngle, true, paint);
        }else{
            rect.set(left, top, right, bottom);
            canvas.drawArc(rect,
                    startAngle, sweepAngle, true, paint);
        }
    }

    private void drawRoundRect(Canvas canvas,
                       float left, float top,
                       float right, float bottom,
                       float backgroundRadius,
                       Paint paint){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom,
                    backgroundRadius, backgroundRadius, paint);
        }else{
            rect.set(left, top, right, bottom);
            canvas.drawRoundRect(rect,
                    backgroundRadius, backgroundRadius, paint);
        }
    }


    /**
     * @param canvas
     * @param x px
     * @param y px
     */
    private void drawButton(Canvas canvas, float x, float y) {
        canvas.drawCircle(x, y, buttonRadius, buttonPaint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(0XffDDDDDD);
        canvas.drawCircle(x, y, buttonRadius, paint);
    }

    @Override
    public void setChecked(boolean checked) {
        if(checked == isChecked()){
            postInvalidate();
            return;
        }
        toggle();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animate) {
        if(!isEnabled()){return;}

        if(isEventBroadcast){
            throw new RuntimeException("should NOT switch the state in method: [onCheckedChanged]!");
        }
        if(!isUiInited){
            isChecked = !isChecked;
            broadcastEvent();
            return;
        }

        if(valueAnimator.isRunning()){
            valueAnimator.cancel();
        }

        if(!enableEffect || !animate){
            isChecked = !isChecked;
            if(isChecked()){
                setCheckedViewState(viewState);
            }else{
                setUncheckViewState(viewState);
            }
            postInvalidate();
            broadcastEvent();
            return;
        }

        animateState = ANIMATE_STATE_SWITCH;
        beforeState.copy(viewState);

        if(isChecked()){
            //切换到unchecked
            setUncheckViewState(afterState);
        }else{
            setCheckedViewState(afterState);
        }
        valueAnimator.start();
    }

    private void broadcastEvent() {
        if(onCheckedChangeListener != null){
            isEventBroadcast = true;
            onCheckedChangeListener.onCheckedChanged(this, isChecked());
        }
        isEventBroadcast = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();

        switch (actionMasked){
            case MotionEvent.ACTION_DOWN:{
                isTouchingDown = true;
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                float eventX = event.getX();
                if(isDragState()){

                    float fraction = eventX / getWidth();
                    fraction = Math.max(0f, Math.min(1f, fraction));

                    viewState.buttonX = buttonMinX
                            + (buttonMaxX - buttonMinX)
                            * fraction;

                    viewState.checkStateColor = (int) argbEvaluator.evaluate(
                            fraction,
                            uncheckColor,
                            checkedColor
                    );

                    postInvalidate();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:{
                isTouchingDown = false;
                if(isDragState()){
                    float eventX = event.getX();
                    float fraction = eventX / getWidth();
                    fraction = Math.max(0f, Math.min(1f, fraction));
                    boolean newCheck = fraction > .5f;
                    if(newCheck == isChecked()){
                        pendingCancelDragState();
                    }else{
                        isChecked = newCheck;
                        pendingSettleState();
                    }
                }else if(isPendingDragState()){
                    pendingCancelDragState();
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }


    private boolean isInAnimating(){
        return animateState != ANIMATE_STATE_NONE;
    }
    private boolean isPendingDragState(){
        return animateState == ANIMATE_STATE_PENDING_DRAG
                || animateState == ANIMATE_STATE_PENDING_RESET;
    }
    private boolean isDragState(){
        return animateState == ANIMATE_STATE_DRAGING;
    }

    public void setShadowEffect(boolean shadowEffect) {
        if(this.shadowEffect == shadowEffect){return;}
        this.shadowEffect = shadowEffect;

        if(this.shadowEffect){
            buttonPaint.setShadowLayer(
                    shadowRadius,
                    0, shadowOffset,
                    shadowColor);
        }else{
            buttonPaint.setShadowLayer(
                    0,
                    0, 0,
                    0);
        }
    }

    /**
     * 80-50
     */
    private void pendingDragState() {
        if(isInAnimating()){return;}
        if(!isTouchingDown){return;}

        if(valueAnimator.isRunning()){
            valueAnimator.cancel();
        }

        animateState = ANIMATE_STATE_PENDING_DRAG;

        beforeState.copy(viewState);
        afterState.copy(viewState);

        if(isChecked()){
            afterState.checkStateColor = checkedColor;
            afterState.buttonX = buttonMaxX;
            afterState.checkLineColor = checkedColor;
        }else{
            afterState.checkStateColor = uncheckColor;
            afterState.buttonX = buttonMinX;
            afterState.radius = viewRadius;
        }

        valueAnimator.start();
    }


    private void pendingCancelDragState() {
        if(isDragState() || isPendingDragState()){
            if(valueAnimator.isRunning()){
                valueAnimator.cancel();
            }

            animateState = ANIMATE_STATE_PENDING_RESET;
            beforeState.copy(viewState);

            if(isChecked()){
                setCheckedViewState(afterState);
            }else{
                setUncheckViewState(afterState);
            }
            valueAnimator.start();
        }
    }


    private void pendingSettleState() {
        if(valueAnimator.isRunning()){
            valueAnimator.cancel();
        }

        animateState = ANIMATE_STATE_PENDING_SETTLE;
        beforeState.copy(viewState);

        if(isChecked()){
            setCheckedViewState(afterState);
        }else{
            setUncheckViewState(afterState);
        }
        valueAnimator.start();
    }


    @Override
    public void setOnClickListener(OnClickListener l) {
        delegateOnClickListener = l;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        delegateOnLongClickListener = l;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener l){
        onCheckedChangeListener = l;
    }

    public interface OnCheckedChangeListener{
        void onCheckedChanged(SwitchButton view, boolean isChecked);
    }

    /*******************************************************/
    private static float dp2px(float dp){
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private static int dp2pxInt(float dp){
        return (int) dp2px(dp);
    }

    private static int optInt(TypedArray typedArray,
                       int index,
                       int def) {
        if(typedArray == null){return def;}
        return typedArray.getInt(index, def);
    }


    private static float optPixelSize(TypedArray typedArray,
                               int index,
                               float def) {
        if(typedArray == null){return def;}
        return typedArray.getDimension(index, def);
    }

    private static int optPixelSize(TypedArray typedArray,
                             int index,
                             int def) {
        if(typedArray == null){return def;}
        return typedArray.getDimensionPixelOffset(index, def);
    }

    private static int optColor(TypedArray typedArray,
                         int index,
                         int def) {
        if(typedArray == null){return def;}
        return typedArray.getColor(index, def);
    }

    private static boolean optBoolean(TypedArray typedArray,
                                      int index,
                                      boolean def) {
        if(typedArray == null){return def;}
        return typedArray.getBoolean(index, def);
    }
    /*******************************************************/


    /**
     * 阴影半径
     */
    private int shadowRadius;
    /**
     * 阴影Y偏移px
     */
    private int shadowOffset;
    /**
     * 阴影颜色
     */
    private int shadowColor ;

    /**
     * 背景半径
     */
    private float viewRadius;
    /**
     * 按钮半径
     */
    private float buttonRadius;

    /**
     * 背景高
     */
    private float height ;
    /**
     * 背景宽
     */
    private float width;
    /**
     * 背景位置
     */
    private float left   ;
    private float top    ;
    private float right  ;
    private float bottom ;
    private float centerX;
    private float centerY;

    /**
     * 背景底色
     */
    private int background;
    /**
     * 背景关闭颜色
     */
    private int uncheckColor;
    /**
     * 背景打开颜色
     */
    private int checkedColor;
    /**
     * 边框宽度px
     */
    private int borderWidth;

    /**
     * 打开指示线颜色
     */
    private int checkLineColor;
    /**
     * 打开指示线宽
     */
    private int checkLineWidth;
    /**
     * 打开指示线长
     */
    private float checkLineLength;
    /**
     * 关闭圆圈颜色
     */
    private int uncheckCircleColor;
    /**
     *关闭圆圈线宽
     */
    private int uncheckCircleWidth;
    /**
     *关闭圆圈位移X
     */
    private float uncheckCircleOffsetX;
    /**
     *关闭圆圈半径
     */
    private float uncheckCircleRadius;
    /**
     *打开指示线位移X
     */
    private float checkedLineOffsetX;
    /**
     *打开指示线位移Y
     */
    private float checkedLineOffsetY;


    /**
     * 按钮最左边
     */
    private float buttonMinX;
    /**
     * 按钮最右边
     */
    private float buttonMaxX;

    /**
     * 按钮画笔
     */
    private Paint buttonPaint;
    /**
     * 背景画笔
     */
    private Paint paint;

    /**
     * 当前状态
     */
    private ViewState viewState;
    private ViewState beforeState;
    private ViewState afterState;

    private RectF rect = new RectF();
    /**
     * 动画状态
     */
    private int animateState = ANIMATE_STATE_NONE;

    /**
     *
     */
    private ValueAnimator valueAnimator;

    private final android.animation.ArgbEvaluator argbEvaluator
            = new android.animation.ArgbEvaluator();

    /**
     *是否选中
     */
    private boolean isChecked;
    /**
     * 是否启用动画
     */
    private boolean enableEffect;
    /**
     * 是否启用阴影效果
     */
    private boolean shadowEffect;
    /**
     * 是否显示指示器
     */
    private boolean showIndicator;
    /**
     * 收拾是否按下
     */
    private boolean isTouchingDown = false;
    /**
     *
     */
    private boolean isUiInited = false;
    /**
     *
     */
    private boolean isEventBroadcast = false;

    private OnCheckedChangeListener onCheckedChangeListener;

    private OnClickListener
            delegateOnClickListener,
            innerOnClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                    if(delegateOnClickListener != null){
                        delegateOnClickListener.onClick(v);
                    }
                }
            };

    private OnLongClickListener delegateOnLongClickListener,
            innerOnLongClickListener = new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    pendingDragState();
                    return true;
                }
            };

    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener
            = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (Float) animation.getAnimatedValue();
            switch (animateState) {
                case ANIMATE_STATE_PENDING_RESET: {
                }
                case ANIMATE_STATE_PENDING_SETTLE: {
                }
                case ANIMATE_STATE_PENDING_DRAG: {
                    viewState.checkLineColor = (int) argbEvaluator.evaluate(
                            value,
                            beforeState.checkLineColor,
                            afterState.checkLineColor
                    );

                    viewState.radius = beforeState.radius
                            + (afterState.radius - beforeState.radius) * value;

                    viewState.buttonX = beforeState.buttonX
                            + (afterState.buttonX - beforeState.buttonX) * value;

                    viewState.checkStateColor = (int) argbEvaluator.evaluate(
                            value,
                            beforeState.checkStateColor,
                            afterState.checkStateColor
                    );

                    break;
                }
                case ANIMATE_STATE_SWITCH: {
                    viewState.buttonX = beforeState.buttonX
                            + (afterState.buttonX - beforeState.buttonX) * value;

                    float fraction = (viewState.buttonX - buttonMinX) / (buttonMaxX - buttonMinX);

                    viewState.checkStateColor = (int) argbEvaluator.evaluate(
                            fraction,
                            uncheckColor,
                            checkedColor
                    );

                    viewState.radius = fraction * viewRadius;
                    viewState.checkLineColor = (int) argbEvaluator.evaluate(
                            fraction,
                            Color.TRANSPARENT,
                            checkLineColor
                    );
                    break;
                }
                default:
                case ANIMATE_STATE_DRAGING: {
                }
                case ANIMATE_STATE_NONE: {
                    break;
                }
            }
            postInvalidate();
        }
    };

    private Animator.AnimatorListener animatorListener
            = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            switch (animateState) {
                case ANIMATE_STATE_DRAGING: {
                    break;
                }
                case ANIMATE_STATE_PENDING_DRAG: {
                    animateState = ANIMATE_STATE_DRAGING;
                    viewState.checkLineColor = Color.TRANSPARENT;
                    viewState.radius = viewRadius;

                    postInvalidate();
                    break;
                }
                case ANIMATE_STATE_PENDING_RESET: {
                    animateState = ANIMATE_STATE_NONE;
                    postInvalidate();
                    break;
                }
                case ANIMATE_STATE_PENDING_SETTLE: {
                    animateState = ANIMATE_STATE_NONE;
                    postInvalidate();
                    broadcastEvent();
                    break;
                }
                case ANIMATE_STATE_SWITCH: {
                    isChecked = !isChecked;
                    animateState = ANIMATE_STATE_NONE;
                    postInvalidate();
                    broadcastEvent();
                    break;
                }
                default:
                case ANIMATE_STATE_NONE: {
                    break;
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

}
