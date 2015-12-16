package id.merv.cdp.book.fragment;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.meruvian.dnabook.R;

/**
 * Created by dianw on 8/26/15.
 */
public class FragmentUtils {
	public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, boolean addToBackStack) {
		if (!addToBackStack) {
			fragmentManager.popBackStack();
		}

		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.container_body, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
