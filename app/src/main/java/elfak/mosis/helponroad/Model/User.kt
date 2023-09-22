package elfak.mosis.helponroad.Model

data class User(
    var id:String,
    var userName:String,
    var brojTelefona:String,
    var brojPoena:Int=0,
    var email:String,
    var password:String,
    var strana:Boolean=false
)
{
    constructor():this("","","",0,"","",false)
}
