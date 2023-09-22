package elfak.mosis.helponroad.Model

data class Komentar(
    var id:String,
    val user:String,
    val radnja:String,
    val komentar:String,
    val datumIVreme:String
)
{
    constructor():this("","","","","")
}
