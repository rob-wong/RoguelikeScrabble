package com.cypherose.app.util

import androidx.compose.ui.tooling.preview.Preview

// Phone sizes
@Preview(name = "Phone Portrait", widthDp = 411, heightDp = 731)
@Preview(name = "Phone Landscape", widthDp = 731, heightDp = 411)

// Tablet sizes
@Preview(name = "Small Tablet Portrait", widthDp = 600, heightDp = 960)
@Preview(name = "Small Tablet Landscape", widthDp = 960, heightDp = 600)
@Preview(name = "Large Tablet Portrait", widthDp = 1024, heightDp = 1366)
@Preview(name = "Large Tablet Landscape", widthDp = 1366, heightDp = 1024)
annotation class DevicePreviews
