package de.frejaundalex.elementsequencing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.frejaundalex.elementsequencing.add.AddActivity
import de.frejaundalex.elementsequencing.library.LibraryFragment
import de.frejaundalex.elementsequencing.menu.HomeFragment
import de.frejaundalex.elementsequencing.menu.ProfileFragment

class MainActivity : AppCompatActivity() {

    private val HOME = "HOME"
    private val LIBRARY = "LIBRARY"
    private val PROFILE = "PROFILE"

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.menuFragment.observe(this, Observer { menuFragment ->
            when (menuFragment) {
                MenuFragment.Home -> showCurrentFragment()
                MenuFragment.AsanaOverview -> showLibraryFragment()
                MenuFragment.Profile -> showSelfFragment()
            }
        })
        viewModel.showAddBookFragment.observe(this, Observer {
            showAddBook()
        })

        val menu = findViewById<BottomNavigationView>(R.id.menu)
        menu.selectedItemId = R.id.current

        menu.setOnNavigationItemSelectedListener { menuItem ->
            viewModel.menuItemClicked(menuItem.itemId)
        }
    }

    private fun showAddBook() {
        startActivity(Intent(this, AddActivity::class.java))
    }

    private fun showSelfFragment() {
        replaceFragment(ProfileFragment(), PROFILE)
    }

    private fun showLibraryFragment() {
        replaceFragment(LibraryFragment(), LIBRARY)
    }

    private fun showCurrentFragment() {
        replaceFragment(HomeFragment(), HOME)
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(
            R.id.frame,
            fragment, tag
        ).commit()
    }
}