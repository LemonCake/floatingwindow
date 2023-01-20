package com.example.floatingwindowapp.ui.main

import IFLService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.floatingwindowapp.service.FLService
import com.example.floatingwindowapp.R

private const val TAG = "MainFragment"

class MainFragment : Fragment() {
    private var flRemoteService: IFLService? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        if (context?.drawOverOtherAppsEnabled() != true) {
            startPermissionDialog()
        }

        view.findViewById<Button>(R.id.floating_button).apply {
            setOnClickListener {
                val intent = Intent(context, FLService::class.java)
                val result = context.bindService(intent, object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        Log.d(TAG, "FLService bound")
                        flRemoteService = IFLService.Stub.asInterface(service)
                        flRemoteService?.showWindow(
                            windowToken
                        )
                    }

                    override fun onServiceDisconnected(name: ComponentName?) = Unit
                }, Context.BIND_AUTO_CREATE)
                Log.d(TAG, "starting bind: $result")
            }
        }

        view.findViewById<ViewPager>(R.id.pager).apply {
            adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
                override fun getItem(position: Int): Fragment = PagerFragment(position)

                override fun getCount(): Int = 2
            }

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    flRemoteService?.moveOffset(
                        windowToken,
                        if (position == 0) 1F - positionOffset else 0F
                    )
                }

                override fun onPageSelected(position: Int) {
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
        }
    }

    override fun onDestroy() {
        flRemoteService?.removeWindow()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        flRemoteService?.moveOffset(view?.windowToken, 1F)
    }

    private fun startPermissionDialog() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:com.example.floatingwindowapp")
        )
        startActivityForResult(intent, 1)
    }

    private fun Context.drawOverOtherAppsEnabled(): Boolean =
        Settings.canDrawOverlays(this)

    class PagerFragment(private val position: Int) : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.pager_fragment, container, false)

            view.findViewById<View>(R.id.page).apply {

                setBackgroundColor(
                    context.getColor(
                        if (position == 0) {
                            R.color.purple_200
                        } else {
                            R.color.teal_200
                        }
                    )
                )
            }
            return view
        }
    }
}
