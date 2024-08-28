package mission.vatsalya.download_manager

interface Downloader {
    fun downloadFile(url: String): Long
}