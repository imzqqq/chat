package com.imzqqq.app.features.roomdirectory

import com.imzqqq.app.core.utils.AssetReader
import javax.inject.Inject

class ExplicitTermFilter @Inject constructor(
        assetReader: AssetReader
) {
    // List of forbidden terms is in file asset forbidden_terms.txt, in lower case
    private val explicitTerms = assetReader.readAssetFile("forbidden_terms.txt")
            .orEmpty()
            .split("\n")
            .map { it.trim() }
            .distinct()
            .filter { it.isNotEmpty() }

    private val explicitContentRegex = explicitTerms
            .joinToString(prefix = ".*\\b(", separator = "|", postfix = ")\\b.*")
            .toRegex(RegexOption.IGNORE_CASE)

    fun canSearchFor(term: String): Boolean {
        return term !in explicitTerms  && term != "18+"
    }

    fun isValid(str: String): Boolean {
        return explicitContentRegex.matches(str.replace("\n", " ")).not() &&
                // Special treatment for "18+" since word boundaries does not work here
                str.contains("18+").not()
    }
}
