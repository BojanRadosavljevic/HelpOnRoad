package elfak.mosis.helponroad.Model

data class Radnja(
    var id:String,
    var Ime:String,
    var Username:String,
    var BrojTelefona:String,
    var email:String,
    var password:String,
    var brojPoena:Int=0,
    var latitude:Double=0.0,
    var longitude:Double=0.0,
    var strana:Boolean=true
)
{
    constructor():this("","","","","","",0,0.0,0.0,true)
}
