package itsmagic.present.imagepicker.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.util.List;

import itsmagic.present.imagepicker.util.ImagePickerUtil;

/**
 * <p/>
 * The custom GUI-less fragment for the class.
 */
public class ImagePickerFragment extends Fragment {

    /** The listener for activity result */
    private OnResultListener mListener;

    /** The output {@link Uri} */
    private Uri mOutputUri;

    /** The bundle key for the {@link Intent} */
    private static final String KEY_INTENT = "intent";

    /**
     * The bundle key that determines which activity this fragment handles.
     * <p/>
     * Currently this key can handle:
     * <ul>
     *     <li>{@link ImagePickerUtil#REQUEST_CODE_CROP}</li>
     *     <li>{@link ImagePickerUtil#REQUEST_CODE_CAMERA}</li>
     *     <li>{@link ImagePickerUtil#REQUEST_CODE_PICKER}</li>
     * </ul>
     */
    private static final String KEY_CODE = "request_code";

    /** The bundle key that determines if this fragment should delete the temp file */
    private static final String KEY_FILE = "temporary_file";

    /**
     * Creates a new instance of this class.
     * @param intent The specified {@link Intent}
     * @param requestCode The specified request code
     * @return A new {@link ImagePickerFragment} instance
     */
    public static ImagePickerFragment newInstance(Intent intent, int requestCode) {
        return newInstance(intent, requestCode, null);
    }

    /**
     * Creates a new instance of this class.
     * @param intent The specified {@link Intent}
     * @param requestCode The specified request code
     * @param tempFile The temporary image file
     * @return A new {@link ImagePickerFragment} instance
     */
    public static ImagePickerFragment newInstance(Intent intent, int requestCode, File tempFile) {
        ImagePickerFragment fragment = new ImagePickerFragment();

        Bundle args = new Bundle();
        args.putParcelable(KEY_INTENT, intent);
        args.putInt(KEY_CODE, requestCode);
        if (tempFile != null) args.putSerializable(KEY_FILE, tempFile);
        fragment.setArguments(args);

        return(fragment);
    }

    /**
     * Get the specified {@link Intent} for this {@link Fragment}
     * @return The specified {@link Intent}
     */
    public Intent getIntent() {
        return getArguments().getParcelable(KEY_INTENT);
    }

    /**
     * Get the request code for this {@link Fragment}
     * @return The request code
     */
    public int getRequestCode() {
        return getArguments().getInt(KEY_CODE);
    }

    /**
     * Get the request code for this {@link Fragment}
     * @return The request code
     */
    public File getTemporaryFile() {
        return (File) getArguments().getSerializable(KEY_FILE);
    }

    /**
     * Set the result listener for this {@link Fragment}
     * @param listener The result listener
     */
    public void setOnResultListener(OnResultListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();

        // Grant write/read uri permission for possible apps
        // Only if output uri is available
        mOutputUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        if (mOutputUri != null) {
            final Context context = getActivity();
            final List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, mOutputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }

        startActivityForResult(intent, getRequestCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deleteTemporaryFile();

        if (mOutputUri != null) {
            final Context context = getActivity();
            context.revokeUriPermission(mOutputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mListener != null) mListener.onResult(requestCode, resultCode, data);
        deleteTemporaryFile();
    }

    /** Deletes the temporary image from storage */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteTemporaryFile() {
        if (getTemporaryFile() != null) {
            getTemporaryFile().delete();
        }
    }

    /** The {@link android.app.Activity} result listener interface */
    public interface OnResultListener {

        /** Called when this fragment obtained the {@link android.app.Activity} result */
        void onResult(int requestCode, int resultCode, Intent data);
    }
}
