package co.ke.mshirika.mshirika_app.ui.util

interface OnMshirikaFragmentAttach {
    fun hideAppBar(hide: Boolean = true)

    fun showAppBar() = hideAppBar(false)
}