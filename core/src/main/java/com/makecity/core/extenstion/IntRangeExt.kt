package com.makecity.core.extenstion

import java.util.Random


fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) +  start