package com.nlm.download_manager

interface Downloader {
    fun downloadFile(url: String): Long
}