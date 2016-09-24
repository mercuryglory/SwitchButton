package com.wzh.switchbutton.view;

import com.wzh.switchbutton.R;
import com.wzh.switchbutton.R.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SwitchButton extends View {

	private Paint paint;
	private Bitmap switchBitmap;
	private Bitmap slideBitmap;
	private int maxLeft;
	private int mSlideLeft = 0;
	private boolean isOpen = false;
	private OnCheckChangeListener mOnCheckChangeListener; // 状态监听接口
	private int downX;

	private int moveDiffX = 0;
	private boolean isClick;

	// 带有样式的时候,创建该控件时调用
	public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	// 在xml中引用该控件时调用
	public SwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		String namespace = "http://schemas.android.com/apk/res-auto";
		// 获取对应的自定义属性的值并修改控件的状态
		boolean isOpen = attrs.getAttributeBooleanValue(namespace, "isOpen",
				false);
		if(isOpen){
			mSlideLeft=maxLeft;
		}else{
			mSlideLeft=0;
		}
		
		//获取滑块的背景
		int slidebitmapId = attrs.getAttributeResourceValue(namespace, "slidebitmap", -1);
		if(slidebitmapId>-1){
			slideBitmap=BitmapFactory.decodeResource(getResources(), slidebitmapId);
		}
				
		invalidate();
	}

	// 当使用代码创建该控件时调用
	public SwitchButton(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	private void init() {
		paint = new Paint();
		paint.setColor(Color.RED);
		// 按钮背景图片
		switchBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_background);
		// 滑块图片
		slideBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.slide_button);

		maxLeft = switchBitmap.getWidth() - slideBitmap.getWidth();

		// 给控件设置点击事件
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isClick) {
					// 根据当前状态设置开关
					if (isOpen) {
						// 是开的就关
						mSlideLeft = 0;
					} else {
						mSlideLeft = maxLeft;
					}
					isOpen = !isOpen;
					if (mOnCheckChangeListener != null) {
						mOnCheckChangeListener.onCheckChanged(isOpen);
					}

					// 让onDraw重新执行
					invalidate();
				}

			}
		});
	}

	// 测量方法
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		setMeasuredDimension(100, 100);
		setMeasuredDimension(switchBitmap.getWidth(), switchBitmap.getHeight());
	}

	// 绘制方法
	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawRect(0, 0, 200, 200, paint);

		canvas.drawBitmap(switchBitmap, 0, 0, null);
		canvas.drawBitmap(slideBitmap, mSlideLeft, 0, null);
	}

	// 定义出状态值返回的接口
	public interface OnCheckChangeListener {
		public void onCheckChanged(boolean isOpen);
	}

	// 设置一个set方法
	public void setOnCheckChangeListener(OnCheckChangeListener listener) {
		this.mOnCheckChangeListener = listener;
	}

	// 当手指触摸控件的时候，这个方法就会被调用
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;

		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int diffX = moveX - downX;

			moveDiffX += Math.abs(diffX);

			mSlideLeft += diffX;
			if (mSlideLeft < 0) {
				mSlideLeft = 0;
			}
			if (mSlideLeft > maxLeft) {
				mSlideLeft = maxLeft;
			}
			// left一改变，就需要重新绘制界面
			invalidate();
			downX = moveX;

			break;
		case MotionEvent.ACTION_UP:
			if (moveDiffX > 5) {
				isClick = false;
			} else {
				isClick = true;
			}

			moveDiffX = 0;

			if (!isClick) {
				// 计算出中心线
				int center = maxLeft / 2;
				if (mSlideLeft < center) {
					mSlideLeft = 0;
					isOpen = false;
				} else {
					mSlideLeft = maxLeft;
					isOpen = true;
				}
				invalidate();
				if (mOnCheckChangeListener != null) {
					mOnCheckChangeListener.onCheckChanged(isOpen);
				}
			}
			break;

		default:
			break;
		}

		// 如果为true由我们自己进行处理触摸事件,view没有获取到触摸事件的,就不能执行点击事件
		return super.onTouchEvent(event);
//		return true;
	}
}
