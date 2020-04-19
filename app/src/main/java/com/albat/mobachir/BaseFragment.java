package com.albat.mobachir;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;


public class BaseFragment extends Fragment {
    String TAG = "BaseFragment";

    public boolean hasChildren() {
        int childCount = getChildFragmentManager().getBackStackEntryCount();

        if (childCount == 0)
            return false;

        return true;
    }

    public boolean popBackStack() {
        try {
            int childCount = getChildFragmentManager().getBackStackEntryCount();

            if (childCount == 0) {
                // it has no child Fragment
                // can not handle the onBackPressed task by itself
                return false;
            }

            FragmentManager childFragmentManager = getChildFragmentManager();
            BaseFragment childFragment = (BaseFragment) childFragmentManager.getFragments().get(childFragmentManager.getFragments().size() - 1);

            // propagate onBackPressed method call to the child Fragment
            if (!childFragment.popBackStack()) {
                // child Fragment was unable to handle the task
                // It could happen when the child Fragment is last last leaf of a chain
                // removing the child Fragment from stack
                childFragmentManager.popBackStackImmediate();

            }

            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true;

//        if (getFragmentManager().getBackStackEntryCount() > 0) {
//            getFragmentManager().popBackStackImmediate();
//            return true;
//        } else
//            return false;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public void goBack() {
        getActivity().onBackPressed();
    }
}
