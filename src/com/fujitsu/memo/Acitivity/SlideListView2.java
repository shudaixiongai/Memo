package com.fujitsu.memo.Acitivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * ���򻬳��˵���ListView ʹ����ע����ListView��Item�Ĳ�����ϣ�
 * ��Ч����ʵ���ǻ�����Item�Ĳ�����ͨ������PaddingLeft��PaddingRight���������Ҳ˵��ģ�
 * ����ʹ�ô�ListViewʱ��������ڲ���Itemʱʹ��PaddingLeft��PaddingRight��
 * �����Լ���д��ListView���Ѵﵽ��Ҫ��ʵ�ַ�ʽ
 * 
 * @author zhangshuo
 */
public class SlideListView2 extends ListView {

	/** ��ֹ�໬ģʽ */
	public static int MOD_FORBID = 0;
	/** �������һ����˵�ģʽ */
	public static int MOD_LEFT = 1;
	/** �������󻬳��˵�ģʽ */
	public static int MOD_RIGHT = 2;
	/** ���Ҿ����Ի����˵�ģʽ */
	public static int MOD_BOTH = 3;
	/** ��ǰ��ģʽ */
	private int mode = MOD_FORBID;
	/** ���˵��ĳ��� */
	private int leftLength = 0;
	/** �Ҳ�˵��ĳ��� */
	private int rightLength = 0;

	/**
	 * ��ǰ������ListView��position
	 */
	private int slidePosition;
	/**
	 * ��ָ����X������
	 */
	private int downY;
	/**
	 * ��ָ����Y������
	 */
	private int downX;
	/**
	 * ListView��item
	 */
	private View itemView;
	/**
	 * ������
	 */
	private Scroller scroller;
	/**
	 * ��Ϊ���û���������С����
	 */
	private int mTouchSlop;

	/**
	 * �ж��Ƿ���Բ��򻬶�
	 */
	private boolean canMove = false;
	/**
	 * ��ʾ�Ƿ���ɲ໬
	 */
	private boolean isSlided = false;

	public SlideListView2(Context context) {
		this(context, null);
	}

	public SlideListView2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideListView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	/**
	 * ��ʼ���˵��Ļ���ģʽ
	 * 
	 * @param mode
	 */
	public void initSlideMode(int mode) {
		this.mode = mode;
	}

	/**
	 * ���������϶�ListView item���߼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		int lastX = (int) ev.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("touch-->" + "down");

			/* ��ǰģʽ������������ֱ�ӷ��أ�����ListView����ȥ���� */
			if (this.mode == MOD_FORBID) {
				return super.onTouchEvent(ev);
			}

			// ������ڲ໬���״̬���໬��ȥ����ֱ�ӷ���
			if (isSlided) {
				scrollBack();
				return false;
			}
			// ����scroller������û�н���������ֱ�ӷ���
			if (!scroller.isFinished()) {
				return false;
			}
			downX = (int) ev.getX();
			downY = (int) ev.getY();

			slidePosition = pointToPosition(downX, downY);

			// ��Ч��position, �����κδ���
			if (slidePosition == AdapterView.INVALID_POSITION) {
				return super.onTouchEvent(ev);
			}

			// ��ȡ���ǵ����item view
			itemView = getChildAt(slidePosition - getFirstVisiblePosition());

			/* �˴��������õĻ���ģʽ���Զ���ȡ�����Ҳ�˵��ĳ��� */
			if (this.mode == MOD_BOTH) {
				this.leftLength = -itemView.getPaddingLeft();
				this.rightLength = -itemView.getPaddingRight();
			} else if (this.mode == MOD_LEFT) {
				this.leftLength = -itemView.getPaddingLeft();
			} else if (this.mode == MOD_RIGHT) {
				this.rightLength = -itemView.getPaddingRight();
			}

			break;
		case MotionEvent.ACTION_MOVE:
			System.out.println("touch-->" + "move");

			if (!canMove
					&& slidePosition != AdapterView.INVALID_POSITION
					&& (Math.abs(ev.getX() - downX) > mTouchSlop && Math.abs(ev
							.getY() - downY) < mTouchSlop)) {
				int offsetX = downX - lastX;
				if (offsetX > 0
						&& (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)) {
					/* �������� */
					canMove = true;
				} else if (offsetX < 0
						&& (this.mode == MOD_BOTH || this.mode == MOD_LEFT)) {
					/* �������һ� */
					canMove = true;
				} else {
					canMove = false;
				}
				/*
				 * �˶δ�����Ϊ�˱��������ڲ��򻬶�ʱͬʱ����ListView��OnItemClickListenerʱ
				 * ��
				 */
				MotionEvent cancelEvent = MotionEvent.obtain(ev);
				cancelEvent
						.setAction(MotionEvent.ACTION_CANCEL
								| (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
				onTouchEvent(cancelEvent);
			}
			if (canMove) {
				/* ���ô����ԣ������ڲ��򻬶�ʱ������ListView�������¹��� */
				requestDisallowInterceptTouchEvent(true);

				// ��ָ�϶�itemView����, deltaX����0���������С��0���ҹ�
				int deltaX = downX - lastX;
				if (deltaX < 0
						&& (this.mode == MOD_BOTH || this.mode == MOD_LEFT)) {
					/* ���� */
					itemView.scrollTo(deltaX, 0);
				} else if (deltaX > 0
						&& (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)) {
					/* ���һ� */
					itemView.scrollTo(deltaX, 0);
				} else {
					itemView.scrollTo(0, 0);
				}
				return true; // �϶���ʱ��ListView������
			}
		case MotionEvent.ACTION_UP:
			System.out.println("touch-->" + "up");
			if (canMove) {
				canMove = false;
				scrollByDistanceX();
			}
			break;
		}

		// ����ֱ�ӽ���ListView������onTouchEvent�¼�
		return super.onTouchEvent(ev);
	}

	/**
	 * ������ָ����itemView�ľ������ж��ǹ�������ʼλ�û�������������ҹ���
	 */
	private void scrollByDistanceX() {
		/* ��ǰģʽ������������ֱ�ӷ��� */
		if (this.mode == MOD_FORBID) {
			return;
		}
		if (itemView.getScrollX() > 0
				&& (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)) {
			/* �������� */
			if (itemView.getScrollX() >= rightLength / 2) {
				scrollLeft();
			} else {
				// ���ص�ԭʼλ��
				scrollBack();
			}
		} else if (itemView.getScrollX() < 0
				&& (this.mode == MOD_BOTH || this.mode == MOD_LEFT)) {
			/* �������һ� */
			if (itemView.getScrollX() <= -leftLength / 2) {
				scrollRight();
			} else {
				// ���ص�ԭʼλ��
				scrollBack();
			}
		} else {
			// ���ص�ԭʼλ��
			scrollBack();
		}

	}

	/**
	 * ���һ�����getScrollX()���ص������Ե�ľ��룬������View���ԵΪԭ�㵽��ʼ�����ľ��룬������
	 * �ұ߻���Ϊ��ֵ
	 */
	private void scrollRight() {
		isSlided = true;
		final int delta = (leftLength + itemView.getScrollX());
		// ����startScroll����������һЩ�����Ĳ�����������computeScroll()�����е���scrollTo������item
		scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
				Math.abs(delta));
		postInvalidate(); // ˢ��itemView
	}

	/**
	 * ���󻬶���������������֪�����󻬶�Ϊ��ֵ
	 */
	private void scrollLeft() {
		isSlided = true;
		final int delta = (rightLength - itemView.getScrollX());
		// ����startScroll����������һЩ�����Ĳ�����������computeScroll()�����е���scrollTo������item
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0,
				Math.abs(delta));
		postInvalidate(); // ˢ��itemView
	}

	/**
	 * ������ԭ����λ��
	 */
	private void scrollBack() {
		isSlided = false;
		scroller.startScroll(itemView.getScrollX(), 0, -itemView.getScrollX(),
				0, Math.abs(itemView.getScrollX()));
		postInvalidate(); // ˢ��itemView
	}

	@Override
	public void computeScroll() {
		// ����startScroll��ʱ��scroller.computeScrollOffset()����true��
		if (scroller.computeScrollOffset()) {
			// ��ListView item���ݵ�ǰ�Ĺ���ƫ�������й���
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}
	}

	/**
	 * �ṩ���ⲿ���ã����Խ��໬�����Ļ���ȥ
	 */
	public void slideBack() {
		this.scrollBack();
	}

}