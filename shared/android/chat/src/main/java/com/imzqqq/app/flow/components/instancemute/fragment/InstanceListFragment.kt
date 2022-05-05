package com.imzqqq.app.flow.components.instancemute.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider.from
import autodispose2.autoDispose
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ActivityListsBinding
import com.imzqqq.app.flow.components.instancemute.adapter.DomainMutesAdapter
import com.imzqqq.app.flow.components.instancemute.interfaces.InstanceActionListener
import com.imzqqq.app.databinding.FragmentInstanceListBinding
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.util.HttpHeaderLink
import com.imzqqq.app.flow.util.hide
import com.imzqqq.app.flow.util.show
import com.imzqqq.app.flow.util.viewBinding
import com.imzqqq.app.flow.view.EndlessOnScrollListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class InstanceListFragment : Fragment(R.layout.fragment_instance_list), InstanceActionListener {

    @Inject lateinit var api: MastodonApi

    private lateinit var binding: FragmentInstanceListBinding
    private var fetching = false
    private var bottomId: String? = null
    private var adapter = DomainMutesAdapter(this)
    private lateinit var scrollListener: EndlessOnScrollListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInstanceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        binding.recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(view.context)
        binding.recyclerView.layoutManager = layoutManager

        scrollListener = object : EndlessOnScrollListener(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, view: RecyclerView) {
                if (bottomId != null) {
                    fetchInstances(bottomId)
                }
            }
        }

        binding.recyclerView.addOnScrollListener(scrollListener)
        fetchInstances()
    }

    override fun mute(mute: Boolean, instance: String, position: Int) {
        if (mute) {
            api.blockDomain(instance).enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Timber.e("Error muting domain $instance")
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        adapter.addItem(instance)
                    } else {
                        Timber.e("Error muting domain $instance")
                    }
                }
            })
        } else {
            api.unblockDomain(instance).enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Timber.e("Error unmuting domain $instance")
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        adapter.removeItem(position)
                        Snackbar.make(binding.recyclerView, getString(R.string.confirmation_domain_unmuted, instance), Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_undo) {
                                mute(true, instance, position)
                            }
                            .show()
                    } else {
                        Timber.e("Error unmuting domain $instance")
                    }
                }
            })
        }
    }

    private fun fetchInstances(id: String? = null) {
        if (fetching) {
            return
        }
        fetching = true
        binding.instanceProgressBar.show()

        if (id != null) {
            binding.recyclerView.post { adapter.bottomLoading = true }
        }

        api.domainBlocks(id, bottomId)
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe(
                { response ->
                    val instances = response.body()

                    if (response.isSuccessful && instances != null) {
                        onFetchInstancesSuccess(instances, response.headers()["Link"])
                    } else {
                        onFetchInstancesFailure(Exception(response.message()))
                    }
                },
                { throwable ->
                    onFetchInstancesFailure(throwable)
                }
            )
    }

    private fun onFetchInstancesSuccess(instances: List<String>, linkHeader: String?) {
        adapter.bottomLoading = false
        binding.instanceProgressBar.hide()

        val links = com.imzqqq.app.flow.util.HttpHeaderLink.parse(linkHeader)
        val next = com.imzqqq.app.flow.util.HttpHeaderLink.findByRelationType(links, "next")
        val fromId = next?.uri?.getQueryParameter("max_id")
        adapter.addItems(instances)
        bottomId = fromId
        fetching = false

        if (adapter.itemCount == 0) {
            binding.messageView.show()
            binding.messageView.setup(
                R.drawable.elephant_friend_empty,
                R.string.message_empty,
                null
            )
        } else {
            binding.messageView.hide()
        }
    }

    private fun onFetchInstancesFailure(throwable: Throwable) {
        fetching = false
        binding.instanceProgressBar.hide()
        Timber.e(throwable, "Fetch failure")

        if (adapter.itemCount == 0) {
            binding.messageView.show()
            if (throwable is IOException) {
                binding.messageView.setup(R.drawable.elephant_offline, R.string.error_network) {
                    binding.messageView.hide()
                    this.fetchInstances(null)
                }
            } else {
                binding.messageView.setup(R.drawable.elephant_error, R.string.error_generic) {
                    binding.messageView.hide()
                    this.fetchInstances(null)
                }
            }
        }
    }

    companion object {
        private const val TAG = "InstanceList" // logging tag
    }
}
