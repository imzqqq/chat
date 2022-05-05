package im.vector.lib.multipicker.entity

data class MultiPickerContactType(
        val displayName: String,
        val photoUri: String?,
        val phoneNumberList: List<String>,
        val emailList: List<String>
)
