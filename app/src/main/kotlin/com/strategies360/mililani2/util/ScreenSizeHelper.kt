import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.WindowManager

class ScreenSize private constructor(context: Context) {
  private val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  private val display: Display
  private val size: Point = Point()

  var width = 0
    get() {
      display.getRealSize(size)
      field = size.x
      return field
    }
    private set
  var height = 0
    get() {
      display.getRealSize(size)
      field = size.y
      return field
    }
    private set

  companion object {
    private var screenSize: ScreenSize? = null
    fun instance(context: Context): ScreenSize? {
      if (null == screenSize) {
        screenSize = ScreenSize(context)
      }
      return screenSize
    }
  }

  init {
    display = wm.defaultDisplay
  }
}
