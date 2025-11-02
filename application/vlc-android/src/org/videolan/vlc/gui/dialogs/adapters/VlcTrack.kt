package org.videolan.vlc.gui.dialogs.adapters

interface VlcTrack {
    fun getId(): String
    fun getName(): String
    fun getWidth(): Int = 0
    fun getHeight(): Int = 0
    fun getProjection(): Int = 0
    fun getFrameRateDen(): Int = 0
    fun getFrameRateNum(): Int = 0
}
