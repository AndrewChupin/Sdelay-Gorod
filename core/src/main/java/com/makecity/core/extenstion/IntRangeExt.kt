package com.makecity.core.extenstion

import java.util.*


fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start