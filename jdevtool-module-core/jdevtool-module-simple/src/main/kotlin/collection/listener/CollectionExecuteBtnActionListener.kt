package com.wxl.jdevtool.collection.listener

import com.wxl.jdevtool.ComponentListener
import com.wxl.jdevtool.collection.CollectionOps
import com.wxl.jdevtool.collection.CollectionTabbedModule
import com.wxl.jdevtool.extension.getUnescapeText
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Create by wuxingle on 2024/01/09
 * 执行按钮
 */
@ComponentListener("collectionTabbedModule.executeBtn")
class CollectionExecuteBtnActionListener(
    @Autowired val collectionTabbedModule: CollectionTabbedModule
) : ActionListener {

    private val log = KotlinLogging.logger { }

    override fun actionPerformed(e: ActionEvent) {
        if (!collectionTabbedModule.check()) {
            return
        }

        val op = CollectionOps.valueOf(collectionTabbedModule.radioGroup.selection.actionCommand)
        log.info { "collection op: ${(op)}" }

        // 分隔符
        var splitStr = collectionTabbedModule.splitTextField.getUnescapeText()
        if (splitStr.isEmpty()) {
            splitStr = "\n"
        }
        val coll1 = getCollection(collectionTabbedModule.leftTextArea1.text, splitStr)
        val coll2 = getCollection(collectionTabbedModule.leftTextArea2.text, splitStr).toSet()

        val coll3 = when (op) {
            CollectionOps.DEDUPLICATE -> coll1.toSet()
            CollectionOps.REPEAT -> coll1.groupingBy { it }.eachCount().filter { it.value > 1 }.keys
            CollectionOps.UNION -> coll1 + coll2
            CollectionOps.INTERSECTION -> coll1 intersect coll2
            CollectionOps.DIFF12 -> coll1 - coll2
            CollectionOps.DIFF21 -> coll2 - coll1.toSet()
        }

        collectionTabbedModule.rightTextArea.text =
            if (coll3.isEmpty()) "(empty list)" else coll3.joinToString(splitStr)
    }

    private fun getCollection(text: String?, split: String): List<String> {
        return if (text.isNullOrBlank()) {
            emptyList()
        } else {
            text.split(split).filter { it.isNotBlank() }
        }
    }

}
