package org.videolan.vlc.gui.helpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import org.videolan.medialibrary.media.MediaLibraryItem
import org.videolan.vlc.gui.view.FocusableTextView
import org.videolan.vlc.gui.view.SwipeRefreshLayout

object DataBindingAdapters {
    
    @JvmStatic
    @BindingAdapter("vlc:layoutMarginTop")
    fun setLayoutMarginTop(view: ImageView, margin: Int?) {
        val params = view.layoutParams as? android.view.ViewGroup.MarginLayoutParams
        params?.topMargin = margin ?: 0
        view.layoutParams = params
    }
    
    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setSwipeRefreshVisibility(view: SwipeRefreshLayout, visibility: Int) {
        view.visibility = visibility
    }
    
    @JvmStatic
    @BindingAdapter("app:ellipsizeMode")
    fun setEllipsizeMode(view: TextView, enabled: Boolean) {
        if (enabled) {
            view.ellipsize = android.text.TextUtils.TruncateAt.END
        }
    }
    
    @JvmStatic
    @BindingAdapter("android:onClick")
    fun setOnClick(view: FocusableTextView, listener: View.OnClickListener?) {
        view.setOnClickListener(listener)
    }
    
    @JvmStatic
    @BindingAdapter("app:media")
    fun setMedia(view: ImageView, item: MediaLibraryItem?) {
        // Placeholder - image loading logic would go here
    }
    
    @JvmStatic
    @BindingAdapter("app:equalizerBackgroundColor")
    fun setEqualizerBackgroundColor(view: ConstraintLayout, enabled: Boolean) {
        // Placeholder - color logic would go here
    }
}

object TalkbackUtil {
    @JvmStatic
    fun getRatio(aspectRatio: String?): String = aspectRatio ?: ""
    
    @JvmStatic
    fun getTime(time: String?): String = time ?: ""
}
