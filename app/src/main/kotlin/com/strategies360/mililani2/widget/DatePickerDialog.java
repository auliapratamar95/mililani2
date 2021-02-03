package com.strategies360.mililani2.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.strategies360.mililani2.R;

public class DatePickerDialog {

    private AlertDialog mDatePickerDialog;

    private DatePickerDialog(@NonNull final DatePickerDialog.Builder builder) {
        final View view = LayoutInflater.from(builder.context).inflate(R.layout.dialog_date_picker, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        if (builder.min != -1) datePicker.setMinDate(builder.min);
        if (builder.max != -1) datePicker.setMaxDate(builder.max);
        datePicker.setCalendarViewShown(builder.isCalendarViewShown);
        datePicker.setSpinnersShown(false);
        datePicker.init(builder.year, builder.monthOfYear, builder.dayOfMonth, null);

        mDatePickerDialog = new AlertDialog.Builder(builder.context, R.style.AppTheme_Dialog_Alert)
                .setTitle(builder.title)
                .setView(view)
                .setPositiveButton(android.R.string.ok,
                        (dialog, whichButton) -> {
                            if (builder.listener != null) {
                                builder.listener.onDateSet(
                                    DatePickerDialog.this, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, whichButton) -> dialog.dismiss())
                .setOnDismissListener(dialog -> {
                    if (builder.listener != null) {
                        builder.listener.onCancelled(DatePickerDialog.this);
                    }
                })
                .create();
    }

    /**
     * Display a date picker dialog
     */
    public void show() {
        if (mDatePickerDialog != null) mDatePickerDialog.show();
        else
            throw new IllegalStateException("Unable to show " + getClass().getSimpleName() + ", make sure you have built the dialog before showing");
    }

    /**
     * The dialog builder class
     */
    public static class Builder {

        private Context context;
        private String title;
        private long min;
        private long max;
        private int year;
        private int monthOfYear;
        private int dayOfMonth;
        private boolean isCalendarViewShown;
        private OnDateSetListener listener;

        /**
         * The constructor of this class
         */
        public Builder(Context context) {
            this.context = context;
            title = null;
            min = -1;
            max = -1;
            year = -1;
            monthOfYear = -1;
            dayOfMonth = -1;
            isCalendarViewShown = true;
        }

        /**
         * Sets the title for the dialog
         *
         * @param title the dialog title
         */
        public Builder withTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the title for the dialog
         *
         * @param titleRes the dialog title resource ID
         */
        public Builder withTitle(int titleRes) {
            this.title = context.getResources().getString(titleRes);
            return this;
        }

        /**
         * Sets the minimum date for the date picker
         *
         * @param min the minimum date (in millis)
         */
        public Builder withMinDate(long min) {
            this.min = min;
            return this;
        }

        /**
         * Sets the maximum date for the date picker
         *
         * @param max the maximum date (in millis)
         */
        public Builder withMaxDate(long max) {
            this.max = max;
            return this;
        }

        /**
         * Sets the default selection for the number picker
         *
         * @param year        the initial year.
         * @param monthOfYear the initial month <strong>starting from zero</strong>.
         * @param dayOfMonth  the initial day of the month.
         */
        public Builder withInitialSelection(int year, int monthOfYear, int dayOfMonth) {
            this.year = year;
            this.monthOfYear = monthOfYear;
            this.dayOfMonth = dayOfMonth;
            return this;
        }

        /**
         * Sets the {@link DatePicker}'s calendar view visibility.
         *
         * @param isCalendarViewShown determines if calendar view should be visible.
         */
        public Builder withCalendarView(boolean isCalendarViewShown) {
            this.isCalendarViewShown = isCalendarViewShown;
            return this;
        }

        /**
         * Sets the {@link DatePicker.OnDateChangedListener}.
         *
         * @param listener the listener
         */
        public Builder withOnDateSetListener(OnDateSetListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * Builds the {@link DatePickerDialog}.
         */
        public DatePickerDialog build() {
            return new DatePickerDialog(this);
        }
    }

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {

        /**
         * @param year        The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *                    with {@link java.util.Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         */
        void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth);

        /**
         * Called when the dialog is closed without any selection made.
         */
        void onCancelled(DatePickerDialog dialog);
    }
}
