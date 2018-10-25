package com.makecity.core.extenstion

import java.util.concurrent.TimeUnit


fun TimeUnit.moreThanNow(time: Long): Boolean = toMillis(time) > System.currentTimeMillis()