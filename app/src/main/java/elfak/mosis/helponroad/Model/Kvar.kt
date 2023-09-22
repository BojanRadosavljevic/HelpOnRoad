package elfak.mosis.helponroad.Model

data class Kvar (
    var id:String,
    val user:String,
    val slika:String,
    val opis:String,
    val latitude:Double=0.0,
    val longitude:Double=0.0,
    val status: String
)
{
 constructor():this("","","","",0.0,0.0,"")
}