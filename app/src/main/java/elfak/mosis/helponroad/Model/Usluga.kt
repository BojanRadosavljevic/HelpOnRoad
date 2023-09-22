package elfak.mosis.helponroad.Model

data class Usluga(
    val id:String,
    val ime:String,
    val radnja:String,
    val cena:Int=0
)
{
    constructor():this("","","",0)
}
