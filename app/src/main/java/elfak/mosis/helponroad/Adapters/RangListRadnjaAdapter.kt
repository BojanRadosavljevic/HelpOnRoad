package elfak.mosis.helponroad.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.helponroad.Model.Radnja
import elfak.mosis.helponroad.R
import kotlinx.coroutines.NonDisposableHandle.parent

class RangListRadnjaAdapter (private val ranglista:List<Radnja>) :
    RecyclerView.Adapter<RangListRadnjaAdapter.RangListRadnjaViewHolder>(){
    class RangListRadnjaViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val textViewIme: TextView = itemView.findViewById(R.id.textViewIme)
        val textViewPoeni: TextView = itemView.findViewById(R.id.textViewPoeni)
        val textViewDodatak: TextView = itemView.findViewById(R.id.textViewDodatak)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RangListRadnjaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return RangListRadnjaViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return ranglista.size
    }

    override fun onBindViewHolder(holder: RangListRadnjaViewHolder, position: Int) {
        val currentu = ranglista[position]
        holder.textViewIme.text = currentu.Username
        holder.textViewPoeni.text = currentu.brojPoena.toString()
        if(position==1)
            holder.textViewDodatak.text = "20% popusta"
        else if(position>1&&position<4)
            holder.textViewDodatak.text = "15% popusta"
        else if(position>4&&position<6)
            holder.textViewDodatak.text = "10% popusta"
        else if(position>5&&position<11)
            holder.textViewDodatak.text = "5% popusta"
        else holder.textViewDodatak.text = "bez popusta"
    }

}

/*package elfak.mosis.helponroad.Adapters




class RangListUsersAdapter(private val ranglista:List<User>) :
    RecyclerView.Adapter<RangListUsersAdapter.RangListUsersViewHolder>(){
     class RangListUsersViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         val textViewIme: TextView = itemView.findViewById(R.id.textViewIme)
         val textViewPoeni: TextView = itemView.findViewById(R.id.textViewPoeni)
         val textViewDodatak: TextView = itemView.findViewById(R.id.textViewDodatak)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RangListUsersViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return RangListUsersViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return ranglista.size
    }

    override fun onBindViewHolder(holder: RangListUsersViewHolder, position: Int) {
        val currentu = ranglista[position]
        holder.textViewIme.text = currentu.userName
        holder.textViewPoeni.text = currentu.brojPoena.toString()
        holder.textViewDodatak.text = position.toString()
    }
}*/
