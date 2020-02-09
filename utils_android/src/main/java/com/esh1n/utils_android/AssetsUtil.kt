import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

fun AssetManager.getJsonFromAssets(fileName: String?): String? {
    fileName?.let {
        return try {
            val `is`: InputStream = open(fileName)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    return null
}