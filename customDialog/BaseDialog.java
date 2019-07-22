package id.meteor.alfamind.customDialog;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public abstract class BaseDialog<T extends BaseDialog<T>> extends Dialog {

    protected String mTag;
    protected Context mContext;
    protected DisplayMetrics mDisplayMetrics;
    protected boolean mCancel;
    protected float mWidthScale = 1;
    protected float mHeightScale;
    private BaseAnimatorSet mShowAnim;
    private BaseAnimatorSet mDismissAnim;
    protected LinearLayout mLlTop;
    protected LinearLayout mLlControlHeight;
    protected View mOnCreateView;
    private boolean mIsShowAnim;
    private boolean mIsDismissAnim;
    protected float mMaxHeight;
    private boolean mIsPopupStyle;
    private boolean mAutoDismiss;
    private long mAutoDismissDelay = 1500;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public BaseDialog(Context context) {
        super(context);
        setDialogTheme();
        mContext = context;
        mTag = getClass().getSimpleName();
        setCanceledOnTouchOutside(true);
        Log.d(mTag, "constructor");
    }

    public BaseDialog(Context context, boolean isPopupStyle) {
        this(context);
        mIsPopupStyle = isPopupStyle;
    }

    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    public abstract View onCreateView();

    public void onViewCreated(View inflate) {}

    public abstract void setUiBeforShow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(mTag, "onCreate");
        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mMaxHeight = mDisplayMetrics.heightPixels - StatusBarUtils.getHeight(mContext);
        // mMaxHeight = mDisplayMetrics.heightPixels;

        mLlTop = new LinearLayout(mContext);
        mLlTop.setGravity(Gravity.CENTER);

        mLlControlHeight = new LinearLayout(mContext);
        mLlControlHeight.setOrientation(LinearLayout.VERTICAL);

        mOnCreateView = onCreateView();
        mLlControlHeight.addView(mOnCreateView);
        mLlTop.addView(mLlControlHeight);
        onViewCreated(mOnCreateView);

        if (mIsPopupStyle) {
            setContentView(mLlTop, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            setContentView(mLlTop, new ViewGroup.LayoutParams(mDisplayMetrics.widthPixels, (int) mMaxHeight));
        }

        mLlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancel) {
                    dismiss();
                }
            }
        });

        mOnCreateView.setClickable(true);
    }

    public View getCreateView() {
        return mOnCreateView;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(mTag, "onAttachedToWindow");

        setUiBeforShow();

        int width;
        if (mWidthScale == 0) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            width = (int) (mDisplayMetrics.widthPixels * mWidthScale);
        }

        int height;
        if (mHeightScale == 0) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (mHeightScale == 1) {
//            height = ViewGroup.LayoutParams.MATCH_PARENT;
            height = (int) mMaxHeight;
        } else {
            height = (int) (mMaxHeight * mHeightScale);
        }

        mLlControlHeight.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        if (mShowAnim != null) {
            mShowAnim.listener(new BaseAnimatorSet.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mIsShowAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mIsShowAnim = false;
                    delayDismiss();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mIsShowAnim = false;
                }
            }).playOn(mLlControlHeight);
        } else {
            BaseAnimatorSet.reset(mLlControlHeight);
            delayDismiss();
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        this.mCancel = cancel;
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void show() {
        Log.d(mTag, "show");
        super.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(mTag, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(mTag, "onStop");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(mTag, "onDetachedFromWindow");
    }

    @Override
    public void dismiss() {
        Log.d(mTag, "dismiss");
        if (mDismissAnim != null) {
            mDismissAnim.listener(new BaseAnimatorSet.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mIsDismissAnim = true;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mIsDismissAnim = false;
                    superDismiss();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mIsDismissAnim = false;
                    superDismiss();
                }
            }).playOn(mLlControlHeight);
        } else {
            superDismiss();
        }
    }

    public void superDismiss() {
        super.dismiss();
    }

    public void show(int animStyle) {
        Window window = getWindow();
        if (window != null)
        window.setWindowAnimations(animStyle);
        show();
    }

    public void showAtLocation(int gravity, int x, int y) {
        if (mIsPopupStyle) {
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            window.setGravity(gravity);
            params.x = x;
            params.y = y;
        }

        show();
    }

    public void showAtLocation(int x, int y) {
        int gravity = Gravity.LEFT | Gravity.TOP;
        showAtLocation(gravity, x, y);
    }

    public T dimEnabled(boolean isDimEnabled) {
        if (isDimEnabled) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        return (T) this;
    }

    public T widthScale(float widthScale) {
        this.mWidthScale = widthScale;
        return (T) this;
    }

    public T heightScale(float heightScale) {
        mHeightScale = heightScale;
        return (T) this;
    }

    public T showAnim(BaseAnimatorSet showAnim) {
        mShowAnim = showAnim;
        return (T) this;
    }

    public T dismissAnim(BaseAnimatorSet dismissAnim) {
        mDismissAnim = dismissAnim;
        return (T) this;
    }

    public T autoDismiss(boolean autoDismiss) {
        mAutoDismiss = autoDismiss;
        return (T) this;
    }

    public T autoDismissDelay(long autoDismissDelay) {
        mAutoDismissDelay = autoDismissDelay;
        return (T) this;
    }

    private void delayDismiss() {
        if (mAutoDismiss && mAutoDismissDelay > 0) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, mAutoDismissDelay);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsDismissAnim || mIsShowAnim || mAutoDismiss) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (mIsDismissAnim || mIsShowAnim || mAutoDismiss) {
            return;
        }
        super.onBackPressed();
    }

    /** dp to px */
    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
