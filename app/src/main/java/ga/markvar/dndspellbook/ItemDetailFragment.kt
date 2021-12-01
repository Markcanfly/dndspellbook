package ga.markvar.dndspellbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ga.markvar.dndspellbook.data.Spell
import ga.markvar.dndspellbook.data.SpellListDatabase
import ga.markvar.dndspellbook.databinding.FragmentItemDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The placeholder content this fragment is presenting.
     */
    private var item: Spell? = null

    lateinit var itemDetailTextView: TextView
    private var toolbarLayout: CollapsingToolbarLayout? = null
    private lateinit var index_name: String

    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private val dragListener = View.OnDragListener { v, event ->
//        if (event.action == DragEvent.ACTION_DROP) {
//            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
//            val dragData = clipDataItem.text
//            item = PlaceholderContent.ITEM_MAP[dragData]
//            updateContent()
//        }
//        true
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                index_name = it.getString(ARG_ITEM_ID)!!
            }
        }
    }

    private fun getSpellFromDb(index_name: String): Spell {
        val database = SpellListDatabase.getDatabase(requireContext())
        val spellDao = database.spellDao()
        return spellDao.get(index_name)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        toolbarLayout = binding.toolbarLayout
        itemDetailTextView = binding.itemDetail

        //updateContent()
        //rootView.setOnDragListener(dragListener)

        return rootView
    }

    private fun updateContent() {
        thread {
            item = getSpellFromDb(index_name)

            toolbarLayout?.title = item?.name

            // Show the placeholder content as text in a TextView.
            item?.let {
                itemDetailTextView.text = it.description
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}