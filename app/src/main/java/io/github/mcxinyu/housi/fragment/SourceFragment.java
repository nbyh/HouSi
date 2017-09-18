package io.github.mcxinyu.housi.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.util.QueryPreferences;

/**
 * Created by huangyuefeng on 2017/9/17.
 * Contact me : mcxinyu@gmail.com
 */
public class SourceFragment extends ABaseFragment {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_spinner)
    Spinner mToolbarSpinner;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    private Unbinder unbinder;

    private FragmentManager mFragmentManager;
    private Fragment currentFragment;
    private SourceBuiltInFragment mSourceBuiltInFragment;

    public static SourceFragment newInstance() {

        Bundle args = new Bundle();

        SourceFragment fragment = new SourceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Callbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks) {
            mCallbacks = (Callbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BackHandledInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_source, container, false);
        unbinder = ButterKnife.bind(this, view);

        initToolbar();

        mFragmentManager = getChildFragmentManager();
        currentFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            currentFragment = mSourceBuiltInFragment = SourceBuiltInFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
                    .commit();
        }
        return view;
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        mToolbarTitle.setText(getString(R.string.action_source));

        String[] strings = {getString(R.string.source_routing_built_in),
                getString(R.string.source_routing_diy),
                getString(R.string.source_routing_file)};

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mToolbarSpinner.setAdapter(adapter);

        int routing = QueryPreferences.getSourceRouting(getActivity());
        mToolbarSpinner.setSelection(routing);

        mToolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                QueryPreferences.setSourceRouting(getActivity(), i);
                switch (i) {
                    case 0:
                        if (mSourceBuiltInFragment == null)
                            mSourceBuiltInFragment = SourceBuiltInFragment.newInstance();
                        switchFragment(mSourceBuiltInFragment);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void switchFragment(Fragment fragment) {

        if (currentFragment != fragment) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (fragment.isAdded()) {
                transaction.hide(currentFragment)
                        .show(fragment)
                        .commit();
            } else {
                transaction.hide(currentFragment)
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
            // currentFragment.setUserVisibleHint(false);
            // fragment.setUserVisibleHint(true);
            currentFragment = fragment;
        }
    }

    @Override
    protected int getMenuItemId() {
        return R.id.nav_source;
    }

    @Override
    protected Toolbar getToolBar() {
        return mToolbar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mCallbacks = null;
    }

    public interface Callbacks {
        // Toolbar getToolbar();
    }
}
