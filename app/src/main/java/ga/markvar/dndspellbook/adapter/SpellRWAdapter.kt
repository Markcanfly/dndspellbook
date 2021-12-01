package ga.markvar.dndspellbook.adapter

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ga.markvar.dndspellbook.data.Spell
import ga.markvar.dndspellbook.databinding.ItemListContentBinding

class SpellRWAdapter(
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener
) : RecyclerView.Adapter<SpellRWAdapter.ViewHolder>() {

    private var values = mutableListOf<Spell>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListContentBinding.
        inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpellRWAdapter.ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.name
        holder.contentView.text = item.classes /* Extend this for more info at a glance */

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
            }

            setOnLongClickListener { v ->
                // Implement drag and drop data through the ClipData API
                val clipItem = ClipData.Item(item.name)
                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    clipItem
                )

                if (Build.VERSION.SDK_INT >= 24) {
                    v.startDragAndDrop(
                        dragData,
                        View.DragShadowBuilder(v),
                        null,
                        0
                    )
                } else {
                    v.startDrag(
                        dragData,
                        View.DragShadowBuilder(v),
                        null,
                        0
                    )
                }
            }
        }
    }

    override fun getItemCount() = values.size

    fun update(values: List<Spell>) {
        this.values.clear()
        this.values.addAll(0, values)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.idText
        val contentView: TextView = binding.content
        // TODO("Extend")
    }
}