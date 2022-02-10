package com.imzqqq.app.features.html

import android.content.Context
import android.text.Spannable
import androidx.core.text.toSpannable
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.features.themes.ThemeUtils
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.html.HtmlPlugin
import org.commonmark.node.Node
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventHtmlRenderer @Inject constructor(private val htmlConfigure: MatrixHtmlPluginConfigure,
                                            private val context: Context) {

    private fun resolveCodeBlockBackground() =
        ThemeUtils.getColor(context, R.attr.code_block_bg_color)
    private fun resolveQuoteBarColor() =
            ThemeUtils.getColor(context, R.attr.quote_bar_color)

    private var codeBlockBackground: Int = resolveCodeBlockBackground()
    private var quoteBarColor: Int = resolveQuoteBarColor()

    interface PostProcessor {
        fun afterRender(renderedText: Spannable)
    }

    private fun buildMarkwon() = Markwon.builder(context)
            .usePlugins(listOf(
                    HtmlPlugin.create(htmlConfigure),
                    object: AbstractMarkwonPlugin() {
                        override fun configureTheme(builder: MarkwonTheme.Builder) {
                            super.configureTheme(builder)
                            builder.codeBlockBackgroundColor(codeBlockBackground)
                                    .codeBackgroundColor(codeBlockBackground)
                                    .blockQuoteColor(quoteBarColor)
                        }
                    }
            ))
            .build()

    private var markwon: Markwon = buildMarkwon()
    get() {
        val newCodeBlockBackground = resolveCodeBlockBackground()
        val newQuoteBarColor = resolveQuoteBarColor()
        var changed = false
        if (codeBlockBackground != newCodeBlockBackground) {
            codeBlockBackground = newCodeBlockBackground
            changed = true
        }
        if (quoteBarColor != newQuoteBarColor) {
            quoteBarColor = newQuoteBarColor
            changed = true
        }
        if (changed) {
            field = buildMarkwon()
        }
        return field
    }

    fun invalidateColors() {
        markwon = buildMarkwon()
    }

    fun parse(text: String): Node {
        return markwon.parse(text)
    }

    /**
     * @param text the text you want to render
     * @param postProcessors an optional array of post processor to add any span if needed
     */
    fun render(text: String, vararg postProcessors: PostProcessor): CharSequence {
        return try {
            val parsed = markwon.parse(text)
            renderAndProcess(parsed, postProcessors)
        } catch (failure: Throwable) {
            Timber.v("Fail to render $text to html")
            text
        }
    }

    /**
     * @param node the node you want to render
     * @param postProcessors an optional array of post processor to add any span if needed
     */
    fun render(node: Node, vararg postProcessors: PostProcessor): CharSequence? {
        return try {
            renderAndProcess(node, postProcessors)
        } catch (failure: Throwable) {
            Timber.v("Fail to render $node to html")
            return null
        }
    }

    private fun renderAndProcess(node: Node, postProcessors: Array<out PostProcessor>): CharSequence {
        val renderedText = markwon.render(node).toSpannable()
        postProcessors.forEach {
            it.afterRender(renderedText)
        }
        return renderedText
    }
}

class MatrixHtmlPluginConfigure @Inject constructor(private val colorProvider: ColorProvider) : HtmlPlugin.HtmlConfigure {

    override fun configureHtml(plugin: HtmlPlugin) {
        plugin
                .addHandler(FontTagHandler())
                .addHandler(MxReplyTagHandler())
                .addHandler(SpanHandler(colorProvider))
    }
}
