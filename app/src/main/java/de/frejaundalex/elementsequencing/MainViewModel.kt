package de.frejaundalex.elementsequencing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val menuFragment = MutableLiveData<MenuFragment>()
    val showAddAsanaFragment = MutableLiveData<Boolean>()

    init {
        menuFragment.value = MenuFragment.Home
    }

    fun menuItemClicked(itemId: Int): Boolean {
        return when (itemId) {
            R.id.add -> {
                showAddAsanaFragment()
                false
            }
            else -> {
                switchFragment(itemId)
                true
            }
        }
    }

    private fun showAddAsanaFragment() {
        showAddAsanaFragment.value = true
        showAddAsanaFragment.value = false
    }

    private fun switchFragment(itemId: Int) {
        val new = when (itemId) {
            R.id.current -> MenuFragment.Home
            R.id.all -> MenuFragment.AsanaOverview
            R.id.self -> MenuFragment.Profile
            else -> throw IllegalArgumentException("cant handle menuItem $itemId")
        }
        if (menuFragment.value != new) menuFragment.value = new
    }
}

sealed class MenuFragment {
    object Home : MenuFragment()
    object Profile : MenuFragment()
    object AsanaOverview : MenuFragment()
}