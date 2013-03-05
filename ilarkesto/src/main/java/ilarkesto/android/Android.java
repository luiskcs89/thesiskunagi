package ilarkesto.android;

import ilarkesto.core.base.Str;
import ilarkesto.core.logging.Log;
import ilarkesto.io.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class Android {

	private static Log log = Log.get(Android.class);

	public static void saveBitmapAsJpg(Bitmap bitmap, File file) {
		saveBitmap(bitmap, file, Bitmap.CompressFormat.JPEG);
	}

	public static void saveBitmap(Bitmap bitmap, File file, Bitmap.CompressFormat format) {
		IO.createDirectory(file.getParent());
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		}
		bitmap.compress(format, 100, out);
		IO.closeQuiet(out);
	}

	public static Bitmap loadBitmap(File file, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file.getPath(), options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * Required permission: android.permission.GET_ACCOUNTS
	 */
	public static List<String> getUsersEmailAddressesFromAccounts(Context context) {
		List<String> ret = new ArrayList<String>();
		for (Account account : AccountManager.get(context).getAccounts()) {
			if (Str.isEmail(account.name) && !ret.contains(account.name)) ret.add(account.name);
		}
		return ret;
	}

	public static String getText(EditText editor) {
		if (editor == null) return null;
		Editable text = editor.getText();
		return text == null ? null : text.toString();
	}

	public static Integer getTextAsInteger(EditText editor) {
		String text = getText(editor);
		if (text == null) return null;
		if (text.trim().length() == 0) return null;
		return Integer.parseInt(text);
	}

	public static String text(Context context, int resId) {
		CharSequence text = context.getResources().getText(resId);
		String s = String.valueOf(text);
		s = s.replace("\\\\", "\\");
		s = s.replace("\\n", "\n");
		return s;
	}

	public static String text(Context context, int resId, Object... params) {
		return Str.replaceIndexedParams(text(context, resId), params);
	}

	public static void showToast(CharSequence text, Context context) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(int textResId, Context context) {
		Toast.makeText(context, textResId, Toast.LENGTH_SHORT).show();
	}

	public static boolean isInternetConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) return false;
		return networkInfo.isConnected();
	}

	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo == null) return false;
		return networkInfo.isConnected();
	}

	public static boolean isOnline(Context context) {
		NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (netInfo == null) return false;
		return netInfo.isConnectedOrConnecting();
	}

	public static void startPickImage(Activity context, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		context.startActivityForResult(intent, requestCode);
	}

	public static void startCameraPicture(Activity context, int requestCode) {
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// EXTRA_OUTPUT is buggy :-(
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		context.startActivityForResult(intent, requestCode);
	}

	public static void startSendEmail(Context context, String email, String subject) {
		startSendEmail(context, email, subject, "");
	}

	public static void startSendEmail(Context context, String email, String subject, String text) {
		final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { email });
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		context.startActivity(intent);
	}

	public static void startCallTelephoneActivity(Context context, String phoneNumber) {
		context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
	}

	public static void startViewGeoLocationActivity(Context context, String location) {
		startViewActivity(context, "geo:0,0?q=" + URLEncoder.encode(location));
	}

	public static void startViewActivity(Context context, String uri) {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
		context.startActivity(intent);
	}

	public static DisplayMetrics getDisplayMetrics(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}

	public static boolean isWideEnoughForDouble(Context context) {
		DisplayMetrics metrics = getDisplayMetrics(context);
		float widthInDp = metrics.widthPixels / metrics.density;
		return widthInDp > 600;
	}

	public static boolean isScreenWidthTiny(Context context) {
		DisplayMetrics metrics = getDisplayMetrics(context);
		float widthInDp = metrics.widthPixels / metrics.density;
		return widthInDp <= 320;
	}

	public static boolean isTabletDevice(Context context) {
		// Verifies if the Generalized Size of the device is XLARGE to be
		// considered a Tablet
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);

		// If XLarge, checks if the Generalized Density is at least MDPI
		// (160dpi)
		if (xlarge) {
			DisplayMetrics metrics = getDisplayMetrics(context);

			// MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
			// DENSITY_TV=213, DENSITY_XHIGH=320
			if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
					|| metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
					|| metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
					|| metrics.densityDpi == DisplayMetrics.DENSITY_TV
					|| metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {

				// Yes, this is a tablet!
				return true;
			}
		}

		// No, this is not a tablet!
		return false;
	}

	public static boolean isOrientationLandscape(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	public static <V extends View> V addToContainer(View parent, int containerResId, V view) {
		removeFromParent(view);
		ViewGroup container = (ViewGroup) parent.findViewById(containerResId);
		container.addView(view);
		return view;
	}

	public static <V extends View> V removeFromParent(V view) {
		if (view == null) return null;
		ViewParent parent = view.getParent();
		if (parent == null) return view;
		if (parent instanceof ViewGroup) {
			((ViewGroup) parent).removeView(view);
			return view;
		}
		log.error("Unsupported parent type:", parent.getClass());
		return view;
	}

	public static MenuItem addMenuItem(Menu menu, int order, int titleResId, boolean showAsAction,
			final Runnable callback) {
		MenuItem item = menu.add(Menu.NONE, Menu.NONE, order, titleResId);
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				callback.run();
				return true;
			}
		});
		if (showAsAction) item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return item;
	}

	public static void postNotification(Context context, int smallIconResId, Bitmap largeIcon,
			CharSequence contentTitle, CharSequence contentText, Intent notificationIntent, int notificationId) {
		postNotification(context, smallIconResId, largeIcon, contentTitle, contentText, contentTitle,
			notificationIntent, notificationId);
	}

	public static void postNotification(Context context, int smallIconResId, Bitmap largeIcon,
			CharSequence contentTitle, CharSequence contentText, CharSequence tickerText, Intent notificationIntent,
			int notificationId) {
		log.info("Posting notification:", "#" + notificationId, "->", tickerText);

		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, notificationId,
			notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new Notification.Builder(context).setContentTitle(contentTitle)
				.setContentText(contentText).setTicker(tickerText).setSmallIcon(smallIconResId).setLargeIcon(largeIcon)
				.setContentIntent(pendingNotificationIntent).setAutoCancel(true).getNotification();

		notification.setLatestEventInfo(context, contentTitle, contentText, pendingNotificationIntent);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}

	public static void cancelNotification(Context context, int notificationId) {
		log.info("Canceling notification:", "#" + notificationId);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationId);
	}

	public static void startActivity(Context context, Class<? extends Activity> activity) {
		context.startActivity(new Intent(context, activity));
	}

	public static String getMarketUri(Package appId) {
		return "market://details?id=" + appId.getName();
	}

}
