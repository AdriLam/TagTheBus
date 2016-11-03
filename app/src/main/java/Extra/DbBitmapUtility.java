package Extra;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DbBitmapUtility {

    /**
     * Convert from bitmap to byte array
     * @param bitmap
     * @return
     */
    public static byte[] getBytes(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] byteArray = stream.toByteArray();

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    /**
     *  Convert from byte array to bitmap
     * @param image
     * @return
     */
    public static Bitmap getImage(byte[] image) {

        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /**
     * Convert from byte array to bitmap
     * @param bitmap
     * @return
     */
    public static Bitmap getCompressedImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();

        Bitmap decoded = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("Original   dimensions", bitmap.getWidth() + " " + bitmap.getHeight());
        Log.e("Compressed dimensions", decoded.getWidth() + " " + decoded.getHeight());

        return decoded;
    }
}
