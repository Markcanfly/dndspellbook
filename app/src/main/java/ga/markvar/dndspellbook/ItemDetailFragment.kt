package ga.markvar.dndspellbook

import android.os.Build
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
    private lateinit var index_name: String // Used only when building view

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
                // Async load item
                index_name = it.getString(ARG_ITEM_ID)!!
            }
        }
    }

    private fun updateSpellFromDb(index_name: String) {
        val database = SpellListDatabase.getDatabase(requireContext())
        val spellDao = database.spellDao()
        thread {
            item = spellDao.get(index_name)!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !requireContext().mainLooper.isCurrentThread) {
                updateContent()
            }
        }.join()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        toolbarLayout = binding.toolbarLayout
        itemDetailTextView = binding.itemDetail

        updateSpellFromDb(index_name)

        //rootView.setOnDragListener(dragListener)

        return rootView
    }

    private fun updateContent() {
        // Check if this is still the fragment we launched the thread on
        toolbarLayout?.title = item?.name

        // Show the placeholder content as text in a TextView.
        item?.let {
            itemDetailTextView.text = it.description
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