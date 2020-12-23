package itsmagic.present.easierspinner;

import android.content.Context;
import android.util.AttributeSet;

/**
 * <p/>
 * A basic {@link EasierSpinnerCore} object.
 * This uses a {@link String} as its item type.
 */
public class StringSpinner extends RefreshableEasierSpinnerCore<String> {

    public StringSpinner(Context context) {
        super(context);
    }

    public StringSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPrepare() {
        // No need to initialize anything
    }
}
