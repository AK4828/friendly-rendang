package id.merv.cdp.book.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meruvian.dnabook.R;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

/**
 * Created by akm on 14/12/15.
 */
public class CategoryTreeFragment extends Fragment {

    public static CategoryTreeFragment newInstance() {
        CategoryTreeFragment fragment = new CategoryTreeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_tree, container, false);

        TreeNode root = TreeNode.root();
        TreeNode computerRoot = new TreeNode("My Computer");

        TreeNode myDocuments = new TreeNode("My Documents");
        TreeNode downloads = new TreeNode("Downloads");
        TreeNode file1 = new TreeNode("Folder 1");
        TreeNode file2 = new TreeNode("Folder 2");
        TreeNode file3 = new TreeNode("Folder 3");
        TreeNode file4 = new TreeNode("Folder 4");
        downloads.addChildren(file1, file2, file3, file4);

        TreeNode myMedia = new TreeNode("Photos");
        TreeNode photo1 = new TreeNode("Folder 1");
        TreeNode photo2 = new TreeNode("Folder 2");
        TreeNode photo3 = new TreeNode("Folder 3");
        myMedia.addChildren(photo1, photo2, photo3);

        myDocuments.addChild(downloads);
        computerRoot.addChildren(myDocuments, myMedia);

        root.addChildren(computerRoot);

        AndroidTreeView treeView = new AndroidTreeView(getActivity(), root);

        container.addView(treeView.getView());

        return view;
    }
}
