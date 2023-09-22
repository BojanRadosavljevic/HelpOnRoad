package elfak.mosis.helponroad.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.helponroad.Model.User
import elfak.mosis.helponroad.R
import kotlinx.coroutines.NonDisposableHandle.parent


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
}