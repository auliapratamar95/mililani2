package itsmagic.present.easierspinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * <p/>
 * The core class for EasierSpinner
 */
public abstract class RefreshableEasierSpinnerCore<DATA_TYPE> extends EasierSpinnerCore<DATA_TYPE> {

    /** The {@link ImageView} widget */
    private ImageView mRefreshImage;

    /** The image resource */
    private int mImageRes = -1;

    /** The image color */
    private int mImageColor = -1;

    /** The refresh listener */
    private OnSpinnerRefreshListener mRefreshListener;

    public RefreshableEasierSpinnerCore(Context context) {
        super(context);
    }

    public RefreshableEasierSpinnerCore(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnSpinnerRefreshListener getOnSpinnerRefreshListener() {
        return mRefreshListener;
    }

    public void setOnSpinnerRefreshListener(OnSpinnerRefreshListener listener) {
        mRefreshListener = listener;
    }

    @Override
    protected int initSpinnerLayoutRes() {
        return R.layout.layout_easier_spinner_refresh;
    }

    @Override
    protected void initEasierSpinnerAttributeSet(Context context, TypedArray attr) {
        // Get the Refresh button icon and color
        mImageRes = attr.getResourceId(R.styleable.EasierSpinner_easyspinner_refreshImage, -1);
        mImageColor = attr.getColor(R.styleable.EasierSpinner_easyspinner_refreshImageColor, -1);
    }

    @Override
    protected void onInitialization() {
        mRefreshImage = findViewById(R.id.easierspinner_refresh);

        // Set the Refresh button icon if applicable
        if (mImageRes != -1) mRefreshImage.setImageResource(mImageRes);

        // Set the Refresh button color if applicable
        if (mImageColor != -1) mRefreshImage.setColorFilter(mImageColor, PorterDuff.Mode.SRC_IN);

        // Add on click listener for the refresh icon
        mRefreshImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRefreshListener != null) mRefreshListener.onSpinnerRefresh(view);
            }
        });
    }

    /** Hide the refresh icon */
    public void dismissRefreshIcon() {
        mRefreshImage.setVisibility(GONE);
    }

    /** Display the refresh icon */
    public void showRefreshIcon() {
        mRefreshImage.setVisibility(VISIBLE);
    }

    /** Set the refresh icon image */
    public void setRefreshImage(int imageRes) {
        mImageRes = imageRes;
        mRefreshImage.setImageResource(mImageRes);
    }

    /** Set the refresh icon color */
    public void setRefreshImageColor(int color, PorterDuff.Mode mode) {
        mImageColor = color;
        mRefreshImage.setColorFilter(mImageColor, mode);
    }

    /** Clears the refresh icon color */
    public void clearRefreshImageColor() {
        mImageColor = -1;
        mRefreshImage.clearColorFilter();
    }

    /** The refresh listener */
    public interface OnSpinnerRefreshListener {

        /** Called when the refresh button is clicked */
        void onSpinnerRefresh(View view);
    }
}
