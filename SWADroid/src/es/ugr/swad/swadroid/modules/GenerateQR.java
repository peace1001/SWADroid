package es.ugr.swad.swadroid.modules;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.ksoap2.SoapFault;
import org.xmlpull.v1.XmlPullParserException;

import com.google.zxing.WriterException;

import es.ugr.swad.swadroid.Constants;
import es.ugr.swad.swadroid.R;
import es.ugr.swad.swadroid.utils.QR;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class GenerateQR extends Module {
	/**
	 * Messages tag name for Logcat
	 */
	public static final String TAG = Constants.APP_TAG + " GenerateQR";
	
	/* (non-Javadoc)
	 * @see es.ugr.swad.swadroid.modules.Module#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generate_qr_layout);
	}

	/* (non-Javadoc)
	 * @see es.ugr.swad.swadroid.modules.Module#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		try {
			Bitmap qrCode = QR.encode(this, prefs.getUserID());
			ImageView qr_image = (ImageView) findViewById(R.id.qr_code_image);
			qr_image.setImageBitmap(qrCode);
		} catch (WriterException e) {
			error(TAG, e.getMessage(), e);
		}
	}

	@Override
	protected void requestService() throws NoSuchAlgorithmException,
			IOException, XmlPullParserException, SoapFault,
			IllegalAccessException, InstantiationException {

	}

	@Override
	protected void connect() {

	}

	@Override
	protected void postConnect() {

	}

	@Override
	protected void onError() {

	}

}