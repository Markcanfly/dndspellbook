package ga.markvar.dndspellbook

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ga.markvar.dndspellbook.adapter.SpellRWAdapter
import ga.markvar.dndspellbook.data.Spell
import ga.markvar.dndspellbook.data.SpellDao
import ga.markvar.dndspellbook.data.SpellListDatabase
import ga.markvar.dndspellbook.databinding.FragmentItemListBinding
import ga.markvar.dndspellbook.databinding.ItemListContentBinding
import kotlin.concurrent.thread

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

class ItemListFragment : Fragment(), AdapterView.OnItemSelectedListener {

    /**
     * Method to intercept global key events in the
     * item list fragment to trigger keyboard shortcuts
     * Currently provides a toast when Ctrl + Z and Ctrl + F
     * are triggered
     */
    private val unhandledKeyEventListenerCompat =
        ViewCompat.OnUnhandledKeyEventListenerCompat { v, event ->
            if (event.keyCode == KeyEvent.KEYCODE_Z && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Undo (Ctrl + Z) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            } else if (event.keyCode == KeyEvent.KEYCODE_F && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Find (Ctrl + F) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            }
            false
        }

    private var _binding: FragmentItemListBinding? = null
    private lateinit var database: SpellListDatabase
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var spellRWAdapter: SpellRWAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)

        database = SpellListDatabase.getDatabase(applicationContext = requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.addOnUnhandledKeyEventListener(view, unhandledKeyEventListenerCompat)

        val recyclerView: RecyclerView = binding.itemList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Spell
            val bundle = Bundle()
            bundle.putString(
                ItemDetailFragment.ARG_ITEM_ID,
                item.index_name
            )
            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        /**
         * Context click listener to handle Right click events
         * from mice and trackpad input to provide a more native
         * experience on larger screen devices
         */
        val onContextClickListener = View.OnContextClickListener { v ->
            val item = v.tag as Spell
            Toast.makeText(
                v.context,
                "Context click of item " + item.name,
                Toast.LENGTH_LONG
            ).show()
            true
        }

        /**
         * Setup Sort Mode Selector spinner
         */
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_types,
            android.R.layout.simple_spinner_item,
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sortType?.adapter = adapter
        }

        binding.sortType?.onItemSelectedListener = this

        setupRecyclerView(recyclerView, onClickListener, onContextClickListener)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener,
        onContextClickListener: View.OnContextClickListener
    ) {
        val adapter = SpellRWAdapter(
            onClickListener,
            onContextClickListener
        )
        recyclerView.adapter = adapter
        spellRWAdapter = adapter
        /**
         * Search bar text change listener to update the RecyclerView
         */
        binding.searchBar?.doOnTextChanged { text, _, _, _ ->
            updateRW(adapter)
        }

        updateRW(adapter)
    }

    private val orderBy: String get() = when(binding.sortType?.selectedItem.toString()) {
            "Alphabetical"  -> SpellDao.ALPHABETICAL
            "Level"         -> SpellDao.LEVEL
            else            -> SpellDao.ALPHABETICAL
    }

    private fun updateRW(adapter: SpellRWAdapter) {
        thread {
            val dao = database.spellDao()
            val query = binding.searchBar?.text.toString()
            val items = when (orderBy) {
                SpellDao.ALPHABETICAL   -> if (query != "") dao.search(query) else dao.getAll()
                SpellDao.LEVEL          -> if (query != "") dao.searchSortedByLevel(query) else dao.getAllSortedByLevel()
                else                    -> if (query != "") dao.search(query) else dao.getAll()
            }
            requireActivity().runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        updateRW(spellRWAdapter)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        updateRW(spellRWAdapter)
    }
}