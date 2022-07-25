package app.htheh.helpthehomeless.ui.homelesslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.htheh.helpthehomeless.databinding.HomelessItemViewBinding
import app.htheh.helpthehomeless.model.Homeless


//Using DiffUtil the adapters calculats the minimum changes when the list gets updated
class RecyclerViewAdapater(val clickListener: HomelessListener): ListAdapter<Homeless, RecyclerViewAdapater.ViewHolder>(AsteroidDiffCallback()) {

    // no need for the data variable nor the getItemCount menthod since we are using ListAdapter
//    var data = listOf<Asteroid>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

//    override fun getItemCount(): Int {
//        return data.size
//    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the RecyclerView.ViewHolder.itemView
     * to reflect the item at the given position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        println("First Name is: " + item.email)
        holder.bind(item!!, clickListener)
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder
     * of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //parent in reality is always the RecyclerView
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: HomelessItemViewBinding): RecyclerView.ViewHolder(binding.root){
        val res = binding.root.context.resources

        fun bind(item: Homeless, clickListener: HomelessListener) {
            binding.homeless = item
            binding.clickListener = clickListener
            //excutes the binding dapaters that are connected to views
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomelessItemViewBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class AsteroidDiffCallback : DiffUtil.ItemCallback<Homeless>() {
    override fun areItemsTheSame(oldItem: Homeless, newItem: Homeless): Boolean {
        return oldItem.firstName == newItem.firstName
    }

    override fun areContentsTheSame(oldItem: Homeless, newItem: Homeless): Boolean {
        return oldItem == newItem
    }

}

class HomelessListener(val clickListener: (hl: Homeless) -> Unit) {
    fun onClick(hl: Homeless) = clickListener(hl)
}
