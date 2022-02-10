package com.imzqqq.app.features.html

import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import com.imzqqq.app.InstrumentedTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

@RunWith(JUnit4::class)
@FixMethodOrder(MethodSorters.JVM)
class SpanUtilsTest : InstrumentedTest {

    private val spanUtils = SpanUtils()

    @Test
    fun canUseTextFutureString() {
        spanUtils.canUseTextFuture("test").shouldBeTrue()
    }

    @Test
    fun canUseTextFutureCharSequenceOK() {
        spanUtils.canUseTextFuture(SpannableStringBuilder().append("hello")).shouldBeTrue()
    }

    @Test
    fun canUseTextFutureCharSequenceWithSpanOK() {
        val string = SpannableString("Text with strikethrough, underline, red spans")
        string.setSpan(ForegroundColorSpan(Color.RED), 36, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanUtils.canUseTextFuture(string) shouldBeEqualTo true
    }

    @Test
    fun canUseTextFutureCharSequenceWithSpanKOStrikethroughSpan() {
        val string = SpannableString("Text with strikethrough, underline, red spans")
        string.setSpan(StrikethroughSpan(), 10, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanUtils.canUseTextFuture(string) shouldBeEqualTo trueIfAlwaysAllowed()
    }

    @Test
    fun canUseTextFutureCharSequenceWithSpanKOUnderlineSpan() {
        val string = SpannableString("Text with strikethrough, underline, red spans")
        string.setSpan(UnderlineSpan(), 25, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanUtils.canUseTextFuture(string) shouldBeEqualTo trueIfAlwaysAllowed()
    }

    @Test
    fun canUseTextFutureCharSequenceWithSpanKOBoth() {
        val string = SpannableString("Text with strikethrough, underline, red spans")
        string.setSpan(StrikethroughSpan(), 10, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(UnderlineSpan(), 25, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanUtils.canUseTextFuture(string) shouldBeEqualTo trueIfAlwaysAllowed()
    }

    @Test
    fun canUseTextFutureCharSequenceWithSpanKOAll() {
        val string = SpannableString("Text with strikethrough, underline, red spans")
        string.setSpan(StrikethroughSpan(), 10, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(UnderlineSpan(), 25, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        string.setSpan(ForegroundColorSpan(Color.RED), 36, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spanUtils.canUseTextFuture(string) shouldBeEqualTo trueIfAlwaysAllowed()
    }

    private fun trueIfAlwaysAllowed() = Build.VERSION.SDK_INT < Build.VERSION_CODES.P
}
